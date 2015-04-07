package IK3;
import java.util.Random;
/**
 * TerrainF represente un terrain avec les source de nourritures,les obstacles et les pheromone laissee par les foumis.
 * @see Fourmiliere
 * @author Quiroz,Al-qazweny
 */
import java.util.Stack;

import javax.swing.event.ChangeEvent;
public class Terrain {
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
	private Fenetre map;
	private int vitesseSimulation=500;
	private Info info;
	public Terrain(int largeur,int nourriture,int obstacle){//Cree un terrain manuellement.
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
			this.map= new Fenetre(this);
			this.info=new Info(this);
			Fenetre1 fenetre=new Fenetre1(map,info);
			info.setTaille("         INFO: Taille="+ter.length+"  nourriture="+nourriture+" obstacle="+obstacle+"     ");
			ter [fourmiliere[0][0]][fourmiliere[0][1]].setFourmiliere(true);//place la fourmiliere.
			placerObstacle(obstacle,alea);
			resoud(fourmiliere[0][0],fourmiliere[0][1],back);
			terInvalide=rendAccessible(back);
			if (terInvalide)
				placerNourriture(nourriture,alea);
			nbObstacle();
			nbNourriture();
		}
		map.repaint();
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
	private int aleaInt(int max,int mini,Random alea){//renvoie aleatoirment un int entre max et mini.
		return alea.nextInt(max-mini+1)+mini;
	}

	private void placerObstacle(int obstacle,Random alea){ //place des obstacles aleatoirement.
		int compteur=0;
		int x,y;
		info.setFonction("      FONCTION : placerObstacle           ");
		while(compteur!=obstacle){
			x=aleaInt(ter.length-1,0,alea);
			y=aleaInt(ter.length-1,0,alea);
			if (!(fourmiliere[0][0]==x && y==fourmiliere[0][1]) && !ter[x][y].isObstacle()){
				ter[x][y].setObstacle(true);
				compteur++;
			}
			sleep();
			map.repaint();
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
		info.setFonction("      FONCTION :      resoud        ");
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

			sleep();
			map.repaint();
		
		}
		while(!back.empty());//on regarde s'il y a des cases disponibles temps que la pile n'est pas vide. 
	}

	private boolean rendAccessible(Stack<int[]> back){//Trouve les cases inaccessibles.
		boolean restInaccessible=true;
		
		int saut=2;//regarde 2 cases apres la zone bloquee.  
		info.setFonction("      FONCTION :      resoud "+saut+"      ");
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
		info.setFonction("      FONCTION :      deBloque        ");
		if (i-saut>=0 && ter[i-saut][j].isAccesible()) {//direction: gauche
			ter[i][j].setAccesible(true);
			for (int k=1;k<saut;k++){//Enleve le nombre d'obstacle entre la case innaccessible et la case accessible.
				ter[i-k][j].setObstacle(false);
				ter[i-k][j].setAccesible(true);
				sleep();
				map.repaint();
			}
			resoud(i,j,back); //je mets les cases accessibles(a true) 
		}
		else if(i+saut<ter.length && ter[i+saut][j].isAccesible()){//direction: droite
			ter[i][j].setAccesible(true);
			for (int k=1;k<saut;k++)
			{
				ter[i+k][j].setObstacle(false);
				ter[i+k][j].setAccesible(true);
				sleep();
				map.repaint();
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
				sleep();
				map.repaint();
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
				sleep();
				map.repaint();
			}
			resoud(i,j,back);
			return false;
		}
		return true;	
	}
	
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
		info.setFonction("      FONCTION :      placerNourriture        ");
		while(compteur!=nourriture){
			x=aleaInt(ter.length-1,0,alea);
			y=aleaInt(ter.length-1,0,alea);
			if (!(fourmiliere[0][0]==x && fourmiliere[0][1]==y) && ter[x][y].isAccesible() && !ter[x][y].isObstacle() && !ter[x][y].isNourriture()){
				ter[x][y].setNourriture(true);
				compteur++;
				sleep();
				map.repaint();
			}
		}
	}
	public void sleep(){
		try {
			Thread.sleep(vitesseSimulation);
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}
	public int getVitesseSimulation() {
		return vitesseSimulation;
	}
	public void setVitesseSimulation(int vitesseSimulation) {
		this.vitesseSimulation = vitesseSimulation;
	}
	public static void main(String[] args)
	{
		//new Terrain(10,2,10);
	//new Terrain(50,2,2000);
		new Terrain(5,1,23);
	}
}