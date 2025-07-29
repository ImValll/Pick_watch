package view.saga;

import model.ButtonEditor;
import model.parameter.actors.Actor;
import model.parameter.genres.Genre;
import model.parameter.platforms.Platform;
import model.parameter.users.User;
import model.saga.GestionnaireSaga;
import model.saga.Saga;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

public class PanelRandomSaga extends JPanel {

	private final GestionnaireSaga gestionnaireSaga;
	private final SagaFrame sagaFrame;

	private String rea;
	private Actor[] actors;
	private Genre[] genres;
	private int nbFilm;
	private Date dateSortie;
	private Date dateSortie2;
	private Platform[] plateformes;
	private int dejaVu = -1;
	private User addBy;

	private Saga saga;
	private JPanel sagaPanel;

	public PanelRandomSaga(GestionnaireSaga gestionnaireSaga, SagaFrame sagaFrame) {
		this.gestionnaireSaga = gestionnaireSaga;
		this.sagaFrame = sagaFrame;
		initializeUI();
	}

	public PanelRandomSaga(GestionnaireSaga gestionnaireSaga, SagaFrame sagaFrame, String rea, Actor[] actors, Genre[] genres, int nbFilm, Date dateSortie, Date dateSortie2, Platform[] plateformes, int dejaVu, User addBy) {
		this.gestionnaireSaga = gestionnaireSaga;
		this.sagaFrame = sagaFrame;
		this.rea = rea;
		this.actors = actors;
		this.genres = genres;
		this.nbFilm = nbFilm;
		this.dateSortie = dateSortie;
		this.dateSortie2 = dateSortie2;
		this.plateformes = plateformes;
		this.dejaVu = dejaVu;
		this.addBy = addBy;

		initializeUI();
	}

	private void initializeUI() {
		setLayout(new BorderLayout());

		JPanel sagaSelectedPanel = createSagaPanel();

		add(sagaSelectedPanel);
	}

	private JPanel createSagaPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		sagaPanel = new JPanel();
		JLabel sagaLabel = new JLabel();
		sagaPanel.add(sagaLabel);

		sagaPanel.setBackground(new Color(50, 50, 50));
		panel.add(sagaPanel, BorderLayout.CENTER);

		Saga sagaSelected = gestionnaireSaga.pickRandomSaga(rea, actors, genres, nbFilm, dateSortie, dateSortie2, plateformes, dejaVu, addBy);

		if (sagaSelected == null) {
			JOptionPane.showMessageDialog(this, "Erreur: Il n'y a aucune saga dans la base de données ou aucune saga ne correspond à votre recherche.", "Erreur", JOptionPane.ERROR_MESSAGE);
			backMenu();
		} else {
			this.saga = sagaSelected;
			updateSagaInfo(sagaSelected);
		}

		JButton btnBack = ButtonEditor.createButton("MENU", new Color(70, 130, 180));
		btnBack.addActionListener(e -> backMenu());

		JButton btnGen = ButtonEditor.createButton("Générer à nouveau", new Color(70, 130, 180));
		btnGen.addActionListener(e -> generateAgain());

		JButton btnDelete = ButtonEditor.createButton("Supprimer la saga de la liste", new Color(70, 130, 180));
		btnDelete.addActionListener(e -> deleteSaga());

		JPanel bottomPanel = new JPanel();
		bottomPanel.add(btnBack);
		bottomPanel.add(btnGen);
		bottomPanel.add(btnDelete);

		bottomPanel.setBackground(new Color(50, 50, 50));
		panel.add(bottomPanel, BorderLayout.SOUTH);

