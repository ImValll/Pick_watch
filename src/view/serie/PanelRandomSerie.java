package view.serie;

import model.ButtonEditor;
import model.parameter.actors.Actor;
import model.parameter.genres.Genre;
import model.parameter.platforms.Platform;
import model.parameter.users.User;
import model.serie.GestionnaireSerie;
import model.serie.Serie;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class PanelRandomSerie extends JPanel {

	private final GestionnaireSerie gestionnaireSerie;
	private final SerieFrame serieFrame;

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

	private Serie serie;
	private JPanel seriePanel;

	public PanelRandomSerie(GestionnaireSerie gestionnaireSerie, SerieFrame serieFrame) {
		this.gestionnaireSerie = gestionnaireSerie;
		this.serieFrame = serieFrame;
		initializeUI();
	}

	public PanelRandomSerie(GestionnaireSerie gestionnaireSerie, SerieFrame serieFrame, Actor[] actors, Genre[] genres, int nbSaison, int nbEpisode, int dureeMoyenne, Date dateSortie, Date dateSortie2, Platform[] plateformes, int dejaVu, User addBy) {
		this.gestionnaireSerie = gestionnaireSerie;
		this.serieFrame = serieFrame;
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

		JPanel serieSelectedPanel = createSeriePanel();

		add(serieSelectedPanel);
	}

	private JPanel createSeriePanel() {
		JPanel panel = new JPanel(new BorderLayout());

		seriePanel = new JPanel();
		JLabel serieLabel = new JLabel();
		seriePanel.add(serieLabel);

		seriePanel.setBackground(new Color(50, 50, 50));
		panel.add(seriePanel, BorderLayout.CENTER);

		Serie serieSelected = gestionnaireSerie.pickRandomSerie(actors, genres, nbSaison, nbEpisode, dureeMoyenne, dateSortie, dateSortie2, plateformes, dejaVu, addBy);

		if (serieSelected == null) {
			JOptionPane.showMessageDialog(this, "Erreur: Il n'y a aucune série dans la base de données ou aucune série ne correspond à votre recherche.", "Erreur", JOptionPane.ERROR_MESSAGE);
			backMenu();
		} else {
			this.serie = serieSelected;
			updateSerieInfo(serieSelected);
		}

		JButton btnBack = ButtonEditor.createButton("MENU", new Color(70, 130, 180));
		btnBack.addActionListener(e -> backMenu());

		JButton btnGen = ButtonEditor.createButton("Générer à nouveau", new Color(70, 130, 180));
		btnGen.addActionListener(e -> generateAgain());

		JButton btnDelete = ButtonEditor.createButton("Supprimer la série de la liste", new Color(70, 130, 180));
		btnDelete.addActionListener(e -> deleteSerie());

		JPanel bottomPanel = new JPanel();
		bottomPanel.add(btnBack);
		bottomPanel.add(btnGen);
		bottomPanel.add(btnDelete);

		bottomPanel.setBackground(new Color(50, 50, 50));
		panel.add(bottomPanel, BorderLayout.SOUTH);

		return panel;
	}

	private void updateSerieInfo(Serie serieSelected) {
		String date;
		if (serie.getDateSortiePremiereSaison() != null) {
			date = new SimpleDateFormat("yyyy").format(serieSelected.getDateSortiePremiereSaison());
		}
		else {
			date = "";
		}

		String date2;
		if (serie.getDateSortieDerniereSaison() != null) {
			date2 = new SimpleDateFormat("yyyy").format(serieSelected.getDateSortieDerniereSaison());
		}
		else {
			date2 = "";
		}

		String serieInfo = "<html>La série choisie est : " + serieSelected.getTitre() + ".<br>" + Arrays.toString(serieSelected.getActeur()) +
				" a/ont joué dedans.<br>La serie appartient à/aux genre(s) " +
				Arrays.toString(serieSelected.getGenre()) + ".<br>Elle possède " + serieSelected.getNombreSaison() +
				" saisons avec " + serieSelected.getNombreEpisode() + "épisodes.<br>Les épisodes durent en moyenne " +
				serieSelected.getDureeMoyenne() + " minutes.<br>La première saison est sorti en " + date + " et la dernière en " +
				date2 + ".<br>Elle est disponible sur " + Arrays.toString(serieSelected.getPlateforme()) +
				".<br>Elle a été ajoutée par " + serieSelected.getAddBy() + ".<br></html>";

		// Création du label texte
		JLabel textLabel = new JLabel(serieInfo);
		textLabel.setForeground(Color.WHITE);

		// Création du label image
		JLabel imageLabel = new JLabel();
		String imagePath = serieSelected.getImagePath();
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
		combinedPanel.setBackground(seriePanel.getBackground()); // pour garder le même fond
		combinedPanel.add(imageLabel, BorderLayout.SOUTH);
		combinedPanel.add(textLabel, BorderLayout.NORTH);

		// Remplacement dans le panel principal
		seriePanel.removeAll();
		seriePanel.add(combinedPanel);
		seriePanel.revalidate();
		seriePanel.repaint();
	}

	public void backMenu() {
		serieFrame.dispose();
		new SerieFrame();
	}

	public void generateAgain() {
		Serie newSerie = gestionnaireSerie.pickRandomSerie(actors, genres, nbSaison, nbEpisode, dureeMoyenne, dateSortie, dateSortie2, plateformes, dejaVu, addBy);
		if (newSerie == null) {
			JOptionPane.showMessageDialog(this, "Erreur: Il n'y a aucune série dans la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
			backMenu();
		} else {
			this.serie = newSerie;
			updateSerieInfo(newSerie);
		}

	}

	public void deleteSerie() {
		gestionnaireSerie.deleteSerie(serie);

		backMenu();
	}

	private ImageIcon resizeImage(String path, int width, int height) {
		ImageIcon icon = new ImageIcon(path);
		Image img = icon.getImage();
		Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return new ImageIcon(newImg);
	}
}
