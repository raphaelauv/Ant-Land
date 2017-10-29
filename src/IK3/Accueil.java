package IK3;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
public class Accueil extends JFrame implements ActionListener{
	Fourmiliere f;
	private JFormattedTextField a = new JFormattedTextField(); //création d'un champ 
	private JLabel a1 = new JLabel("Nombre de fourmis");
	private JFormattedTextField b = new JFormattedTextField();
	private JLabel b1 = new JLabel("Taille du terrain");
	private JFormattedTextField c = new JFormattedTextField();
	private JLabel c1 = new JLabel("Nombre de nourritures");
	private JFormattedTextField d = new JFormattedTextField();
	private JLabel d1 = new JLabel("Nombre d'obstacles");
	private JButton ok = new JButton("ok");
	private JCheckBox intelligence = new JCheckBox("intelligence");
	private JButton alea=new JButton("ALEATOIRE");
	private Thread t;
	public Accueil(){
		int largeur=150,longeur=800;
		Font font = new Font("Arial",Font.BOLD,13);
		a1.setFont(font);
		b1.setFont(font);
		c1.setFont(font);
		d1.setFont(font);
		this.setTitle("Accueil");
		this.setSize(longeur, largeur);
		this.setMinimumSize(new Dimension(100,100));
		this.setLocationRelativeTo(null);
		JPanel content0= new JPanel(); // conteneur de tous les boutons
		content0.setLayout(new BoxLayout(content0,BoxLayout.PAGE_AXIS));
		JPanel content1= new JPanel();
		JPanel milieu =new JPanel();
		JPanel content = new JPanel();
		content.setMaximumSize(new Dimension(1000,20));
		content0.add(content1); // bouton aleatoire
		content0.add(milieu); // bouton intelligence
		content0.add(content); // bouton manuel


		content.setLayout(new BoxLayout(content,BoxLayout.LINE_AXIS));
		content.add(a1);
		content.add(a);
		content.add(b1);
		content.add(b);
		content.add(c1);
		content.add(c);
		content.add(d1);
		content.add(d);
		content.add(ok);
		content1.setLayout(new BoxLayout(content1,BoxLayout.LINE_AXIS));
		intelligence.setSelected(true);
		milieu.add(intelligence);

		content1.add(alea);


		ok.addActionListener(this);
		alea.addActionListener(this);
		this.setContentPane(content0);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public synchronized void actionPerformed(ActionEvent r){
		if(r.getSource() == ok){
			int fourmi=0,nombreNourriture=0,nombreObstacle=0,terrain=0; 
			try {
				fourmi=Integer.parseInt(a.getText());
				terrain=Integer.parseInt(b.getText());
				nombreNourriture=Integer.parseInt(c.getText());
				nombreObstacle=Integer.parseInt(d.getText());
				if(fourmi>=0 && terrain>=20 && terrain<2001 && nombreNourriture+nombreObstacle<terrain*terrain){ // on test si les champs remplis correspond à un terrain valide
					this.dispose();
					t = new Thread(new Play()); //permet de générer la simulation 
					t.start();	     
				}
				else{
					String newLine = System.getProperty("line.separator");
					JOptionPane.showMessageDialog(this,
							"Le nombre de fourmis doit etre inferieur a 10 000."+newLine
							+ "Le terrain doit etre de mimimum 20 case et maximum 180."+newLine
							+"La somme des source de nourriture et des obstacle dois etre strictement inferieur au nombre de case du terrain:"+newLine
							+"ATTENTION"+newLine
							+"Avec un terrain de " +terrain +" cases , vous ne pouvez pas mettre plus de "+(terrain*terrain-1) +" obstacle et nourriture au total",
							"warning: Fourmiland Beta.",
							JOptionPane.WARNING_MESSAGE); //affichage d'une fenêtre d'alerte si les champs ne sont pas valides
					reset();
				}

			} catch (NumberFormatException i) {
				JOptionPane.showMessageDialog(this,"Les Champs doivent etre des nombres Entier et obligatoirement rempli.",
						"Erreur",
						JOptionPane.ERROR_MESSAGE);
				reset();
			}
		}
		if(r.getSource() == alea){
			this.dispose();
			t = new Thread(new Play2());
			t.start();

		}
	}
	private void reset(){
		a1.setText("Nombre de Fourmis");
		b1.setText("Taille du Terrain");
		c1.setText("Nombre de Nourritures");
		d1.setText("Nombre d'obstacles");
		this.repaint();
	}
	class Play implements Runnable{
		public  synchronized void run(){
			f = new Fourmiliere(
					Integer.valueOf(a.getText()),
					Integer.valueOf(b.getText()),
					Integer.valueOf(c.getText()),
					Integer.valueOf(d.getText()),100,
					intelligence.isSelected()
					);
			f.affichage();
		}
	}
	class Play2 implements Runnable{
		public synchronized void run(){
			f = new Fourmiliere(intelligence.isSelected());
			f.affichage();
		}
	}
}