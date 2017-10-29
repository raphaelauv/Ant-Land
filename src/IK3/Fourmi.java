package IK3;

import java.util.Stack;
/**
 * @author Raphael
*/
public class Fourmi {
	private  int x; // position x dans le tableau
	private  int y; // position y dans le tableau
	 boolean stockageNourriture; // true == nourriture sur elle , false == pas de nourriture sur elle
     Stack<int[]> piledespostions = new Stack<int[]>();
    private int compteur;
    private boolean compteurDepilage=false;
    private int smart; // =0 sans utilisation de la pile , =1 utilisation de la pile
    
	public Fourmi (int xFourmiliere , int yFourmiliere, int intelligence){ // initialise les positions de toutes les fourmis a la position de la Fourmiliere
		
		x= xFourmiliere;
		y= yFourmiliere;
		smart=intelligence;
		stockageNourriture=false; // Toutes les fourmis sont vide de nourriture a la creation
	}

	// get set de chaque variable
	
	public  boolean getstockageNourriture() {
		return stockageNourriture;
	}
	public  void setstockageNourriture(boolean x) {
		stockageNourriture = x;
	}
	public  int getX() {
		return x;
	}
	public  void setX(int y) {
		x = y;
	}
	public  int getY() {
		return y;
	}
	public  void setY(int z) {
		y = z;}
	
	
	// les set et get de la pile de la fourmi
	
	public   void push (int [] tab ) { // rajoute un couple x et y de coordonnes dans la pile
		piledespostions.push(tab);
	}
	
	public void pop () { // retire le dernier couple x et y de la pile
		piledespostions.pop();
	}
	public int [] peek(){ // retourne le dernier couple x et y de la pile sans depiler
		return piledespostions.peek();
	
	}
	public  boolean empty(){ // test si la pile est vide
		return piledespostions.empty();
	}

	public int getCompteur() {
		return compteur;
	}

	public void setCompteur(int compteur) {
		this.compteur = compteur;
	}

	public boolean isCompteurDepilage() {
		return compteurDepilage;
	}

	public void setCompteurDepilage(boolean compteurDepilage) {
		this.compteurDepilage = compteurDepilage;
	}

	public int getSmart() {
		return smart;
	}

	public void setSmart(int smart) {
		this.smart = smart;
	}
	
}