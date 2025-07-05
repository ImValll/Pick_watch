package view.serie_courte;

import model.ButtonEditor;
import model.DataManager;
import model.parameter.actors.Actor;
import model.parameter.genres.Genre;
import model.parameter.platforms.Platform;
import model.parameter.users.User;
import model.serie_courte.GestionnaireSerieCourte;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PanelFilterRandomSerieCourte extends JPanel {

	private final GestionnaireSerieCourte gestionnaireSerieCourte;
	private final SerieCourteFrame serieCourteFrame;
	private List<Actor> selectedActors;
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

	public PanelFilterRandomSerieCourte(GestionnaireSerieCourte gestionnaireSerieCourte, SerieCourteFrame serieCourteFrame) {
		this.gestionnaireSerieCourte = gestionnaireSerieCourte;
		this.serieCourteFrame = serieCourteFrame;
		initializeUI();
	}

	private void initializeUI() {
		setLayout(new BorderLayout());

		JPanel serieCourteSelectedPanel = filterSerieCourtePanel();

		add(serieCourteSelectedPanel);
	}

	private JPanel filterSerieCourtePanel() {
		JPanel filterPanel = new JPanel();
		filterPanel.setLayout(new BorderLayout());

		// Center panel with filters
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

		// Acteur filter
		centerPanel.add(new JLabel(" "));
		JLabel labelActeur = new JLabel("Acteur :");
		labelActeur.setForeground(Color.WHITE);
		centerPanel.add(labelActeur);

		ArrayList<Actor> actors = DataManager.loadActor();
		DefaultComboBoxModel<Actor> actorComboModel = new DefaultComboBoxModel<>();
		for (Actor actor : actors) {
			actorComboModel.addElement(actor);
		}
		JComboBox<Actor> actorComboBox = new JComboBox<>(actorComboModel);

		JPanel selectedActorsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		selectedActorsPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		selectedActors = new ArrayList<>();

		actorComboBox.addActionListener(e -> {
			Actor selected = (Actor) actorComboBox.getSelectedItem();
			if (selected != null && !selectedActors.contains(selected)) {
				selectedActors.add(selected);

				JPanel tagPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
				tagPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
				JLabel nameLabel = new JLabel(selected.getName());
				JButton removeButton = new JButton("x");
				removeButton.setMargin(new Insets(0, 2, 0, 2));
				removeButton.setFont(new Font("Arial", Font.BOLD, 10));

				removeButton.addActionListener(ev -> {
					selectedActors.remove(selected);
					selectedActorsPanel.remove(tagPanel);
					selectedActorsPanel.revalidate();
					selectedActorsPanel.repaint();
				});

				tagPanel.add(nameLabel);
				tagPanel.add(removeButton);
				selectedActorsPanel.add(tagPanel);
				selectedActorsPanel.revalidate();
				selectedActorsPanel.repaint();
			}
		});
		actorComboBox.setMaximumSize(new Dimension(600, 30));
		selectedActorsPanel.setMaximumSize(new Dimension(600, 30));
		centerPanel.add(actorComboBox);
		centerPanel.add(selectedActorsPanel);

		// Genre filter
		centerPanel.add(new JLabel(" "));
		JLabel labelGenre = new JLabel("Genres :");
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
		scrollPaneGenre.setMaximumSize(new Dimension(600, 50));
		centerPanel.add(scrollPaneGenre);

		// Number season filter
		centerPanel.add(new JLabel(" "));
		JLabel labelNbSeason = new JLabel("Nombre de saisons :");
		labelNbSeason.setForeground(Color.WHITE);
		centerPanel.add(labelNbSeason);

		nbSeasonField = new JTextField();
		nbSeasonField.setBackground(Color.LIGHT_GRAY);
		nbSeasonField.setMaximumSize(new Dimension(500, 30));
		centerPanel.add(nbSeasonField);

		// Number episode filter
		centerPanel.add(new JLabel(" "));
		JLabel labelNbEpisode = new JLabel("Nombre d'épisodes par saison :");
		labelNbEpisode.setForeground(Color.WHITE);
		centerPanel.add(labelNbEpisode);

		nbEpisodeField = new JTextField();
		nbEpisodeField.setBackground(Color.LIGHT_GRAY);
		nbEpisodeField.setMaximumSize(new Dimension(500, 30));
		centerPanel.add(nbEpisodeField);

		// mean length filter
		centerPanel.add(new JLabel(" "));
		JLabel labelMeanLength = new JLabel("Durée moyenne des épisodes :");
		labelMeanLength.setForeground(Color.WHITE);
		centerPanel.add(labelMeanLength);

		meanLengthField = new JTextField();
		meanLengthField.setBackground(Color.LIGHT_GRAY);
		meanLengthField.setMaximumSize(new Dimension(500, 30));
		centerPanel.add(meanLengthField);

		// Year filter
		centerPanel.add(new JLabel(" "));
		JLabel labelYear = new JLabel("Année de sortie de la première saison :");
		labelYear.setForeground(Color.WHITE);
		centerPanel.add(labelYear);

		yearField = new JTextField();
		yearField.setBackground(Color.LIGHT_GRAY);
		yearField.setMaximumSize(new Dimension(500, 30));
		centerPanel.add(yearField);

		// Year filter 2
		centerPanel.add(new JLabel(" "));
		JLabel labelYear2 = new JLabel("Année de sortie de la dernière saison :");
		labelYear2.setForeground(Color.WHITE);
		centerPanel.add(labelYear2);

		year2Field = new JTextField();
		year2Field.setBackground(Color.LIGHT_GRAY);
		year2Field.setMaximumSize(new Dimension(500, 30));
		centerPanel.add(year2Field);

		// Platform filter
		centerPanel.add(new JLabel(" "));
		JLabel labelPlateforme = new JLabel("Plateformes :");
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
		scrollPanePlatform.setMaximumSize(new Dimension(600, 50));
		centerPanel.add(scrollPanePlatform);

		//dejaVu filter
		// Créer les boutons radio pour "Déjà vu" et "Pas encore vu"
		centerPanel.add(new JLabel(" "));
		dejaVuButton = new JRadioButton("Déjà vu");
		dejaVuButton.setBackground(new Color(50, 50, 50));
		dejaVuButton.setForeground(Color.WHITE);
		pasEncoreVuButton = new JRadioButton("Pas encore vu");
		pasEncoreVuButton.setBackground(new Color(50, 50, 50));
		pasEncoreVuButton.setForeground(Color.WHITE);
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
		vuPanel.setBackground(new Color(50, 50, 50));
		vuPanel.setMaximumSize(new Dimension(500, 30));
		centerPanel.add(vuPanel);


		// User filter
		centerPanel.add(new JLabel(" "));
		JLabel labelUtilisateur = new JLabel("Utilisateur :");
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
		userComboBox.setMaximumSize(new Dimension(500, 30));
		centerPanel.add(userComboBox);

		// Bottom panel with buttons
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton backButton = ButtonEditor.createButton("Retour au menu", Color.BLUE);
		backButton.addActionListener(e -> backMenu());
		JButton submitButton = ButtonEditor.createButton("Envoyer", Color.GREEN);
		submitButton.addActionListener(e -> askRandomSerieCourte());
		bottomPanel.add(backButton);
		bottomPanel.add(submitButton);

		centerPanel.setBackground(new Color(50, 50, 50));
		bottomPanel.setBackground(new Color(50, 50, 50));

		filterPanel.add(centerPanel, BorderLayout.CENTER);
		filterPanel.add(bottomPanel, BorderLayout.SOUTH);

		return filterPanel;
	}

	public void backMenu() {
		serieCourteFrame.dispose();
		new SerieCourteFrame();
	}

	public void askRandomSerieCourte() {

		// Get actor
		List<Actor> selectedActorsCopy = new ArrayList<>(selectedActors);
		Actor[] actorsArray = selectedActorsCopy.isEmpty() ? null : selectedActorsCopy.toArray(new Actor[0]);

		// Get genres
		List<Genre> selectedGenres = new ArrayList<>();
		for (JCheckBox checkBox : genreCheckBoxes) {
			if (checkBox.isSelected()) {
				selectedGenres.add(new Genre(checkBox.getText()));
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
			dateSortie = new Date(year - 1900, Calendar.JANUARY, 1); // Using deprecated Date constructor for simplicity
		}

		// Get year 2
		Date dateSortie2 = null;
		String yearText2 = year2Field.getText().trim();
		if (!yearText2.isEmpty()) {
			int year2 = Integer.parseInt(yearText2);
			dateSortie2 = new Date(year2 - 1900, Calendar.JANUARY, 1); // Using deprecated Date constructor for simplicity
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
			addBy = (User) selectedUser;
		}

		// Create PanelRandomSerieCourte
		PanelRandomSerieCourte panelRandomSerieCourte = new PanelRandomSerieCourte(gestionnaireSerieCourte, serieCourteFrame, actorsArray, genresArray, nbSeason, nbEpisode, meanLength, dateSortie, dateSortie2, platformsArray, dejaVu, addBy);

		// Replace current panel with PanelRandomSerie
		serieCourteFrame.getContentPane().removeAll();
		serieCourteFrame.add(panelRandomSerieCourte);
		serieCourteFrame.revalidate();
		serieCourteFrame.repaint();
	}
}
