package view.serie;

import model.ButtonEditor;
import model.Language;
import model.parameter.actors.Actor;
import model.parameter.genres.Genre;
import model.parameter.platforms.Platform;
import model.parameter.users.User;
import model.serie.GestionnaireSerie;
import model.serie.Serie;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
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
			JOptionPane.showMessageDialog(this, Language.getBundle().getString("erreur.erreurBDVideRechercheSerie"), Language.getBundle().getString("app.erreur"), JOptionPane.ERROR_MESSAGE);
			backMenu();
		} else {
			this.serie = serieSelected;
			updateSerieInfo(serieSelected);
		}

		JButton btnBack = ButtonEditor.createButton(Language.getBundle().getString("app.btnMenu"), new Color(70, 130, 180));
		btnBack.addActionListener(e -> backMenu());

		JButton btnGen = ButtonEditor.createButton(Language.getBundle().getString("pick.genererNouveau"), new Color(70, 130, 180));
		btnGen.addActionListener(e -> generateAgain());

		JButton btnDelete = ButtonEditor.createButton(Language.getBundle().getString("pick.supprimerListeSerie"), new Color(70, 130, 180));
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
		String date1 = serieSelected.getDateSortiePremiereSaison() != null
				? new SimpleDateFormat("yyyy").format(serieSelected.getDateSortiePremiereSaison()) : null;
		String date2 = serieSelected.getDateSortieDerniereSaison() != null
				? new SimpleDateFormat("yyyy").format(serieSelected.getDateSortieDerniereSaison()) : null;

		StringBuilder sb = new StringBuilder("<html>");
		sb.append("<html><div style='font-family:Segoe UI; font-size:13px; color:white;'>");

		String acteursHTML = buildObjectList(serieSelected.getActeur(), Language.getBundle().getString("pick.acteur"));
		if (!acteursHTML.isEmpty()) sb.append(acteursHTML);

		String genresHTML = buildObjectList(serieSelected.getGenre(), Language.getBundle().getString("pick.genre"));
		if (!genresHTML.isEmpty()) sb.append(genresHTML);

		if (serieSelected.getNombreSaison() > 0)
			sb.append(Language.getBundle().getString("pick.saison")).append(serieSelected.getNombreSaison()).append(".<br>");

		if (serieSelected.getNombreEpisode() > 0)
			sb.append(Language.getBundle().getString("pick.episode")).append(serieSelected.getNombreEpisode()).append(".<br>");

		if (serieSelected.getDureeMoyenne() > 0)
			sb.append(Language.getBundle().getString("pick.dureeMoyenne")).append(serieSelected.getDureeMoyenne()).append(Language.getBundle().getString("pick.minute") + ".<br>");

		if (date1 != null)
			sb.append(Language.getBundle().getString("pick.premiereSaison")).append(date1).append(".<br>");

		if (date2 != null)
			sb.append(Language.getBundle().getString("pick.derniereSaison")).append(date2).append(".<br>");

		String plateformesHTML = buildObjectList(serieSelected.getPlateforme(), Language.getBundle().getString("pick.disponible"));
		if (!plateformesHTML.isEmpty()) sb.append(plateformesHTML);

		User addedBy = serieSelected.getAddBy();
		sb.append("<p><i>" + Language.getBundle().getString("pick.ajoutePar"))
				.append((addedBy != null && !addedBy.getName().trim().isEmpty()) ? addedBy : Language.getBundle().getString("pick.inconnu"))
				.append("</i></p>");

		sb.append("</html>");
		String serieInfoHtml = sb.toString();

		JLabel titleLabel = new JLabel(serieSelected.getTitre() + "\n", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
		titleLabel.setForeground(Color.WHITE);

		// --- Texte HTML avec scroll ---
		JLabel textLabel = new JLabel(serieInfoHtml);
		textLabel.setForeground(Color.WHITE);

		// Important : autoriser le texte à s'étendre verticalement
		textLabel.setVerticalAlignment(SwingConstants.TOP);

		// Panneau scrollable pour le texte
		JScrollPane scrollPane = new JScrollPane(textLabel);
		scrollPane.setPreferredSize(new Dimension(400, 500)); // Hauteur maximale visible
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(null); // Supprime la bordure si tu veux rester sobre
		scrollPane.getViewport().setBackground(seriePanel.getBackground()); // Conserver le fond

		// --- Image du film ---
		JLabel imageLabel = new JLabel();
		String imagePath = serieSelected.getImagePath();
		if (imagePath != null && new File(imagePath).exists()) {
			imageLabel.setIcon(resizeImage(imagePath, 240, 360));
		} else {
			imageLabel.setText(Language.getBundle().getString("affiche.aucuneAfficheDisponible"));
			imageLabel.setForeground(Color.GRAY);
			imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
			imageLabel.setPreferredSize(new Dimension(240, 360));
		}

		JPanel contentPanel = new JPanel(new BorderLayout(10, 0));
		contentPanel.setBackground(seriePanel.getBackground());
		contentPanel.add(imageLabel, BorderLayout.WEST);
		contentPanel.add(scrollPane, BorderLayout.CENTER);

		JPanel finalPanel = new JPanel(new BorderLayout());
		finalPanel.setBackground(seriePanel.getBackground());
		finalPanel.add(titleLabel, BorderLayout.NORTH);
		finalPanel.add(contentPanel, BorderLayout.CENTER);

		seriePanel.removeAll();
		seriePanel.add(finalPanel);
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
			JOptionPane.showMessageDialog(this, Language.getBundle().getString("erreur.erreurBDVideSerie"), Language.getBundle().getString("app.erreur"), JOptionPane.ERROR_MESSAGE);
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

	private <T> String buildObjectList(T[] objects, String label) {
		if (objects == null || objects.length == 0) return "";

		StringBuilder sb = new StringBuilder();
		sb.append("<p><b>").append(label).append(" :</b></p><ul style='margin-top:-8px;'>");

		boolean atLeastOne = false;
		for (T obj : objects) {
			if (obj != null) {
				String value = null;
				try {
					Method getNom = obj.getClass().getMethod("getNom");
					value = String.valueOf(getNom.invoke(obj));
				} catch (Exception e) {
					value = obj.toString(); // fallback
				}

				if (value != null && !value.isBlank()) {
					sb.append("<li>").append(value).append("</li>");
					atLeastOne = true;
				}
			}
		}
		sb.append("</ul>");

		return atLeastOne ? sb.toString() : "";
	}
}
