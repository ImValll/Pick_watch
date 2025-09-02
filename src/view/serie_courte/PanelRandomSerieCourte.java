package view.serie_courte;

import model.ButtonEditor;
import model.Language;
import model.parameter.actors.Actor;
import model.parameter.genres.Genre;
import model.parameter.platforms.Platform;
import model.parameter.users.User;
import model.serie_courte.GestionnaireSerieCourte;
import model.serie_courte.SerieCourte;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
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
			JOptionPane.showMessageDialog(this, Language.getBundle().getString("erreur.erreurBDVideRechercheSerie"), Language.getBundle().getString("app.erreur"), JOptionPane.ERROR_MESSAGE);
			backMenu();
		} else {
			this.serieCourte = serieCourteSelected;
			updateSerieCourteInfo(serieCourteSelected);
		}

		JButton btnBack = ButtonEditor.createButton(Language.getBundle().getString("app.btnMenu"), new Color(70, 130, 180));
		btnBack.addActionListener(e -> backMenu());

		JButton btnGen = ButtonEditor.createButton(Language.getBundle().getString("pick.genererNouveau"), new Color(70, 130, 180));
		btnGen.addActionListener(e -> generateAgain());

		JButton btnDelete = ButtonEditor.createButton(Language.getBundle().getString("pick.supprimerListeSerieCourte"), new Color(70, 130, 180));
		btnDelete.addActionListener(e -> deleteSerieCourte());

		JPanel bottomPanel = new JPanel();
		bottomPanel.add(btnBack);
		bottomPanel.add(btnGen);
		bottomPanel.add(btnDelete);

		bottomPanel.setBackground(new Color(50, 50, 50));
		panel.add(bottomPanel, BorderLayout.SOUTH);

		return panel;
	}

	private void updateSerieCourteInfo(SerieCourte scSelected) {
		String date1 = scSelected.getDateSortiePremiereSaison() != null
				? new SimpleDateFormat("yyyy").format(scSelected.getDateSortiePremiereSaison()) : null;
		String date2 = scSelected.getDateSortieDerniereSaison() != null
				? new SimpleDateFormat("yyyy").format(scSelected.getDateSortieDerniereSaison()) : null;

		StringBuilder sb = new StringBuilder("<html>");
		sb.append("<html><div style='font-family:Segoe UI; font-size:13px; color:white;'>");

		String acteursHTML = buildObjectList(scSelected.getActeur(), Language.getBundle().getString("pick.acteur"));
		if (!acteursHTML.isEmpty()) sb.append(acteursHTML);

		String genresHTML = buildObjectList(scSelected.getGenre(), Language.getBundle().getString("pick.genre"));
		if (!genresHTML.isEmpty()) sb.append(genresHTML);

		if (scSelected.getNombreSaison() > 0)
			sb.append(Language.getBundle().getString("pick.saison")).append(scSelected.getNombreSaison()).append(".<br>");

		if (scSelected.getNombreEpisode() > 0)
			sb.append(Language.getBundle().getString("pick.episode")).append(scSelected.getNombreEpisode()).append(".<br>");

		if (scSelected.getDureeMoyenne() > 0)
			sb.append(Language.getBundle().getString("pick.dureeMoyenne")).append(scSelected.getDureeMoyenne()).append(Language.getBundle().getString("pick.minute") + ".<br>");

		if (date1 != null)
			sb.append(Language.getBundle().getString("pick.premiereSaison")).append(date1).append(".<br>");

		if (date2 != null)
			sb.append(Language.getBundle().getString("pick.derniereSaison")).append(date2).append(".<br>");

		String plateformesHTML = buildObjectList(scSelected.getPlateforme(), Language.getBundle().getString("pick.disponible"));
		if (!plateformesHTML.isEmpty()) sb.append(plateformesHTML);

		User addedBy = scSelected.getAddBy();
		sb.append("<p><i>" + Language.getBundle().getString("pick.ajoutePar"))
				.append((addedBy != null && !addedBy.getName().trim().isEmpty()) ? addedBy : Language.getBundle().getString("pick.inconnu"))
				.append("</i></p>");

		sb.append("</html>");
		String serieCourteInfoHtml = sb.toString();

		JLabel titleLabel = new JLabel(scSelected.getTitre() + "\n", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
		titleLabel.setForeground(Color.WHITE);

		// --- Texte HTML avec scroll ---
		JLabel textLabel = new JLabel(serieCourteInfoHtml);
		textLabel.setForeground(Color.WHITE);

		// Important : autoriser le texte à s'étendre verticalement
		textLabel.setVerticalAlignment(SwingConstants.TOP);

		// Panneau scrollable pour le texte
		JScrollPane scrollPane = new JScrollPane(textLabel);
		scrollPane.setPreferredSize(new Dimension(400, 500)); // Hauteur maximale visible
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(null); // Supprime la bordure si tu veux rester sobre
		scrollPane.getViewport().setBackground(serieCourtePanel.getBackground()); // Conserver le fond

		// --- Image du film ---
		JLabel imageLabel = new JLabel();
		String imagePath = scSelected.getImagePath();
		if (imagePath != null && new File(imagePath).exists()) {
			imageLabel.setIcon(resizeImage(imagePath, 240, 360));
		} else {
			imageLabel.setText(Language.getBundle().getString("affiche.aucuneAfficheDisponible"));
			imageLabel.setForeground(Color.GRAY);
			imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
			imageLabel.setPreferredSize(new Dimension(240, 360));
		}

		JPanel contentPanel = new JPanel(new BorderLayout(10, 0));
		contentPanel.setBackground(serieCourtePanel.getBackground());
		contentPanel.add(imageLabel, BorderLayout.WEST);
		contentPanel.add(scrollPane, BorderLayout.CENTER);

		JPanel finalPanel = new JPanel(new BorderLayout());
		finalPanel.setBackground(serieCourtePanel.getBackground());
		finalPanel.add(titleLabel, BorderLayout.NORTH);
		finalPanel.add(contentPanel, BorderLayout.CENTER);

		serieCourtePanel.removeAll();
		serieCourtePanel.add(finalPanel);
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
			JOptionPane.showMessageDialog(this, Language.getBundle().getString("erreur.erreurBDVideSerie"), Language.getBundle().getString("app.erreur"), JOptionPane.ERROR_MESSAGE);
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
