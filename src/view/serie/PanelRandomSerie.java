package view.serie;

import model.ButtonEditor;
import model.parameter.genres.Genre;
import model.parameter.platforms.Platform;
import model.parameter.users.User;
import model.serie.GestionnaireSerie;
import model.serie.Serie;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class PanelRandomSerie extends JPanel {

	private final GestionnaireSerie gestionnaireSerie;
	private final SerieFrame serieFrame;

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
	private JLabel serieLabel;

	public PanelRandomSerie(GestionnaireSerie gestionnaireSerie, SerieFrame serieFrame) {
		this.gestionnaireSerie = gestionnaireSerie;
		this.serieFrame = serieFrame;
		initializeUI();
	}

	public PanelRandomSerie(GestionnaireSerie gestionnaireSerie, SerieFrame serieFrame, Genre[] genres, int nbSaison, int nbEpisode, int dureeMoyenne, Date dateSortie, Date dateSortie2, Platform[] plateformes, int dejaVu, User addBy) {
		this.gestionnaireSerie = gestionnaireSerie;
		this.serieFrame = serieFrame;
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
		serieLabel = new JLabel();
		seriePanel.add(serieLabel);

		seriePanel.setBackground(new Color(50, 50, 50));
		panel.add(seriePanel, BorderLayout.CENTER);

		Serie serieSelected = gestionnaireSerie.pickRandomSerie(genres, nbSaison, nbEpisode, dureeMoyenne, dateSortie, dateSortie2, plateformes, dejaVu, addBy);

		if (serieSelected == null) {
			JOptionPane.showMessageDialog(this, "Erreur: Il n'y a aucune série dans la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
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

		String serieInfo = "<html>La série choisie est : " + serieSelected.getTitre() + ".<br>La serie appartient à/aux genre(s) " +
				Arrays.toString(serieSelected.getGenre()) + ".<br>Elle possède " + serieSelected.getNombreSaison() +
				" saisons avec " + serieSelected.getNombreEpisode() + "épisodes.<br>Les épisodes durent en moyenne " +
				serieSelected.getDureeMoyenne() + " minutes.<br>La première saison est sorti en " + date + " et la dernière en " +
				date2 + ".<br>Elle est disponible sur " + Arrays.toString(serieSelected.getPlateforme()) +
				".<br>Elle a été ajoutée par " + serieSelected.getAddBy() + "</html>";

		serieLabel.setForeground(Color.WHITE);

		serieLabel.setText(serieInfo);
		seriePanel.revalidate();
		seriePanel.repaint();
	}

	public void backMenu() {
		serieFrame.dispose();
		new SerieFrame();
	}

	public void generateAgain() {
		Serie newSerie = gestionnaireSerie.pickRandomSerie(genres, nbSaison, nbEpisode, dureeMoyenne, dateSortie, dateSortie2, plateformes, dejaVu, addBy);
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
}
