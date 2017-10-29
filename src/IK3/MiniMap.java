package IK3;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
public class MiniMap extends JPanel implements MouseMotionListener{//Cree une minimap sur laquelle on peut voir une grande partie de la map.
	//les fourmis et les pheromones. On aura un rectangle qui marquera la zone zoomer.
	//Si le terrain est plus grand que 100*100(case) on pourra se deplacer a l'aide des touches directionnelles, pour aller voir plus loin.
	private Case[][] ter;
	private int mX=0,mY=0,avantX=0,avantY=0,debutX,debutY,finX,finY,positionX, positionY,widthCase=0,heightCase=0,zoom,x,y;
	FenetreTerrain fenetreterrain;
	/**
	 * Cree une minimap reprentant le terrain.
	 * @param fourmiliere @see Fourmiliere
	 * @param fenetreterrain @see FenetreTerrain
	 */
	public MiniMap(Fourmiliere fourmiliere,final FenetreTerrain fenetreterrain){
		this.fenetreterrain=fenetreterrain;
		this.ter=fourmiliere.getLamap().getTer();
		this.setBackground(Color.white);
		//Initialisation de la zone zoomer au milieu de la fourmiliere.
		x=fourmiliere.getLamap().getFourmiliere()[0][1];//abscisses fourmiliere.
		y=fourmiliere.getLamap().getFourmiliere()[0][0];//ordonnees fourmiliere.
		zoom=20;//zoom initialise a 20*20(case).
		this.addMouseListener(new MouseAdapter(){//permet de cliquer sur la zone qu'on voudra zoomer.	
			public void mousePressed(MouseEvent e){
				x=e.getX()/widthCase;//Recuperation de l'abscisse ou l'utilisateur a clicker.Je le divise pas la taille d'une case pour obtenir la case dans mon tableau (ter).
				y=e.getY()/heightCase;
				position();
				fenetreterrain.zoneZoom(zoom, positionX/widthCase+debutX, positionY/heightCase+debutY);
			}
		});
		if(ter.length>100){ //Si le terrain et superieur a 100*100(case).
			addMouseMotionListener(this);//permet de capter les mouvements de la souris de l'utilisateur.
			//Initialisation des variables qui me permette de savoir ou va etre ma minimap par rapport a mon terrain entier.
			debutX=x-50;//(abscisse)j'initialise le debut 50 avant ma foumiliere.
			finX=0;
			debutY=y-50;//(ordonner)
			finY=0;
			positionMinimap();
			KeyboardFocusManager.getCurrentKeyboardFocusManager()
			.addKeyEventDispatcher(new KeyEventDispatcher() {
				@Override
				public boolean dispatchKeyEvent(KeyEvent e) {//recupere les touches du clavier actives. 
					int code=e.getKeyCode();//renvoie le code de la touche activee par l'utilisateur.
					if(code==37){//gauche
						mX-=3;
					}
					if(code==38){//haut
						mY-=3;
					}
					if(code==39){//droite
						mX+=3;
					}
					if(code==40){//bas
						mY+=3;
					}
					deplacementMinimap();
					return false;
				}
		  });
		}
		else{ //si la map est strictement plus petite que 100*100(case)
			debutX=0;//Je prends donc toute la map.
			debutY=0;
			finX=ter.length;
			finY=ter.length;
		}
		//je fais en sorte de savoir les coordonnees de la fourmiliere dans la fourmiliere.
		//j'enleve donc l'excès. (x,y) sont desormais les coordonnees de la foumiliere dans ma miniMap (et ne plus de la map entiere)
		x-=debutX;
		y-=debutY;
	}
	/**
	 * 
	 * @param zoom taille du zoom.
	 */
	public void setZoom(int zoom) {
		this.zoom = zoom;
		position();
		//Je divise positionX et positionY par la taille d'une case pour obtenir les coordonnees dans la minimap.
		//puis je rajoute l'excŽ pour avoir les coordonnees dans la map entiere(coordonnees dans ter).
		fenetreterrain.zoneZoom(zoom, positionX/widthCase+debutX, positionY/heightCase+debutY);
	}
	public void paintComponent(Graphics g){
		if (widthCase==0){//Ce if sert a resoudre un probleme rencontre; getWidth() et getHeight() sont egales a 0 a l'appel
			//de ces fonctions dans le constructeur alors qu'ici il prend les valeurs reelles. Et donc la methode position() ne pouvait marcher.
			//donc ce if sert a faire marcher la methode position des le debut.(voir fonctionnaliteer position())
			widthCase=getWidth()/(finX-debutX);//Je divise pas la taille du minimap pour avoir la taille d'une case(en px)
			heightCase=getHeight()/(finY-debutY);
			position();
		}
			
		g.setColor(Color.white);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());//permet d'effacer tout ce qu'il avait avant.Pour pas avoir des problemes de superposition
		widthCase=getWidth()/(finX-debutX);//Je divise pas la taille du minimap pour avoir la taille d'une case(en px)
		heightCase=getHeight()/(finY-debutY);
		/*Boucle servant a parcourir ma map(ter)
		 * 	x,y sont les coordonnees des px sur l'ecran. On les initialise a O.
		 * 	a chaque iteration on les incremente de la taille d'une case.
		 */
		int x=0,y=0;
			for (int i=debutY;i<finY;i++){
				x=0;
				for (int j=debutX;j<finX;j++){
					if(ter[i][j].getMeat()>0){ //S'il a de la pheromone meat.
						Graphics2D g2=(Graphics2D) g;
						g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.35f));//je donne de la tranparence.
						g2.setColor(Color.red);//je choisis la couleur rouge.
						g2.drawOval(x, y, widthCase, heightCase);//je trace un oval aux coordonnees (x,y)(px) et de longeur WidthCase et largeur heightCase
						g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1.0f));//j'enleve la transparence.
					}
					if(ter[i][j].isFourmiliere()) { //S'il y a une fourmiliere
						g.setColor(Color.BLUE);
						g.fillRect(x, y, widthCase+3, heightCase+3);
					}
					else if (ter[i][j].isObstacle()){ //sinon s'il a un obstacle.
						g.setColor(Color.GRAY);
						g.fillRect(x, y, widthCase+3, heightCase+3);
					}
					
					else if (ter[i][j].isNourriture()){//sinon, s'il y a de la nourriture
						g.setColor(Color.RED);
						g.fillRect(x, y, widthCase+3, heightCase+3);
					}
					else if(ter[i][j].getNombreDeFourmis()!=0){ //sinon, s'il a une foumi
						g.setColor(Color.cyan);
						g.drawOval(x, y, widthCase, heightCase);
					}
					else {
						g.setColor(Color.GRAY);
						g.drawOval(x, y, widthCase, heightCase);
					}
					
					x+=widthCase;
				}		
				y+=heightCase;
			}
			g.setColor(Color.BLACK);
			g.drawRect(positionX, positionY, zoom*widthCase,zoom*heightCase);//ce rectangle signal la partie zoomer dans FenetreTerrain.
			g.setColor(Color.RED);
			if(ter.length<100){//si le terrain est plus petit que 100*100(case)
				g.drawRect(0, 0, widthCase*ter.length, heightCase*ter.length); // je signal les limites du terrain.
				}
			else{//si mon terrain plus grand que 100*100(case)
				if (debutX==0){// Si je suis tout a gauche de ma map.
					g.drawLine(0, 0, 0, 100*heightCase);//je trace une ligne rouge pour signaler qu'on peut plus monter.
					g.drawLine(1, 0, 1, 100*heightCase);
				}
				if(debutY==0){//si je suis tout en haut.
					g.drawLine(0, 0, 100*widthCase, 0);
					g.drawLine(0, 1, 100*widthCase, 1);
				}
				if (finX==ter.length){//si je suis tout a droite
					g.drawLine(100*widthCase,0, 100*widthCase, 100*heightCase);
					g.drawLine(100*widthCase-1,0, 100*widthCase-1, 100*heightCase);
				}
				if (finY==ter.length){//devine?
					g.drawLine(0, 100*heightCase,100*widthCase, 100*heightCase);
					g.drawLine(0, 100*heightCase-1,100*widthCase, 100*heightCase-1);
				}
			}
	}
	private void positionMinimap(){//cette methode positionne ma minimap et fait en sorte de pas sortir de ma map.
		if(debutX<0)//si mon debutX est negatif(ca veut dire que je suis sorti de mon terrain (ter))
			debutX=0;
		if(debutX>ter.length)//si debutX est plus grand que la taille de mon terrain(ter).
			debutX=ter.length-100;
		if(debutY<0)
			debutY=0;
		if(debutY>ter.length)
			debutY=ter.length-100;
		if(debutX+100>ter.length)
			debutX=ter.length-100;
		if(debutY+100>ter.length)
			debutY=ter.length-100;
		//cette fonction est appelee uniquement si le terrain est strictement Superieur a 100
		// Donc je sais que ma fin se situe 100 cases apres.
		finX=debutX+100;
		finY=debutY+100;
	}
	private void position(){//cette methode positionne le rectangle qui represente la partie zoomer dans FenetreTerrain
		positionX=x;//coordonnees(dans la minimap) de la case ou on a clicke ou la fourmiliere(au debut).
		positionY=y;
		boolean posX=false,posY=false;//Ces boolean me servent a savoir s'il a eu des changements sur les coordonnees.
		if(positionY-zoom/2<=0 ){//si ordonnee negative,ca veut dire qu'on est sorti de la minimap.
			positionY=0;//pour pas etre hors du tableau on met l'ordonnee a 0
			posY=true;//et on indique qu'on a change les ordonnees.
				}
		if(positionX-zoom/2<=0){//si abscisse negative
			positionX=0;
			posX=true;//on indique qu'on a change les abscisses.
		}
		if(positionY+zoom>=(finY-debutY)+1){//si ordonnee plus grand que la taille de la minimap.
			positionY=heightCase*(finY-debutY)-zoom*heightCase;//on met donc ordonnees a la fin de la minimap (heightCase*(finY-debutY)) et on enleve la place(px) prise par le zoom. 
			posY=true;//et on indique qu'on a change les ordonnees.
		}
		if(positionX+zoom>=(finX-debutX)+1){//si abscisse plus grand que la taille de la minimap.
			positionX=widthCase*(finX-debutX)-zoom*widthCase;
			posX=true;
		}
		if(!posX) {//si on a pas change les abscisses
				positionX=widthCase*positionX-zoom*widthCase/2;//pour trouver l'abscisse en px en multiplie positionX par la taille d'une case puis on enleve la place(px) prise par le zoom
			}
		if(!posY)//si on a pas change les ordonnees.
			positionY=heightCase*positionY-zoom*heightCase/2;
		}
		@Override
		public void mouseDragged(MouseEvent e) {//cette fonction est appelee quand je click(sur la minimap).
			// TODO Auto-generated method stub
			mouseMoved(e);
			int  moveX= (int) e.getPoint().getX();//abscisse actuelle de ma souris 
			int moveY= (int) e.getPoint().getY();//ordonnee actuelle de ma souris 
			if (avantX>moveX){//l'abscisse est plus grand qu'avant ca veut dire qu'on est allr vers la droite avec la souris.
				mX++;
			}
			else if (avantX<moveX){//l'abscisse est plus petit qu'avant ca veut dire qu'on est alle vers la gauche avec la souris.
				mX--;
			}
			if (avantY>moveY){//l'ordonnee est plus petit qu'avant ca veut dire qu'on est alle vers le haut avec la souris.
				mY++;
			}
			else if (avantY<moveY ){//l'ordonnee est plus grand qu'avant ca veut dire qu'on est alle vers le bas avec la souris.
				mY--;
			}
			//j'enregistre les positions actuelles.
			avantX=moveX;
			avantY=moveY;
			deplacementMinimap();
		}
		@Override
		public void mouseMoved(MouseEvent e) {//cette methode me sert a donner toutes les coordonnees(px) de ma souris quand elle bouge(constament)
		}
		public void deplacementMinimap(){//cette methode sert a deplacer la minimap.
			boolean changement=false;//ce boolean me sert a savoir si les coordonnees ont change.
			if(mX>=3 || mX<=-3){//si l'utilisateur est alle a droite ou a gauche avec la souris (3 fois d'affiler)
				debutX+=mX*5; //on bouge la minimap de 15 cases vers la droite ou la gauche.
				changement=true;//on dit qu'on a change les coordonnees
			}
			if(mY>=3 || mY<=-3){//si l'utilisateur est alle en haut ou en bas avec la souris (3 fois d'affiler)
				debutY+=mY*5; //on bouge la minimap de 15 cases vers en haut ou en bas.
				changement=true;//on dit qu'on a change les coordonnees
			}
			if (changement){//s'il y a eu des changements.
				mY=0;//Je reInitialise mY,mX.
				mX=0;
				positionMinimap();//je repositionne ma minimap
				position();//je repositionne mon rectangle qui indique l'endroit que je zone.
				fenetreterrain.zoneZoom(zoom, positionX/widthCase+debutX, positionY/heightCase+debutY);//et je met a jour ma FenetreTerrain.
			}
		}
	}
