package IK3;
import javax.swing.*;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.event.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
public class Info extends JPanel implements ChangeListener{
	JSlider speed;
	Terrain map;
	JLabel fonction;
	JLabel taille;
	public Info(Terrain map){
		this.map=map;
		this.setLayout(new BoxLayout(this,BoxLayout.LINE_AXIS));
		speed = new JSlider(1,1000,map.getVitesseSimulation());
		speed.addChangeListener(this);
		speed.setMaximumSize(new Dimension(300,100));
		speed.setMajorTickSpacing(200); 
		speed.setMinorTickSpacing(1); 
		speed.setPaintTicks(false); 
		speed.setPaintLabels(false);
		taille=new JLabel("INFO:  ");
		fonction=new JLabel("FONCTION: ");
		taille.setText("         INFO : Slut"          );
		fonction.setText("      FONCTION : Constructeur TerrainF        ");
		Font font = new Font("Arial",Font.BOLD,15);
		fonction.setFont(font);
		taille.setFont(font);
		this.add(speed);
		this.add(taille);
		this.add(fonction);
	}
	
	public void stateChanged(ChangeEvent a){ //permet d'interargir avec les sliders
		map.setVitesseSimulation(speed.getValue());
	}
	public void setFonction(String a) {
		this.fonction.setText(a);
	}
	public void setTaille(String a) {
		this.taille.setText(a);
	}
	
	
}
