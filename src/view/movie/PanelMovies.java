package view.movie;

import model.*;
import model.parameter.actors.Actor;
import model.parameter.genres.Genre;
import model.parameter.platforms.Platform;
import model.parameter.users.User;
import model.movie.GestionnaireMovie;
import model.movie.Movie;
import model.movie.MoviesTableModel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.List;

public class PanelMovies extends JPanel {

	MovieFrame movieFrame;

	private final CardLayout cardLayout = new CardLayout();
	private final JPanel cards = new JPanel(cardLayout); // Panel that uses CardLayout
	private JTable tableArea; // Display book information
	private MoviesTableModel tableModel;
	GestionnaireMovie gestionnaireMovie;
	private JTextField searchTitleField;

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

		JButton searchButton = ButtonEditor.createButton("Rechercher un film", new Color(70, 130, 180));
		searchButton.addActionListener(this::searchMovies);

		JButton addMovieButton = ButtonEditor.createButton("Ajouter un film", new Color(70, 130, 180));

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

		JButton btnBack = ButtonEditor.createButton("Retour", new Color(70, 130, 180));
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

		// Ajout de rendus personnalisés pour les colonnes "Visualiser", "Modifier" et "Supprimer"
		tableArea.getColumn("Visualiser").setCellRenderer(new ButtonRenderer());
		tableArea.getColumn("Visualiser").setCellEditor(new ButtonEditor(this));

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
		final File[] selectedPosterFile = {null}; // Pour stocker temporairement le fichier choisi

		JTextField titleField = new JTextField();
		JTextField reaField = new JTextField();

		ArrayList<Actor> actors = DataManager.loadActor();
		DefaultComboBoxModel<Actor> actorComboModel = new DefaultComboBoxModel<>();
		for (Actor actor : actors) {
			actorComboModel.addElement(actor);
		}
		JComboBox<Actor> actorComboBox = new JComboBox<>(actorComboModel);

		JPanel selectedActorsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		selectedActorsPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		List<Actor> selectedActors = new ArrayList<>();

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

		JTextField descriptionField = new JTextField();

