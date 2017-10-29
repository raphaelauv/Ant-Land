package IK3;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
public class FenetreTerrain extends JPanel {
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
	private int debutX,debutY,finX,finY,zoom,widthCase,heigtCase;
	public int getDebutX() {
		return debutX;
	}
	/**
	 * 
	 * @param debutX represente l'abscisse dans le terrain(debut du zoom).   
	 */
	public void setDebutX(int debutX) {
		this.debutX = debutX;
	}
	/**
	 * 
	 * @return l'ordonner de la derniere case du zoom.
	 */
	public int getDebutY() {
		return debutY;
	}
	/**
	 * 
	 * @param debutY change le debut(abscisse) du zoom.
	 */
	public void setDebutY(int debutY) {
		this.debutY = debutY;
	}
	/**
	 * @param fourmiliere1 @see Fourmiliere 
	 */
	public FenetreTerrain(Fourmiliere fourmiliere1){//cree le JPanel ou on pourra voir la partie zoomer en detail.
		zoom=20;//le zoom est initialise a 20.
		this.ter=fourmiliere1.getLamap().getTer();
		position(fourmiliere1.getLamap().getFourmiliere()[0][1],fourmiliere1.getLamap().getFourmiliere()[0][0]);
		 try {
			 
			 //terrain=ImageIO.read(Thread.currentThread().getContextClassLoader().getResource("herbe.png"));
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
	/**
	 * 
	 * @param zoom taille du zoom.
	 * @param x abscisse de la premiere case du zoom.
	 * @param y ordonner de la premiere case du zoom
	 */
	public void zoneZoom(int zoom,int x,int y){//cette partie sert a changer la zone de zoom.
		if (ter.length>20){//Si map plus grand que 20*20(case)
			//on initialise le debut du zoom a la case de la foumiliere.
			debutX=x;
			debutY=y;
			finX=x+zoom;
			finY=y+zoom;
			this.zoom=zoom;
		}
		else{//Si le terrain est plus petit que 20*20(On peut pas faire de terrain plus petit que 20*20)
			debutX=0;
			debutY=0;
			finX=ter.length;
			finY=ter.length;
		}
	}
	/**
	 * paint le JPanel. Pour voir le terrain et les fourmis. 
	 */
	public void paintComponent(Graphics g){ 
		widthCase=getWidth()/zoom;//Je divise la taille de mon jpanel par la taille du zoom. J'obtiens la taille d'une case.
		heigtCase=getHeight()/zoom;
		g.setColor(Color.gray);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());//permet d'effacer tout ce qu'il y avait avant.Pour pas avoir des problemes de superposition quand on repaint.
		/*Boucles servant a parcourir la map(ter)
		 * 	x,y sont les coordonnees des px sur l'ecran. On les inialise a O.
		 * 	a chaque iteration on les incremente de la taille d'une case.
		 */
		int x=0,y=0;
		for (int i=debutY;i<finY;i++){
			x=0;
			for (int j=debutX;j<finX;j++){
				g.drawImage(terrain,x, y, widthCase, heigtCase,this);//J'affiche l'image terrain(qui represente le sol)
				//aux coordonnees (x,y) et de taille (widthCase,heigtCase).	
				if(ter[i][j].getMeat()>0){//sinon, s'il y a de la pheromone meat. 
					int meat=ter[i][j].getMeat();
					if (meat<4){//s'il y a strictement moins de 4 pheromones meat.
						g.drawImage(meat0,x, y, widthCase, heigtCase,this);
					}
					else if (meat>=4 && meat<8){//s'il y a plus de 4 pheromones meat.
						g.drawImage(meat1,x, y, widthCase, heigtCase,this);
					}
					else{//s'il y a plus de 8 pheromones meat.
						g.drawImage(meat2,x, y, widthCase, heigtCase,this);
					}	
				}
				else if (ter[i][j].isNid()){//si la case i,j il y a de la pheromonde Nid.
					g.drawImage(nid,x, y, widthCase, heigtCase,this);//affiche la photo nid.
				}
				if (ter[i][j].isNourriture()){//si la case i,j est de la nourriture.
					g.drawImage(nourriture,x, y, widthCase, heigtCase,this);
				}
				else if (ter[i][j].isObstacle()){//sinon, si la case i,j est un obstacle.
					g.drawImage(obstacle,x, y, widthCase, heigtCase,this);
				}
				else if (ter[i][j].isFourmiliere()){//Si la case I,J est une foumiliere.
					g.drawImage(fourmiliere,x, y, widthCase, heigtCase,this);//affiche la photo fourmiliere
				}
				if (ter[i][j].getNombreDeFourmis()>0)//s'il y a des fourmis a la case i,j.
					g.drawImage(fourmi,x, y, widthCase, heigtCase,this);
				x+=widthCase;
			}		
			y+=heigtCase;
		}
	}
	/**
	 * 
	 * @return la longeur(en px) d'une case dans ce JPanel.
	 */
	public int getWidthCase() {
		return widthCase;
	}
	/**
	 * 
	 * @return la largeur(en px) d'une case dans le JPanel.
	 */
	public int getHeigtCase() {
		return heigtCase;
	}
	private void position(int x, int y){//Cette fonction sert au lancement de d'un objet FenetreTerrain.Il positionne le zoom au milieu de fourmiliere. 
		int [] position=new int [2];//Creation d'un tableau de int a 2 case. pour les coordonner du debutX,debutY.
		boolean posX=false,posY=false;//ces boolean serve a savoir si il y a eut des changement de coordonner.
		if(y-zoom/2<0){//si y-zoom est negatif. Ce qui veut dire qu'il sort du tableau(ter).
			position[1]=0;
			posY=true;//on dit que les ordonnees ont change.
		}
		if(x-zoom/2<0){//si x-zoom est negatif. Ce qui veut dire qu'il sort du tableau(ter).
			position[0]=0;
			posX=true;//on dit que les abscisses ont change.
		}
		if(y+zoom>ter.length){//si y-zoom est plus grand que ter.length. Ce qui veut dire qu'il sort du tableau(ter).
			position[1]=ter.length-zoom;
			posY=true;
		}
		if(x+zoom>ter.length){//si x-zoom est plus grand que ter.length. Ce qui veut dire qu'il sort du tableau(ter).
			position[0]=ter.length-zoom;
			posX=true;
		}
		if(!posX) {//s'il n'y a pas eu de changement.
			position[0]=x-zoom/2;
		}
		if(!posY)
			position[1]=y-zoom/2;
		debutX=position[0];
		debutY=position[1];
		finX=debutX+zoom;
		finY=debutY+zoom;
	}
}