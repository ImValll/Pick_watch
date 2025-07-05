package view.saga;

import model.*;
import model.parameter.actors.Actor;
import model.parameter.genres.Genre;
import model.parameter.platforms.Platform;
import model.parameter.users.User;
import model.saga.SagaTableModel;
import model.saga.GestionnaireSaga;
import model.saga.Saga;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.List;

public class PanelSaga extends JPanel{

	SagaFrame sagaFrame;

	private final CardLayout cardLayout = new CardLayout();
	private final JPanel cards = new JPanel(cardLayout); // Panel that uses CardLayout
	private JTable tableArea; // Display book information
	private SagaTableModel tableModel;
	GestionnaireSaga gestionnaireSaga;
	private JTextField searchTitleField;

	public PanelSaga(GestionnaireSaga gestionnaireSaga, SagaFrame sagaFrame) {
		this.gestionnaireSaga = gestionnaireSaga;
		this.sagaFrame = sagaFrame;
		initializeUI();
	}

	private void initializeUI() {
		setLayout(new BorderLayout());
		add(cards, BorderLayout.CENTER);

		JPanel sagaGrid = createSagaPanel();

		add(sagaGrid);
	}

	private JPanel createSagaPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		// Top panel for search
		JPanel topPanel = new JPanel();
		searchTitleField = new JTextField(20);

		JButton searchButton = ButtonEditor.createButton("Rechercher une saga", new Color(70, 130, 180));
		searchButton.addActionListener(this::searchSaga);

		JButton addSagaButton = ButtonEditor.createButton("Ajouter une saga", new Color(70, 130, 180));

		JLabel titleLabel = new JLabel("Titre : ");
		titleLabel.setForeground(Color.WHITE);
		topPanel.add(titleLabel);

		topPanel.add(searchTitleField);
		topPanel.add(searchButton);
		topPanel.add(addSagaButton);

		addSagaButton.addActionListener(e -> addSaga());

		// Get JScrollPane from showAllSaga method
		JScrollPane scrollPane = showAllSaga();

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

