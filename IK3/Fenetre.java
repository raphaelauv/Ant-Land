package IK3;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
public class Fenetre extends JPanel {
	Case [][] ter;
	BufferedImage terrain=null;
	BufferedImage fourmi=null;
	BufferedImage obstacle=null;
	BufferedImage nourriture=null;
	BufferedImage fourmiliere=null;
	BufferedImage meat0=null;
	BufferedImage meat1=null;
	BufferedImage meat2=null;	
	BufferedImage nid=null;
	int a,b;
	public Fenetre(Terrain map){//cree le JPanel ou on pourra voir la partie zoomer en detail.
		this.ter=map.getTer();
		 try {
	        	terrain = ImageIO.read(this.getClass().getClassLoader().getResource("herbe.png"));
	        	fourmi = ImageIO.read(this.getClass().getClassLoader().getResource("fourmi.png"));
	        	obstacle= ImageIO.read(this.getClass().getClassLoader().getResource("obstacle2.png"));
	        	nourriture=ImageIO.read(this.getClass().getClassLoader().getResource("nourriture3.png"));
	        	fourmiliere=ImageIO.read(this.getClass().getClassLoader().getResource("fourmiliere.png"));
	    		meat0=ImageIO.read(this.getClass().getClassLoader().getResource("pheromoneMeat/pheromone1.png"));
	    		meat1=ImageIO.read(this.getClass().getClassLoader().getResource("pheromoneMeat/pheromone2.png"));
	    		meat2=ImageIO.read(this.getClass().getClassLoader().getResource("pheromoneMeat/pheromone3.png"));
	    		nid = ImageIO.read(this.getClass().getClassLoader().getResource("herbeNid.png"));
	        } catch (IOException e) {
	            System.out.println("Une image na pas etait trouver.");
	        }
	}
	public void paintComponent(Graphics g){ 
		int widthCase=getWidth()/ter.length;//Je divise la taille de mon jpanel par la taille du zoom. J'obtiens la taille d'une case.
		int heigtCase=getHeight()/ter.length;
		a=widthCase;
		b=heigtCase;
		g.setColor(Color.gray);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());//permet d'effacer tout ce qu'il y avait avant.Pour pas avoir des problemes de superposition quand on repaint.
		/*Boucles servant a parcourir la map(ter)
		 * 	x,y sont les coordonnees des px sur l'ecran. On les inialise a O.
		 * 	a chaque iteration on les incremente de la taille d'une case.
		 */
		
		int x=0,y=0;
		for (int i=0;i<ter.length;i++){
			x=0;
			for (int j=0;j<ter.length;j++){
				if(ter[i][j].isFourmiliere()){
					g.drawImage(fourmiliere,x, y, widthCase, heigtCase,this);
				}
				else {
					if(ter[i][j].isObstacle()){
						g.drawImage(obstacle,x, y, widthCase, heigtCase,this);
					}
					if(ter[i][j].isAccesible()){
						g.drawImage(terrain,x, y, widthCase, heigtCase,this);
					}
					if(ter[i][j].isNourriture()){
						g.drawImage(nourriture,x, y, widthCase, heigtCase,this);
					}
				}
				x+=widthCase;
			}		
			y+=heigtCase;
		}
	}
}
