package IK3;
import java.util.Random;
/**
 * TerrainF represente un terrain avec les source de nourritures,les obstacles et les pheromone laissee par les foumis.
 * @see Fourmiliere
 * @author Quiroz,Al-qazweny
 */
import java.util.Stack;
public class TerrainF {
	/**
	 * Le terrain est representer sour forme de tableau de Case. 
	 * @see Case
	 */
	private Case [][] ter;
	/**
	 * ce tableau represente la possition de la foumiliere dans le tableau ter.
	 */
	private int [][] fourmiliere=new int [1][2];
	/**
	 * represente le nombre d'obstacle dans ce terrain.
	 */
	private int nbObstacle;
	/**
	 * represente le nombre de source de nourriture dans ce terrain.
	 */
	private int nbNourriture;	
	/**
	 * <p>
	 * Cree un tableau de case representant ce terrain.
	 * </p>
	 * @see Case
	 */
	public TerrainF() { // un Terrain aleatoire.
		Random alea=new Random();
		Stack<int[]> back=new Stack<int[]>();
		int largeurMini=20; //largeur mini du terrain
		int largeurMax=179; //Largeur max du terrain.
		double maxObstacle=0.2; //Pourcentage maximun d'obstacle par rapport au nombre de case du tableau ter.	
		double minObstacle=0.03; //Pourcentage minimal d'obstacle par rapport au nombre de case du tableau ter.
		boolean terInvalide=false; // Boolean qui nous sert a verifier que le terrain soit 100% praticable.
		while(!terInvalide) // Je recree un terrain temps que le terrain n'est pas 100% praticable.
		{
			int largeur=aleaInt(largeurMax,largeurMini,alea);	
			int nombreCase=largeur*largeur;
			int obstacle=aleaInt((int)(nombreCase*maxObstacle),(int)(nombreCase*minObstacle),alea);
			int nourriture=1;
			if(largeur<30)
				nourriture=aleaInt(15,3,alea);			
			else if(largeur<51){
					nourriture=aleaInt(20,7,alea);
			}
			else if (largeur<100){
				nourriture=aleaInt(30,10,alea);
			}
			else 
				nourriture=aleaInt(30,15,alea);
			ter=new Case[largeur][largeur];//creation de Tableau de Case
			for(int i=0;i<ter.length;i++) 
				for(int j=0;j<ter[i].length;j++){
					ter[i][j]=new Case();
				}
			fourmiliere[0][0]=aleaInt(largeur-1,0,alea);
			fourmiliere[0][1]=aleaInt(largeur-1,0,alea);
			ter [fourmiliere[0][0]][fourmiliere[0][1]].setFourmiliere(true);//Place la foumiliere
			placerObstacle(obstacle,alea);
			resoud(fourmiliere[0][0],fourmiliere[0][1],back);

			terInvalide=rendAccessible(back);
			if(terInvalide)
				placerNourriture(nourriture,alea);
			nbObstacle();
			nbNourriture();
		}
	}
	/**
	 * <p>
	 * cree un tableau de Case representant ce terrain.
	 * </p>
	 * @param largeur
	 * 		largeur du terrain. (le terrain sera toujours carre).
	 * @param nourriture
	 * 		nombre de source de nourriture dans ce terrain.
	 * @param obstacle
	 * 		nombre d'obstacle dans ce terrain.
	 */
	public TerrainF(int largeur,int nourriture,int obstacle){//Cree un terrain manuellement.
		Random alea=new Random(); 
		Stack<int[]> back=new Stack<int[]>();
		boolean terInvalide=false;
		while(!terInvalide){ //Je recree un terrain temps que le terrain n'est pas 100% praticable.
			ter=new Case[largeur][largeur];//creation de Tableau de Case;
			for(int i=0;i<ter.length;i++)
				for(int j=0;j<ter[i].length;j++){
					ter[i][j]=new Case();
				}
			fourmiliere[0][0]=aleaInt(largeur-1,0,alea);
			fourmiliere[0][1]=aleaInt(largeur-1,0,alea);
			ter [fourmiliere[0][0]][fourmiliere[0][1]].setFourmiliere(true);//place la fourmiliere.
			placerObstacle(obstacle,alea);
			resoud(fourmiliere[0][0],fourmiliere[0][1],back);
			terInvalide=rendAccessible(back);
			if (terInvalide)
				placerNourriture(nourriture,alea);
			nbObstacle();
			nbNourriture();
		}
	}
	/**
	 * @return le tableau de case representant le terrain.
	 */
	public Case[][] getTer() {
		return ter;
	}
	/**
	 * @return le tableau contenant la position de la fourmiliere dans ce terrain. 
	 */
	public int[][] getFourmiliere() {
		return fourmiliere;
	}