	private JScrollPane showAllSaga() {
		java.util.List<Saga> listSaga = gestionnaireSaga.getSaga();
		tableModel = new SagaTableModel(listSaga);
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

	private void searchSaga(ActionEvent e) {
		String titre = searchTitleField.getText();
		java.util.List<Saga> result = gestionnaireSaga.searchSaga(titre);
		if (result.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Erreur: Aucune saga trouvé pour ce titre.", "Erreur", JOptionPane.ERROR_MESSAGE);
		} else {
			tableModel.setSaga(result); // Mettre à jour le modèle du tableau
		}
	}

	private void addSaga() {
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
		JPanel genrePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		java.util.List<JCheckBox> genreCheckBoxes = new ArrayList<>();
		for (Genre genre : genres) {
			JCheckBox checkBox = new JCheckBox(genre.getName());
			genreCheckBoxes.add(checkBox);
			genrePanel.add(checkBox);
		}
		JScrollPane scrollPaneGenre = new JScrollPane(genrePanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		JTextField nbFilmField = new JTextField();

		//Date premier film
		UtilDateModel model = new UtilDateModel();
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

		//Date dernier film
		UtilDateModel model2 = new UtilDateModel();
		Properties p2 = new Properties();
		p2.put("text.today", "Today");
		p2.put("text.month", "Month");
		p2.put("text.year", "Year");
		JDatePanelImpl datePanel2 = new JDatePanelImpl(model2, p2);
		JDatePickerImpl datePicker2 = new JDatePickerImpl(datePanel2, new DateLabelFormatter());


		ArrayList<Platform> platforms = DataManager.loadPlatform();
		JPanel platformPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		java.util.List<JCheckBox> platformCheckBoxes = new ArrayList<>();
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
					imagePathLabel.setText(selectedFile.getName());
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
				new JLabel("Nombre de films"),
				nbFilmField,
				new JLabel("Date de sortie du premier film"),
				datePicker,
				new JLabel("Date de sortie du dernier film"),
				datePicker2,
				new JLabel("Plateforme"),
				scrollPanePlatform,
				new JLabel("Déjà vu"),
				vuPanel,
				new JLabel("Ajouté par"),
				addByComboBox,
				new JLabel("Affiche de la saga"),
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
				"Ajouter une saga",
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
					java.util.List<Genre> selectedGenres = new ArrayList<>();
					for (JCheckBox checkBox : genreCheckBoxes) {
						if (checkBox.isSelected()) {
							selectedGenres.add(new Genre(checkBox.getText()));
						}
					}
					Genre[] genresArray = selectedGenres.isEmpty() ? null : selectedGenres.toArray(new Genre[0]);

					int nbFilm = 0; // Valeur par défaut
					if (!nbFilmField.getText().isEmpty()) {
						nbFilm = Integer.parseInt(nbFilmField.getText());
					}

					Date dateSortie = null; // Valeur par défaut
					Object value = datePicker.getModel().getValue();
					if (value instanceof Date) {
						dateSortie = (Date) value;
					}

					Date dateSortie2 = null; // Valeur par défaut
					Object value2 = datePicker2.getModel().getValue();
					if (value2 instanceof Date) {
						dateSortie2 = (Date) value2;
					}

					// Get platforms
					java.util.List<Platform> selectedPlatforms = new ArrayList<>();
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
						File destinationDir = new File("DATA/posters/saga");
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

					java.util.List<Saga> listSaga = gestionnaireSaga.getSaga();
					for (Saga saga : listSaga) {
						if (saga.getTitre().equalsIgnoreCase(titre)) {
							canBeAdd = false;
							if (Objects.equals(saga.getAddBy().getName(), "Tous") || saga.getAddBy() == addBy) {
								JOptionPane.showMessageDialog(this, "Erreur: La saga " + titre + " a déjà été ajoutée", "Erreur doublons", JOptionPane.ERROR_MESSAGE);
							} else {
								gestionnaireSaga.updateSagaAddBy(saga, new User("Tous"));
								JOptionPane.showMessageDialog(this, "La saga " + titre + " a déjà été ajouté par un autre utilisateur son attribut de personne qui a ajoutée passe donc à Tous.", "Erreur saga déjà ajoutée par un utilisateur", JOptionPane.INFORMATION_MESSAGE);
							}
							break;
						}
					}

					if (canBeAdd) {
						Saga newSaga = new Saga(titre, rea, actorsArray, desc, genresArray, nbFilm, dateSortie, dateSortie2, platformsArray, dejaVu, addBy, imagePath);
						gestionnaireSaga.addSaga(newSaga); // Ajouter la saga à votre gestionnaire de sagas
						JOptionPane.showMessageDialog(this, "Saga ajoutée avec succès: " + titre, "Saga Ajoutée", JOptionPane.INFORMATION_MESSAGE);
					}
				}

				// Mettre à jour le modèle de tableau après l'ajout de la saga
				java.util.List<Saga> updatedSaga = gestionnaireSaga.getSaga(); // Récupérer la liste mise à jour des sagas
				tableModel.setSaga(updatedSaga); // Mettre à jour le modèle du tableau

			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "Erreur: L'année de sortie doit être un nombre valide.", "Erreur de Format", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void backMenu() {
		sagaFrame.dispose();
		new SagaFrame();
	}

	public void deleteSaga(Saga saga) {
		gestionnaireSaga.deleteSaga(saga);

		java.util.List<Saga> updatedSaga = gestionnaireSaga.getSaga(); // Récupérer la liste mise à jour des sagas
		tableModel.setSaga(updatedSaga); // Mettre à jour le modèle du tableau
	}

	public void editSaga(Saga saga) {
		final File[] selectedPosterFile = {null};
		String oldImagePath = saga.getImagePath();

		String oldTitle = saga.getTitre();

		JTextField titleField = new JTextField(saga.getTitre());
		titleField.setEnabled(false);
		JTextField reaField = new JTextField(saga.getRealistateur());

		ArrayList<Actor> allActors = DataManager.loadActor(); // Tous les acteurs
		DefaultComboBoxModel<Actor> actorComboModel = new DefaultComboBoxModel<>();
		for (Actor actor : allActors) {
			actorComboModel.addElement(actor);
		}
		JComboBox<Actor> actorComboBox = new JComboBox<>(actorComboModel);

		JPanel selectedActorsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		selectedActorsPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		Actor[] actorSaga = saga.getActeur();
		List<Actor> selectedActors;
		if (actorSaga != null) {
			selectedActors = new ArrayList<>(List.of(actorSaga));
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

		JTextField descriptionField = new JTextField(saga.getDescription());

		ArrayList<Genre> genres = DataManager.loadGenre();
		JPanel genrePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		java.util.List<JCheckBox> genreCheckBoxes = new ArrayList<>();
		for (Genre genre : genres) {
			JCheckBox checkBox = new JCheckBox(genre.getName());
			if(saga.getGenre() != null && check(saga.getGenre(), genre.getName())) {
				checkBox.setSelected(true);
			}
			genreCheckBoxes.add(checkBox);
			genrePanel.add(checkBox);
		}
		JScrollPane scrollPaneGenre = new JScrollPane(genrePanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);


		JTextField nbFilmField = new JTextField();
		nbFilmField.setText(String.valueOf(saga.getNombreFilms())); // Préremplir avec le nombre de film

		UtilDateModel model = new UtilDateModel();
		Date oldDateSortie = saga.getDateSortiePremier(); // Supposons que saga.getDateSortiePremier() renvoie la date de sortie du film

		if (oldDateSortie != null) {
			model.setValue(oldDateSortie);
		}

		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

		UtilDateModel model2 = new UtilDateModel();
		Date oldDateSortie2 = saga.getDateSortieDernier(); // Supposons que saga.getDateSortiePremier() renvoie la date de sortie du film

		if (oldDateSortie2 != null) {
			model2.setValue(oldDateSortie2);
		}

		Properties p2 = new Properties();
		p2.put("text.today", "Today");
		p2.put("text.month", "Month");
		p2.put("text.year", "Year");
		JDatePanelImpl datePanel2 = new JDatePanelImpl(model2, p2);
		JDatePickerImpl datePicker2 = new JDatePickerImpl(datePanel2, new DateLabelFormatter());

		ArrayList<Platform> platforms = DataManager.loadPlatform();
		JPanel platformPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		java.util.List<JCheckBox> platformCheckBoxes = new ArrayList<>();
		for (Platform platform : platforms) {
			JCheckBox checkBox = new JCheckBox(platform.getName());
			if(saga.getPlateforme() != null && check(saga.getPlateforme(), platform.getName())) {
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

		// Sélectionner le bouton approprié en fonction de l'état actuel de la saga
		if (saga.getDejaVu()) {
			dejaVuButton.setSelected(true);
		} else {
			pasEncoreVuButton.setSelected(true);
		}

		// Ajouter les boutons radio à un panneau
		JPanel vuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		vuPanel.add(dejaVuButton);
		vuPanel.add(pasEncoreVuButton);

		ArrayList<User> users = DataManager.loadUser();
		DefaultComboBoxModel<Object> addByModel = new DefaultComboBoxModel<>();
		for (User user : users) {
			if(Objects.equals(user.getName(), saga.getAddBy().getName())) {
				addByModel.addElement(saga.getAddBy());
			} else {
				addByModel.addElement(user);
			}
		}
		JComboBox<Object> addByComboBox = new JComboBox<>(addByModel);
		addByComboBox.setSelectedItem(saga.getAddBy());

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
					imagePathLabel.setText(selectedFile.getName());
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
				new JLabel("Nombre de films"),
				nbFilmField,
				new JLabel("Date de sortie du premier film"),
				datePicker,
				new JLabel("Date de sortie du dernier film"),
				datePicker2,
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
				"Modifier une saga",
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
					java.util.List<Genre> selectedGenres = new ArrayList<>();
					for (JCheckBox checkBox : genreCheckBoxes) {
						if (checkBox.isSelected()) {
							selectedGenres.add(new Genre(checkBox.getText()));
						}
					}
					Genre[] genresArray = selectedGenres.isEmpty() ? null : selectedGenres.toArray(new Genre[0]);

					int nbFilm = 0; // Valeur par défaut
					if (!nbFilmField.getText().isEmpty()) {
						nbFilm = Integer.parseInt(nbFilmField.getText());
					}

					Date dateSortie = null; // Valeur par défaut
					Object value = datePicker.getModel().getValue();
					if (value instanceof Date) {
						dateSortie = (Date) value;
					}

					Date dateSortie2 = null; // Valeur par défaut
					Object value2 = datePicker2.getModel().getValue();
					if (value2 instanceof Date) {
						dateSortie2 = (Date) value2;
					}

					// Get platforms
					java.util.List<Platform> selectedPlatforms = new ArrayList<>();
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
						File destinationDir = new File("DATA/posters/saga");
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

					Saga newSaga = new Saga(titre, rea, actorsArray, desc, genresArray, nbFilm, dateSortie, dateSortie2, platformsArray, dejaVu, addBy, imagePath);
					gestionnaireSaga.editSaga(oldTitle, newSaga); // Ajouter la saga à votre gestionnaire de sagas
				}

				// Mettre à jour le modèle de tableau après l'ajout de la saga
				List<Saga> updatedSaga = gestionnaireSaga.getSaga(); // Récupérer la liste mise à jour des sagas
				tableModel.setSaga(updatedSaga); // Mettre à jour le modèle du tableau

				JOptionPane.showMessageDialog(this, "Saga modifiée avec succès: " + titre, "Saga modifiée", JOptionPane.INFORMATION_MESSAGE);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "Erreur: L'année de sortie doit être un nombre valide.", "Erreur de Format", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	public void detailsSaga(Saga saga) {
		StringBuilder acteurs = new StringBuilder();
		if(saga.getActeur() != null) {
			saga.getActeur();
			for (Actor actor : saga.getActeur()) {
				acteurs.append(actor.getName()).append(", ");
			}
		}

		StringBuilder genres = new StringBuilder();
		if(saga.getGenre() != null) {
			saga.getGenre();
			for (Genre genre : saga.getGenre()) {
				genres.append(genre.getName()).append(", ");
			}
		}

		StringBuilder plateforme = new StringBuilder();
		if(saga.getPlateforme() != null) {
			saga.getPlateforme();
			for (Platform platform : saga.getPlateforme()) {
				plateforme.append(platform.getName()).append(", ");
			}
		}

		// Affiche ou texte de remplacement
		JLabel imageLabel = new JLabel();
		String imagePath = saga.getImagePath();

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
				new JLabel(saga.getTitre()),
				new JSeparator(SwingConstants.HORIZONTAL),

				new JLabel(titreStyle + "Réalisateur :</span></html>"),
				new JLabel(saga.getRealistateur()),
				new JSeparator(SwingConstants.HORIZONTAL),

				new JLabel(titreStyle + "Acteurs :</span></html>"),
				new JLabel("<html>" + acteurs.toString().replaceAll(", ", "<br>") + "</html>"),
				new JSeparator(SwingConstants.HORIZONTAL),

				new JLabel(titreStyle + "Description :</span></html>"),
				new JLabel("<html><div style='width:300px'>" + saga.getDescription() + "</div></html>"),
				new JSeparator(SwingConstants.HORIZONTAL),

				new JLabel(titreStyle + "Genres :</span></html>"),
				new JLabel("<html>" + genres.toString().replaceAll(", ", "<br>") + "</html>"),
				new JSeparator(SwingConstants.HORIZONTAL),

				new JLabel(titreStyle + "Nombre de films :</span></html>"),
				new JLabel(saga.getNombreFilms() + " min"),
				new JSeparator(SwingConstants.HORIZONTAL),

				new JLabel(titreStyle + "Date de sortie du premier film :</span></html>"),
				new JLabel(String.valueOf(saga.getDateSortiePremier())),
				new JSeparator(SwingConstants.HORIZONTAL),

				new JLabel(titreStyle + "Date de sortie du dernier film :</span></html>"),
				new JLabel(String.valueOf(saga.getDateSortieDernier())),
				new JSeparator(SwingConstants.HORIZONTAL),

				new JLabel(titreStyle + "Plateforme :</span></html>"),
				new JLabel("<html>" + plateforme.toString().replaceAll(", ", "<br>") + "</html>"),
				new JSeparator(SwingConstants.HORIZONTAL),

				new JLabel(titreStyle + "Déjà vu :</span></html>"),
				new JLabel(saga.getDejaVu() ? "Oui" : "Non"),
				new JSeparator(SwingConstants.HORIZONTAL),

				new JLabel(titreStyle + "Ajouté par :</span></html>"),
				new JLabel(saga.getAddBy().getName())
		};

		JOptionPane.showMessageDialog(this, inputs, "Détails de la saga", JOptionPane.PLAIN_MESSAGE);
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

	public GestionnaireSaga getGestionnaire() {
		return gestionnaireSaga;
	}

	private ImageIcon resizeImage(String path, int width, int height) {
		ImageIcon icon = new ImageIcon(path);
		Image img = icon.getImage();
		Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return new ImageIcon(newImg);
	}
}
