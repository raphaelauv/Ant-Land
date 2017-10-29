package IK3;
import javax.swing.*;
import java.awt.event.*;
public class InfoHaut extends JPanel{
	JLabel temps;
	JLabel Mouvements;
	JLabel nombreFourmi;
	JLabel nourritureAcquise;
	Timer chrono;
	JLabel tailleTerrain;
	public InfoHaut(final Fourmiliere laFourmiliere){ // la Fourmiliere a ete declare en final Fourmiliere car je change une methode de la class Timer(de java)
		//ce qui exige un objet final
		this.setLayout(new BoxLayout(this,BoxLayout.LINE_AXIS));//Layout vertical.
		temps=new JLabel("0");
		nombreFourmi=new JLabel("<html>Nombre de Fourmi: <br\\> "+ laFourmiliere.getTotalFourmis() +"</html>");
		Mouvements=new JLabel("<html>Nombre de deplacements: <br\\> "+ laFourmiliere.getCompteur() +"</html>");
		nourritureAcquise=new JLabel("<html> Nourriture acquise: <br\\>"+laFourmiliere.getNourritureAquise()+"</html>");
		tailleTerrain = new JLabel("<html> Taille Terrain: <br\\>"+laFourmiliere.getLamap().getTer().length+" cases"+"</html>");
		this.add(temps);
		this.add(Mouvements);
		this.add(nombreFourmi);
		this.add(nourritureAcquise);
		this.add(tailleTerrain);
		final Time time=new Time();//un nouveau objet Time qui contient juste un int initialise a 0.
		chrono=new Timer(1000,new ActionListener(){//le timer se declenche toutes les secondes.
			@Override
			public void actionPerformed(ActionEvent e){
				int b=laFourmiliere.getTotalFourmis();
				int c=laFourmiliere.getNourritureAquise();
				int d=laFourmiliere.getCompteur();
				int seconde,minute=0,heur=0;
				time.setTime(time.getTime()+1);//j'incremente le int dans l'objet time.(car le timer se declenche toutes les secondes)
				seconde=time.getTime();
				minute=seconde/60;
				heur=minute/60;
				seconde-=minute*60;
				minute-=heur*60;
				temps.setText("<html> TEMPS: <br\\>"+heur+":"+minute+":"+seconde+"</html>");
				nombreFourmi.setText("<html>Nombre de Fourmi: <br\\>"+ b +"</html>");
				Mouvements.setText("<html>Nombre de deplacements: <br\\>"+ d +"</html>");
				nourritureAcquise.setText("<html> Nourriture acquise: <br\\>"+c+"</html>");
			}
		});
		chrono.start();//Je mets en marche le Timer.
	}
	public void stopChrono(){//arrete le chrono
		chrono.stop();
		//nombreFourmi.setText("<html>Nombre de Fourmi: <br\\>" +"a ACTUALISER" +"</html>");
	}
	public void startChrono(){
		chrono.start();
	}
	public void UpdateNombredeFourmis(int a){
		nombreFourmi.setText("<html>Nombre de Fourmi: <br\\>"+ a +"</html>");
	}
}