	private int aleaInt(int max,int mini,Random alea){//renvoie aleatoirment un int entre max et mini.
		return alea.nextInt(max-mini+1)+mini;
	}

	private void placerObstacle(int obstacle,Random alea){ //place des obstacles aleatoirement.
		int compteur=0;
		int x,y;
		while(compteur!=obstacle){
			x=aleaInt(ter.length-1,0,alea);
			y=aleaInt(ter.length-1,0,alea);
			if (!(fourmiliere[0][0]==x && y==fourmiliere[0][1]) && !ter[x][y].isObstacle()){
				ter[x][y].setObstacle(true);
				compteur++;
			}
		}
	}

	private void resoud(int x,int y,Stack<int []> back) {// Cette methode met a true toutes les cases accessibles 
		/*
		 * Cette methode fait un backtraking sur le terrain
		 * 		-si elle peut aller a une case x,y elle met la case accessible. 
		 * 		-sinon elle revient en arriere et regarde si on peut aller sur une autre case. 
		 */
		int [] c=new int [2];
		c[0]=x;
		c[1]=y;
		back.push(c);//pile la position ou on commence(Supposer accessible)
		do{
			ter[x][y].setAccesible(true); //on confirme que la case et bien accessible
			if (x+1<ter.length && !ter[x+1][y].isObstacle() && !ter[x+1][y].isAccesible()) {//regarde a droite si c'est accessible.
				int [] a=new int [2];
				a[0]=x;//j'enregistre la position actuelle.
				a[1]=y;//*******
				back.push(a); //Je pile ma position.
				x++;// Je passe a la case de droite.
			}
			else if(y-1>=0 && !ter[x][y-1].isObstacle() && !ter[x][y-1].isAccesible()) {//regarde en haut si c'est accessible.
				int [] a=new int [2];
				a[0]=x;
				a[1]=y;
				back.push(a);
				y--;
			} 
			else if (x-1>=0 && !ter[x-1][y].isObstacle() && !ter[x-1][y].isAccesible()) {//regarde en gauche si c'est accessible.
				int [] a=new int [2];
				a[0]=x;
				a[1]=y;
				back.push(a);
				x--;
			}
			else if (y+1<ter.length && !ter[x][y+1].isObstacle() && !ter[x][y+1].isAccesible()) {//regarde en bas si c'est accessible.
				int [] a=new int [2];
				a[0]=x;
				a[1]=y;
				back.push(a);
				y++;
			}
			else{// aucune case est accessible a l'entoure.On depile.  
				int [] b=back.pop();
				x=b[0];
				y=b[1];
			}
		}
		while(!back.empty());//on regarde s'il y a des cases disponibles temps que la pile n'est pas vide. 
	}

	private boolean rendAccessible(Stack<int[]> back){//Trouve les cases inaccessibles.
		boolean restInaccessible=true;
		int saut=2;//regarde 2 cases apres la zone bloquee.  
		int casSuivant;
		do{
			casSuivant=0;
			restInaccessible=false;
			for(int i=0;i<ter.length;i++)
				for(int j=0;j<ter.length;j++){
					if (!ter[i][j].isAccesible() && !ter[i][j].isObstacle()){
						restInaccessible=true;
						if (deBloque(back,saut,i,j)){
							casSuivant++;
						}
					}
				}
			if (casSuivant>0)
				saut++;
		}while(restInaccessible && saut<=ter.length);
		return saut<=ter.length;
	}

