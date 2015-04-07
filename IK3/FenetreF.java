package IK3;
import java.awt.*;
import javax.swing.*;
public class FenetreF extends JFrame {//Cette class regroupe tous les JPanel.
	private JPanel comteneur;
	private JPanel comteneur1;
	private JPanel comteneur0;
	private InfoHaut infoHaut;
	private FenetreTerrain fenetreTerrain;
	private InfoBas infoBas;
	private JFrame fenetre;
	private Bouton bouton;
	private MiniMap miniMap;
	private int longeur;
	private int largeur;
	public FenetreF(Fourmiliere lesfourmis){
		//recuperation de votre resolution;
		int longeur=getToolkit().getScreenSize().width;//width de votre ecran.
		int largeur=getToolkit().getScreenSize().height;//height maximal de votre ecran
		//J'affiche ma fenetre sur 90% de l'espace disponible.
		this.largeur=(int)(largeur*0.90);
		this.longeur=(int)(longeur*0.90);
		fenetre=new JFrame();
		fenetre.setTitle("FourmiLand");
		fenetre.setSize(this.longeur, this.largeur);//je donne la taille de ma fenetre.
		comteneur = new JPanel();
		infoHaut=new InfoHaut(lesfourmis);
		infoBas=new InfoBas(lesfourmis , this);
		fenetreTerrain=new FenetreTerrain(lesfourmis);
		fenetreTerrain.setPreferredSize(new Dimension((int)(fenetre.getWidth()),(int)(fenetre.getHeight()*0.50)));
		bouton=new Bouton(lesfourmis.getLamap(),this,lesfourmis);
		bouton.setPreferredSize(new Dimension((int)(fenetre.getWidth()*0.22),(int)(fenetre.getWidth()*0.22)));
		bouton.setMinimumSize(new Dimension(200,500));
		bouton.InitialisationBoutons();
		miniMap=new MiniMap(lesfourmis,(FenetreTerrain)fenetreTerrain);
		miniMap.setPreferredSize(new Dimension((int)(fenetre.getWidth()*0.27),(int)(fenetre.getWidth()*0.27)));
		comteneur0=new JPanel();
		comteneur0.setLayout(new BorderLayout());
		comteneur.setLayout(new BoxLayout(comteneur,BoxLayout.LINE_AXIS));
		comteneur.add(comteneur0);
		comteneur0.add(bouton,BorderLayout.NORTH);
		comteneur0.add(miniMap,BorderLayout.SOUTH);
		comteneur1=new JPanel();
		comteneur1.setLayout(new BoxLayout(comteneur1,BoxLayout.PAGE_AXIS));

		comteneur1.add(infoHaut);
		comteneur1.add(fenetreTerrain);
		comteneur1.add(infoBas);
		comteneur.add(comteneur1);
		fenetre.setContentPane(comteneur);
		fenetre.setLocationRelativeTo(null);
		fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenetre.setVisible(true);

	}
	public JPanel getFenetreTerrain() {
		return fenetreTerrain;
	}
	public void minimum(){ // pour empecher l'utilisateur d'avoir une fenetre trop petite

		if(fenetre.getWidth()<1700 && fenetre.getHeight()<500){ // minimum largeur
			this.fenetre.setSize(longeur, largeur);
		}

		if(fenetre.getWidth()<500 && fenetre.getHeight()<1700){ // minimum longeur
			this.fenetre.setSize(longeur, largeur);
		}

	}
	public void setFenetreTerrain(JPanel fenetreTerrain) {
		this.fenetreTerrain = (FenetreTerrain) fenetreTerrain;
	}
	public void move(){
		fenetreTerrain.repaint();
		miniMap.repaint();
	}

	public int getDebutX(){
		return fenetreTerrain.getDebutX();
	}
	public int getDebutY(){
		return fenetreTerrain.getDebutY();
	}
	public void setZoom(int zoom){
		miniMap.setZoom(zoom);
	}
	public void stopChrono(){
		infoHaut.stopChrono();
	}
	public void startChrono(){
		infoHaut.startChrono();
	}
	public void UpdateNombredeFourmis(int nbFourmis){
		infoHaut.UpdateNombredeFourmis(nbFourmis);
	}
	public JFrame getFenetre() {
		return fenetre;
	}
	public void setFenetre(JFrame fenetre) {
		this.fenetre = fenetre;
	}
	public void activeBoutonFourmi(boolean activer){
		bouton.activeBoutonFourmi(activer);
	}
	public Bouton getBouton() {
		return bouton;
	}
	public void setBouton(Bouton bouton) {
		this.bouton = bouton;
	}
}