package view.movie;

import model.ButtonEditor;
import model.DataManager;
import model.parameter.genres.Genre;
import model.parameter.platforms.Platform;
import model.parameter.users.User;
import model.movie.GestionnaireMovie;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PanelFilterRandomMovie extends JPanel {

	private final GestionnaireMovie gestionnaireMovie;
	private final MovieFrame movieFrame;
	private JTextField directorField;
	private List<JCheckBox> genreCheckBoxes;
	private JTextField durationField;
	private JTextField yearField;
	private List<JCheckBox> platformCheckBoxes;
	JRadioButton dejaVuButton;
	JRadioButton pasEncoreVuButton;
	private JComboBox<Object> userComboBox;

	public PanelFilterRandomMovie(GestionnaireMovie gestionnaireMovie, MovieFrame movieFrame) {
		this.gestionnaireMovie = gestionnaireMovie;
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

		ArrayList<Genre> genres = DataManager.loadGenre();
		JPanel genrePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		genreCheckBoxes = new ArrayList<>();
		for (Genre genre : genres) {
			JCheckBox checkBox = new JCheckBox(genre.getName());
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

		ArrayList<Platform> platforms = DataManager.loadPlatform();
		JPanel platformPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		platformCheckBoxes = new ArrayList<>();
		for (Platform platform : platforms) {
			JCheckBox checkBox = new JCheckBox(platform.getName());
			checkBox.setBackground(new Color(50, 50, 50));
			checkBox.setForeground(Color.WHITE);
			platformCheckBoxes.add(checkBox);
			platformPanel.add(checkBox);
		}
		JScrollPane scrollPanePlatform = new JScrollPane(platformPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		platformPanel.setBackground(new Color(50, 50, 50));
		centerPanel.add(scrollPanePlatform);

		//dejaVu filter
		// Créer les boutons radio pour "Déjà vu" et "Pas encore vu"
		dejaVuButton = new JRadioButton("Déjà vu");
		pasEncoreVuButton = new JRadioButton("Pas encore vu");
		ButtonGroup vuGroup = new ButtonGroup();
		vuGroup.add(dejaVuButton);
		vuGroup.add(pasEncoreVuButton);

		// Ajouter un bouton pour désélectionner les boutons radio
		JButton clearVuButton = new JButton("Annuler la sélection");
		clearVuButton.addActionListener(e -> vuGroup.clearSelection());

		// Ajouter les boutons radio à un panneau
		JPanel vuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		vuPanel.add(dejaVuButton);
		vuPanel.add(pasEncoreVuButton);
		vuPanel.add(clearVuButton);
		centerPanel.add(vuPanel);


		// User filter
		JLabel labelUtilisateur = new JLabel("Utilisateur:");
		labelUtilisateur.setForeground(Color.WHITE);
		centerPanel.add(labelUtilisateur);

		ArrayList<User> users = DataManager.loadUser();
		DefaultComboBoxModel<Object> comboBoxModel = new DefaultComboBoxModel<>();
		comboBoxModel.addElement("Ignorer");
		for (User user : users) {
			comboBoxModel.addElement(user);
		}
		userComboBox = new JComboBox<>(comboBoxModel);
		userComboBox.setSelectedItem("Ignorer"); // Default value
		userComboBox.setBackground(Color.LIGHT_GRAY);
		userComboBox.setForeground(Color.BLACK);
		centerPanel.add(userComboBox);

		// Bottom panel with buttons
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton backButton = ButtonEditor.createButton("Retour au menu", Color.BLUE);
		backButton.addActionListener(e -> backMenu());
		JButton submitButton = ButtonEditor.createButton("Envoyer", Color.GREEN);
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
				selectedGenres.add(new Genre(checkBox.getText()));
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
		List<Platform> selectedPlatforms = new ArrayList<>();
		for (JCheckBox checkBox : platformCheckBoxes) {
			if (checkBox.isSelected()) {
				selectedPlatforms.add(new Platform(checkBox.getText()));
			}
		}
		Platform[] platformsArray = selectedPlatforms.isEmpty() ? null : selectedPlatforms.toArray(new Platform[0]);

		// Get "Déjà vu" status
		int dejaVu;
		if(dejaVuButton.isSelected()) {
			dejaVu = 1;
		}
		else if(pasEncoreVuButton.isSelected()) {
			dejaVu = 0;
		}
		else {
			dejaVu = -1;
		}

		// Get user
		User addBy = null;
		Object selectedUser = userComboBox.getSelectedItem();
		if (selectedUser instanceof User) {
			addBy = (User)selectedUser;
		}

		// Create PanelRandomMovie
		PanelRandomMovie panelRandomMovie = new PanelRandomMovie(gestionnaireMovie, movieFrame, director, genresArray, duration, dateSortie, platformsArray, dejaVu, addBy);

		// Replace current panel with PanelRandomMovie
		movieFrame.getContentPane().removeAll();
		movieFrame.add(panelRandomMovie);
		movieFrame.revalidate();
		movieFrame.repaint();
	}
}