	private boolean deBloque(Stack<int[]> back,int saut,int i,int j){//Trouve les obstacles a enlever pour rendre une zone innaccessible accessible.
		/*
		 * Le principe de cette methode est de regarder si son voisin est accessible , si c'elle-ci est accessible on enleve 
		 * les obstables qui sont entre les deux.
		 * */
		if (i-saut>=0 && ter[i-saut][j].isAccesible()) {//direction: gauche
			ter[i][j].setAccesible(true);
			for (int k=1;k<saut;k++){//Enleve le nombre d'obstacle entre la case innaccessible et la case accessible.
				ter[i-k][j].setObstacle(false);
				ter[i-k][j].setAccesible(true);
			}
			resoud(i,j,back); //je mets les cases accessibles(a true) 
		}
		else if(i+saut<ter.length && ter[i+saut][j].isAccesible()){//direction: droite
			ter[i][j].setAccesible(true);
			for (int k=1;k<saut;k++)
			{
				ter[i+k][j].setObstacle(false);
				ter[i+k][j].setAccesible(true);
			}
			resoud(i,j,back);
			return false;
		}
		else if(j+saut<ter.length && ter[i][j+saut].isAccesible()){//direction: bas
			ter[i][j].setAccesible(true);
			for (int k=1;k<saut;k++)
			{
				ter[i][j+k].setObstacle(false);
				ter[i][j+k].setAccesible(true);
			}
			resoud(i,j,back);
			return false;
		}
		else if(j-saut>=0 && ter[i][j-saut].isAccesible()){//direction: haut
			ter[i][j].setAccesible(true);
			for (int k=1;k<saut;k++)
			{
				ter[i][j-k].setObstacle(false);
				ter[i][j-k].setAccesible(true);
			}
			resoud(i,j,back);
			return false;
		}
		return true;	
	}
	/**
	 * Ajoute un obstacle aleatoirement.
	 */
	public void AjoutObstacleAleatoire(){//pour ajouter un obstacle aleatoirement
		int a=(int)(Math.random()*ter.length);//choisir un chiffre au hazard
		int b=(int)(Math.random()*ter.length);//choisir un chiffre au hazard
		while(ter[a][b].isObstacle()==true || ter[a][b].isFourmiliere()==true || ter[a][b].isNourriture()==true){//test si cette case ne contient pas une fourmiliere ni obstacle ni nourriture, dans ce cas on ajoute un obstacle 
			//sinon on recherche un autre chiffre
			a=(int)(Math.random()*ter.length);
			b=(int)(Math.random()*ter.length);
		}
		ter[a][b].setObstacle(true);
		ter[a][b].setAccesible(false);
		nbObstacle++;

	}
	/**
	 *  Ajoute un obstacle manuellement.
	 * @param x
	 * 	represente l'abscisse de l'obstacle a ajouter dans le tableau ter.
	 * @param y
	 * 	represente l'ordonner de l'obstacle a ajouter dans le tableau ter.
	 */
	public void AjoutObstacleManuel(int x,int y){
		if(ter[x][y].isObstacle()==true || ter[x][y].isNourriture()==true || ter[x][y].isFourmiliere()==true){//test si cette case ne contient pas une fourmiliere ni obstacle ni nourriture, dans ce cas on ajoute un obstacle 
			return;
		}
		ter[x][y].setObstacle(true);
		ter[x][y].setAccesible(false);
		nbObstacle++;
	}
	/**
	 * supprime un obstacle aleatoirement.
	 */
	public void SupprimerObstacleAleatoire(){
		//meme principe que ajouter 
		int a=(int)(Math.random()*ter.length);
		int b=(int)(Math.random()*ter.length);
		while(ter[a][b].isObstacle()==false){
			a=(int)(Math.random()*ter.length);
			b=(int)(Math.random()*ter.length);
		}
		ter[a][b].setObstacle(false);
		ter[a][b].setAccesible(true);
		nbObstacle--;
	}
	/**
	 * Supprime un obstacle manuellement.
	 * @param x
	 *    represente l'abscisse de l'obstacle a supprimer dans le tableau ter.
	 * @param y
	 *    represente l'ordonner de l'obstacle a supprimer dans le tableau ter.
	 */
	public void SupprimerObstacleManuel(int x,int y){//supprimer manuelle
		if(ter[x][y].isObstacle()==true){//si cette case contient un obstacle , on supprime 
			ter[x][y].setObstacle(false);
			ter[x][y].setAccesible(true);
			nbObstacle--;
		}
	}
	/**
	 * Ajoute une nourriture aleatoirement
	 */
	public void AjoutNourritureAleatoire(){//meme principe que la methode ajouter un obstacle aleatoirement 
		int a=(int)(Math.random()*ter.length);
		int b=(int)(Math.random()*ter.length);
		while(ter[a][b].isObstacle()==true || ter[a][b].isFourmiliere()==true || ter[a][b].isNourriture()==true){
			a=(int)(Math.random()*ter.length);
			b=(int)(Math.random()*ter.length);
		}
		int nourriture=0;
		if(ter[a][b].isNourriture()==true){
			nourriture=ter[a][b].getNourritureint();
			nourriture++;
		}
		ter[a][b].setNourritureint(nourriture);
		ter[a][b].setNourriture(true);
		nbNourriture++;
	}
	/**
	 * Ajoute une nourriture manuellement
	 * @param x
	 *   represente l'abscisse de la nourriture a ajouter dans le tableau ter.
	 * @param y
	 *   represente l'ordonner de la nourriture a ajouter dans le tableau ter.
	 */
	public void AjoutNourritureManuel(int x,int y){
		if(ter[x][y].isObstacle()==true || ter[x][y].isFourmiliere()==true){
			return;
		}
		int nourriture=0;
		if(ter[x][y].isNourriture()==true){
			nourriture=ter[x][y].getNourritureint();
			nourriture++;
		}
		ter[x][y].setNourritureint(nourriture);
		ter[x][y].setNourriture(true);
		nbNourriture++;
	}
	/**
	 * Supprime une nourriture aleatoirement.
	 */
	public void SupprimerNourritureAleatoire(){
		int a=(int)(Math.random()*ter.length);
		int b=(int)(Math.random()*ter.length);
		while(ter[a][b].isNourriture()==false || ter[a][b].isFourmiliere()==true || ter[a][b].isObstacle()==true){
			a=(int)(Math.random()*ter.length);
			b=(int)(Math.random()*ter.length);
		}
		int nourriture=0;
		if(ter[a][b].getNourritureint()>=2){
			nourriture--;
			ter[a][b].setNourritureint(nourriture);
		}
		if(ter[a][b].getNourritureint()==1){
			nourriture--;
			ter[a][b].setNourritureint(nourriture);
		}
		ter[a][b].setNourriture(false);
		ter[a][b].setAccesible(true);
		nbNourriture--;
	}
	/**
	 * Supprime une nourriture manuellement
	 * @param x
	 *   represente l'abscisse de la nourriture a supprimer dans le tableau ter.
	 * @param y
	 *   represente l'ordonner de la nourriture a supprimer dans le tableau ter.
	 */
	public void SupprimerNourritureManuel(int x,int y){
		int nourriture=0;
		if(ter[x][y].isNourriture()==true){
			if(ter[x][y].getNourritureint()>=2){
				nourriture--;
				ter[x][y].setNourritureint(nourriture);
			}
			if(ter[x][y].getNourritureint()==1){
				nourriture--;
				ter[x][y].setNourritureint(nourriture);
			}
		}

		ter[x][y].setNourriture(false);
		ter[x][y].setAccesible(true);
		nbNourriture--;
	}
	/**
	 * Calcule le nombre total d'obstacle dans ce terrain.
	 */
	public void nbObstacle(){//une methode pour compter le nombre d'obstacle dans le terrain
		int compteur=0;
	for(int i=0;i<ter.length;i++){
		for(int j=0;j<ter.length;j++){
			if(ter[i][j].isObstacle()==true){
				compteur++;
			}
		}
	}
	nbObstacle=compteur;
	}
	/**
	 * Calcule le nombre total de source de nourriture dans ce terrain.
	 */
	public void nbNourriture(){// une methode pour compter le nombre de nourriture dans terrain
		int compteur=0;
		for(int i=0;i<ter.length;i++){
			for(int j=0;j<ter[i].length;j++){
				if(ter[i][j].isNourriture()==true){
					compteur++;
				}
			}
		}
		nbNourriture=compteur;
	}
	/**
	 * 
	 * @return le nombre total d'obstacle dans ce terrain.
	 */
	public int getNbObstacle() {
		return nbObstacle;
	}
	/**
	 * 
	 * @return le nombre totam de source de nourriture dans ce terrain.
	 */
	public int getNbNourriture() {
		return nbNourriture;
	}
	/**
	 * 
	 * @return true si il reste des Case vide, sinon false.
	 * @see Case
	 */
	public boolean caseVide(){
		if(nbObstacle<0) //sert RŽsoudre un probleme de derniere minute. 
			nbObstacle=0;
		if(nbNourriture<0)
			nbNourriture=0;
		return nbObstacle+nbNourriture<(ter.length*ter.length)-1;
	}
	private void placerNourriture(int nourriture,Random alea){// pour placer la nourriture dans terrain
		int compteur=0;
		int x,y;
		while(compteur!=nourriture){
			x=aleaInt(ter.length-1,0,alea);
			y=aleaInt(ter.length-1,0,alea);
			if (!(fourmiliere[0][0]==x && fourmiliere[0][1]==y) && ter[x][y].isAccesible() && !ter[x][y].isObstacle() && !ter[x][y].isNourriture()){
				ter[x][y].setNourriture(true);
				compteur++;
			}
		}
	}
}