		ArrayList<Genre> genres = DataManager.loadGenre();
		JPanel genrePanel = new JPanel(new GridLayout(0, 4, 4, 4));
		List<JCheckBox> genreCheckBoxes = new ArrayList<>();
		for (Genre genre : genres) {
			JCheckBox checkBox = new JCheckBox(genre.getName());
			genreCheckBoxes.add(checkBox);
			genrePanel.add(checkBox);
		}
		JScrollPane scrollPaneGenre = new JScrollPane(genrePanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		JTextField dureeField = new JTextField();
		((AbstractDocument) dureeField.getDocument()).setDocumentFilter(new NumericDocumentFilter());

		UtilDateModel model = new UtilDateModel();

		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		datePicker.setPreferredSize(new Dimension(120, 25));


		ArrayList<Platform> platforms = DataManager.loadPlatform();
		JPanel platformPanel = new JPanel(new GridLayout(0, 4, 4, 4));
		List<JCheckBox> platformCheckBoxes = new ArrayList<>();
		for (Platform platform : platforms) {
			JCheckBox checkBox = new JCheckBox(platform.getName());
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

		ArrayList<User> users = DataManager.loadUser();
		DefaultComboBoxModel<Object> addByModel = new DefaultComboBoxModel<>();
		for (User user : users) {
			addByModel.addElement(user);
		}
		JComboBox<Object> addByComboBox = new JComboBox<>(addByModel);

		JButton chooseImageButton = new JButton("Choisir une affiche");
		JLabel imagePathLabel = new JLabel("Aucune image sélectionnée");

		chooseImageButton.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Choisir une nouvelle affiche");

			// ✅ Filtrer les fichiers pour n'afficher que les images
			FileNameExtensionFilter imageFilter = new FileNameExtensionFilter(
					"Images (*.jpg, *.jpeg, *.png)", "jpg", "jpeg", "png"
			);
			fileChooser.setAcceptAllFileFilterUsed(false); // désactive le filtre "Tous les fichiers"
			fileChooser.setFileFilter(imageFilter);

			int resultImg = fileChooser.showOpenDialog(null);
			if (resultImg == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				selectedPosterFile[0] = selectedFile;

				// ✅ Vérifier l'extension sélectionnée (sécurité supplémentaire)
				String name = selectedFile.getName().toLowerCase();
				if (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png")) {
					try {
						// Lire l'image d'origine
						BufferedImage original = ImageIO.read(selectedFile);
						if (original == null) throw new IOException("Image invalide");

						// Créer une image RGB (sans alpha), fond blanc
						BufferedImage converted = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_RGB);
						Graphics2D g2d = converted.createGraphics();
						g2d.setColor(Color.WHITE);
						g2d.fillRect(0, 0, converted.getWidth(), converted.getHeight());
						g2d.drawImage(original, 0, 0, null);
						g2d.dispose();

						// Créer le dossier d'affiches s'il n'existe pas
						File postersDir = new File("affiches");
						if (!postersDir.exists()) postersDir.mkdirs();

						// Nom unique (UUID ou autre identifiant)
						String uniqueName = UUID.randomUUID().toString() + ".jpg";
						File output = new File(postersDir, uniqueName);

						// Sauvegarde en JPEG
						ImageIO.write(converted, "jpg", output);

						// Mise à jour du chemin
						selectedPosterFile[0] = output;
						imagePathLabel.setText(output.getName());
					} catch (IOException ex) {
						JOptionPane.showMessageDialog(null,
								"L’image sélectionnée est dans un format ou une structure non supportée par Java.\n\n" +
										"💡 Astuce : ouvrez l’image dans un éditeur d’images (comme Paint, GIMP, Photoshop...) puis\n" +
										"ré-enregistrez-la au format JPEG ou PNG standard, sans transparence.\n\n" +
										"Format refusé : " + selectedFile.getName(),
								"Image invalide", JOptionPane.ERROR_MESSAGE);
						selectedPosterFile[0] = null;
						imagePathLabel.setText("Aucune image sélectionnée");
						ex.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null,
							"Format d'image invalide. Seuls les fichiers JPG, JPEG et PNG sont acceptés.",
							"Erreur de format", JOptionPane.ERROR_MESSAGE);
					selectedPosterFile[0] = null;
					imagePathLabel.setText("Aucune image sélectionnée");
				}
			}
		});

		final JComponent[] inputs = new JComponent[] {
			new JLabel("Titre*"),
			titleField,
			new JLabel("Réalisateur"),
			reaField,
			new JLabel("Acteurs"),
			actorComboBox,
			selectedActorsPanel,
			new JLabel("Description"),
			descriptionField,
			new JLabel("Genres"),
			scrollPaneGenre,
			new JLabel("Durée (en minutes)"),
			dureeField,
			new JLabel("Date de sortie"),
			datePicker,
			new JLabel("Plateforme"),
			scrollPanePlatform,
			new JLabel("Déjà vu"),
			vuPanel,
			new JLabel("Ajouté par"),
			addByComboBox,
			new JLabel("Affiche du film"),
			chooseImageButton,
			imagePathLabel,
		};



		// Crée un panel vertical pour empiler les composants
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

		for (JComponent comp : inputs) {
			comp.setAlignmentX(Component.LEFT_ALIGNMENT); // meilleur alignement visuel
			inputPanel.add(comp);
			inputPanel.add(Box.createVerticalStrut(8)); // espacement vertical entre champs
		}

		// Ajoute le panel dans un JScrollPane avec taille fixe
		JScrollPane scrollPane = new JScrollPane(inputPanel);
		scrollPane.setPreferredSize(new Dimension(500, 600)); // Ajuste comme tu veux
		scrollPane.getVerticalScrollBar().setUnitIncrement(16); // défilement fluide

		// Affiche la boîte de dialogue avec défilement
		int result = JOptionPane.showConfirmDialog(
				this,
				scrollPane,
				"Ajouter un film",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE
		);

		if (result == JOptionPane.OK_OPTION) {
			try {
				String titre = titleField.getText();

				if(titre.isEmpty()) {
					JOptionPane.showMessageDialog(this, "Erreur: Le titre doit être entré.", "Erreur titre vide", JOptionPane.ERROR_MESSAGE);
				}
				else {
					String rea = reaField.getText();

					List<Actor> selectedActorsCopy = new ArrayList<>(selectedActors);
					Actor[] actorsArray = selectedActorsCopy.isEmpty() ? null : selectedActorsCopy.toArray(new Actor[0]);

					String desc = descriptionField.getText();

					// Get genres
					List<Genre> selectedGenres = new ArrayList<>();
					for (JCheckBox checkBox : genreCheckBoxes) {
						if (checkBox.isSelected()) {
							selectedGenres.add(new Genre(checkBox.getText()));
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
					List<Platform> selectedPlatforms = new ArrayList<>();
					for (JCheckBox checkBox : platformCheckBoxes) {
						if (checkBox.isSelected()) {
							selectedPlatforms.add(new Platform(checkBox.getText()));
						}
					}
					Platform[] platformsArray = selectedPlatforms.isEmpty() ? null : selectedPlatforms.toArray(new Platform[0]);

					// Get "Déjà vu" status
					boolean dejaVu = dejaVuButton.isSelected();

					User addBy;
					if(addByComboBox.getSelectedItem() == null) {
						addBy = new User("Tous");
					} else {
						addBy = new User(addByComboBox.getSelectedItem().toString());
					}

					String imagePath = null;
					if (selectedPosterFile[0] != null) {
						File destinationDir = new File("DATA/posters/movie");
						if (!destinationDir.exists()) destinationDir.mkdirs();

						// Pour éviter les doublons, tu peux renommer selon le titre du film
						String fileExtension = selectedPosterFile[0].getName().substring(selectedPosterFile[0].getName().lastIndexOf('.'));
						File destinationFile = new File(destinationDir, titre.replaceAll("\\s+", "_") + fileExtension);

						try {
							Files.copy(selectedPosterFile[0].toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
							imagePath = destinationFile.getPath();
						} catch (IOException e) {
							JOptionPane.showMessageDialog(this, "Erreur lors de la copie de l'image : " + e.getMessage(), "Erreur d'image", JOptionPane.ERROR_MESSAGE);
						}
					}

					boolean canBeAdd = true;

					List<Movie> listMovies = gestionnaireMovie.getMovies();
					for (Movie movie : listMovies) {
						if (movie.getTitre().equalsIgnoreCase(titre)) {
							canBeAdd = false;
							if (Objects.equals(movie.getAddBy().getName(), "Tous") || movie.getAddBy() == addBy) {
								JOptionPane.showMessageDialog(this, "Erreur: Le film " + titre + " a déjà été ajouté", "Erreur doublons", JOptionPane.ERROR_MESSAGE);
							} else {
								gestionnaireMovie.updateMovieAddBy(movie, new User("Tous"));
								JOptionPane.showMessageDialog(this, "Le film " + titre + " a déjà été ajouté par un autre utilisateur son attribut de personne qui a ajouté passe donc à Tous.", "Erreur film déjà ajouté par un utilisateur", JOptionPane.INFORMATION_MESSAGE);
							}
							break;
						}
					}

					if (canBeAdd) {
						Movie newMovie = new Movie(titre, rea, actorsArray, desc, genresArray, duree, dateSortie, platformsArray, dejaVu, addBy, imagePath);
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
		final File[] selectedPosterFile = {null};
		String oldImagePath = movie.getImagePath();

		String oldTitle = movie.getTitre();

		JTextField titleField = new JTextField(movie.getTitre());
		titleField.setEnabled(false);
		JTextField reaField = new JTextField(movie.getRealistateur());

		ArrayList<Actor> allActors = DataManager.loadActor(); // Tous les acteurs
		DefaultComboBoxModel<Actor> actorComboModel = new DefaultComboBoxModel<>();
		for (Actor actor : allActors) {
			actorComboModel.addElement(actor);
		}
		JComboBox<Actor> actorComboBox = new JComboBox<>(actorComboModel);

		JPanel selectedActorsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		selectedActorsPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		Actor[] actorMovie = movie.getActeur();
		List<Actor> selectedActors;
		if (actorMovie != null) {
			selectedActors = new ArrayList<>(List.of(actorMovie));
		} else {
			selectedActors = new ArrayList<>();
		}

		for (Actor selected : selectedActors) {
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
		}

		// Logique de sélection manuelle depuis le combo
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

		JTextField descriptionField = new JTextField(movie.getDescription());

		ArrayList<Genre> genres = DataManager.loadGenre();
		JPanel genrePanel = new JPanel(new GridLayout(0, 4, 4, 4));
		List<JCheckBox> genreCheckBoxes = new ArrayList<>();
		for (Genre genre : genres) {
			JCheckBox checkBox = new JCheckBox(genre.getName());
			if(movie.getGenre() != null && check(movie.getGenre(), genre.getName())) {
				checkBox.setSelected(true);
			}
			genreCheckBoxes.add(checkBox);
			genrePanel.add(checkBox);
		}
		JScrollPane scrollPaneGenre = new JScrollPane(genrePanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);


		JTextField dureeField = new JTextField();
		((AbstractDocument) dureeField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
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
		datePicker.setPreferredSize(new Dimension(120, 25));

		ArrayList<Platform> platforms = DataManager.loadPlatform();
		JPanel platformPanel = new JPanel(new GridLayout(0, 4, 4, 4));
		List<JCheckBox> platformCheckBoxes = new ArrayList<>();
		for (Platform platform : platforms) {
			JCheckBox checkBox = new JCheckBox(platform.getName());
			if(movie.getPlateforme() != null && check(movie.getPlateforme(), platform.getName())) {
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

		ArrayList<User> users = DataManager.loadUser();
		DefaultComboBoxModel<User> addByModel = new DefaultComboBoxModel<>();
		for (User user : users) {
			if(Objects.equals(user.getName(), movie.getAddBy().getName())) {
				addByModel.addElement(movie.getAddBy());
			} else {
				addByModel.addElement(user);
			}
		}
		JComboBox<User> addByComboBox = new JComboBox<>(addByModel);
		addByComboBox.setSelectedItem(movie.getAddBy());

		JButton chooseImageButton = new JButton("Changer l'affiche");
		JLabel imagePathLabel = new JLabel((oldImagePath != null) ? new File(oldImagePath).getName() : "Aucune image sélectionnée");

		chooseImageButton.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Choisir une nouvelle affiche");

			// ✅ Filtrer les fichiers pour n'afficher que les images
			FileNameExtensionFilter imageFilter = new FileNameExtensionFilter(
					"Images (*.jpg, *.jpeg, *.png)", "jpg", "jpeg", "png"
			);
			fileChooser.setAcceptAllFileFilterUsed(false); // désactive le filtre "Tous les fichiers"
			fileChooser.setFileFilter(imageFilter);

			int resultImg = fileChooser.showOpenDialog(null);
			if (resultImg == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				selectedPosterFile[0] = selectedFile;

				// ✅ Vérifier l'extension sélectionnée (sécurité supplémentaire)
				String name = selectedFile.getName().toLowerCase();
				if (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png")) {
					try {
						// Lire l'image d'origine
						BufferedImage original = ImageIO.read(selectedFile);
						if (original == null) throw new IOException("Image invalide");

						// Créer une image RGB (sans alpha), fond blanc
						BufferedImage converted = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_RGB);
						Graphics2D g2d = converted.createGraphics();
						g2d.setColor(Color.WHITE);
						g2d.fillRect(0, 0, converted.getWidth(), converted.getHeight());
						g2d.drawImage(original, 0, 0, null);
						g2d.dispose();

						// Créer le dossier d'affiches s'il n'existe pas
						File postersDir = new File("affiches");
						if (!postersDir.exists()) postersDir.mkdirs();

						// Nom unique (UUID ou autre identifiant)
						String uniqueName = UUID.randomUUID().toString() + ".jpg";
						File output = new File(postersDir, uniqueName);

						// Sauvegarde en JPEG
						ImageIO.write(converted, "jpg", output);

						// Mise à jour du chemin
						selectedPosterFile[0] = output;
						imagePathLabel.setText(output.getName());
					} catch (IOException ex) {
						JOptionPane.showMessageDialog(null,
								"L’image sélectionnée est dans un format ou une structure non supportée par Java.\n\n" +
										"💡 Astuce : ouvrez l’image dans un éditeur d’images (comme Paint, GIMP, Photoshop...) puis\n" +
										"ré-enregistrez-la au format JPEG ou PNG standard, sans transparence.\n\n" +
										"Format refusé : " + selectedFile.getName(),
								"Image invalide", JOptionPane.ERROR_MESSAGE);
						selectedPosterFile[0] = null;
						imagePathLabel.setText("Aucune image sélectionnée");
						ex.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null,
							"Format d'image invalide. Seuls les fichiers JPG, JPEG et PNG sont acceptés.",
							"Erreur de format", JOptionPane.ERROR_MESSAGE);
					selectedPosterFile[0] = null;
					imagePathLabel.setText("Aucune image sélectionnée");
				}
			}
		});

		final JComponent[] inputs = new JComponent[] {
				new JLabel("Titre*"),
				titleField,
				new JLabel("Réalisateur"),
				reaField,
				new JLabel("Acteurs"),
				actorComboBox,
				selectedActorsPanel,
				new JLabel("Description"),
				descriptionField,
				new JLabel("Genres"),
				scrollPaneGenre,
				new JLabel("Durée (en minutes)"),
				dureeField,
				new JLabel("Date de sortie"),
				datePicker,
				new JLabel("Plateforme"),
				scrollPanePlatform,
				new JLabel("Déjà vu"),
				vuPanel,
				new JLabel("Ajouté par"),
				addByComboBox,
				new JLabel("Affiche"),
				chooseImageButton,
				imagePathLabel,
		};

		// Crée un panel vertical pour empiler les composants
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

		for (JComponent comp : inputs) {
			comp.setAlignmentX(Component.LEFT_ALIGNMENT); // meilleur alignement visuel
			inputPanel.add(comp);
			inputPanel.add(Box.createVerticalStrut(8)); // espacement vertical entre champs
		}

		// Ajoute le panel dans un JScrollPane avec taille fixe
		JScrollPane scrollPane = new JScrollPane(inputPanel);
		scrollPane.setPreferredSize(new Dimension(500, 600)); // Ajuste comme tu veux
		scrollPane.getVerticalScrollBar().setUnitIncrement(16); // défilement fluide

		// Affiche la boîte de dialogue avec défilement
		int result = JOptionPane.showConfirmDialog(
				this,
				scrollPane,
				"Modifier un film",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE
		);
		
		if (result == JOptionPane.OK_OPTION) {
			try {
				String titre = titleField.getText();

				if(titre.isEmpty()) {
					JOptionPane.showMessageDialog(this, "Erreur: Le titre doit être entré.", "Erreur titre vide", JOptionPane.ERROR_MESSAGE);
				}
				else {
					String rea = reaField.getText();

					List<Actor> selectedActorsCopy = new ArrayList<>(selectedActors);
					Actor[] actorsArray = selectedActorsCopy.isEmpty() ? null : selectedActorsCopy.toArray(new Actor[0]);

					String desc = descriptionField.getText();

					// Get genres
					List<Genre> selectedGenres = new ArrayList<>();
					for (JCheckBox checkBox : genreCheckBoxes) {
						if (checkBox.isSelected()) {
							selectedGenres.add(new Genre(checkBox.getText()));
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
					List<Platform> selectedPlatforms = new ArrayList<>();
					for (JCheckBox checkBox : platformCheckBoxes) {
						if (checkBox.isSelected()) {
							selectedPlatforms.add(new Platform(checkBox.getText()));
						}
					}
					Platform[] platformsArray = selectedPlatforms.isEmpty() ? null : selectedPlatforms.toArray(new Platform[0]);

					// Get "Déjà vu" status
					boolean dejaVu = dejaVuButton.isSelected();

					User addBy;
					if(addByComboBox.getSelectedItem() == null) {
						addBy = new User("Tous");
					} else {
						addBy = new User(addByComboBox.getSelectedItem().toString());
					}

					String imagePath = oldImagePath; // par défaut, conserver l'ancienne

					if (selectedPosterFile[0] != null) {
						File destinationDir = new File("DATA/posters/movie");
						if (!destinationDir.exists()) destinationDir.mkdirs();

						String newExtension = selectedPosterFile[0].getName().substring(selectedPosterFile[0].getName().lastIndexOf('.'));
						String baseFileName = titre.replaceAll("\\s+", "_");
						File destinationFile = new File(destinationDir, baseFileName + newExtension);

						// Supprimer l'ancien fichier si l'extension a changé
						if (oldImagePath != null && !oldImagePath.endsWith(newExtension)) {
							File oldFile = new File(oldImagePath);
							if (oldFile.exists()) {
								oldFile.delete(); // ⚠️ silencieux
							}
						}

						try {
							Files.copy(selectedPosterFile[0].toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
							imagePath = destinationFile.getPath();
						} catch (IOException e) {
							JOptionPane.showMessageDialog(this, "Erreur lors de la copie de l'image : " + e.getMessage(), "Erreur d'image", JOptionPane.ERROR_MESSAGE);
						}
					}

					Movie newMovie = new Movie(titre, rea, actorsArray, desc, genresArray, duree, dateSortie, platformsArray, dejaVu, addBy, imagePath);
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

	public void detailsMovie(Movie movie) {
		StringBuilder acteurs = new StringBuilder();
		if(movie.getActeur() != null) {
			movie.getActeur();
			for (Actor actor : movie.getActeur()) {
				acteurs.append(actor.getName()).append(", ");
			}
		}

		StringBuilder genres = new StringBuilder();
		if(movie.getGenre() != null) {
			movie.getGenre();
			for (Genre genre : movie.getGenre()) {
				genres.append(genre.getName()).append(", ");
			}
		}

		StringBuilder plateforme = new StringBuilder();
		if(movie.getPlateforme() != null) {
			movie.getPlateforme();
			for (Platform platform : movie.getPlateforme()) {
				plateforme.append(platform.getName()).append(", ");
			}
		}

		// Affiche ou texte de remplacement
		JLabel imageLabel = new JLabel();
		String imagePath = movie.getImagePath();

		if (imagePath != null && new File(imagePath).exists()) {
			imageLabel.setIcon(resizeImage(imagePath, 150, 200));
		} else {
			imageLabel.setText("Aucune affiche disponible");
			imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		}

		// Style HTML pour les titres
		String titreStyle = "<html><span style='font-family:Arial; font-size:14pt; font-weight:bold; text-decoration: underline;'>";

		final JComponent[] inputs = new JComponent[] {
				imageLabel,

				new JLabel(titreStyle + "Titre :</span></html>"),
				new JLabel(movie.getTitre()),
				new JSeparator(SwingConstants.HORIZONTAL),

				new JLabel(titreStyle + "Réalisateur :</span></html>"),
				new JLabel(movie.getRealistateur()),
				new JSeparator(SwingConstants.HORIZONTAL),

				new JLabel(titreStyle + "Acteurs :</span></html>"),
				new JLabel("<html>" + acteurs.toString().replaceAll(", ", "<br>") + "</html>"),
				new JSeparator(SwingConstants.HORIZONTAL),

				new JLabel(titreStyle + "Description :</span></html>"),
				new JLabel("<html><div style='width:300px'>" + movie.getDescription() + "</div></html>"),
				new JSeparator(SwingConstants.HORIZONTAL),

				new JLabel(titreStyle + "Genres :</span></html>"),
				new JLabel("<html>" + genres.toString().replaceAll(", ", "<br>") + "</html>"),
				new JSeparator(SwingConstants.HORIZONTAL),

				new JLabel(titreStyle + "Durée :</span></html>"),
				new JLabel(movie.getDuree() + " min"),
				new JSeparator(SwingConstants.HORIZONTAL),

				new JLabel(titreStyle + "Date de sortie :</span></html>"),
				new JLabel(String.valueOf(movie.getDateSortie())),
				new JSeparator(SwingConstants.HORIZONTAL),

				new JLabel(titreStyle + "Plateforme :</span></html>"),
				new JLabel("<html>" + plateforme.toString().replaceAll(", ", "<br>") + "</html>"),
				new JSeparator(SwingConstants.HORIZONTAL),

				new JLabel(titreStyle + "Déjà vu :</span></html>"),
				new JLabel(movie.getDejaVu() ? "Oui" : "Non"),
				new JSeparator(SwingConstants.HORIZONTAL),

				new JLabel(titreStyle + "Ajouté par :</span></html>"),
				new JLabel(movie.getAddBy().getName())
		};

		JOptionPane.showMessageDialog(this, inputs, "Détails du film", JOptionPane.PLAIN_MESSAGE);
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

	public JTable getTableArea() {
		return tableArea;
	}

	public GestionnaireMovie getGestionnaire() {
		return gestionnaireMovie;
	}

	private ImageIcon resizeImage(String path, int width, int height) {
		ImageIcon icon = new ImageIcon(path);
		Image img = icon.getImage();
		Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return new ImageIcon(newImg);
	}
}
