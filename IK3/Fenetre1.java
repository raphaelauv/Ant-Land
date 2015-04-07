package IK3;
import java.awt.*;

import javax.swing.*;
public class Fenetre1 extends JFrame {
	public Fenetre1(Fenetre map,Info info){
		int longeur=getToolkit().getScreenSize().width;//width de votre ecran.
		int largeur=getToolkit().getScreenSize().height;//height maximal de votre ecran
		//J'affiche ma fenetre sur 90% de l'espace disponible.
		largeur=(int)(largeur*0.90);
		longeur=(int)(longeur*0.90);
		JFrame fenetre=new JFrame();
		fenetre.setTitle("FourmiLand");
		fenetre.setSize(longeur, largeur);//je donne la taille de ma fenetre.
		JPanel comteneur0= new JPanel();
		comteneur0.setLayout(new BoxLayout(comteneur0,BoxLayout.PAGE_AXIS));
		comteneur0.add(info);
		map.setPreferredSize(new Dimension((int)(fenetre.getWidth()*0.15),(int)(fenetre.getWidth()*0.5)));
		comteneur0.add(map);
		fenetre.setContentPane(comteneur0);
		fenetre.setLocationRelativeTo(null);
		fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenetre.setVisible(true);
	}
}

