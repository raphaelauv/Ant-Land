package IK3;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
public class Bouton extends JPanel{
	TerrainF ter;
	FenetreF lafenetre;
	Fourmiliere fourmiliere;
	private JPanel container=new JPanel();// Jpanel qui va contenir les boutons aleatoires,manuelles et les Jlabel
	private JPanel pan1=new JPanel();// JPanel pour les boutons ajouter,supprimer une fourmi aleatoirement
	private JPanel pan2=new JPanel();//JPanel pour les boutons ajouter,supprimer une source de nourriture aleatoirement
	private JPanel pan3=new JPanel();// JPanel pour les boutons ajouter,supprimer un obstacle aleatoirement
	private JButton plusdefourmis;// bouton pour ajouter une fourmi aleatoirement
	private JButton plusdenourriture;// bouton pour ajouter une source de nourriture aleatoirement
	private JButton plusdobstacle;// bouton pour ajouter un obstacle aleatoirement
	private JButton suppdefourmis; // bouton pour supprimer une fourmi aleatoirement
	private JButton suppdenourriture; // bouton pour supprimer une source de nourriture aleatoirement
	private JButton suppdobstacle; // bouton pour supprimer un obstacle aleatoirement
	private JButton plusdefourmisman; // bouton pour ajouter une fourmi manuellement
	private JButton plusdenourritureman; // bouton pour ajouter une source de nourriture manuellement
	private JButton plusdobstacleman;// bouton pour ajouter un obstacle manuellement
	private JButton poubelle;// bouton poubelle pour supprimer une source de nourriture , un obstacle ou une fourmi manuellement
	private JButton screen; // bouton pour capture d'ecran
	private JLabel creeLabel(String s) {//une methode pour creer des JLabel (text)
		JLabel resultat = new JLabel(s);
		resultat.setFont(new Font("Verdana",Font.BOLD,10));
		return resultat;
	}

