package view.serie;

import model.Genre;
import model.Plateforme;
import model.Utilisateur;
import model.serie.GestionnaireSerie;
import view.serie.PanelRandomSerie;
import view.serie.SerieFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PanelFilterRandomSerie extends JPanel {

	private GestionnaireSerie gestionnaireSerie;
	private SerieFrame serieFrame;
	private List<JCheckBox> genreCheckBoxes;
	private JTextField nbSeasonField;
	private JTextField nbEpisodeField;
	private JTextField meanLengthField;
	private JTextField yearField;
	private JTextField year2Field;
	private List<JCheckBox> platformCheckBoxes;
	JRadioButton dejaVuButton;
	JRadioButton pasEncoreVuButton;
	private JComboBox<Object> userComboBox;

	public PanelFilterRandomSerie(GestionnaireSerie gestionnaireSerie, SerieFrame serieFrame) {
		this.gestionnaireSerie = gestionnaireSerie;
		this.serieFrame = serieFrame;
		initializeUI();
	}

	private void initializeUI() {
		setLayout(new BorderLayout());

		JPanel serieSelectedPanel = filterSeriePanel();

		add(serieSelectedPanel);
	}

	private JPanel filterSeriePanel() {
		JPanel filterPanel = new JPanel();
		filterPanel.setLayout(new BorderLayout());

		// Center panel with filters
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(0, 1)); // One column layout

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

		// Number season filter
		JLabel labelNbSeason = new JLabel("Nombre de saisons:");
		labelNbSeason.setForeground(Color.WHITE);
		centerPanel.add(labelNbSeason);

		nbSeasonField = new JTextField();
		nbSeasonField.setBackground(Color.LIGHT_GRAY);
		centerPanel.add(nbSeasonField);

		// Number episode filter
		JLabel labelNbEpisode = new JLabel("Nombre d'épisodes par saison:");
		labelNbEpisode.setForeground(Color.WHITE);
		centerPanel.add(labelNbEpisode);

		nbEpisodeField = new JTextField();
		nbEpisodeField.setBackground(Color.LIGHT_GRAY);
		centerPanel.add(nbEpisodeField);

		// mean length filter
		JLabel labelMeanLength = new JLabel("Durée moyenne des épisodes:");
		labelMeanLength.setForeground(Color.WHITE);
		centerPanel.add(labelMeanLength);

		meanLengthField = new JTextField();
		meanLengthField.setBackground(Color.LIGHT_GRAY);
		centerPanel.add(meanLengthField);

		// Year filter
		JLabel labelYear = new JLabel("Année de sortie de la première saison:");
		labelYear.setForeground(Color.WHITE);
		centerPanel.add(labelYear);

		yearField = new JTextField();
		yearField.setBackground(Color.LIGHT_GRAY);
		centerPanel.add(yearField);

		// Year filter 2
		JLabel labelYear2 = new JLabel("Année de sortie de la dernière saison:");
		labelYear2.setForeground(Color.WHITE);
		centerPanel.add(labelYear2);

		year2Field = new JTextField();
		year2Field.setBackground(Color.LIGHT_GRAY);
		centerPanel.add(year2Field);

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

		Utilisateur[] users = Utilisateur.values();
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
		submitButton.addActionListener(e -> askRandomSerie());
		bottomPanel.add(backButton);
		bottomPanel.add(submitButton);

		centerPanel.setBackground(new Color(50, 50, 50));
		bottomPanel.setBackground(new Color(50, 50, 50));

		filterPanel.add(centerPanel, BorderLayout.CENTER);
		filterPanel.add(bottomPanel, BorderLayout.SOUTH);

		return filterPanel;
	}

	public void backMenu() {
		serieFrame.dispose();
		new SerieFrame();
	}

	public void askRandomSerie() {

		// Get genres
		List<Genre> selectedGenres = new ArrayList<>();
		for (JCheckBox checkBox : genreCheckBoxes) {
			if (checkBox.isSelected()) {
				selectedGenres.add(Genre.valueOf(checkBox.getText()));
			}
		}
		Genre[] genresArray = selectedGenres.isEmpty() ? null : selectedGenres.toArray(new Genre[0]);

		// Get nbSeason
		int nbSeason = 0;
		String nbSeasonText = nbSeasonField.getText().trim();
		if (!nbSeasonText.isEmpty()) {
			nbSeason = Integer.parseInt(nbSeasonText);
		}

		// Get nbEpisode
		int nbEpisode = 0;
		String nbEpisodeText = nbEpisodeField.getText().trim();
		if (!nbEpisodeText.isEmpty()) {
			nbEpisode = Integer.parseInt(nbEpisodeText);
		}

		// Get meanLength
		int meanLength = 0;
		String meanLengthText = meanLengthField.getText().trim();
		if (!meanLengthText.isEmpty()) {
			meanLength = Integer.parseInt(meanLengthText);
		}

		// Get year
		Date dateSortie = null;
		String yearText = yearField.getText().trim();
		if (!yearText.isEmpty()) {
			int year = Integer.parseInt(yearText);
			dateSortie = new Date(year - 1900, 0, 1); // Using deprecated Date constructor for simplicity
		}

		// Get year 2
		Date dateSortie2 = null;
		String yearText2 = year2Field.getText().trim();
		if (!yearText2.isEmpty()) {
			int year2 = Integer.parseInt(yearText2);
			dateSortie2 = new Date(year2 - 1900, 0, 1); // Using deprecated Date constructor for simplicity
		}

		// Get platforms
		List<Plateforme> selectedPlatforms = new ArrayList<>();
		for (JCheckBox checkBox : platformCheckBoxes) {
			if (checkBox.isSelected()) {
				selectedPlatforms.add(Plateforme.valueOf(checkBox.getText()));
			}
		}
		Plateforme[] platformsArray = selectedPlatforms.isEmpty() ? null : selectedPlatforms.toArray(new Plateforme[0]);

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
		Utilisateur addBy = null;
		Object selectedUser = userComboBox.getSelectedItem();
		if (selectedUser instanceof Utilisateur) {
			addBy = (Utilisateur) selectedUser;
		}

		// Create PanelRandomSerie
		PanelRandomSerie panelRandomSerie = new PanelRandomSerie(gestionnaireSerie, serieFrame, genresArray, nbSeason, nbEpisode, meanLength, dateSortie, dateSortie2, platformsArray, dejaVu, addBy);

		// Replace current panel with PanelRandomSerie
		serieFrame.getContentPane().removeAll();
		serieFrame.add(panelRandomSerie);
		serieFrame.revalidate();
		serieFrame.repaint();
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
