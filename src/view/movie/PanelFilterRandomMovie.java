package view.movie;

import model.Genre;
import model.Gestionnaire;
import model.Plateforme;
import model.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PanelFilterRandomMovie extends JPanel {

	private Gestionnaire gestionnaire;
	private MovieFrame movieFrame;
	private JTextField directorField;
	private List<JCheckBox> genreCheckBoxes;
	private JTextField durationField;
	private JTextField yearField;
	private List<JCheckBox> platformCheckBoxes;
	private JComboBox<Object> userComboBox;

	public PanelFilterRandomMovie(Gestionnaire gestionnaire, MovieFrame movieFrame) {
		this.gestionnaire = gestionnaire;
		this.movieFrame = movieFrame;
		initializeUI();
	}

	private void initializeUI() {
		setLayout(new BorderLayout());

		JPanel movieSelectedPanel = filterMoviePanel();

		add(movieSelectedPanel);
	}

	private JPanel filterMoviePanel() {
		JPanel filterPanel = new JPanel();
		filterPanel.setLayout(new BorderLayout());

		// Center panel with filters
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(0, 1)); // One column layout

		// Director filter
		JLabel labelRea = new JLabel("Réalisateur:");
		labelRea.setForeground(Color.WHITE);
		centerPanel.add(labelRea);

		directorField = new JTextField();
		directorField.setBackground(Color.LIGHT_GRAY);
		centerPanel.add(directorField);

		// Genre filter
		JLabel labelGenre = new JLabel("Genres:");
		labelGenre.setForeground(Color.WHITE);
		centerPanel.add(labelGenre);

		Genre[] genres = Genre.values();
		JPanel genrePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		genreCheckBoxes = new ArrayList<>();
		for (Genre genre : genres) {
			JCheckBox checkBox = new JCheckBox(genre.name());
			checkBox.setBackground(new Color(50, 50, 50));
			checkBox.setForeground(Color.WHITE);
			genreCheckBoxes.add(checkBox);
			genrePanel.add(checkBox);
		}
		JScrollPane scrollPaneGenre = new JScrollPane(genrePanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		genrePanel.setBackground(new Color(50, 50, 50));
		centerPanel.add(scrollPaneGenre);

		// Duration filter
		JLabel labelDuree = new JLabel("Durée maximale (minutes):");
		labelDuree.setForeground(Color.WHITE);
		centerPanel.add(labelDuree);

		durationField = new JTextField();
		durationField.setBackground(Color.LIGHT_GRAY);
		centerPanel.add(durationField);

		// Year filter
		JLabel labelYear = new JLabel("Année de sortie:");
		labelYear.setForeground(Color.WHITE);
		centerPanel.add(labelYear);

		yearField = new JTextField();
		yearField.setBackground(Color.LIGHT_GRAY);
		centerPanel.add(yearField);

		// Platform filter
		JLabel labelPlateforme = new JLabel("Plateformes:");
		labelPlateforme.setForeground(Color.WHITE);
		centerPanel.add(labelPlateforme);

		Plateforme[] platforms = Plateforme.values();
		JPanel platformPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		platformCheckBoxes = new ArrayList<>();
		for (Plateforme platform : platforms) {
			JCheckBox checkBox = new JCheckBox(platform.name());
			checkBox.setBackground(new Color(50, 50, 50));
			checkBox.setForeground(Color.WHITE);
			platformCheckBoxes.add(checkBox);
			platformPanel.add(checkBox);
		}
		JScrollPane scrollPanePlatform = new JScrollPane(platformPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		platformPanel.setBackground(new Color(50, 50, 50));
		centerPanel.add(scrollPanePlatform);

		// User filter
		JLabel labelUtilisateur = new JLabel("Utilisateur:");
		labelUtilisateur.setForeground(Color.WHITE);
		centerPanel.add(labelUtilisateur);

		Utilisateur[] users = {Utilisateur.Valentin, Utilisateur.Ambre, Utilisateur.Nous2};
		userComboBox = new JComboBox<>(new Object[]{"Ignorer", users[0], users[1], users[2]});
		userComboBox.setSelectedItem("Ignorer"); // Default value
		userComboBox.setBackground(Color.LIGHT_GRAY);
		userComboBox.setForeground(Color.BLACK);
		centerPanel.add(userComboBox);

		// Bottom panel with buttons
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton backButton = createButton("Retour au menu", Color.BLUE);
		backButton.addActionListener(e -> backMenu());
		JButton submitButton = createButton("Envoyer", Color.GREEN);
		submitButton.addActionListener(e -> askRandomMovie());
		bottomPanel.add(backButton);
		bottomPanel.add(submitButton);

		centerPanel.setBackground(new Color(50, 50, 50));
		bottomPanel.setBackground(new Color(50, 50, 50));

		filterPanel.add(centerPanel, BorderLayout.CENTER);
		filterPanel.add(bottomPanel, BorderLayout.SOUTH);

		return filterPanel;
	}

	public void backMenu() {
		movieFrame.dispose();
		new MovieFrame();
	}

	public void askRandomMovie() {
		// Get director
		String director = directorField.getText().trim();
		if (director.isEmpty()) {
			director = null;
		}

		// Get genres
		List<Genre> selectedGenres = new ArrayList<>();
		for (JCheckBox checkBox : genreCheckBoxes) {
			if (checkBox.isSelected()) {
				selectedGenres.add(Genre.valueOf(checkBox.getText()));
			}
		}
		Genre[] genresArray = selectedGenres.isEmpty() ? null : selectedGenres.toArray(new Genre[0]);

		// Get duration
		int duration = 0;
		String durationText = durationField.getText().trim();
		if (!durationText.isEmpty()) {
			duration = Integer.parseInt(durationText);
		}

		// Get year
		Date dateSortie = null;
		String yearText = yearField.getText().trim();
		if (!yearText.isEmpty()) {
			int year = Integer.parseInt(yearText);
			dateSortie = new Date(year - 1900, 0, 1); // Using deprecated Date constructor for simplicity
		}

		// Get platforms
		List<Plateforme> selectedPlatforms = new ArrayList<>();
		for (JCheckBox checkBox : platformCheckBoxes) {
			if (checkBox.isSelected()) {
				selectedPlatforms.add(Plateforme.valueOf(checkBox.getText()));
			}
		}
		Plateforme[] platformsArray = selectedPlatforms.isEmpty() ? null : selectedPlatforms.toArray(new Plateforme[0]);

		// Get user
		Utilisateur addBy = null;
		Object selectedUser = userComboBox.getSelectedItem();
		if (selectedUser instanceof Utilisateur) {
			addBy = (Utilisateur) selectedUser;
		}

		// Create PanelRandomMovie
		PanelRandomMovie panelRandomMovie = new PanelRandomMovie(gestionnaire, movieFrame, director, genresArray, duration, dateSortie, platformsArray, addBy);

		// Replace current panel with PanelRandomMovie
		movieFrame.getContentPane().removeAll();
		movieFrame.add(panelRandomMovie);
		movieFrame.revalidate();
		movieFrame.repaint();
	}

	public JButton createButton(String title, Color color) {
		JButton button = new JButton(title);
		button.setBackground(color); // Set the button color
		button.setForeground(Color.WHITE); // Set the text color to white
		button.setFont(new Font("Arial", Font.BOLD, 18)); // Set font style and size
		button.setFocusPainted(false); // Remove focus painting
		button.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30)); // Add padding

		return button;
	}
}