	public Bouton(TerrainF ter,FenetreF lafenetre,Fourmiliere fourmiliere ){

		this.ter=ter;
		this.lafenetre=lafenetre;
		this.fourmiliere=fourmiliere;

		JLabel automatique=new JLabel("AUTOMATIQUE");
		JLabel manuel=new JLabel("MANUEL");
		JLabel fourmis=creeLabel("FOURMIS");
		JLabel nourriture=creeLabel("NOURRITURE");
		JLabel obstacle=creeLabel("OBSTACLE");
		automatique.setFont(new Font("Verdana",Font.BOLD,17));
		manuel.setFont(new Font("Verdana",Font.BOLD,17));

		//la creation des images Icones
		ImageIcon image1 = new ImageIcon(this.getClass().getClassLoader().getResource("plus.png"));
		ImageIcon image4 =  new ImageIcon(this.getClass().getClassLoader().getResource("fourmi2.png"));
		ImageIcon image5 = new ImageIcon(this.getClass().getClassLoader().getResource("nourriture.png"));
		ImageIcon image6 = new ImageIcon(this.getClass().getClassLoader().getResource("obstacle.png"));
		ImageIcon image7 = new ImageIcon(this.getClass().getClassLoader().getResource("poubelle.png"));
		ImageIcon image8 = new ImageIcon(this.getClass().getClassLoader().getResource("supprimer.png"));
		ImageIcon image10 = new ImageIcon(this.getClass().getClassLoader().getResource("screen.png"));

		//chaque bouton prend une image
		plusdefourmis=new JButton(image1);
		plusdenourriture=new JButton(image1);
		plusdobstacle=new JButton(image1);
		suppdefourmis=new JButton(image8);
		suppdenourriture=new JButton(image8);
		suppdobstacle=new JButton(image8);
		plusdefourmisman=new JButton(image4);
		plusdenourritureman=new JButton(image5);
		plusdobstacleman=new JButton(image6);
		poubelle=new JButton(image7);
		screen=new JButton(image10);

		//chaque bouton est associe ? son mouse listener
		plusdefourmisman.addMouseListener(new ActionPlusdeFourmisMan());
		plusdefourmisman.setEnabled(false);// desactiv? le bouton(ajouter une fourmi manuelle) au depart
		plusdenourritureman.addMouseListener(new ActionPlusdeNourritureMan());
		plusdobstacleman.addMouseListener(new ActionPlusdObstacleMan());
		poubelle.addMouseListener(new BoutonPoubelle());
		// les boutons aleatoires
		suppdefourmis.setPreferredSize(new Dimension(30,30));
		plusdefourmis.setPreferredSize(new Dimension(30,30));
		suppdenourriture.setPreferredSize(new Dimension(30,30));
		plusdenourriture.setPreferredSize(new Dimension(30,30));
		suppdobstacle.setPreferredSize(new Dimension(30,30));
		plusdobstacle.setPreferredSize(new Dimension(30,30));
		screen.setPreferredSize(new Dimension(40,40));
		// les boutons manuels
		poubelle.setPreferredSize(new Dimension(40,40));
		plusdenourritureman.setPreferredSize(new Dimension(40,40));
		plusdobstacleman.setPreferredSize(new Dimension(40,40));
		plusdefourmisman.setPreferredSize(new Dimension(40,40));
		//java2.com
		//
		suppdefourmis.addActionListener(new ActionSupprimerdeFourmis ());
		suppdenourriture.addActionListener(new ActionSupprimerdeNourriture());
		suppdobstacle.addActionListener(new ActionSupprimerdObstacle());
		plusdefourmis.addActionListener(new ActionPlusdeFourmis());
		plusdenourriture.addActionListener(new ActionPlusdeNourriture());
		plusdobstacle.addActionListener(new ActionPlusdObstacle());
		screen.addActionListener(new Screen());

		JPanel boutonbas=new JPanel();// Jpanel pour les boutons manuelles
		boutonbas.setLayout(new FlowLayout());//son layout

		//ajouter les boutons manuelles dans leurs JPanel
		boutonbas.add(plusdefourmisman);
		boutonbas.add(plusdenourritureman);
		boutonbas.add(plusdobstacleman);
		boutonbas.add(poubelle);
		boutonbas.add(screen);

		//Jpanel pour les boutons ajouter,supprimer une fourmi aleatoirement
		pan1.setLayout(new FlowLayout());//son layout
		//ajouter les boutons ajouter,supprimer dans leus JPanel
		pan1.add(plusdefourmis);
		pan1.add(fourmis);
		pan1.add(suppdefourmis);

		//JPanel pour les boutons ajouter,supprimer une source de nourriture aleatoirement
		pan2.setLayout(new FlowLayout());
		pan2.add(plusdenourriture);
		pan2.add(nourriture);
		pan2.add(suppdenourriture);

		//JPanel pour les boutons ajouter,supprimer un obstacle aleatoirement
		pan3.setLayout(new FlowLayout());
		pan3.add(plusdobstacle);
		pan3.add(obstacle);
		pan3.add(suppdobstacle);


		JPanel sup=new JPanel();// le JPanel principale
		container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));// le layout de
		JPanel TextAuto = new JPanel();//JPanel pour le JLabel (text "AUTOMATIQUE")
		JPanel TextManuel = new JPanel();//JPanel pour le JLabel (text "MANUEL")
		TextAuto.setLayout(new FlowLayout());
		TextAuto.add(automatique);
		TextManuel.setLayout(new FlowLayout());
		TextManuel.add(manuel);
		//ajouter tous les JPanel au JPanel container qui contiens les JPanel de JLabel et les autres JPanel
		container.add(TextAuto);
		container.add(Box.createRigidArea(new Dimension(0,0)));
		container.add(pan1);
		container.add(pan2);
		container.add(pan3);
		container.add(TextManuel);
		container.add(boutonbas);
		//ajouter le JPanel container ? sup qui est le JPanel principale
		sup.add(container,BorderLayout.WEST);
		this.add(sup);

	}
	public void InitialisationBoutons(){//une methode pour tester des le depart du programme si nombre de nourriture=0 ou obstacle=0 ou nombre de fourmis est entre 0 et 5000
		//si oui les boutons sont desactiv?s sinon , je les laisse comme tels
		if(ter.getNbNourriture()==0){
			suppdenourriture.setEnabled(false);
		}
		if(ter.getNbObstacle()==0){
			suppdobstacle.setEnabled(false);
		}
		if(fourmiliere.getTotalFourmis()==0){
			suppdefourmis.setEnabled(false);
		}
		if(fourmiliere.getTotalFourmis()>5000){
			plusdefourmis.setEnabled(false);
		}
		if(!ter.caseVide()){
			plusdenourriture.setEnabled(false);
			plusdobstacle.setEnabled(false);
			plusdenourritureman.setEnabled(false);
			plusdobstacleman.setEnabled(false);
		}
	}
	//Une class interne qui contiens la methode (poubelle) pour supprimer manuellement un obstacle,une source de nourriture ou une fourmi
	public class BoutonPoubelle implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mousePressed(MouseEvent arg0) {


		}

		@Override
		//cette methode est appell?e lorsqu'on relache le clique gauche de la souris
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			Point p=lafenetre.getFenetreTerrain().getMousePosition(true);//declare un objet de type point
			int x=0;int y=0;
			try{

				//recupere les coordonn?es x,y de l'ecran et les enregistrer dans les variables x,y
				x=(int)(p.getX());
				y=(int)(p.getY());
			}
			catch(java.lang.NullPointerException e){

			}
			FenetreTerrain a=(FenetreTerrain)(lafenetre.getFenetreTerrain());//declarer une objet (JPanel) de terrain
			int widthcase=a.getWidthCase();// recuperer la largeur d'une case dans terrain
			int heightcase=a.getHeigtCase();// recuperer la hauteur d'une case dans terrain
			if(p!=null && lafenetre.getFenetreTerrain().getWidth()>=x && lafenetre.getFenetreTerrain().getHeight()>=y){// tester si l'objet point vaut pas null et si l'utilisateur a relache le clique gauche dans le bon endroit (c'est-a-dire le clique est relach? sur le terrain et il n'est relach? sur la minimap ou dans un autre endroit)
				int i,j;
				i=y/heightcase;
				j=x/widthcase;
				//i et j sont les coordon?es x et y de la case
				if(i<ter.getTer().length && j<ter.getTer().length){//test si on est dans le tableau
					if(ter.getNbNourriture()>0 || ter.getNbObstacle()>0){// test si le nombre d'obstacles est superieure ? zero et le nombre de source de nourriture est superiure ? zero sinon on ne fait rien
						if(ter.getTer()[i+lafenetre.getDebutY()][j+lafenetre.getDebutX()].isNourriture()&&!ter.getTer()[i+lafenetre.getDebutY()][j+lafenetre.getDebutX()].isObstacle()&&!ter.getTer()[i+lafenetre.getDebutY()][j+lafenetre.getDebutX()].isFourmiliere()&&ter.getTer()[i+lafenetre.getDebutY()][j+lafenetre.getDebutX()].getNombreDeFourmis()==0){// test si la souris est relach?e sur une source de nourriture sinon on ne fait rien
							ter.SupprimerNourritureManuel(i+lafenetre.getDebutY(), j+lafenetre.getDebutX());// appele de la methode pour supprimer une source de nourriture
							plusdefourmis.setEnabled(true);//bouton ajouter d'une fourmi est active
							plusdenourriture.setEnabled(true);//bouton ajouter d'une source de nourriture aleatoirement est active
							plusdenourritureman.setEnabled(true);//bouton ajouter d'une source de nourriture manuellement est active
							plusdobstacle.setEnabled(true);//bouton ajouter d'un obstacle aleatoirement est active
							plusdobstacleman.setEnabled(true);//bouton ajouter d'un obstacle manuellement est active
							if(ter.getNbNourriture()==0){//test si le nombre de nourriture = zero
								suppdenourriture.setEnabled(false);//bouton supprimer une source de nourriture aleatoirement est desactiv?
							}
							if(ter.getNbNourriture()==0 && ter.getNbObstacle()==0 && fourmiliere.getTotalFourmis()==0){// test si le nombre d'obstacle,nourriture et fourmis =zero le bouton poubelle est desactiv?e parce qu'il n'y a rien ? supprimer
								poubelle.setEnabled(false);
							}
						}      
						lafenetre.move();//appeler la methode move pour repaint

						//la meme chose que nourriture mais cette fois avec les obstacles
						if(ter.getTer()[i+lafenetre.getDebutY()][j+lafenetre.getDebutX()].isObstacle()&&!ter.getTer()[i+lafenetre.getDebutY()][j+lafenetre.getDebutX()].isNourriture()&&!ter.getTer()[i][j].isFourmiliere()){
							ter.SupprimerObstacleManuel(i+lafenetre.getDebutY(), j+lafenetre.getDebutX());
							plusdefourmis.setEnabled(true);
							plusdenourriture.setEnabled(true);
							plusdenourritureman.setEnabled(true);
							plusdobstacle.setEnabled(true);
							plusdobstacleman.setEnabled(true);
							if(ter.getNbObstacle()==0){
								suppdobstacle.setEnabled(false);
							}
							if(ter.getNbNourriture()==0 && ter.getNbObstacle()==0 && fourmiliere.getTotalFourmis()==0){
								poubelle.setEnabled(false);
							}
						}
						lafenetre.move();
					}
					else{
						poubelle.setEnabled(false);
						suppdenourriture.setEnabled(false);
						suppdobstacle.setEnabled(false);
					}
					//test is le programme est en pause sinon on ne peut pas supprimer une fourmi manuellement
					if(!fourmiliere.isPlayOuPause()){
						// TODO Auto-generated method stub
						if(fourmiliere.getTotalFourmis()>0){ //test si le nombre de fourmis est superiure ? zero , on peut supprimer
							suppdefourmis.setEnabled(true);
							fourmiliere.supprimerUneFourmiManuel(i+lafenetre.getDebutY(),j+lafenetre.getDebutX());//appele de la methode pour supprimer une fourmi
							lafenetre.UpdateNombredeFourmis(fourmiliere.getTotalFourmis());
							if(fourmiliere.getTotalFourmis()==0){// si nombre de fourmis =zero
								suppdefourmis.setEnabled(false);//bouton supprimer une fourmi aleatoirement est desactiv?e
							}
							if(fourmiliere.getTotalFourmis()==0 && ter.getNbNourriture()==0 && ter.getNbObstacle()==0){
								poubelle.setEnabled(false);
							}
						}
						else{
							suppdefourmis.setEnabled(false);

						}
					}
					else{ // on supprime une nourriture
						ter.SupprimerNourritureManuel(i+lafenetre.getDebutY(), j+lafenetre.getDebutX());// appele de la methode pour supprimer une source de nourriture

					}
				}
			}
		}

	}
	public class Screen implements ActionListener{ // pour la capture d'ecran

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub

			Dimension rec= lafenetre.getFenetre().getSize();
			BufferedImage lacapture=(BufferedImage)lafenetre.getFenetre().createImage(rec.width,rec.height); // je recupere la taille de la capture

			lafenetre.getFenetre().paint(lacapture.getGraphics()); // je recupere l'image de la capture
			try {
				JFileChooser Fenetredechoix = new JFileChooser(); // pour avoir la fenetre de choix d'emplacement du screenshot

				Fenetredechoix.setSelectedFile(new File("captureFourmiLand")); // pour mettre un nom par defaut au fichier de la capture

				int returnVal = Fenetredechoix.showSaveDialog(Fenetredechoix); // je recupere le choix
				if(returnVal == JFileChooser.APPROVE_OPTION) { // si le choix a ?t? effectu?

					File outputFile = Fenetredechoix.getSelectedFile(); // demande l'emplacement de sauvegarde dans l'ordinateur choisi precedement
					String a=outputFile.getCanonicalPath(); // pour recuper le chemin et le nom du fichier ( emplacement choisi par l'utilisateur )
					File sortie=new File(a+".png"); // pour mettre a la suite l'extension au fichier de l'utilisateur
					ImageIO.write(lacapture, "png",sortie); // pour ecrire le fichier a l'enplacement voulut
				}
			}

			catch (IOException e) { //Si erreur avec l'enregistrement du fichier ,probleme de droit d'utilisateur , manque d'espace ...
				e.printStackTrace();
			}
		}
	}

	public class ActionPlusdeFourmis implements ActionListener{// une class pour l'action d'ajouter une fourmi aleatoirement

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if(fourmiliere.getTotalFourmis()<=5000){// si le nombre de fourmis est inferieur ou egale ? 5000 , on peut en ajouter

				if(!fourmiliere.isPlayOuPause()){ //si la simulation est en pause
					fourmiliere.ajouterFourmi();
				}
				else{fourmiliere.setAjoutSupprimerFourmis(fourmiliere.getAjoutSupprimerFourmis()+1);}

				if(fourmiliere.getTotalFourmis()>=5000){//si le nombre est superieure ? 5000 le bouton ajouter une fourmi est desactiv?e
					plusdefourmis.setEnabled(false);
				}
				suppdefourmis.setEnabled(true);
				poubelle.setEnabled(true);
				lafenetre.UpdateNombredeFourmis(fourmiliere.getTotalFourmis());

			}
			else{
				plusdefourmis.setEnabled(false);
			}
		}
	}

	public class ActionPlusdeNourriture implements ActionListener{//class pour ajouter une source de nourriture aleatoirement
		public void actionPerformed(ActionEvent arg0){
			if(ter.caseVide()){//test si il reste des cases vides
				ter.AjoutNourritureAleatoire();//appele de la methode pour ajouter une nourriture
				fourmiliere.changerNourriture(fourmiliere.getDisparitionNourriture());//update le nombre de nourritures
				suppdenourriture.setEnabled(true);
				poubelle.setEnabled(true);
				if(!ter.caseVide()){//si il n'y a pas de cases vides , tous les boutons ajouter manuellement (sauf fourmi) et aleatoirement (sauf fourmi) sont desactiv?s , bouton poubelle est acitiv?
					plusdenourriture.setEnabled(false);
					plusdenourritureman.setEnabled(false);
					plusdobstacle.setEnabled(false);
					plusdobstacleman.setEnabled(false);
					poubelle.setEnabled(true);
				}
				lafenetre.move();
			}
			else{//meme chose que le test precedent
				plusdenourriture.setEnabled(false);
				plusdenourriture.setEnabled(false);
				plusdobstacle.setEnabled(false);
				plusdobstacleman.setEnabled(false);
				poubelle.setEnabled(true);
			}
		}
	}
	public class ActionPlusdObstacle implements ActionListener{//classe pour ajouter un obstacle aleatoirement , meme principe que la classe ajouter une nourriture aleatoirement
		public void actionPerformed(ActionEvent arg0){
			if(ter.caseVide()){
				ter.AjoutObstacleAleatoire();
				suppdobstacle.setEnabled(true);
				poubelle.setEnabled(true);
				if(!ter.caseVide()){
					plusdobstacle.setEnabled(false);
					plusdobstacleman.setEnabled(false);
					plusdenourriture.setEnabled(false);
					plusdenourritureman.setEnabled(false);
					poubelle.setEnabled(true);
				}
				lafenetre.move();
			}
			else{
				plusdobstacle.setEnabled(false);
				plusdobstacleman.setEnabled(false);
				plusdenourriture.setEnabled(false);
				plusdenourritureman.setEnabled(false);
				poubelle.setEnabled(true);
			}
		}
	}
	public class ActionSupprimerdeFourmis implements ActionListener{//class pour supprimer une fourmi aleatoirement

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub

			if(fourmiliere.getTotalFourmis()>0){//test si le nombre de fourmis est superieur ? zero , on speut supprimer
				suppdefourmis.setEnabled(true);
				if(!fourmiliere.isPlayOuPause()){ //si la simulation est en pause
					fourmiliere.supprimerFourmi();// pour supprimer des fourmis quand en pause
				}
				else{
					fourmiliere.setAjoutSupprimerFourmis(fourmiliere.getAjoutSupprimerFourmis()-1);//appele de la methode pour supprimer quand en play
				}
				lafenetre.UpdateNombredeFourmis(fourmiliere.getTotalFourmis());

				if(fourmiliere.getTotalFourmis()<5000){//si le nombre de fourmis est inferieur ? 5000 , on peur rajouter
					plusdefourmis.setEnabled(true);
				}
				if(fourmiliere.getTotalFourmis()==0){//sinon on ne peut rien faire
					suppdefourmis.setEnabled(false);
				}
				if(fourmiliere.getTotalFourmis()==0 && ter.getNbNourriture()==0 && ter.getNbObstacle()==0){
					poubelle.setEnabled(false);
				}
			}
			else{
				suppdefourmis.setEnabled(false);
			}

		}
	}
	public class ActionSupprimerdeNourriture implements ActionListener{//une classe pour supprimer une source de nourriture aleatoirement
		public void actionPerformed(ActionEvent arg0){
			if(ter.getNbNourriture()>0){//si le nombre de nourriture est superieur ? zero , on peut supprimer
				ter.SupprimerNourritureAleatoire();//appelle de la methode pour supprimer
				//reactiver les boutons ajouter et poubelle
				plusdenourriture.setEnabled(true);
				plusdenourritureman.setEnabled(true);
				plusdobstacle.setEnabled(true);
				plusdobstacleman.setEnabled(true);
				poubelle.setEnabled(true);
				if(ter.getNbNourriture()==0){//sinon on ne peut pas supprimer
					suppdenourriture.setEnabled(false);
					if(ter.getNbNourriture()==0 && ter.getNbObstacle()==0 && fourmiliere.getTotalFourmis()==0){
						poubelle.setEnabled(false);
					}
				}
				lafenetre.move();
			}
			else{
				suppdenourriture.setEnabled(false);
			}
		}
	}
	public class ActionSupprimerdObstacle implements ActionListener{//meme principe que la classe ActionSupprimer de Nourriture mais cette fois avec les obstacles
		public void actionPerformed(ActionEvent arg0){
			if(ter.getNbObstacle()>0){
				plusdobstacle.setEnabled(true);
				plusdobstacleman.setEnabled(true);
				plusdenourriture.setEnabled(true);
				plusdenourritureman.setEnabled(true);
				plusdefourmis.setEnabled(true);
				ter.SupprimerObstacleAleatoire();
				if(ter.getNbObstacle()==0){
					suppdobstacle.setEnabled(false);

				}
				if(ter.getNbNourriture()==0 && ter.getNbObstacle()==0 && fourmiliere.getTotalFourmis()==0){
					poubelle.setEnabled(false);
				}
				lafenetre.move();
			}
			else{
				suppdobstacle.setEnabled(false);
			}
		}
	}
	public class ActionPlusdeFourmisMan implements MouseListener{//ajouter une fourmi manuellement meme principe que la classe Bouton poubelle
		//lorsqu'on relache le clique gauche cette classe est appell?e puis on recupere les coordonn?es x et y et on test si cette case ne contient pas un obstacle on ajoute sinon on n'ajoute pas

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			Point p=lafenetre.getFenetreTerrain().getMousePosition(true);
			int x=0;int y=0;
			try{


				x=(int)(p.getX());
				y=(int)(p.getY());
			}
			catch(java.lang.NullPointerException e2){
			}
			FenetreTerrain a=(FenetreTerrain)(lafenetre.getFenetreTerrain());
			int widthcase=a.getWidthCase();
			int heightcase=a.getHeigtCase();

			if(p!=null && lafenetre.getFenetreTerrain().getWidth()>=x && lafenetre.getFenetreTerrain().getHeight()>=y){
				int i,j;
				i=y/heightcase;
				j=x/widthcase;
				if(i<ter.getTer().length && j<ter.getTer().length && !fourmiliere.isPlayOuPause()){
					plusdefourmisman.setEnabled(true);
					if(!fourmiliere.isPlayOuPause() && !ter.getTer()[i+lafenetre.getDebutY()][j+lafenetre.getDebutX()].isObstacle()){
						fourmiliere.ajouterUneFourmiMan(i+lafenetre.getDebutY(), j+lafenetre.getDebutX());
						suppdefourmis.setEnabled(true);
						poubelle.setEnabled(true);
						lafenetre.UpdateNombredeFourmis(fourmiliere.getTotalFourmis());
					}
				}
				else{
					plusdefourmisman.setEnabled(false);
				}
			}
		}



	}
	public class ActionPlusdeNourritureMan implements MouseListener{//class pour ajouter une nourriture manuellement meme chose que la classe precedente

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent arg0) {

			// TODO Auto-generated method stub

			Point p=lafenetre.getFenetreTerrain().getMousePosition(true);
			int x=0;int y=0;
			try{


				x=(int)(p.getX());
				y=(int)(p.getY());
			}
			catch(java.lang.NullPointerException e){
			}
			FenetreTerrain a=(FenetreTerrain)(lafenetre.getFenetreTerrain());
			int widthcase=a.getWidthCase();
			int heightcase=a.getHeigtCase();

			if(p!=null && lafenetre.getFenetreTerrain().getWidth()>=x && lafenetre.getFenetreTerrain().getHeight()>=y){
				int i,j;
				i=y/heightcase;
				j=x/widthcase;
				if(i<ter.getTer().length && j<ter.getTer().length){
					if(ter.caseVide()){
						ter.AjoutNourritureManuel(i+lafenetre.getDebutY(), j+lafenetre.getDebutX());
						fourmiliere.changerNourriture(fourmiliere.getDisparitionNourriture());//update le nombre de sources de nourritures
						poubelle.setEnabled(true);//bouton activ? , il y a quelque chose ? supprimer
						suppdenourriture.setEnabled(true);
						if(!ter.caseVide()){//si il n'y a pas de cases vides , tous les boutons ajouter manuellement (sauf fourmi) et aleatoirement (sauf fourmi) sont desactiv?s , bouton poubelle est acitiv?
							plusdenourriture.setEnabled(false);
							plusdobstacle.setEnabled(false);
							plusdenourritureman.setEnabled(false);
							plusdobstacleman.setEnabled(false);
							poubelle.setEnabled(true);
						}
						lafenetre.move();
					}
					else{
						plusdenourriture.setEnabled(false);
						plusdobstacle.setEnabled(false);
						plusdenourritureman.setEnabled(false);
						plusdobstacleman.setEnabled(false);
						poubelle.setEnabled(true);
					}
				}
			}
		}
	}
	public class ActionPlusdObstacleMan implements MouseListener{// classe pour ajouter un obstacle meme chose que la precedente

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub


			Point p=lafenetre.getFenetreTerrain().getMousePosition(true);
			int x=0;int y=0;
			try{


				x=(int)(p.getX());
				y=(int)(p.getY());
			}
			catch(java.lang.NullPointerException e1){

			}
			FenetreTerrain a=(FenetreTerrain)(lafenetre.getFenetreTerrain());
			int widthcase=a.getWidthCase();
			int heightcase=a.getHeigtCase();

			if(p!=null && lafenetre.getFenetreTerrain().getWidth()>=x && lafenetre.getFenetreTerrain().getHeight()>=y){
				int i,j;
				i=y/heightcase;
				j=x/widthcase;
				if(i<ter.getTer().length && j<ter.getTer().length){
					if(ter.caseVide()){
						ter.AjoutObstacleManuel(i+lafenetre.getDebutY(), j+lafenetre.getDebutX());
						poubelle.setEnabled(true);
						suppdobstacle.setEnabled(true);
						if(!ter.caseVide()){
							plusdefourmis.setEnabled(true);
							plusdenourriture.setEnabled(false);
							plusdobstacle.setEnabled(false);
							plusdenourritureman.setEnabled(false);
							plusdobstacleman.setEnabled(false);
						}
					}
					lafenetre.move();
				}
				else{
					plusdefourmis.setEnabled(true);
					plusdenourriture.setEnabled(false);
					plusdobstacle.setEnabled(false);
					plusdenourritureman.setEnabled(false);
					plusdobstacleman.setEnabled(false);
				}
			}                      
		}      
	}
	public void activeBoutonFourmi(boolean activer){
		plusdefourmisman.setEnabled(activer);
	}
}