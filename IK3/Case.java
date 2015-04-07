package IK3;
/**
 * @author Raphael and Andres Quiroz Nieva De las Vegas.
 */
public class Case { 
	private boolean accessible; // false == il ya un obstacles , true == case vide ou case fourmiliere ou case nourriture
	private boolean obstacle; // variable permettant la construction du terrain dans la class TerrainF
	private boolean nourriture ;  //true == il ya de la nourriture  , false == il n'y a pas de nourriture sur la case
	private int nourritureint; // POUR LA QUANTITE DE NOURRITURE     si 0  alors case sans nourriture  , !=0 il ya de la nourriture
	private boolean fourmiliere ;  // true == il ya la fourmiliere, false == case sans la fourmiliere
    private int meat ; // ==0 pas de pheromone , incremente a chaque passage de fourmi en mode meat
    private boolean nid ; // true une formi est passé en mode nid sur la case
    private int nombreDeFourmis; // =0 pas de fourmi sur la case
    private int nidValeur;
public Case (){
	accessible=false;
	obstacle=false;
	nourriture=false;
	fourmiliere=false;
	nourritureint=0;
	meat=0;
	nid=false;
	nidValeur=0;
  }
//les get et set des variable de class Case 

public boolean isAccesible() {
	return accessible;
}

public boolean isObstacle() {
	return obstacle;
}

public void setObstacle(boolean obstacle) {
	this.obstacle = obstacle;
}

public  void setAccesible(boolean accesible2) {
	accessible = accesible2;
}

public  boolean isNourriture() {
	return nourriture;
}

public  void setNourriture(boolean nourriture2) {
	nourriture = nourriture2;
}

public  int getNourritureint() {
	return nourritureint;
}
public  void setNourritureint(int nourritureint2) {
	nourritureint = nourritureint2;
}
public  boolean isFourmiliere() {
	return fourmiliere;
}
public  void setFourmiliere(boolean fourmiliere2) {
	fourmiliere = fourmiliere2;
}
public  int getMeat() {
	return meat;
}
public  void setMeat(int meat2) {
	meat = meat2;
}
public  boolean isNid() {
	return nid;
}
public  void setNid(boolean nid2) {
	nid = nid2;
}

public int getNombreDeFourmis() {
	return nombreDeFourmis;
}

public void setNombreDeFourmis(int nombreDeFourmis) {
	this.nombreDeFourmis = nombreDeFourmis;
}

public int getNidValeur() {
	return nidValeur;
}

public void setNidValeur(int nidValeur) {
	this.nidValeur = nidValeur;
}

}