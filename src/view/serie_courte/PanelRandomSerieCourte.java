package view.serie_courte;

import model.ButtonEditor;
import model.parameter.actors.Actor;
import model.parameter.genres.Genre;
import model.parameter.platforms.Platform;
import model.parameter.users.User;
import model.serie_courte.GestionnaireSerieCourte;
import model.serie_courte.SerieCourte;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class PanelRandomSerieCourte extends JPanel {

	private final GestionnaireSerieCourte gestionnaireSerieCourte;
	private final SerieCourteFrame serieCourteFrame;

	private Actor[] actors;
	private Genre[] genres;
	private int nbSaison;
	private int nbEpisode;
	private int dureeMoyenne;
	private Date dateSortie;
	private Date dateSortie2;
	private Platform[] plateformes;
	private int dejaVu = -1;
	private User addBy;

	private SerieCourte serieCourte;
	private JPanel serieCourtePanel;

	public PanelRandomSerieCourte(GestionnaireSerieCourte gestionnaireSerieCourte, SerieCourteFrame serieCourteFrame) {
		this.gestionnaireSerieCourte = gestionnaireSerieCourte;
		this.serieCourteFrame = serieCourteFrame;
		initializeUI();
	}

	public PanelRandomSerieCourte(GestionnaireSerieCourte gestionnaireSerieCourte, SerieCourteFrame serieCourteFrame, Actor[] actors, Genre[] genres, int nbSaison, int nbEpisode, int dureeMoyenne, Date dateSortie, Date dateSortie2, Platform[] plateformes, int dejaVu, User addBy) {
		this.gestionnaireSerieCourte = gestionnaireSerieCourte;
		this.serieCourteFrame = serieCourteFrame;
		this.actors = actors;
		this.genres = genres;
		this.nbSaison = nbSaison;
		this.nbEpisode = nbEpisode;
		this.dureeMoyenne = dureeMoyenne;
		this.dateSortie = dateSortie;
		this.dateSortie2 = dateSortie2;
		this.plateformes = plateformes;
		this.dejaVu = dejaVu;
		this.addBy = addBy;

		initializeUI();
	}

	private void initializeUI() {
		setLayout(new BorderLayout());

		JPanel serieCourteSelectedPanel = createSerieCourtePanel();

		add(serieCourteSelectedPanel);
	}

	private JPanel createSerieCourtePanel() {
		JPanel panel = new JPanel(new BorderLayout());

		serieCourtePanel = new JPanel();
		JLabel serieCourteLabel = new JLabel();
		serieCourtePanel.add(serieCourteLabel);

		serieCourtePanel.setBackground(new Color(50, 50, 50));
		panel.add(serieCourtePanel, BorderLayout.CENTER);

		SerieCourte serieCourteSelected = gestionnaireSerieCourte.pickRandomSerieCourte(actors, genres, nbSaison, nbEpisode, dureeMoyenne, dateSortie, dateSortie2, plateformes, dejaVu, addBy);

		if (serieCourteSelected == null) {
			JOptionPane.showMessageDialog(this, "Erreur: Il n'y a aucune série dans la base de données ou aucune série courte ne correspond à votre recherche.", "Erreur", JOptionPane.ERROR_MESSAGE);
			backMenu();
		} else {
			this.serieCourte = serieCourteSelected;
			updateSerieCourteInfo(serieCourteSelected);
		}

		JButton btnBack = ButtonEditor.createButton("MENU", new Color(70, 130, 180));
		btnBack.addActionListener(e -> backMenu());

		JButton btnGen = ButtonEditor.createButton("Générer à nouveau", new Color(70, 130, 180));
		btnGen.addActionListener(e -> generateAgain());

		JButton btnDelete = ButtonEditor.createButton("Supprimer la série courte de la liste", new Color(70, 130, 180));
		btnDelete.addActionListener(e -> deleteSerieCourte());

		JPanel bottomPanel = new JPanel();
		bottomPanel.add(btnBack);
		bottomPanel.add(btnGen);
		bottomPanel.add(btnDelete);

		bottomPanel.setBackground(new Color(50, 50, 50));
		panel.add(bottomPanel, BorderLayout.SOUTH);

		return panel;
	}

	private void updateSerieCourteInfo(SerieCourte serieCourteSelected) {
		String date;
		if (serieCourte.getDateSortiePremiereSaison() != null) {
			date = new SimpleDateFormat("yyyy").format(serieCourteSelected.getDateSortiePremiereSaison());
		}
		else {
			date = "";
		}

		String date2;
		if (serieCourte.getDateSortieDerniereSaison() != null) {
			date2 = new SimpleDateFormat("yyyy").format(serieCourteSelected.getDateSortieDerniereSaison());
		}
		else {
			date2 = "";
		}

		String serieCourteInfo = "<html>La série courte choisie est : " + serieCourteSelected.getTitre() + ".<br>" + Arrays.toString(serieCourteSelected.getActeur()) +
				" a/ont joué dedans.<br>La serie appartient à/aux genre(s) " +
				Arrays.toString(serieCourteSelected.getGenre()) + ".<br>Elle possède " + serieCourteSelected.getNombreSaison() +
				" saisons avec " + serieCourteSelected.getNombreEpisode() + "épisodes.<br>Les épisodes durent en moyenne " +
				serieCourteSelected.getDureeMoyenne() + " minutes.<br>La première saison est sorti en " + date + " et la dernière en " +
				date2 + ".<br>Elle est disponible sur " + Arrays.toString(serieCourteSelected.getPlateforme()) +
				".<br>Elle a été ajoutée par " + serieCourteSelected.getAddBy() + ".<br></html>";

		// Création du label texte
		JLabel textLabel = new JLabel(serieCourteInfo);
		textLabel.setForeground(Color.WHITE);

		// Création du label image
		JLabel imageLabel = new JLabel();
		String imagePath = serieCourteSelected.getImagePath();
		if (imagePath != null && new File(imagePath).exists()) {
			imageLabel.setIcon(resizeImage(imagePath, 240, 360));
		} else {
			imageLabel.setText("Aucune affiche");
			imageLabel.setForeground(Color.GRAY);
			imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
			imageLabel.setPreferredSize(new Dimension(240, 360));
		}

		// Création du panel d'affichage combiné
		JPanel combinedPanel = new JPanel(new BorderLayout(10, 0));
		combinedPanel.setBackground(serieCourtePanel.getBackground()); // pour garder le même fond
		combinedPanel.add(imageLabel, BorderLayout.SOUTH);
		combinedPanel.add(textLabel, BorderLayout.NORTH);

		// Remplacement dans le panel principal
		serieCourtePanel.removeAll();
		serieCourtePanel.add(combinedPanel);
		serieCourtePanel.revalidate();
		serieCourtePanel.repaint();
	}

	public void backMenu() {
		serieCourteFrame.dispose();
		new SerieCourteFrame();
	}

	public void generateAgain() {
		SerieCourte newSerieCourte = gestionnaireSerieCourte.pickRandomSerieCourte(actors, genres, nbSaison, nbEpisode, dureeMoyenne, dateSortie, dateSortie2, plateformes, dejaVu, addBy);
		if (newSerieCourte == null) {
			JOptionPane.showMessageDialog(this, "Erreur: Il n'y a aucune série dans la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
			backMenu();
		} else {
			this.serieCourte = newSerieCourte;
			updateSerieCourteInfo(newSerieCourte);
		}

	}

	public void deleteSerieCourte() {
		gestionnaireSerieCourte.deleteSerieCourte(serieCourte);

		backMenu();
	}

	private ImageIcon resizeImage(String path, int width, int height) {
		ImageIcon icon = new ImageIcon(path);
		Image img = icon.getImage();
		Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return new ImageIcon(newImg);
	}
}
