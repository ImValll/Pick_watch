package view.movie;

import model.*;
import model.movie.GestionnaireMovie;
import model.movie.Movie;
import model.movie.MoviesTableModel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class PanelMovies extends JPanel {

	MovieFrame movieFrame;

	private CardLayout cardLayout = new CardLayout();
	private JPanel cards = new JPanel(cardLayout); // Panel that uses CardLayout
	private JTable tableArea; // Display book information
	private MoviesTableModel tableModel;
	GestionnaireMovie gestionnaireMovie;
	private JTextField searchTitleField;
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	public PanelMovies(GestionnaireMovie gestionnaireMovie, MovieFrame movieFrame) {
		this.gestionnaireMovie = gestionnaireMovie;
		this.movieFrame = movieFrame;
		initializeUI();
	}

	private void initializeUI() {
		setLayout(new BorderLayout());
		add(cards, BorderLayout.CENTER);

		JPanel moviesGrid = createMoviesPanel();

		add(moviesGrid);
	}

	private JPanel createMoviesPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		// Top panel for search
		JPanel topPanel = new JPanel();
		searchTitleField = new JTextField(20);

		JButton searchButton = createButton("Rechercher un film", new Color(70, 130, 180));
		searchButton.addActionListener(this::searchMovies);

		JButton addMovieButton = createButton("Ajouter un film", new Color(70, 130, 180));

		JLabel titleLabel = new JLabel("Titre : ");
		titleLabel.setForeground(Color.WHITE);
		topPanel.add(titleLabel);

		topPanel.add(searchTitleField);
		topPanel.add(searchButton);
		topPanel.add(addMovieButton);

		addMovieButton.addActionListener(e -> addMovie());

		// Get JScrollPane from showAllMovies method
		JScrollPane scrollPane = showAllMovies();

		topPanel.setBackground(new Color(50, 50, 50));
		scrollPane.setBackground(new Color(50, 50, 50));
		panel.add(topPanel, BorderLayout.NORTH);
		panel.add(scrollPane, BorderLayout.CENTER);

		JButton btnBack = createButton("Retour", new Color(70, 130, 180));
		btnBack.addActionListener(e -> backMenu());

		JPanel bottomPanel = new JPanel();
		bottomPanel.setBackground(new Color(50, 50, 50));
		bottomPanel.add(btnBack);
		panel.add(bottomPanel, BorderLayout.SOUTH);

		return panel;
	}

	private JScrollPane showAllMovies() {
		List<Movie> listMovies = gestionnaireMovie.getMovies();
		tableModel = new MoviesTableModel(listMovies);
		tableArea = new JTable(tableModel);
		tableArea.setBackground(Color.LIGHT_GRAY);

		// Ajout de rendus personnalisés pour les colonnes "Modifier" et "Supprimer"
		tableArea.getColumn("Modifier").setCellRenderer(new ButtonRenderer());
		tableArea.getColumn("Modifier").setCellEditor(new ButtonEditor(this));

		tableArea.getColumn("Supprimer").setCellRenderer(new ButtonRenderer());
		tableArea.getColumn("Supprimer").setCellEditor(new ButtonEditor(this));

		return new JScrollPane(tableArea);
	}

	private void searchMovies(ActionEvent e) {
		String titre = searchTitleField.getText();
		List<Movie> result = gestionnaireMovie.searchMovie(titre);
		if (result.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Erreur: Aucun film trouvé pour ce titre.", "Erreur", JOptionPane.ERROR_MESSAGE);
		} else {
			tableModel.setMovies(result); // Mettre à jour le modèle du tableau
		}
	}

	private void addMovie() {
		JTextField titleField = new JTextField();
		JTextField reaField = new JTextField();
		JTextField descriptionField = new JTextField();



		Genre[] genres = Genre.values();
		JPanel genrePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		List<JCheckBox> genreCheckBoxes = new ArrayList<>();
		for (Genre genre : genres) {
			JCheckBox checkBox = new JCheckBox(genre.name());
			genreCheckBoxes.add(checkBox);
			genrePanel.add(checkBox);
		}
		JScrollPane scrollPaneGenre = new JScrollPane(genrePanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		JTextField dureeField = new JTextField();

		UtilDateModel model = new UtilDateModel();

		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());


		Plateforme[] platforms = Plateforme.values();
		JPanel platformPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		List<JCheckBox> platformCheckBoxes = new ArrayList<>();
		for (Plateforme platform : platforms) {
			JCheckBox checkBox = new JCheckBox(platform.name());
			platformCheckBoxes.add(checkBox);
			platformPanel.add(checkBox);
		}
		JScrollPane scrollPanePlatform = new JScrollPane(platformPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		// Créer les boutons radio pour "Déjà vu" et "Pas encore vu"
		JRadioButton dejaVuButton = new JRadioButton("OUI");
		JRadioButton pasEncoreVuButton = new JRadioButton("NON", true);
		ButtonGroup vuGroup = new ButtonGroup();
		vuGroup.add(dejaVuButton);
		vuGroup.add(pasEncoreVuButton);

		// Ajouter les boutons radio à un panneau
		JPanel vuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		vuPanel.add(dejaVuButton);
		vuPanel.add(pasEncoreVuButton);

		JComboBox<Utilisateur> addByComboBox = new JComboBox<>(Utilisateur.values());

		final JComponent[] inputs = new JComponent[] {
			new JLabel("Titre*"),
			titleField,
			new JLabel("Réalisateur"),
			reaField,
			new JLabel("Description"),
			descriptionField,
			new JLabel("Genres"),
			scrollPaneGenre,
			new JLabel("Durée"),
			dureeField,
			new JLabel("Date de sortie"),
			datePicker,
			new JLabel("Plateforme"),
			scrollPanePlatform,
			new JLabel("Déjà vu"),
			vuPanel,
			new JLabel("Ajouté par"),
			addByComboBox
		};

		int result = JOptionPane.showConfirmDialog(this, inputs, "Ajouter un nouveau film", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			try {
				String titre = titleField.getText();

				if(titre.isEmpty()) {
					JOptionPane.showMessageDialog(this, "Erreur: Le titre doit être entré.", "Erreur titre vide", JOptionPane.ERROR_MESSAGE);
				}
				else {
					String rea = reaField.getText();
					String desc = descriptionField.getText();

					// Get genres
					List<Genre> selectedGenres = new ArrayList<>();
					for (JCheckBox checkBox : genreCheckBoxes) {
						if (checkBox.isSelected()) {
							selectedGenres.add(Genre.valueOf(checkBox.getText()));
						}
					}
					Genre[] genresArray = selectedGenres.isEmpty() ? null : selectedGenres.toArray(new Genre[0]);

					int duree = 0; // Valeur par défaut
					if (!dureeField.getText().isEmpty()) {
						duree = Integer.parseInt(dureeField.getText());
					}

					Date dateSortie = null; // Valeur par défaut
					Object value = datePicker.getModel().getValue();
					if (value instanceof Date) {
						dateSortie = (Date) value;
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
					boolean dejaVu = dejaVuButton.isSelected();

					Utilisateur addBy = Utilisateur.valueOf(addByComboBox.getSelectedItem().toString());

					boolean canBeAdd = true;

					List<Movie> listMovies = gestionnaireMovie.getMovies();
					for (int i = 0; i < listMovies.size(); i++) {
						Movie movie = listMovies.get(i);
						if (movie.getTitre().equalsIgnoreCase(titre)) {
							canBeAdd = false;
							if (movie.getAddBy() == Utilisateur.Nous2 || movie.getAddBy() == addBy) {
								JOptionPane.showMessageDialog(this, "Erreur: Le film " + titre + " a déjà été ajouté", "Erreur doublons", JOptionPane.ERROR_MESSAGE);
							} else {
								gestionnaireMovie.updateMovieAddBy(movie, Utilisateur.Nous2);
								JOptionPane.showMessageDialog(this, "Le film " + titre + " a déjà été ajouté par un autre utilisateur son attribut de personne qui a ajouté passe donc à Nous2.", "Erreur film déjà ajouté par un utilisateur", JOptionPane.INFORMATION_MESSAGE);
							}
							break;
						}
					}

					if (canBeAdd) {
						Movie newMovie = new Movie(titre, rea, desc, genresArray, duree, dateSortie, platformsArray, dejaVu, addBy);
						gestionnaireMovie.addMovie(newMovie); // Ajouter le film à votre gestionnaire de films
						JOptionPane.showMessageDialog(this, "Film ajouté avec succès: " + titre, "Film Ajouté", JOptionPane.INFORMATION_MESSAGE);
					}
				}

				// Mettre à jour le modèle de tableau après l'ajout du film
				List<Movie> updatedMovies = gestionnaireMovie.getMovies(); // Récupérer la liste mise à jour des films
				tableModel.setMovies(updatedMovies); // Mettre à jour le modèle du tableau

			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "Erreur: L'année de sortie doit être un nombre valide.", "Erreur de Format", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void backMenu() {
		movieFrame.dispose();
		new MovieFrame();
	}

	public void deleteMovie(Movie movie) {
		gestionnaireMovie.deleteMovie(movie);

		List<Movie> updatedMovies = gestionnaireMovie.getMovies(); // Récupérer la liste mise à jour des films
		tableModel.setMovies(updatedMovies); // Mettre à jour le modèle du tableau
	}

	public void editMovie(Movie movie) {

		String oldTitle = movie.getTitre();

		JTextField titleField = new JTextField(movie.getTitre());
		titleField.setEnabled(false);
		JTextField reaField = new JTextField(movie.getRealistateur());
		JTextField descriptionField = new JTextField(movie.getDescription());

		Genre[] genres = Genre.values();
		JPanel genrePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		List<JCheckBox> genreCheckBoxes = new ArrayList<>();
		for (Genre genre : genres) {
			JCheckBox checkBox = new JCheckBox(genre.name());
			if(movie.getGenre() != null && check(movie.getGenre(), genre.name())) {
				checkBox.setSelected(true);
			}
			genreCheckBoxes.add(checkBox);
			genrePanel.add(checkBox);
		}
		JScrollPane scrollPaneGenre = new JScrollPane(genrePanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);


		JTextField dureeField = new JTextField();
		dureeField.setText(String.valueOf(movie.getDuree())); // Préremplir avec la durée du film

		UtilDateModel model = new UtilDateModel();

		Date oldDateSortie = movie.getDateSortie(); // Supposons que movie.getDateSortie() renvoie la date de sortie du film

		if (oldDateSortie != null) {
			model.setValue(oldDateSortie);
		}

		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

		Plateforme[] platforms = Plateforme.values();
		JPanel platformPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		List<JCheckBox> platformCheckBoxes = new ArrayList<>();
		for (Plateforme platform : platforms) {
			JCheckBox checkBox = new JCheckBox(platform.name());
			if(movie.getPlateforme() != null && check(movie.getPlateforme(), platform.name())) {
				checkBox.setSelected(true);
			}
			platformCheckBoxes.add(checkBox);
			platformPanel.add(checkBox);
		}
		JScrollPane scrollPanePlatform = new JScrollPane(platformPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		// Créer les boutons radio pour "Déjà vu" et "Pas encore vu"
		JRadioButton dejaVuButton = new JRadioButton("Déjà vu");
		JRadioButton pasEncoreVuButton = new JRadioButton("Pas encore vu");
		ButtonGroup vuGroup = new ButtonGroup();
		vuGroup.add(dejaVuButton);
		vuGroup.add(pasEncoreVuButton);

		// Sélectionner le bouton approprié en fonction de l'état actuel du film
		if (movie.getDejaVu()) {
			dejaVuButton.setSelected(true);
		} else {
			pasEncoreVuButton.setSelected(true);
		}

		// Ajouter les boutons radio à un panneau
		JPanel vuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		vuPanel.add(dejaVuButton);
		vuPanel.add(pasEncoreVuButton);

		JComboBox<Utilisateur> addByComboBox = new JComboBox<>(Utilisateur.values());
		addByComboBox.setSelectedItem(movie.getAddBy());

		final JComponent[] inputs = new JComponent[] {
				new JLabel("Titre*"),
				titleField,
				new JLabel("Réalisateur"),
				reaField,
				new JLabel("Description"),
				descriptionField,
				new JLabel("Genres"),
				scrollPaneGenre,
				new JLabel("Durée"),
				dureeField,
				new JLabel("Date de sortie"),
				datePicker,
				new JLabel("Plateforme"),
				scrollPanePlatform,
				new JLabel("Déjà vu"),
				vuPanel,
				new JLabel("Ajouté par"),
				addByComboBox
		};

		int result = JOptionPane.showConfirmDialog(this, inputs, "Modifier un film", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			try {
				String titre = titleField.getText();

				if(titre.isEmpty()) {
					JOptionPane.showMessageDialog(this, "Erreur: Le titre doit être entré.", "Erreur titre vide", JOptionPane.ERROR_MESSAGE);
				}
				else {
					String rea = reaField.getText();
					String desc = descriptionField.getText();

					// Get genres
					List<Genre> selectedGenres = new ArrayList<>();
					for (JCheckBox checkBox : genreCheckBoxes) {
						if (checkBox.isSelected()) {
							selectedGenres.add(Genre.valueOf(checkBox.getText()));
						}
					}
					Genre[] genresArray = selectedGenres.isEmpty() ? null : selectedGenres.toArray(new Genre[0]);

					int duree = 0; // Valeur par défaut
					if (!dureeField.getText().isEmpty()) {
						duree = Integer.parseInt(dureeField.getText());
					}

					Date dateSortie = null; // Valeur par défaut
					Object value = datePicker.getModel().getValue();
					if (value instanceof Date) {
						dateSortie = (Date) value;
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
					boolean dejaVu = dejaVuButton.isSelected();

					Utilisateur addBy = Utilisateur.valueOf(addByComboBox.getSelectedItem().toString());

					Movie newMovie = new Movie(titre, rea, desc, genresArray, duree, dateSortie, platformsArray, dejaVu, addBy);
					gestionnaireMovie.editMovie(oldTitle, newMovie); // Ajouter le film à votre gestionnaire de films
				}

				// Mettre à jour le modèle de tableau après l'ajout du film
				List<Movie> updatedMovies = gestionnaireMovie.getMovies(); // Récupérer la liste mise à jour des films
				tableModel.setMovies(updatedMovies); // Mettre à jour le modèle du tableau

				JOptionPane.showMessageDialog(this, "Film modifié avec succès: " + titre, "Film modifié", JOptionPane.INFORMATION_MESSAGE);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "Erreur: L'année de sortie doit être un nombre valide.", "Erreur de Format", JOptionPane.ERROR_MESSAGE);
			}
		}

	}



	public static boolean check(Object[] tab, Object val) {
		boolean b = false;

		for(Object i : tab){
			if(i.toString().equals(val.toString())){
				b = true;
				break;
			}
		}
		return b;
	}

	public JButton createButton(String title, Color color) {
		JButton button = new JButton(title);

		button.setBackground(color); // Bleu foncé
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Arial", Font.BOLD, 18));
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

		return button;
	}

	public JTable getTableArea() {
		return tableArea;
	}

	public GestionnaireMovie getGestionnaire() {
		return gestionnaireMovie;
	}
}
