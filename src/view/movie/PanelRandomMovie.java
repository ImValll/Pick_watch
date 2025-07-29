package view.movie;

import model.ButtonEditor;
import model.parameter.actors.Actor;
import model.parameter.genres.Genre;
import model.parameter.platforms.Platform;
import model.parameter.users.User;
import model.movie.GestionnaireMovie;
import model.movie.Movie;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class PanelRandomMovie extends JPanel {

	private final GestionnaireMovie gestionnaireMovie;
	private final MovieFrame movieFrame;

	private String rea;
	private Actor[] actors;
	private Genre[] genres;
	private int duree;
	private Date dateSortie;
	private Platform[] plateformes;
	private int dejaVu = -1;
	private User addBy;

	private Movie movie;
	private JPanel moviePanel;

	public PanelRandomMovie(GestionnaireMovie gestionnaireMovie, MovieFrame movieFrame) {
		this.gestionnaireMovie = gestionnaireMovie;
		this.movieFrame = movieFrame;
		initializeUI();
	}

	public PanelRandomMovie(GestionnaireMovie gestionnaireMovie, MovieFrame movieFrame, String rea, Actor[] actors, Genre[] genres, int duree, Date dateSortie, Platform[] plateformes, int dejaVu, User addBy) {
		this.gestionnaireMovie = gestionnaireMovie;
		this.movieFrame = movieFrame;
		this.rea = rea;
		this.actors = actors;
		this.genres = genres;
		this.duree = duree;
		this.dateSortie = dateSortie;
		this.plateformes = plateformes;
		this.dejaVu = dejaVu;
		this.addBy = addBy;

		initializeUI();
	}

	private void initializeUI() {
		setLayout(new BorderLayout());

		JPanel movieSelectedPanel = createMoviePanel();

		add(movieSelectedPanel);
	}

	private JPanel createMoviePanel() {
		JPanel panel = new JPanel(new BorderLayout());

		moviePanel = new JPanel();
		JLabel movieLabel = new JLabel();
		moviePanel.add(movieLabel);

		moviePanel.setBackground(new Color(50, 50, 50));
		panel.add(moviePanel, BorderLayout.CENTER);

		Movie movieSelected = gestionnaireMovie.pickRandomMovie(rea, actors, genres, duree, dateSortie, plateformes, dejaVu, addBy);

		if (movieSelected == null) {
			JOptionPane.showMessageDialog(this, "Erreur: Il n'y a aucun film dans la base de données ou aucun film ne correspond à votre recherche.", "Erreur", JOptionPane.ERROR_MESSAGE);
			backMenu();
		} else {
			this.movie = movieSelected;
			updateMovieInfo(movieSelected);
		}

		JButton btnBack = ButtonEditor.createButton("MENU", new Color(70, 130, 180));
		btnBack.addActionListener(e -> backMenu());

		JButton btnGen = ButtonEditor.createButton("Générer à nouveau", new Color(70, 130, 180));
		btnGen.addActionListener(e -> generateAgain());

		JButton btnDelete = ButtonEditor.createButton("Supprimer le film de la liste", new Color(70, 130, 180));
		btnDelete.addActionListener(e -> deleteMovie());

		JPanel bottomPanel = new JPanel();
		bottomPanel.add(btnBack);
		bottomPanel.add(btnGen);
		bottomPanel.add(btnDelete);

		bottomPanel.setBackground(new Color(50, 50, 50));
		panel.add(bottomPanel, BorderLayout.SOUTH);

		return panel;
	}

	private void updateMovieInfo(Movie movieSelected) {
		String date = (movieSelected.getDateSortie() != null)
				? new SimpleDateFormat("yyyy").format(movieSelected.getDateSortie())
				: null;

		// Construire le contenu HTML dynamiquement
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<html><div style='font-family:Segoe UI; font-size:13px; color:white;'>");

		if (movieSelected.getRealistateur() != null && !movieSelected.getRealistateur().trim().isEmpty()) {
			htmlBuilder.append("<p><b>Réalisateur :</b> ").append(movieSelected.getRealistateur()).append("</p>");
		}

		String acteursHTML = buildObjectList(movieSelected.getActeur(), "Acteur(s)");
		if (!acteursHTML.isEmpty()) htmlBuilder.append(acteursHTML);

		String genresHTML = buildObjectList(movieSelected.getGenre(), "Genre(s)");
		if (!genresHTML.isEmpty()) htmlBuilder.append(genresHTML);

		if (movieSelected.getDuree() > 0) {
			htmlBuilder.append("<p><b>Durée :</b> ").append(movieSelected.getDuree()).append(" minutes</p>");
		}

		if (date != null) {
			htmlBuilder.append("<p><b>Date de sortie :</b> ").append(date).append("</p>");
		}

		String plateformesHTML = buildObjectList(movieSelected.getPlateforme(), "Disponible sur");
		if (!plateformesHTML.isEmpty()) htmlBuilder.append(plateformesHTML);

		// Toujours afficher "Ajouté par", même vide
		User addedBy = movieSelected.getAddBy();
		htmlBuilder.append("<p><i>Ajouté par ")
				.append((addedBy != null && !addedBy.getName().trim().isEmpty()) ? addedBy : "inconnu")
				.append("</i></p>");

		htmlBuilder.append("</div></html>");
		String movieInfoHtml = htmlBuilder.toString();

		// --- Label du titre du film ---
		JLabel titleLabel = new JLabel(movieSelected.getTitre() + "\n", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0)); // Marge autour du titre

		// --- Texte HTML avec scroll ---
		JLabel textLabel = new JLabel(movieInfoHtml);
		textLabel.setForeground(Color.WHITE);

		// Important : autoriser le texte à s'étendre verticalement
		textLabel.setVerticalAlignment(SwingConstants.TOP);

		// Panneau scrollable pour le texte
		JScrollPane scrollPane = new JScrollPane(textLabel);
		scrollPane.setPreferredSize(new Dimension(400, 500)); // Hauteur maximale visible
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(null); // Supprime la bordure si tu veux rester sobre
		scrollPane.getViewport().setBackground(moviePanel.getBackground()); // Conserver le fond

		// --- Image du film ---
		JLabel imageLabel = new JLabel();
		String imagePath = movieSelected.getImagePath();
		if (imagePath != null && new File(imagePath).exists()) {
			imageLabel.setIcon(resizeImage(imagePath, 240, 360));
		} else {
			imageLabel.setText("Aucune affiche");
			imageLabel.setForeground(Color.GRAY);
			imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
			imageLabel.setPreferredSize(new Dimension(240, 360));
		}

		JPanel contentPanel = new JPanel(new BorderLayout(10, 0));
		contentPanel.setBackground(moviePanel.getBackground());
		contentPanel.add(imageLabel, BorderLayout.WEST);
		contentPanel.add(scrollPane, BorderLayout.CENTER);

		JPanel finalPanel = new JPanel(new BorderLayout());
		finalPanel.setBackground(moviePanel.getBackground());
		finalPanel.add(titleLabel, BorderLayout.NORTH);
		finalPanel.add(contentPanel, BorderLayout.CENTER);

		moviePanel.removeAll();
		moviePanel.add(finalPanel);
		moviePanel.revalidate();
		moviePanel.repaint();
	}

	public void backMenu() {
		movieFrame.dispose();
		new MovieFrame();
	}

	public void generateAgain() {
		Movie newMovie = gestionnaireMovie.pickRandomMovie(rea, actors, genres, duree, dateSortie, plateformes, dejaVu, addBy);
		if (newMovie == null) {
			JOptionPane.showMessageDialog(this, "Erreur: Il n'y a aucun film dans la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
			backMenu();
		} else {
			this.movie = newMovie;
			updateMovieInfo(newMovie);
		}

	}

	public void deleteMovie() {
		gestionnaireMovie.deleteMovie(movie);

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
