package IK3;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.event.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

public class InfoBas extends JPanel implements ChangeListener{
	JButton play;
	JButton pause;
	//JLabel la = new JLabel("Slider Actif: ");
	private JButton retour;

	public JButton getRetour() {
		return retour;
	}
	Fourmiliere f;
	FenetreF b;
	JSlider speed;
	JSlider intel;
	JSlider zoom;
	JSlider distance;
	JSlider nourriture;
	JSlider pheromone;
	JLabel l = new JLabel("vitesse");
	JLabel l2 = new JLabel("Intelligence");
	JLabel l3 = new JLabel("Zoom");
	JLabel l4 = new JLabel("Distance");
	JLabel l5 = new JLabel("Nourriture");
	JLabel l6 = new JLabel("Pheromone");
	private Thread t;

	public InfoBas(Fourmiliere f , FenetreF b ){
		this.f = f; // initialisation
		this.b=b;
		ImageIcon image1 = new ImageIcon(this.getClass().getClassLoader().getResource("retour1.png"));
		retour = new JButton(image1);
		ImageIcon image2 = new ImageIcon(this.getClass().getClassLoader().getResource("pause1.png"));
		pause = new JButton(image2);
		ImageIcon image4 = new ImageIcon(this.getClass().getClassLoader().getResource("play1.png"));
		play = new JButton(image4);
		
		retour.setPreferredSize(new Dimension(30,30));
		pause.setPreferredSize(new Dimension(30,30));
		play.setPreferredSize(new Dimension(30,30));
		
		retour.setMaximumSize(new Dimension(30,30));
		pause.setMaximumSize(new Dimension(30,30));
		play.setMaximumSize(new Dimension(30,30));
		
		// inialisation des valeurs de slideur
		String vitesse="Vitesse : "+"0,"+f.getVitesseSimulation()+"/S";
		l.setText(vitesse);
		String Zoom="Zoom : MAXIMUM";
		l3.setText(Zoom);
		String dist="Distance :"+f.getDistanceMax();
		l4.setText(dist);
		l5.setText("Nourriture : INFINI");
		String intelec="";
		if(f.getSmartOuPas()==0){intelec="Intelligence : Non";}else{intelec="Intelligence : Oui";}
		l2.setText(intelec);
		String phero="Pheromone : -1";
		l6.setText(phero);

		b.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	
		play.addActionListener(new BoutonPlay());
		JPanel haut=new JPanel();
		JPanel bas=new JPanel();
		JPanel droit=new JPanel();
		JPanel gauche=new JPanel();
		this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
		this.add(haut);
		this.add(bas);
		bas.setLayout(new BoxLayout(bas,BoxLayout.LINE_AXIS));
		bas.add(gauche);
		bas.add(droit);
		haut.setLayout(new BoxLayout(haut,BoxLayout.LINE_AXIS));
		//haut.setBackground(Color.gray);

		haut.add(play);
		pause.addActionListener(new BoutonPause());
		haut.add(pause);
		retour.addActionListener(new BoutonRetour());
		haut.add(retour);
		//haut.add(la);
		speed = new JSlider(0,1000,f.getVitesseSimulation()); // crée un slider avec (min, max, où commence le slider)
		speed.addChangeListener(this);
		speed.setMajorTickSpacing(200); 
		speed.setMinorTickSpacing(1); 
		speed.setPaintTicks(false); 
		speed.setPaintLabels(false);
		JPanel comteneur0=new JPanel();
		comteneur0.setLayout(new BoxLayout(comteneur0,BoxLayout.LINE_AXIS));
		JPanel comteneur1=new JPanel();
		comteneur1.setLayout(new BoxLayout(comteneur1,BoxLayout.LINE_AXIS));
		JPanel comteneur2=new JPanel();
		comteneur2.setLayout(new BoxLayout(comteneur2,BoxLayout.LINE_AXIS));
		JPanel comteneur3=new JPanel();
		comteneur3.setLayout(new BoxLayout(comteneur3,BoxLayout.LINE_AXIS));
		gauche.setLayout(new BoxLayout(gauche,BoxLayout.PAGE_AXIS));
		droit.setLayout(new BoxLayout(droit,BoxLayout.PAGE_AXIS));
		gauche.add(comteneur0);
		gauche.add(comteneur1);
		droit.add(comteneur2);
		droit.add(comteneur3);
		comteneur0.add(l);
		comteneur0.add(speed);
		intel = new JSlider(0,1, f.getSmartOuPas()); // je crée un slide en lui donnant des valeur entre 0 et 1 et en débutant à partir d'un point précis
		intel.addChangeListener(this); //action écoutée 
		intel.setMaximumSize(new Dimension(50,100));

		comteneur1.add(l2);
		comteneur1.add(intel); //affichage du slider
		int zoomMax;//Creation d'un variable zoom max.
		if(f.getLamap().getTer().length<70)
			zoomMax=f.getLamap().getTer().length;
		else
			zoomMax=70;
		zoom =new JSlider (20, zoomMax , 20);
		zoom.addChangeListener(this);
		zoom.setMajorTickSpacing(20); //affiche les grande graduation toute les 20
		zoom.setMinorTickSpacing(1); //affiche des petite graduation tout les 1.
		zoom.setPaintTicks(false); //affiche les graduatio avec des petit tirŽ
		zoom.setPaintLabels(false); //Affiche les graduation avec des nombre
		zoom.setMaximumSize(new Dimension(300,50));
		pheromone = new JSlider(1,100 ,1);
		pheromone.addChangeListener(this);
		pheromone.setMajorTickSpacing(100);
		pheromone.setMinorTickSpacing(1);
		pheromone.setPaintTicks(false);
		pheromone.setPaintLabels(false);

		comteneur2.add(l3);
		comteneur2.add(zoom);
		comteneur2.add(l6);
		comteneur2.add(pheromone);
		distance = new JSlider(50, f.getDistanceMax(), f.getDistanceMax());
		distance.addChangeListener(this);
		distance.setMajorTickSpacing(f.getDistanceMax()/2); 
		distance.setMinorTickSpacing(1); 
		distance.setPaintTicks(false); 
		distance.setPaintLabels(false);
		comteneur3.add(l4);
		comteneur3.add(distance);
		nourriture = new JSlider(10, 10000, f.getDisparitionNourriture());
		nourriture.addChangeListener(this);
		nourriture.setMajorTickSpacing(3000); 
		nourriture.setMinorTickSpacing(1); 
		nourriture.setPaintTicks(false); 
		nourriture.setPaintLabels(false);
		comteneur3.add(l5);
		comteneur3.add(nourriture);



	}


