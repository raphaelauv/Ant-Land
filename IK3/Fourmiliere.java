package IK3;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.lang.Math;
/**
 * @author Raphael
 */
public class Fourmiliere{
	//private Random alea=new Random();
	private TerrainF lamap; // la carte qui stock dans un tableau de type Case les informations de chaque "case"
	public int nourritureAquise; // variable a incrementer dans la methode deplacement
	private int compteur; // nombre d'apelles a deplacement effectue
	List<Fourmi> lesFourmis = new ArrayList<Fourmi>(); // une liste prenant des objets fourmi
	private int totalFourmis;// le nombre de fourmis total present sur la map
	private int smartOuPas; // int qui renseigne si une fourmi doit devenir intelligente ou pas au passage sur la fourmiliere
	private int vitesseSimulation; // pour choisir le temps de sleep de la simulation entre chaque deplacement
	private boolean playOuPause; // si faux le program ne tourne pas , si vrai le program tourne
	private int distanceMax; // defini la limite de la pile d'une fourmi ( sont nombre maximum de deplacement sans trouve de la nourriture)
	private int ajoutSupprimerFourmis; // quand =0 aucune modification a faire , quand >0 il y a des fourmis a jouter a la liste , inferieur a 0 des fourmis a supprimer
	private int disparitionNourriture; // si egale a 10000 les sources nourritures sont infini , a 0 ils disparaissent , de 1 a 9999 ils valents la valeur de la variable
	private int disparitionPheromone;
	private int maxNid=100;
	private int maxMeat=100;
	private int vitesseDisparitionPheromone=1;
	private boolean simulation;
	Fourmiliere (int nombredefourmi, int taille , int nourriture , int obstacles, int vitesse, boolean intelligence){ // avec les variables de l'utilisateur
		simulation=true;
		lamap= new TerrainF(taille,nourriture,obstacles); // declare un terrain defini par l'utilisateur
		this.lesFourmis=Fourmiliste(nombredefourmi); // une fourmilieres composer d'une liste d'objet Fourmi du
		totalFourmis=nombredefourmi; //inialise le nombre de fourmis total present sur la map
		vitesseSimulation=vitesse;
		if(intelligence){smartOuPas=1;//1 egale intelligent
		}
		else{smartOuPas=0;}
		playOuPause=true;
		distanceMax=taille*5;
		disparitionNourriture=10000; // on initialise les nourritues a infini
		changerNourriture(disparitionNourriture); // inialise toutes les nourritures
	}
	Fourmiliere(boolean intelligence){  // avec un terrain aleatoire et nombre de fourmi aleatoire
		simulation=true;
		lamap = new TerrainF(); // un terrain aleatoire
		int a=lamap.getTer().length; // recupere la longeur d'un cote de la map
		totalFourmis=a*10; // inialise le nombre de fourmis total present sur la map
		this.lesFourmis=Fourmiliste(totalFourmis); // une fourmiliere composer de la longeur (du terrain) au carre
		vitesseSimulation=500;
		if(intelligence){smartOuPas=1;//1 egale intelligent
		}
		else{smartOuPas=0;}
		playOuPause=true;
		distanceMax=getLamap().getTer().length*5;
		disparitionNourriture=10000;// on initialise les nourritues a infini
		changerNourriture(disparitionNourriture); // initialise toutes les nourritures
	}
	public void affichage(){
		FenetreF objetfenetre = new FenetreF(this);// l'interface graphique de la simulation
		while(simulation){// la simualtion tourne
			try {
				Thread.sleep(vitesseSimulation);
			} catch(InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
			//			System.out.println(totalFourmis);
			play(); // un deplacement pour chaque fourmi
			objetfenetre.minimum();
			objetfenetre.move();// un repaint
		}
	}
	public void play(){  // demande 1 deplacement pour chaque fourmi de la liste
		if(playOuPause){
			List<Fourmi> lesFourmis2=new ArrayList<Fourmi>(); // on applique les deplacements sur une nouvelles liste pour pouvoir supprimer une fourmi durant le parcout de cette liste
			if(ajoutSupprimerFourmis!=0){ // il a des fourmis a ajouter ou supprimer
				if (ajoutSupprimerFourmis>0){ // il ya des fourmis a ajouter a la liste
					while(ajoutSupprimerFourmis!=0){
						ajoutSupprimerFourmis--;
						ajouterFourmi();
					}
				}
				else{ // il ya des fourmis a supprimer a la liste
					while(ajoutSupprimerFourmis!=0){
						if(totalFourmis!=0){ // pour eviter d'etre en nombre negatif de fourmis
							ajoutSupprimerFourmis++;
							supprimerFourmi();
						}
					}
				}
			}
			if (!lesFourmis.isEmpty()){// la liste n'est pas vide   , il y a des fourmis
				for (Fourmi f: lesFourmis){ // on parcour la liste
					if (!deplacement(f)){ // la fourmi est enferme on la suprime
						totalFourmis--; // on retire 1 au nombre total de fourmis
					}
					else{lesFourmis2.add(f);} // on ajoute la fourmi encore vivante a la liste de transition
				}
				compteur++;
			}
			lesFourmis=lesFourmis2; // la liste de transition redevient la "vrai" liste
			reductionPheromone();
		}
		else{
			try { // petite pause pour que quand la simulation est en pause , le program ne boucle pas trop vite avec le if(false)
				Thread.sleep(300);
			}
			catch(InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}
	}
	public boolean fourmiEnfermer(Fourmi fourmiadeplacer){ // pour tester si une fourmi n'est pas enfermer 
		int x=fourmiadeplacer.getX(); // je recupere la position X actuel de la fourmi
		int y=fourmiadeplacer.getY();// je recupere la position Y actuel de la fourmi
		boolean gauche;
		boolean haut;
		boolean droit;
		boolean bas;
		if (y-1==-1) {gauche=false;}
		else {gauche = lamap.getTer()[x][y-1].isAccesible();}
		if (x-1==-1) {haut=false;}
		else {haut = lamap.getTer()[x-1][y].isAccesible();}
		if(y+1==lamap.getTer().length){droit=false;}
		else {droit = lamap.getTer()[x][y+1].isAccesible();}
		if (x+1==lamap.getTer().length) {bas=false;}
		else {bas = lamap.getTer()[x+1][y].isAccesible();}
		if (!gauche && !haut && !droit && !bas){
			lamap.getTer()[x][y].setNombreDeFourmis(lamap.getTer()[x][y].getNombreDeFourmis()-1); // la fourmi va etre supprime on retire 1 fourmis au total present sur la case
			return true;} // la fourmi est enferme

		else {return false;} // la fourmi n'est pas enferme
	}
	public void deplacementVide(Fourmi fourmiadeplacer){// la fourmi n'a pas de nourriture sur elle
		int x=fourmiadeplacer.getX(); // je recupere la position X actuel de la fourmi
		int y=fourmiadeplacer.getY();// je recupere la position Y actuel de la fourmi
		int [] tab = new int [2]; // tableau pour stocker les positions actuels dans la pile de la fourmiadeplacer
		tab[0]=x;
		tab[1]=y;
		if(!lamap.getTer()[x][y].isNid()){lamap.getTer()[x][y].setNid(true);} // elle declare la case comme accessible a la colonie
		if(lamap.getTer()[x][y].getNidValeur()<maxNid){ // limite la pheromone NID
			lamap.getTer()[x][y].setNidValeur(lamap.getTer()[x][y].getNidValeur()+1); // la fourmi cherche , donc elle ajoute de la pheromone NID
		}
		if (lamap.getTer()[x][y].isFourmiliere()){/* si la fourmi se trouve sur une fourmiliere on va effacer tous les
    deplacement de jusqu'a maintenant ( contenu dans la pile )
		 */
			fourmiadeplacer.setCompteur(0); // je remet a zero son compteur de nombre de deplacemement
			fourmiadeplacer.setCompteurDepilage(false);  // elle le droit de chercher ( donc de piler )

			if(!fourmiadeplacer.empty()){ // je verifie que ce n'est pas le tout premier deplacement de la fourmi
				while(!fourmiadeplacer.empty()){fourmiadeplacer.pop();} // on vide la pile
				verifierIntelligenceFourmiSurFourmiliere(fourmiadeplacer); // on verifie que la fourmi est dans le meme mode d'intelligence que la fourmiliere
			}
		}
		if (lamap.getTer()[x][y].isNourriture()){// si la fourmi se trouve sur une source de nourriture

			if(lamap.getTer()[x][y].getNourritureint()!=10000){ // si la case n'est pas nourriture illimite
				lamap.getTer()[x][y].setNourritureint(lamap.getTer()[x][y].getNourritureint()-1); // je retire 1 au int de la nourriture sur la case
			}
			if(lamap.getTer()[x][y].getNourritureint()==0){ // si la nourriture est epuise elle disparait
				lamap.getTer()[x][y].setNourriture(false);
			}
			fourmiadeplacer.setstockageNourriture(true);//  elle prend de la nourriture
			fourmiadeplacer.setCompteur(0); // je remet a zero son compteur de deplacement car elle va maintenant depiler
			fourmiadeplacer.setCompteurDepilage(false); // elle le droit de rentrer ( donc de depiler )
			if(lamap.getTer()[x][y].getMeat()<maxMeat){ // nombre maximal de pheromone sur une case
				lamap.getTer()[x][y].setMeat(lamap.getTer()[x][y].getMeat()+1);} // la fourmi incremante de 1 la pheromone MEAT sur la case
		}
		else{ // la fourmi n'est pas sur une source de nourriture . Maintenant on analyse les cases autour de la fourmi


			lamap.getTer()[x][y].setNombreDeFourmis(lamap.getTer()[x][y].getNombreDeFourmis()-1); // je retire 1 au nombre de fourmis present sur la case car la fourmi vas forcement bouger
			int pDd = 4; // nombre de Possibilite De Directions ( gauche , haut , droit , bas)
			boolean gauche; // sera True si la case est accessible
			boolean haut;
			boolean droit;
			boolean bas;
			int gauche1; // pour sotcker la quantite de pheromone Meat sur chaque case
			int haut1;
			int droit1;
			int bas1;
			boolean nourritureGauche=false;
			boolean nourritureHaut=false;
			boolean nourritureDroit=false;
			boolean nourritureBas=false;

			if (y-1==-1) {// empeche la fourmi de sortir du tableau a gauche
				gauche = false;gauche1=-1;}
			else {nourritureGauche=lamap.getTer()[x][y-1].isNourriture(); // la case a gauche existe
			gauche = lamap.getTer()[x][y-1].isAccesible();gauche1 = lamap.getTer()[x][y-1].getMeat();}// savoir si si la case a gauche est accessible a la fourmi ou pas
			if (x-1==-1) {// empeche la fourmi de sortir du tableau en haut
				haut = false;haut1=-1;}
			else {nourritureHaut=lamap.getTer()[x-1][y].isNourriture();
			haut = lamap.getTer()[x-1][y].isAccesible();haut1 = lamap.getTer()[x-1][y].getMeat();}
			if(y+1==lamap.getTer().length){// empeche la fourmi de sortir du tableau a droite
				droit = false;droit1=-1;}
			else {nourritureDroit=lamap.getTer()[x][y+1].isNourriture();
			droit = lamap.getTer()[x][y+1].isAccesible();droit1 = lamap.getTer()[x][y+1].getMeat();}
			if (x+1==lamap.getTer().length) { // empeche la fourmi de sortir du tableau en bas    
				bas = false;bas1=-1;}
			else {nourritureBas=lamap.getTer()[x+1][y].isNourriture();
			bas = lamap.getTer()[x+1][y].isAccesible();bas1 = lamap.getTer()[x+1][y].getMeat();}
			boolean []tableauisAccesible= new boolean[pDd]; // tableau pour sotcke les possibilite (false = obstacle , true = accessible)
			tableauisAccesible[0]=gauche;
			tableauisAccesible[1]=haut;
			tableauisAccesible[2]=droit;
			tableauisAccesible[3]=bas;
			int []tableauDeMeat= new int[pDd]; // tableau pour sotcke les valeurs de meat ( 0 = pas de meat )
			tableauDeMeat[0]=gauche1;
			tableauDeMeat[1]=haut1;
			tableauDeMeat[2]=droit1;
			tableauDeMeat[3]=bas1;
			boolean []tableauCasePrioritaire= new boolean[pDd]; // tableau pour stocker si il y a une nourriture a cote de la fourmi
			tableauCasePrioritaire[0]=nourritureGauche;
			tableauCasePrioritaire[1]=nourritureHaut;
			tableauCasePrioritaire[2]=nourritureDroit;
			tableauCasePrioritaire[3]=nourritureBas;
			boolean ilYaUneCasePrioritaire = false;
			int []tableauchoix= new int[pDd]; // tableau pour le choix de la direction
			boolean ilYaDeLaPheromoneACote = false; // pour stocker l'information : il ya de la pheromone sur au moins une case a cote de la fourmi
			for (int b = 0; b<tableauisAccesible.length; b++){ // parcour pour recuperer les cases accessibles et leur pheromones MEAT respective
				if (tableauisAccesible[b]){ // la case est accessible
					if(tableauCasePrioritaire[b]){// il y a une case nourriture a cote ( case prioritaire)
						tableauchoix[b]=-1;
						ilYaUneCasePrioritaire=true; // Il y a une nourriture a cote de la fourmi
					}
					else{
						if(tableauDeMeat[b]>0){ // la case a deja de la pheromone meat
							ilYaDeLaPheromoneACote = true; // il y a de la pheromone a cote de la fourmi
							tableauchoix[b]=tableauDeMeat[b]+1; // on stock la quantite dans le tableau des choix de possibilite , +1 car le 0 est reserve aus obstalces
						}
						else if(tableauDeMeat[b]==0){tableauchoix[b]=1;} // si il n'y a pas de pheromone sur la case
						else{tableauchoix[b]=0;} // on stock zero car la case n'est pas dans le tableau
					}
				}
				else {tableauchoix[b]=0;}// on stock 0 , car la case n'est pas accessible ( c'est donc un obstacle )

			}
			// -1 est une nourriture , 0 est un obstacle , 56 est egale a 55 de pheromone MEAT , 1 est une case simple

			if(ilYaUneCasePrioritaire){ // il y a une case prioritaire ( nourriture ) a cote de la fourmi
				int laMeilleurCase=-2;
				Random rand = new Random();
				while(laMeilleurCase==-2){ // pour un choix aleatoire si il a 2 source de nourriture a coté de la fourmi
					int pseudoRandomNumber = rand.nextInt(4);
					if (tableauchoix[pseudoRandomNumber]==-1){laMeilleurCase=pseudoRandomNumber;}
				}
				fourmiadeplacer.push(tab); // je stock la position actuel dans la pile de la fourmi avant de modifier sa position
				if (laMeilleurCase==0){fourmiadeplacer.setY(y-1);// on applique le deplacement
				lamap.getTer()[x][y-1].setNombreDeFourmis(lamap.getTer()[x][y-1].getNombreDeFourmis()+1); // j'ajoute 1 au nombre de fourmis present sur la case car la fourmi vas forcement bouger
				incrementerIntCompteur(fourmiadeplacer);
				}
				else if (laMeilleurCase==1){fourmiadeplacer.setX(x-1);
				lamap.getTer()[x-1][y].setNombreDeFourmis(lamap.getTer()[x-1][y].getNombreDeFourmis()+1);
				incrementerIntCompteur(fourmiadeplacer);
				}
				else if (laMeilleurCase==2){fourmiadeplacer.setY(y+1);
				lamap.getTer()[x][y+1].setNombreDeFourmis(lamap.getTer()[x][y+1].getNombreDeFourmis()+1);
				incrementerIntCompteur(fourmiadeplacer);
				}
				else if (laMeilleurCase==3){fourmiadeplacer.setX(x+1);
				lamap.getTer()[x+1][y].setNombreDeFourmis(lamap.getTer()[x+1][y].getNombreDeFourmis()+1);  
				incrementerIntCompteur(fourmiadeplacer);
				}
				else{}
			}
			else{// il n'ya pas de case prioritaire
				Random randam = new Random();
				int aleatoire=totalFourmis;
				if(totalFourmis<100){aleatoire=300;}
				int randomJeNeSuitPasLaPheromone = randam.nextInt(aleatoire); // dans des cas la fourmi ne cherche pas a suivre la pheromone
				if(ilYaDeLaPheromoneACote && randomJeNeSuitPasLaPheromone!=0 ){// test si il y a de la pheromone a cote de la fourmi
					int []tableauPheromones=tableauchoix; /* tableau qui stock les cases avec obstacles
                    et celle avec pheromones (et leurs quantite ) mais ou l'ancienne case de la fourmi vas etre condisdere comme un obstacle
					 */
					if(!fourmiadeplacer.empty()){ /* je verifie que la pile n'est pas vide
                                     empecher la fourmi de retourner sur la case d'ou elle vient en cas d'egalite de pheromone
                                     pour eviter les aller retours entre la fourmiliere et la case avec le plus de pheromone      
					 */
						int culDeSac=4; // nombre max de directions possible a prendre par la fourmi
						for (int i=0; i<tableauPheromones.length; i++){ // je parcours le tableau des choix pour voir si la fourmi n'est pas dans un cul de sac et donc l'autoriser a revenir sur ces pas
							if(tableauPheromones[i]==0){culDeSac--;} // je reduis le nombre de direction possible quand il y a un obstacle a cote de la fourmi ( les limites du tableau sont considere comme des obstacles)
						}
						if (culDeSac!=1){
							int []anciennePosition=fourmiadeplacer.peek(); // je recupere l'ancienne position de la fourmi
							int xAncien=anciennePosition[0];
							int yAncien=anciennePosition[1];
							empecherRetour(tableauPheromones, x , y ,xAncien , yAncien);
						}
					}
					/*int laMeilleurCase=-2;
					  while(laMeilleurCase==-2){// version aleatoire du choix de la pheromone
						Random rand = new Random();
						int aletoire= rand.nextInt(4);
						System.out.println(aletoire);
						if(tableauPheromones[aletoire]!=0){laMeilleurCase = aletoire;}
					}*/
					int laMeilleurCase=0;
					int valeurVariable=tableauPheromones[0];
					for (int c=1; c<4; c++){ // on cherche la case avec le plus de pheromone
						if (tableauPheromones[c]>valeurVariable){valeurVariable=tableauPheromones[c]; laMeilleurCase=c;}
						 
						/*		if (tableauPheromones[c]!=0){ // les cases obstacles sont eliminer du choix
							
							if (tableauPheromones[c]>tableauPheromones[c-1]){laMeilleurCase=c;}
							if (tableauPheromones[c]==tableauPheromones[c-1]){ //en cas d'egalite de concentration de pheromone MEAT entre 2 cases
								Random rand = new Random();
								int pseudoRandomNumber = rand.nextInt(1); // soit 0 soit 1 aleatoirement
								if(pseudoRandomNumber==0){
									if(tableauPheromones[c-1]!=0){laMeilleurCase=c-1;}
									else{laMeilleurCase=c;}
								}
								else{laMeilleurCase=c;}
							}
							if (tableauPheromones[c]<tableauPheromones[c-1]){laMeilleurCase=c-1;}
						}*/
					}
					fourmiadeplacer.push(tab); // je stock la position actuel dans la pile de la fourmi avant de modifier sa position
					incrementerIntCompteur(fourmiadeplacer);// la fourmi fait un deplacement de plus
					if (laMeilleurCase==0){fourmiadeplacer.setY(y-1);// on applique le deplacement
					lamap.getTer()[x][y-1].setNombreDeFourmis(lamap.getTer()[x][y-1].getNombreDeFourmis()+1); // j'ajoute 1 au nombre de fourmis present sur la case car la fourmi vas forcement bouger
					}
					else if (laMeilleurCase==1){fourmiadeplacer.setX(x-1);
					lamap.getTer()[x-1][y].setNombreDeFourmis(lamap.getTer()[x-1][y].getNombreDeFourmis()+1);
					}
					else if (laMeilleurCase==2){fourmiadeplacer.setY(y+1);
					lamap.getTer()[x][y+1].setNombreDeFourmis(lamap.getTer()[x][y+1].getNombreDeFourmis()+1);
					}
					else if (laMeilleurCase==3){fourmiadeplacer.setX(x+1);
					lamap.getTer()[x+1][y].setNombreDeFourmis(lamap.getTer()[x+1][y].getNombreDeFourmis()+1);  
					}
					else{}
				}
				else {  // il n'y a aucune case avec de la pheromone MEAT a cote , on vas choisir une case au hasard parmi celle qui sont accessible

					//on vas eviter a la fourmi de revenir sur sa case , ( pour eviter les aller retour aleatoire quand elle cherche de la nourriture)
					if(!fourmiadeplacer.empty()){ // je verifie que la pile n'est pas vide
						int culDeSac=4; // nombre max de directions possible a prendre par la fourmi
						for (int i=0; i<tableauchoix.length; i++){ // je parcours le tableau des choix pour voir si la fourmi n'est pas dans un cul de sac et donc l'autoriser a revenir sur ces pas

							if(tableauchoix[i]==0){culDeSac--;} // je reduis le nombre de direction possible quand il y a un obstacle a cote de la fourmi ( les limites du tableau sont considere comme des obstacles)
						}
						if (culDeSac!=1){ // si il y a plus d'une case accessible alors j'empeche a la fourmi de revenir en arriere
							int []anciennePosition=fourmiadeplacer.peek(); // je recupere l'ancienne position de la fourmi
							int xAncien=anciennePosition[0];
							int yAncien=anciennePosition[1];
							empecherRetour(tableauchoix, x , y ,xAncien , yAncien);

						}
					}
					boolean laboucle=true;
					while(laboucle){
						Random rand = new Random();
						int pseudoRandomNumber = rand.nextInt(4); //Un nombre aleatoire entre 0 , 1 , 2 et 3 ( nombre de direction possible -1 )
						if ((tableauchoix[pseudoRandomNumber])!=0){// le nombre choisi aleatoirement ne renvoi pas sur un obstacle donc on peut faire le deplacement
							laboucle=false; // donc on arrete la boucle
							fourmiadeplacer.push(tab); // je stock la position actuel dans la pile de la fourmi avant de modifier sa position
							if (pseudoRandomNumber==0){fourmiadeplacer.setY(y-1);// on applique le deplacement
							lamap.getTer()[x][y-1].setNombreDeFourmis(lamap.getTer()[x][y-1].getNombreDeFourmis()+1); // j'ajoute 1 au nombre de fourmis present sur la case car la fourmi vas forcement bouger
							incrementerIntCompteur(fourmiadeplacer);
							}
							else if (pseudoRandomNumber==1){fourmiadeplacer.setX(x-1);
							lamap.getTer()[x-1][y].setNombreDeFourmis(lamap.getTer()[x-1][y].getNombreDeFourmis()+1);
							incrementerIntCompteur(fourmiadeplacer);
							}
							else if (pseudoRandomNumber==2){fourmiadeplacer.setY(y+1);
							lamap.getTer()[x][y+1].setNombreDeFourmis(lamap.getTer()[x][y+1].getNombreDeFourmis()+1);
							incrementerIntCompteur(fourmiadeplacer);
							}
							else if (pseudoRandomNumber==3){fourmiadeplacer.setX(x+1);
							lamap.getTer()[x+1][y].setNombreDeFourmis(lamap.getTer()[x+1][y].getNombreDeFourmis()+1);
							incrementerIntCompteur(fourmiadeplacer);
							}
							else System.out.println("il y a un probleme au hasard");
						}

					}
				}
			}
		}

	}
	public void empecherRetour (int []tableauchoix, int x , int y ,int xAncien ,int yAncien){
		if (x==xAncien && (y-1)==yAncien ){ tableauchoix[0]=0;}
		else{
			if ((x-1)==xAncien && y==yAncien) { tableauchoix[1]=0;}
			else{
				if (x==xAncien && (y+1)==yAncien ) { tableauchoix[2]=0;}
				else{
					if ((x+1)==xAncien && y==yAncien) { tableauchoix[3]=0;}
				}
			}
		}
	}
	public void deplacementPleine(Fourmi fourmiadeplacer){ // sans intelligence
		// la fourmi a de la nourriture
		int x=fourmiadeplacer.getX(); // je recupere la position X actuel de la fourmi
		int y=fourmiadeplacer.getY();// je recupere la position Y actuel de la fourmi

		if(lamap.getTer()[x][y].getMeat()<maxMeat){  lamap.getTer()[x][y].setMeat(lamap.getTer()[x][y].getMeat()+1); }// la fourmi incremante de 1 la pheromone MEAT sur la case

		if (lamap.getTer()[x][y].isFourmiliere()){// si on se trouve sur une fourmiliere
			verifierIntelligenceFourmiSurFourmiliere(fourmiadeplacer); // on verifie que la fourmi est dans le meme mode d'intelligence que la fourmiliere
			while(!fourmiadeplacer.empty()){fourmiadeplacer.pop();} // on vide la pile
			fourmiadeplacer.setstockageNourriture(false);//  elle lache sa nourriture
			nourritureAquise++; // la quantite de nourriture aquise par la fourmiliere est incremente
		}
		depilage(fourmiadeplacer);
	}
	public void incrementerIntCompteur(Fourmi fourmiadeplacer){
		// la fourmi a realiser un mouvement de plus
		fourmiadeplacer.setCompteur(fourmiadeplacer.getCompteur()+1);
	}
	public void deplacementRetourBredouille(Fourmi fourmiadeplacer){
		int x=fourmiadeplacer.getX(); // je recupere la position X actuel de la fourmi
		int y=fourmiadeplacer.getY();// je recupere la position Y actuel de la fourmi
		if (lamap.getTer()[x][y].isFourmiliere()){// si on se trouve sur une fourmiliere
			verifierIntelligenceFourmiSurFourmiliere(fourmiadeplacer); // on verifie que la fourmi est dans le meme mode d'intelligence que la fourmiliere
			while(!fourmiadeplacer.empty()){fourmiadeplacer.pop();} // on vide la pile
			fourmiadeplacer.setCompteur(0); // on remet a zero le nombre de deplacement de la fourmi
			fourmiadeplacer.setCompteurDepilage(false); // la fourmi a re-le droit de piler ( de chercher )
		}
		depilage(fourmiadeplacer); // la fourmi depile pour rentrer a la fourmiliere
	}
	public boolean deplacement (Fourmi fourmiadeplacer){
		boolean nourriture= fourmiadeplacer.getstockageNourriture(); //recupere si la fourmi a de la nourriture ou pas
		int intelligence = fourmiadeplacer.getSmart(); // savoir si la fourmi est intelligente ou pas
		if (fourmiEnfermer(fourmiadeplacer)){return false;} // la fourmi est enferme il faut la supprimer
		else{
			if (intelligence==1){
				if (fourmiadeplacer.isCompteurDepilage()){ deplacementRetourBredouille(fourmiadeplacer);} //  si la fourmi a chercher trop longtemp elle depile pour rentrer a la fourmilliere
				else {

					if (fourmiadeplacer.getCompteur()==distanceMax){fourmiadeplacer.setCompteurDepilage(true);} // la fourmi a chercher trop lontemps sans trouve
					if (nourriture==false){deplacementVide(fourmiadeplacer);}
					if(nourriture){deplacementPleine(fourmiadeplacer);}
					return true; // la fourmi n'etait pas enferme
				}
				return true; // la fourmi n'etait pas enferme
			}
			if(intelligence==0){

				if (nourriture==false){deplacementVideNormal(fourmiadeplacer);}
				if(nourriture){deplacementPleineNormal(fourmiadeplacer);}
				return true;  // la fourmi n'etait pas enferme
			}
			else{ System.out.println("il y a un probleme sur le choix d'int pour l'algo d'intelligence de la fourmi");
			return false; // la fourmi doit mourrir !!
			}
		}
	}
	public boolean [] testAlentours(boolean NidOuMeat, Fourmi fourmiadeplacer){// methode qui renvoi un tableau avec le choix de la direction de la fourmi
		boolean []tableauchoixBoolean=new boolean[4]; // true sur la case ou la fourmi devra aller , false sur les autres
		tableauchoixBoolean[0]=false;
		tableauchoixBoolean[1]=false;
		tableauchoixBoolean[2]=false;
		tableauchoixBoolean[3]=false;
		int [] tab = new int [2]; // tableau pour stocker les positions actuels dans la pile de la fourmiadeplacer

		int x=fourmiadeplacer.getX(); // je recupere la position X actuel de la fourmi
		int y=fourmiadeplacer.getY();// je recupere la position Y actuel de la fourmi
		lamap.getTer()[x][y].setNombreDeFourmis(lamap.getTer()[x][y].getNombreDeFourmis()-1); // je retire 1 au nombre de fourmis present sur la case car la fourmi vas forcement bouger
		tab[0]=x;
		tab[1]=y;
		int pDd = 4; // nombre de Possibilite De Directions ( gauche , haut , droit , bas)
		boolean gauche;
		boolean haut;
		boolean droit;
		boolean bas;
		boolean fourmiliereGauche=false;
		boolean fourmiliereHaut=false;
		boolean fourmiliereDroit=false;
		boolean fourmiliereBas=false;
		boolean nourritureGauche=false;
		boolean nourritureHaut=false;
		boolean nourritureDroit=false;
		boolean nourritureBas=false;
		int gaucheNid=-1; // pour sotcker la quantite de pheromone Meat sur chaque case
		int hautNid=-1;
		int droitNid=-1;
		int basNid=-1;
		int gaucheMeat=-1;
		int hautMeat=-1;
		int droitMeat=-1;
		int basMeat=-1;

		if (y-1==-1) {// empeche la fourmi de sortir du tableau a gauche
			gauche = false;gaucheNid=0;gaucheMeat=0;}
		else {
			gauche = lamap.getTer()[x][y-1].isAccesible();
			if(NidOuMeat){gaucheNid = lamap.getTer()[x][y-1].getNidValeur();
			fourmiliereGauche=lamap.getTer()[x][y-1].isFourmiliere();}
			else{gaucheMeat = lamap.getTer()[x][y-1].getMeat();
			nourritureGauche=lamap.getTer()[x][y-1].isNourriture();}
		}

		if (x-1==-1) {// empeche la fourmi de sortir du tableau en haut
			haut = false;hautNid=0;hautMeat=0;}
		else {
			haut = lamap.getTer()[x-1][y].isAccesible();
			if(NidOuMeat){hautNid = lamap.getTer()[x-1][y].getNidValeur();
			fourmiliereHaut=lamap.getTer()[x-1][y].isFourmiliere();}
			else{hautMeat = lamap.getTer()[x-1][y].getMeat();
			nourritureHaut=lamap.getTer()[x-1][y].isNourriture();}
		}

		if(y+1==lamap.getTer().length){// empeche la fourmi de sortir du tableau a droite
			droit = false;droitNid=0;droitMeat=0;}
		else {
			droit = lamap.getTer()[x][y+1].isAccesible();
			if(NidOuMeat){droitNid = lamap.getTer()[x][y+1].getNidValeur();
			fourmiliereDroit=lamap.getTer()[x][y+1].isFourmiliere();}
			else{droitMeat = lamap.getTer()[x][y+1].getMeat();
			nourritureDroit=lamap.getTer()[x][y+1].isNourriture();}
		}

		if (x+1==lamap.getTer().length) { // empeche la fourmi de sortir du tableau en bas    
			bas = false;basNid=0;basMeat=0;}
		else {
			bas = lamap.getTer()[x+1][y].isAccesible();
			if(NidOuMeat){basNid = lamap.getTer()[x+1][y].getNidValeur();
			fourmiliereBas=lamap.getTer()[x+1][y].isFourmiliere();}
			else{basMeat = lamap.getTer()[x+1][y].getMeat();
			nourritureBas=lamap.getTer()[x+1][y].isNourriture();}
		}

		boolean []tableauisAccesible= new boolean[pDd]; // tableau pour sotcke les possibilite (false = obstacle , true = accessible)
		tableauisAccesible[0]=gauche;
		tableauisAccesible[1]=haut;
		tableauisAccesible[2]=droit;
		tableauisAccesible[3]=bas;

		boolean []tableauCasePrioritaire= new boolean[pDd];
		int []tableauDePheromones= new int[pDd];

		if(NidOuMeat){ // je cherche de la NID
			tableauCasePrioritaire[0]=fourmiliereGauche;
			tableauCasePrioritaire[1]=fourmiliereHaut;
			tableauCasePrioritaire[2]=fourmiliereDroit;
			tableauCasePrioritaire[3]=fourmiliereBas;
			tableauDePheromones[0]=gaucheNid;
			tableauDePheromones[1]=hautNid;
			tableauDePheromones[2]=droitNid;
			tableauDePheromones[3]=basNid;}
		else{ // je cherche de la MEAT
			tableauCasePrioritaire[0]=nourritureGauche;
			tableauCasePrioritaire[1]=nourritureHaut;
			tableauCasePrioritaire[2]=nourritureDroit;
			tableauCasePrioritaire[3]=nourritureBas;
			tableauDePheromones[0]=gaucheMeat;
			tableauDePheromones[1]=hautMeat;
			tableauDePheromones[2]=droitMeat;
			tableauDePheromones[3]=basMeat;}

		int []tableauchoix= new int[pDd]; // tableau pour le choix de la direction


		boolean ilYaDeLaPheromoneVoulut = false; // pour stocker l'information : il ya de la pheromone sur au moins une case a cote de la fourmi
		boolean ilYaUneCasePrioritaire = false;


		for (int b = 0; b<tableauisAccesible.length; b++){ // parcour pour recuperer les cases accessibles et leur pheromones MEAT respective
			if (tableauisAccesible[b]){ // la case est accessible
				if(tableauCasePrioritaire[b]){// il y a une fourmiliere ou une case nourriture a cote ( case prioritaire)
					tableauchoix[b]=-1;
					ilYaUneCasePrioritaire=true;
				}
				else{
					if(tableauDePheromones[b]>0){ // la case a deja de la pheromone meat
						ilYaDeLaPheromoneVoulut = true; // il y a de la pheromone a cote de la fourmi
						tableauchoix[b]=tableauDePheromones[b]+1; // on stock la quantite dans le tableau des choix de possibilite , +1 car le 0 est reserve aus obstalces
					}
					if(tableauDePheromones[b]==0){tableauchoix[b]=1;} // si il n'y a pas de pheromone sur la case
				}
			}
			else {tableauchoix[b]=0;}// on stock 0 , car la case n'est pas accessible ( c'est donc un obstacle )
		}

		if(ilYaUneCasePrioritaire){
			fourmiadeplacer.push(tab);
			int laMeilleurCase=0;

			for (int c=1; c<tableauchoix.length; c++){ // on cherche la case avec une fourmiliere ou de la nourriture
				if (tableauchoix[c]!=0){ // les cases obstacles sont eliminer du choix
					if (tableauchoix[c]==-1){laMeilleurCase=c;}

				}
			}
			if (laMeilleurCase==0){tableauchoixBoolean[0]=true;// on applique le deplacement            
			lamap.getTer()[x][y-1].setNombreDeFourmis(lamap.getTer()[x][y-1].getNombreDeFourmis()+1); // j'ajoute 1 au nombre de fourmis present sur la case car la fourmi vas forcement bouger

			}
			if (laMeilleurCase==1){tableauchoixBoolean[1]=true;
			lamap.getTer()[x-1][y].setNombreDeFourmis(lamap.getTer()[x-1][y].getNombreDeFourmis()+1);

			}
			if (laMeilleurCase==2){tableauchoixBoolean[2]=true;
			lamap.getTer()[x][y+1].setNombreDeFourmis(lamap.getTer()[x][y+1].getNombreDeFourmis()+1);

			}
			if (laMeilleurCase==3){tableauchoixBoolean[3]=true;
			lamap.getTer()[x+1][y].setNombreDeFourmis(lamap.getTer()[x+1][y].getNombreDeFourmis()+1);  

			}

		}
		else{
			Random randam = new Random();
			int jeNeSuisPasLaPheromone = randam.nextInt(10);
			if(ilYaDeLaPheromoneVoulut && jeNeSuisPasLaPheromone!=0){

				if(!fourmiadeplacer.empty()){

					int culDeSac=4; // nombre max de directions possible a prendre par la fourmi
					for (int i=0; i<tableauchoix.length; i++){ // je parcours le tableau des choix pour voir si la fourmi n'est pas dans un cul de sac et donc l'autoriser a revenir sur ces pas

						if(tableauchoix[i]==0){culDeSac--;} // je reduis le nombre de direction possible quand il y a un obstacle a cote de la fourmi ( les limites du tableau sont considere comme des obstacles)
					}
					if (culDeSac!=1){
						int []anciennePosition=fourmiadeplacer.peek(); // je recupere l'ancienne position de la fourmi
						fourmiadeplacer.pop();

						int xAncien=anciennePosition[0];
						int yAncien=anciennePosition[1];
						if (x==xAncien && (y-1)==yAncien ){ tableauchoix[0]=0;} //System.out.println("jy etais");
						if ((x-1)==xAncien && y==yAncien) { tableauchoix[1]=0;}
						if (x==xAncien && (y+1)==yAncien ) { tableauchoix[2]=0;} 
						if ((x+1)==xAncien && y==yAncien) { tableauchoix[3]=0;} 
					}

				}
				int laMeilleurCase=0;
				int aleatoire = (int)( Math.random()*( 3- 1 + 1 ) ) + 1; // on rend aleatoire le choix de la case avec pheromone
				for (int c=0; c<tableauchoix.length; c++){
					tableauchoix[c]=tableauchoix[c]*aleatoire;
				}
				int valeurVariable=tableauchoix[0];
				for (int c=1; c<4; c++){ // on cherche la case avec le plus de pheromone
					if (tableauchoix[c]>valeurVariable){valeurVariable=tableauchoix[c]; laMeilleurCase=c;}
					
				}

				fourmiadeplacer.push(tab);

				if (laMeilleurCase==0){tableauchoixBoolean[0]=true;// on applique le deplacement            
				lamap.getTer()[x][y-1].setNombreDeFourmis(lamap.getTer()[x][y-1].getNombreDeFourmis()+1); // j'ajoute 1 au nombre de fourmis present sur la case car la fourmi vas forcement bouger

				}
				if (laMeilleurCase==1){tableauchoixBoolean[1]=true;
				lamap.getTer()[x-1][y].setNombreDeFourmis(lamap.getTer()[x-1][y].getNombreDeFourmis()+1);

				}
				if (laMeilleurCase==2){tableauchoixBoolean[2]=true;
				lamap.getTer()[x][y+1].setNombreDeFourmis(lamap.getTer()[x][y+1].getNombreDeFourmis()+1);

				}
				if (laMeilleurCase==3){tableauchoixBoolean[3]=true;
				lamap.getTer()[x+1][y].setNombreDeFourmis(lamap.getTer()[x+1][y].getNombreDeFourmis()+1);  

				}
			}

			else{

				if(!fourmiadeplacer.empty()){

					int culDeSac=4; // nombre max de directions possible a prendre par la fourmi
					for (int i=0; i<tableauchoix.length; i++){ // je parcours le tableau des choix pour voir si la fourmi n'est pas dans un cul de sac et donc l'autoriser a revenir sur ces pas

						if(tableauchoix[i]==0){culDeSac--;} // je reduis le nombre de direction possible quand il y a un obstacle a cote de la fourmi ( les limites du tableau sont considere comme des obstacles)
					}
					if (culDeSac!=1){
						int []anciennePosition=fourmiadeplacer.peek(); // je recupere l'ancienne position de la fourmi
						fourmiadeplacer.pop();
						int xAncien=anciennePosition[0];
						int yAncien=anciennePosition[1];
						if (x==xAncien && (y-1)==yAncien ){ tableauchoix[0]=0;}
						if ((x-1)==xAncien && y==yAncien) { tableauchoix[1]=0;}
						if (x==xAncien && (y+1)==yAncien ) { tableauchoix[2]=0;} 
						if ((x+1)==xAncien && y==yAncien) { tableauchoix[3]=0;} 
					}

				}
				boolean laboucle=true;
				fourmiadeplacer.push(tab);
				while(laboucle){                                                              
					Random rand = new Random();
					int pseudoRandomNumber = rand.nextInt(4); //Un nombre aleatoire entre 0 , 1 , 2 et 3 ( nombre de direction possible -1 )

					if ((tableauchoix[pseudoRandomNumber])!=0){// le nombre choisi aleatoirement ne renvoi pas sur un obstacle donc on peut faire le deplacement

						laboucle=false; // donc on arrete la boucle

						if (pseudoRandomNumber==0){tableauchoixBoolean[0]=true;// on applique le deplacement
						lamap.getTer()[x][y-1].setNombreDeFourmis(lamap.getTer()[x][y-1].getNombreDeFourmis()+1); // j'ajoute 1 au nombre de fourmis present sur la case car la fourmi vas forcement bouger

						}
						if (pseudoRandomNumber==1){tableauchoixBoolean[1]=true;
						lamap.getTer()[x-1][y].setNombreDeFourmis(lamap.getTer()[x-1][y].getNombreDeFourmis()+1);

						}
						if (pseudoRandomNumber==2){tableauchoixBoolean[2]=true;
						lamap.getTer()[x][y+1].setNombreDeFourmis(lamap.getTer()[x][y+1].getNombreDeFourmis()+1);

						}
						if (pseudoRandomNumber==3){tableauchoixBoolean[3]=true;
						lamap.getTer()[x+1][y].setNombreDeFourmis(lamap.getTer()[x+1][y].getNombreDeFourmis()+1);
						}
					}

				}
			}
		}
		return tableauchoixBoolean;
	}
	public void deplacementVideNormal(Fourmi fourmiadeplacer){

		int x=fourmiadeplacer.getX(); // je recupere la position X actuel de la fourmi
		int y=fourmiadeplacer.getY();// je recupere la position Y actuel de la fourmi

		if (lamap.getTer()[x][y].isNourriture()){// si la fourmi se trouve sur une source de nourriture
			fourmiadeplacer.setstockageNourriture(true);//  elle prend de la nourriture
			if(!fourmiadeplacer.empty()){while(fourmiadeplacer.empty()){fourmiadeplacer.pop();}}
		}
		if (lamap.getTer()[x][y].isFourmiliere()){// si on se trouve sur une fourmiliere
			verifierIntelligenceFourmiSurFourmiliere(fourmiadeplacer); // on verifie que la fourmi est dans le meme mode d'intelligence que la fourmiliere
		}

		// la fourmi n'est pas sur une source de nourriture. Maintenant on analyse les cases autour de la fourmi

		if(lamap.getTer()[x][y].getNidValeur()<maxNid){ // limite  la pheromone NID
			lamap.getTer()[x][y].setNidValeur(lamap.getTer()[x][y].getNidValeur()+1); // je cherche , donc j'ajoute de la pheromone NID
		}
		boolean NidOuMeat=false;
		boolean[] tableauDeLadirectionAprendre=testAlentours(NidOuMeat,fourmiadeplacer);
		if (tableauDeLadirectionAprendre[0]==true){fourmiadeplacer.setY(y-1);}// on applique le deplacement
		if (tableauDeLadirectionAprendre[1]==true){fourmiadeplacer.setX(x-1);}
		if (tableauDeLadirectionAprendre[2]==true){fourmiadeplacer.setY(y+1);}
		if (tableauDeLadirectionAprendre[3]==true){fourmiadeplacer.setX(x+1);}

	}
	public void deplacementPleineNormal(Fourmi fourmiadeplacer){ //sans intelligence
		// la fourmi a de la nourriture
		int x=fourmiadeplacer.getX(); // je recupere la position X actuel de la fourmi
		int y=fourmiadeplacer.getY();// je recupere la position Y actuel de la fourmi

		if (lamap.getTer()[x][y].isFourmiliere()){// si on se trouve sur une fourmiliere
			fourmiadeplacer.setstockageNourriture(false);//  elle lache sa nourriture
			nourritureAquise++; // la quantite de nourriture aquise par la fourmiliere est incremente
			// System.out.println("NOURRITURE DEPOSER");
			while(fourmiadeplacer.empty()){fourmiadeplacer.pop();}
			verifierIntelligenceFourmiSurFourmiliere(fourmiadeplacer); // on verifie que la fourmi est dans le meme mode d'intelligence que la fourmiliere

		}
		else{
			if(lamap.getTer()[x][y].getMeat()<maxMeat){ // nombre maximal de pheromone sur une case
				lamap.getTer()[x][y].setMeat(lamap.getTer()[x][y].getMeat()+1); // la fourmi incremante de 1 la pheromone MEAT sur la case
			}
			boolean NidOuMeat=true;
			boolean[] tableauDeLadirectionAprendre=testAlentours(NidOuMeat,fourmiadeplacer);
			if (tableauDeLadirectionAprendre[0]==true){fourmiadeplacer.setY(y-1);}// on applique le deplacement
			if (tableauDeLadirectionAprendre[1]==true){fourmiadeplacer.setX(x-1);}
			if (tableauDeLadirectionAprendre[2]==true){fourmiadeplacer.setY(y+1);}
			if (tableauDeLadirectionAprendre[3]==true){fourmiadeplacer.setX(x+1);}
		}

	}
	public void depilage (Fourmi fourmiadeplacer){

		if(!fourmiadeplacer.empty()){ // verifie si la pile n'est pas vide

			int []positionDansLaPile=fourmiadeplacer.peek(); // je recupere l'ancienne position de la fourmi
			int x=positionDansLaPile[0];
			int y=positionDansLaPile[1];

			int postionXdelafourmis=fourmiadeplacer.getX(); // je recupere la position x actuel de la fourmi ( avant le deplacement dus au depilage )
			int postionYdelafourmis=fourmiadeplacer.getY();

			if(!lamap.getTer()[x][y].isAccesible()){ // si la fourmi rencontre un obstacle durant son depilage

				fourmiadeplacer.setSmart(0); // la fourmi devient NON intelligente pour regagner la fourmiliere
			}
			else{
				lamap.getTer()[postionXdelafourmis][postionYdelafourmis].setNombreDeFourmis(lamap.getTer()[postionXdelafourmis][postionYdelafourmis].getNombreDeFourmis()-1); // je retire 1 au nombre de fourmis present sur la case
				fourmiadeplacer.pop();// je depile la position actuel de la fourmi de la pile de l'objet fourmi
				fourmiadeplacer.setX(x);
				fourmiadeplacer.setY(y);
				lamap.getTer()[x][y].setNombreDeFourmis(lamap.getTer()[x][y].getNombreDeFourmis()+1); // je rajoute 1 au nombre de fourmis present sur la case
			}
		}
	}
	public List<Fourmi> Fourmiliste (int nombre){ // cree une liste avec en argument le nombre de fourmis)
		//Toutes les fourmis sont initialise a la position x et y de la fourmiliere
		int x=lamap.getFourmiliere()[0][0];; // position x de la fourmiliere
		int y=lamap.getFourmiliere()[0][1];; // position y de la fourmiliere
		lamap.getTer()[x][y].setNombreDeFourmis(nombre); // je met le nombre de fourmi sur la case fourmilliere
		for(int i = 0; i<nombre; i++){
			Fourmi unefourmi = new Fourmi(x,y,1); // une nouvelle fourmi non intelligente
			lesFourmis.add(unefourmi); // que je stock dans la liste
		}
		return lesFourmis; // je retourne la liste de taille "nombre" fourmi
	}
	public void reductionPheromoneQuantite(int n){ // reduit la pheromone sur les cases
		for(int axeX=0 ; axeX< lamap.getTer().length; axeX++){
			for (int axeY=0 ; axeY<lamap.getTer().length; axeY++){ // si il y a de la pheromone MEAT
				if (lamap.getTer()[axeX][axeY].getMeat()>n){
					lamap.getTer()[axeX][axeY].setMeat(lamap.getTer()[axeX][axeY].getMeat()-n);
				}
				else{
					lamap.getTer()[axeX][axeY].setMeat(0);
				}
				if (lamap.getTer()[axeX][axeY].getNidValeur()>n){
					lamap.getTer()[axeX][axeY].setNidValeur(lamap.getTer()[axeX][axeY].getNidValeur()-n);
				}
				else{
					lamap.getTer()[axeX][axeY].setMeat(0);
				}
			}
		}
	}
	public void reductionPheromone(){ // demande la reduction de Pheromone en fonction du nombre de fourmis et de la vitesse voulut par l'utilisateur
		int n=totalFourmis;

		if(n>50 && vitesseDisparitionPheromone!=1  &&vitesseDisparitionPheromone<90 && compteur%20==0){
			reductionPheromoneQuantite(vitesseDisparitionPheromone);
		}
		else if (vitesseDisparitionPheromone>90){reductionPheromoneQuantite(vitesseDisparitionPheromone);}
		else{
			if(compteur%100==0){
				reductionPheromoneQuantite(1);}
		}
	}
	public void changerNourriture(int quantite){ // methode qui va change la quantite des nourritures
		for(int axeX=0 ; axeX< lamap.getTer().length; axeX++){
			for (int axeY=0 ; axeY<lamap.getTer().length; axeY++){// je parcour les cases
				if(lamap.getTer()[axeX][axeY].isNourriture()){ // si la case est une case nourriture
					lamap.getTer()[axeX][axeY].setNourritureint(quantite); // elle prends la valeur maximum indique

				}
			}
		}
	}
	public void inverserIntelligente(){// methode a relier au bouton  checkbox pour inverser l'intelligence des fourmis
		if(getSmartOuPas()==0){
			setSmartOuPas(1);
		}
		else{setSmartOuPas(0);}
	}
	public void ajouterFourmi(){ // methode qui ajoute 10% ou 1 fourmi
		int nombreFourmis=lesFourmis.size();
		int x=lamap.getFourmiliere()[0][0];; // position x de la fourmiliere
		int y=lamap.getFourmiliere()[0][1];; // position y de la fourmiliere
		if (nombreFourmis<10){ // on ajoute des fourmis une par une si il y en a moins de 10
			ajouterUneFourmi(x,y);
		}
		else{
			nombreFourmis=nombreFourmis/10; // on va ajouter 10% de fourmis en plus
			for(int i = 0; i<nombreFourmis; i++){
				ajouterUneFourmi(x,y);
			}
		}
	}
	public void ajouterUneFourmi(int x , int y){ // methode qui ajouter 1 nouvelle fourmi a la fourmiliere
		Fourmi unefourmi = new Fourmi(x,y,smartOuPas); // une nouvelle fourmi  
		lesFourmis.add(unefourmi); // que je stock dans la liste
		totalFourmis++;
		lamap.getTer()[x][y].setNombreDeFourmis(lamap.getTer()[x][y].getNombreDeFourmis()+1); // on ajoute une fourmis a la case sur la quel elle etait (pour l'affichage)

	}
	public void ajouterUneFourmiMan(int x , int y){ // methode qui ajouter 1 nouvelle fourmi a la fourmiliere
		Fourmi unefourmi = new Fourmi(x,y,0); // une nouvelle fourmi  
		lesFourmis.add(unefourmi); // que je stock dans la liste
		totalFourmis++;
		lamap.getTer()[x][y].setNombreDeFourmis(lamap.getTer()[x][y].getNombreDeFourmis()+1); // on ajoute une fourmis a la case sur la quel elle etait (pour l'affichage)

	}
	public void supprimerFourmi(){// methode qui supprime 10% ou 1 fourmi
		int nombreFourmis=lesFourmis.size();
		if (!lesFourmis.isEmpty()){
			if (nombreFourmis<10){
				supprimerUneFourmi();
			}
			else{
				nombreFourmis=nombreFourmis/10; // on va retirer 10%
				for ( int i=0; i<nombreFourmis ; i++){
					if (!lesFourmis.isEmpty()){
						supprimerUneFourmi();
					}
				}
			}
		}
	}
	public void supprimerUneFourmiManuel(int x ,int y){ // position manuel sur la map
		if (lamap.getTer()[x][y].getNombreDeFourmis()>0){
			int compteur=0;
			for (Fourmi f: lesFourmis){// on parcour la liste
				if(f.getX()==x && f.getY()==y){ // quand on trouve une fourmi sur la case x y
					lesFourmis.remove(compteur); //on la supprime de la liste
					lamap.getTer()[x][y].setNombreDeFourmis(lamap.getTer()[x][y].getNombreDeFourmis()-1); // supprimer de l'affichage
					totalFourmis--; // supprimer du compteur de nombre de fourmis
					break; // on arrete de chercher
				}
				compteur++;
			}
		}
	}
	public void supprimerUneFourmi(){ // methode qui retire 1 fourmi a la fourmiliere
		Fourmi aSuprimer = lesFourmis.get(0); // je recupere une fourmi a suprimer
		int x=aSuprimer.getX();
		int y=aSuprimer.getY();
		lamap.getTer()[x][y].setNombreDeFourmis(lamap.getTer()[x][y].getNombreDeFourmis()-1); // on retire une fourmis a la case sur la quel elle etait (pour l'affichage)
		lesFourmis.remove(0);  // je supprime la fourmis de la liste
		totalFourmis--; // on retire 1 au nombre total de fourmis
	}
	public int totalFourmi(){ // methode donnant le nombre de fourmis de total sur la map
		int nombredefourmisautotal=0;
		for(int axeX=0 ; axeX< getLamap().getTer().length ; axeX++){
			for (int axeY=0 ; axeY<getLamap().getTer().length; axeY++){
				nombredefourmisautotal=nombredefourmisautotal+getLamap().getTer()[axeX][axeY].getNombreDeFourmis();
			}
		}
		return nombredefourmisautotal;
	}
	public void verifierIntelligenceFourmiSurFourmiliere(Fourmi fourmiadeplacer){// cette methode s'apelle des qu'une fourmi passe sur la fourmiliere
		if(getSmartOuPas()!=fourmiadeplacer.getSmart()){ // si la fourmi est dans un mode d'intelligence different de la fourmiliere

			fourmiadeplacer.setSmart(getSmartOuPas()); // elle se met dans le mode d'intelligence de la fourmiliere
		}
		if(getSmartOuPas()==0){while(!fourmiadeplacer.empty()){fourmiadeplacer.pop();} // si la fourmi devient non intelligente , on lui vide sa pile
		fourmiadeplacer.setCompteur(0); // je remet a zero les viarables d'inteligence de la fourmi
		fourmiadeplacer.setCompteurDepilage(false);
		}
	}
	// les get et set de la fourmilliere
	public int getNourritureAquise() {
		return nourritureAquise;
	}
	public void setNourritureAquise(int nourritureAquise) {
		this.nourritureAquise = nourritureAquise;
	}
	public TerrainF getLamap() {
		return lamap;
	}
	public void setLamap(TerrainF lamap) {
		this.lamap = lamap;
	}
	public int getCompteur() {
		return compteur;
	}
	public void setCompteur(int compteur) {
		this.compteur = compteur;
	}
	public int getTotalFourmis() {
		return totalFourmis;
	}
	public void setTotalFourmis(int totalFourmis) {
		this.totalFourmis = totalFourmis;
	}
	public int getSmartOuPas() {
		return smartOuPas;
	}
	public void setSmartOuPas(int smartOuPas) {
		this.smartOuPas = smartOuPas;
	}
	public int getVitesseSimulation() {
		return vitesseSimulation;
	}
	public void setVitesseSimulation(int vitesseSimulation) {
		this.vitesseSimulation = vitesseSimulation;
	}
	public boolean isPlayOuPause() {
		return playOuPause;
	}
	public void setPlayOuPause(boolean playOuPause) {
		this.playOuPause = playOuPause;
	}
	public int getDistanceMax() {
		return distanceMax;
	}
	public void setDistanceMax(int distanceMax) {
		this.distanceMax = distanceMax;
	}
	public int getAjoutSupprimerFourmis() {
		return ajoutSupprimerFourmis;
	}
	public void setAjoutSupprimerFourmis(int ajoutSupprimerFourmis) {
		this.ajoutSupprimerFourmis = ajoutSupprimerFourmis;
	}
	public int getDisparitionNourriture() {
		return disparitionNourriture;
	}
	public void setDisparitionNourriture(int disparitionNourriture) {
		this.disparitionNourriture = disparitionNourriture;
	}
	public int getDisparitionPheromone() {
		return disparitionPheromone;
	}
	public void setDisparitionPheromone(int disparitionPheromone) {
		this.disparitionPheromone = disparitionPheromone;
	}
	public int getMaxMeat() {
		return maxMeat;
	}
	public void setMaxMeat(int maxMeat) {
		this.maxMeat = maxMeat;
	}
	public int getMaxNid() {
		return maxNid;
	}
	public void setMaxNid(int maxNid) {
		this.maxNid = maxNid;
	}
	public void setMaxPheromone(int max){
		setMaxNid(max);
		setMaxMeat(max);
	}
	public int getMaxPheromone(){
		return getMaxMeat();
	}
	public int getVitesseDisparitionPheromone() {
		return vitesseDisparitionPheromone;
	}
	public void setVitesseDisparitionPheromone(int vitesseDisparitionPheromone) {
		this.vitesseDisparitionPheromone = vitesseDisparitionPheromone;
	}
	public boolean isSimulation() {
		return simulation;
	}
	public void setSimulation(boolean simulation) {
		this.simulation = simulation;
	}
}