		return panel;
	}

	private void updateSagaInfo(Saga sagaSelected) {
		String date1 = sagaSelected.getDateSortiePremier() != null
				? new SimpleDateFormat("yyyy").format(sagaSelected.getDateSortiePremier()) : null;
		String date2 = sagaSelected.getDateSortieDernier() != null
				? new SimpleDateFormat("yyyy").format(sagaSelected.getDateSortieDernier()) : null;

		StringBuilder sb = new StringBuilder("<html>");
		sb.append("<html><div style='font-family:Segoe UI; font-size:13px; color:white;'>");

		if (sagaSelected.getRealistateur() != null && !sagaSelected.getRealistateur().trim().isEmpty())
			sb.append("Réalisée par <b>").append(sagaSelected.getRealistateur()).append("</b>.<br>");

		String acteursHTML = buildObjectList(sagaSelected.getActeur(), "Acteur(s)");
		if (!acteursHTML.isEmpty()) sb.append(acteursHTML);

		String genresHTML = buildObjectList(sagaSelected.getGenre(), "Genre(s)");
		if (!genresHTML.isEmpty()) sb.append(genresHTML);

		if (sagaSelected.getNombreFilms() > 0)
			sb.append("Nombre de films : ").append(sagaSelected.getNombreFilms()).append(".<br>");

		if (date1 != null)
			sb.append("Premier film sorti en ").append(date1).append(".<br>");

		if (date2 != null)
			sb.append("Dernier film sorti en ").append(date2).append(".<br>");

		String plateformesHTML = buildObjectList(sagaSelected.getPlateforme(), "Disponible sur");
		if (!plateformesHTML.isEmpty()) sb.append(plateformesHTML);

		User addedBy = sagaSelected.getAddBy();
		sb.append("<p><i>Ajouté par ")
				.append((addedBy != null && !addedBy.getName().trim().isEmpty()) ? addedBy : "inconnu")
				.append("</i></p>");

		sb.append("</html>");
		String sagaInfoHtml = sb.toString();

		JLabel titleLabel = new JLabel(sagaSelected.getTitre() + "\n", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
		titleLabel.setForeground(Color.WHITE);

		// --- Texte HTML avec scroll ---
		JLabel textLabel = new JLabel(sagaInfoHtml);
		textLabel.setForeground(Color.WHITE);

		// Important : autoriser le texte à s'étendre verticalement
		textLabel.setVerticalAlignment(SwingConstants.TOP);

		// Panneau scrollable pour le texte
		JScrollPane scrollPane = new JScrollPane(textLabel);
		scrollPane.setPreferredSize(new Dimension(400, 500)); // Hauteur maximale visible
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(null); // Supprime la bordure si tu veux rester sobre
		scrollPane.getViewport().setBackground(sagaPanel.getBackground()); // Conserver le fond

		// --- Image du film ---
		JLabel imageLabel = new JLabel();
		String imagePath = sagaSelected.getImagePath();
		if (imagePath != null && new File(imagePath).exists()) {
			imageLabel.setIcon(resizeImage(imagePath, 240, 360));
		} else {
			imageLabel.setText("Aucune affiche");
			imageLabel.setForeground(Color.GRAY);
			imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
			imageLabel.setPreferredSize(new Dimension(240, 360));
		}

		JPanel contentPanel = new JPanel(new BorderLayout(10, 0));
		contentPanel.setBackground(sagaPanel.getBackground());
		contentPanel.add(imageLabel, BorderLayout.WEST);
		contentPanel.add(scrollPane, BorderLayout.CENTER);

		JPanel finalPanel = new JPanel(new BorderLayout());
		finalPanel.setBackground(sagaPanel.getBackground());
		finalPanel.add(titleLabel, BorderLayout.NORTH);
		finalPanel.add(contentPanel, BorderLayout.CENTER);

		sagaPanel.removeAll();
		sagaPanel.add(finalPanel);
		sagaPanel.revalidate();
		sagaPanel.repaint();
	}

	public void backMenu() {
		sagaFrame.dispose();
		new SagaFrame();
	}

	public void generateAgain() {
		Saga newSaga = gestionnaireSaga.pickRandomSaga(rea, actors, genres, nbFilm, dateSortie, dateSortie2, plateformes, dejaVu, addBy);
		if (newSaga == null) {
			JOptionPane.showMessageDialog(this, "Erreur: Il n'y a aucune saga dans la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
			backMenu();
		} else {
			this.saga = newSaga;
			updateSagaInfo(newSaga);
		}

	}

	public void deleteSaga() {
		gestionnaireSaga.deleteSaga(saga);

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