	public void stateChanged(ChangeEvent a){ //permet d'interargir avec les sliders

		int s = speed.getValue();
		f.setVitesseSimulation(s);
		int i = intel.getValue(); // on récupère la valeur du slider
		f.setSmartOuPas(i); //ceci fait varier l'intelligence des fourmis 
		int z = zoom.getValue();
		b.setZoom(z);	
		int d = distance.getValue();
		f.setDistanceMax(d); 
		int n = nourriture.getValue();
		f.setDisparitionNourriture(n);
		f.changerNourriture(n);
		int p = pheromone.getValue();
		f.setVitesseDisparitionPheromone(p);

		/*
			String valeur = String.valueOf( ((JSlider) a.getSource()).getValue() );
			if (((JSlider) a.getSource()).getValue() == f.getLamap().getTer().length ){valeur="ZOOM MAX";}
			if (((JSlider) a.getSource()).getValue() == 10000){valeur="NOURRITURE INFINI";}
			if (((JSlider) a.getSource()).getValue() == 1000){valeur="1 deplacement/S";}
			if (((JSlider) a.getSource()).getValue() == 0){valeur="VITESSE MAX";}
			la.setText("Slider Actif: "+valeur);
		 */
		String intel;
		if(i==0){intel = "Non";}else{intel = "Oui";}
		l2.setText("Intelligence : "+intel);

		String speed = String.valueOf("0,"+s+"/S");
		if(s==0){speed = "MAXIMUM";}
		if(s==1000){speed = "MINIMUM";}
		l.setText("Vitesse : "+speed);
		String nourriture=String.valueOf(n);
		if(n==10000){nourriture="INFINI";}
		l5.setText("Nourriture : "+nourriture);
		String distance=String.valueOf(d);
		l4.setText("Distance : "+distance);
		String zoom=String.valueOf(z+" cases");
		if(z==f.getLamap().getTer().length || z==70){zoom="MINIMAL";}
		if(z==20){zoom="MAXIMUM";}
		l3.setText("Zoom : "+zoom);
		String pheromone = String.valueOf(p);
		l6.setText("Pheromone : -"+pheromone);
	}

	class BoutonPlay implements ActionListener{ // Action sur le bouton ">"
		public void actionPerformed(ActionEvent a){
			if(f.getCompteur()== 0){
				t = new Thread(new Play()); 
				t.start();	
			}
			f.setPlayOuPause(true);
			//bou.setSuppdefourmis(false);} // empeche d'ajouter des fourmis manuellement 
			b.startChrono();
			b.activeBoutonFourmi(false);
		}
	}

	class BoutonPause implements ActionListener{ // Action sur le bouton "||"
		public void actionPerformed(ActionEvent a){
			if(a.getSource() == pause){
				f.setPlayOuPause(false);
				b.stopChrono();
				b.activeBoutonFourmi(true);
			}
		}
	}

	class BoutonRetour implements ActionListener{ // Action sur le bouton "<--"
		public void actionPerformed(ActionEvent a){
			if(a.getSource() == retour){
				int choix2 = JOptionPane.showConfirmDialog(null,"Retour à l'accueil ?", null, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(choix2 !=JOptionPane.NO_OPTION){
					f.setSimulation(false);
					if(!f.isSimulation()){b.getFenetre().dispose();}
					
					
					new Accueil();
				}

			}
		}
	}

	class Play implements Runnable{
		public void run(){
			f.affichage();
		}
	}
}



