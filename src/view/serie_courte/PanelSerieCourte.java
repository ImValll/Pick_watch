package view.serie_courte;

import model.*;
import model.parameter.actors.Actor;
import model.parameter.genres.Genre;
import model.parameter.platforms.Platform;
import model.parameter.users.User;
import model.serie_courte.GestionnaireSerieCourte;
import model.serie_courte.SerieCourte;
import model.serie_courte.SerieCourteTableModel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.List;

public class PanelSerieCourte extends JPanel {

	SerieCourteFrame serieCourteFrame;

	private final CardLayout cardLayout = new CardLayout();
	private final JPanel cards = new JPanel(cardLayout); // Panel that uses CardLayout
	private JTable tableArea; // Display book information
	private SerieCourteTableModel tableModel;
	GestionnaireSerieCourte gestionnaireSerieCourte;
	private JTextField searchTitleField;

	public PanelSerieCourte(GestionnaireSerieCourte gestionnaireSerieCourte, SerieCourteFrame serieCourteFrame) {
		this.gestionnaireSerieCourte = gestionnaireSerieCourte;
		this.serieCourteFrame = serieCourteFrame;
		initializeUI();
	}

	private void initializeUI() {
		setLayout(new BorderLayout());
		add(cards, BorderLayout.CENTER);

		JPanel serieCourteGrid = createSerieCourtePanel();

		add(serieCourteGrid);
	}

	private JPanel createSerieCourtePanel() {
		JPanel panel = new JPanel(new BorderLayout());

		// Top panel for search
		JPanel topPanel = new JPanel();
		searchTitleField = new JTextField(20);

		JButton searchButton = ButtonEditor.createButton(Language.getBundle().getString("serieCourte.btnSearch"), new Color(70, 130, 180));
		searchButton.addActionListener(this::searchSerieCourte);

		JButton addSerieCourteButton = ButtonEditor.createButton(Language.getBundle().getString("serieCourte.ajouterSerieCourte"), new Color(70, 130, 180));

		JLabel titleLabel = new JLabel(Language.getBundle().getString("filtre.titre"));
		titleLabel.setForeground(Color.WHITE);
		topPanel.add(titleLabel);

		topPanel.add(searchTitleField);
		topPanel.add(searchButton);
		topPanel.add(addSerieCourteButton);

		addSerieCourteButton.addActionListener(e -> addSerieCourte());

		// Get JScrollPane from showAllSerieCourte method
		JScrollPane scrollPane = showAllSerieCourte();

		topPanel.setBackground(new Color(50, 50, 50));
		scrollPane.setBackground(new Color(50, 50, 50));
		panel.add(topPanel, BorderLayout.NORTH);
		panel.add(scrollPane, BorderLayout.CENTER);

		JButton btnBack = ButtonEditor.createButton(Language.getBundle().getString("app.retour"), new Color(70, 130, 180));
		btnBack.addActionListener(e -> backMenu());

		JPanel bottomPanel = new JPanel();
		bottomPanel.setBackground(new Color(50, 50, 50));
		bottomPanel.add(btnBack);
		panel.add(bottomPanel, BorderLayout.SOUTH);

		return panel;
	}

	private JScrollPane showAllSerieCourte() {
		java.util.List<SerieCourte> listSerieCourte = gestionnaireSerieCourte.getSerieCourte();
		tableModel = new SerieCourteTableModel(listSerieCourte);
		tableArea = new JTable(tableModel);

		tableArea.setFillsViewportHeight(true);
		tableArea.setRowHeight(28);
		tableArea.setIntercellSpacing(new Dimension(1, 1));
		tableArea.setShowGrid(true);
		tableArea.setGridColor(new Color(100, 100, 100));
		tableArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		tableArea.setForeground(Color.WHITE);
		tableArea.setBackground(new Color(60, 63, 65));
		tableArea.setSelectionBackground(new Color(96, 99, 102));
		tableArea.setSelectionForeground(Color.WHITE);
		tableArea.getTableHeader().setReorderingAllowed(false);
		tableArea.getTableHeader().setBackground(new Color(80, 80, 80));
		tableArea.getTableHeader().setForeground(Color.WHITE);
		tableArea.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		for (int i = 0; i < tableArea.getColumnCount(); i++) {
			String columnName = tableArea.getColumnName(i);
			if (!columnName.equals(Language.getBundle().getString("app.visualiser")) && !columnName.equals(Language.getBundle().getString("app.modifier")) && !columnName.equals(Language.getBundle().getString("app.supprimer"))) {
				tableArea.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
			}
		}

		tableArea.getColumn(Language.getBundle().getString("app.visualiser")).setCellRenderer(new ButtonRenderer());
		tableArea.getColumn(Language.getBundle().getString("app.visualiser")).setCellEditor(new ButtonEditor(this));

		tableArea.getColumn(Language.getBundle().getString("app.modifier")).setCellRenderer(new ButtonRenderer());
		tableArea.getColumn(Language.getBundle().getString("app.modifier")).setCellEditor(new ButtonEditor(this));

		tableArea.getColumn(Language.getBundle().getString("app.supprimer")).setCellRenderer(new ButtonRenderer());
		tableArea.getColumn(Language.getBundle().getString("app.supprimer")).setCellEditor(new ButtonEditor(this));

		JScrollPane scrollPane = new JScrollPane(tableArea);
		scrollPane.getViewport().setBackground(new Color(60, 63, 65));
		scrollPane.setBorder(BorderFactory.createEmptyBorder());

		return scrollPane;
	}

	private void searchSerieCourte(ActionEvent e) {
		String titre = searchTitleField.getText();
		java.util.List<SerieCourte> result = gestionnaireSerieCourte.searchSerieCourte(titre);
		if (result.isEmpty()) {
			JOptionPane.showMessageDialog(this, Language.getBundle().getString("erreur.erreurPasSerieTrouveTitre"), Language.getBundle().getString("app.erreur"), JOptionPane.ERROR_MESSAGE);
		} else {
			tableModel.setSerieCourte(result); // Mettre à jour le modèle du tableau
		}
	}

	private void addSerieCourte() {
		final File[] selectedPosterFile = {null}; // Pour stocker temporairement le fichier choisi

		JTextField titleField = new JTextField();

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
		java.util.List<JCheckBox> genreCheckBoxes = new ArrayList<>();
		for (Genre genre : genres) {
			JCheckBox checkBox = new JCheckBox(genre.getName());
			genreCheckBoxes.add(checkBox);
			genrePanel.add(checkBox);
		}
		JScrollPane scrollPaneGenre = new JScrollPane(genrePanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		JTextField nbSaisonField = new JTextField();
		((AbstractDocument) nbSaisonField.getDocument()).setDocumentFilter(new NumericDocumentFilter());

		JTextField nbEpisodeField = new JTextField();
		((AbstractDocument) nbEpisodeField.getDocument()).setDocumentFilter(new NumericDocumentFilter());

		JTextField dureeMoyenneField = new JTextField();
		((AbstractDocument) dureeMoyenneField.getDocument()).setDocumentFilter(new NumericDocumentFilter());

		//Date première saison
		UtilDateModel model = new UtilDateModel();
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		datePicker.setPreferredSize(new Dimension(120, 25));

		//Date dernière saison
		UtilDateModel model2 = new UtilDateModel();
		Properties p2 = new Properties();
		p2.put("text.today", "Today");
		p2.put("text.month", "Month");
		p2.put("text.year", "Year");
		JDatePanelImpl datePanel2 = new JDatePanelImpl(model2, p2);
		JDatePickerImpl datePicker2 = new JDatePickerImpl(datePanel2, new DateLabelFormatter());
		datePicker2.setPreferredSize(new Dimension(120, 25));


		ArrayList<Platform> platforms = DataManager.loadPlatform();
		JPanel platformPanel = new JPanel(new GridLayout(0, 4, 4, 4));
		java.util.List<JCheckBox> platformCheckBoxes = new ArrayList<>();
		for (Platform platform : platforms) {
			JCheckBox checkBox = new JCheckBox(platform.getName());
			platformCheckBoxes.add(checkBox);
			platformPanel.add(checkBox);
		}
		JScrollPane scrollPanePlatform = new JScrollPane(platformPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		// Créer les boutons radio pour "Déjà vu" et "Pas encore vu"
		JRadioButton dejaVuButton = new JRadioButton(Language.getBundle().getString("app.oui"));
		JRadioButton pasEncoreVuButton = new JRadioButton(Language.getBundle().getString("app.non"), true);
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

		JButton chooseImageButton = new JButton(Language.getBundle().getString("affiche.choisirAffiche"));
		JLabel imagePathLabel = new JLabel(Language.getBundle().getString("affiche.aucuneImage"));

		chooseImageButton.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle(Language.getBundle().getString("affiche.choisirNouvelleAffiche"));

			// ✅ Filtrer les fichiers pour n'afficher que les images
			FileNameExtensionFilter imageFilter = new FileNameExtensionFilter(
					Language.getBundle().getString("affiche.imageExtension"), "jpg", "jpeg", "png"
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
						if (original == null) throw new IOException(Language.getBundle().getString("erreur.imageInvalide"));

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
								Language.getBundle().getString("erreur.erreurImageInvalide") + selectedFile.getName(),
								Language.getBundle().getString("erreur.imageInvalide"), JOptionPane.ERROR_MESSAGE);
						selectedPosterFile[0] = null;
						imagePathLabel.setText(Language.getBundle().getString("affiche.aucuneImage"));
						ex.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null,
							Language.getBundle().getString("erreur.erreurFormatAffiche"),
							Language.getBundle().getString("erreur.erreurFormat"), JOptionPane.ERROR_MESSAGE);
					selectedPosterFile[0] = null;
					imagePathLabel.setText(Language.getBundle().getString("affiche.aucuneImage"));
				}
			}
		});

		final JComponent[] inputs = new JComponent[] {
				new JLabel(Language.getBundle().getString("carac.titre")),
				titleField,
				new JLabel(Language.getBundle().getString("carac.acteurs")),
				actorComboBox,
				selectedActorsPanel,
				new JLabel(Language.getBundle().getString("carac.description")),
				descriptionField,
				new JLabel(Language.getBundle().getString("carac.genres")),
				scrollPaneGenre,
				new JLabel(Language.getBundle().getString("carac.nombreSaison")),
				nbSaisonField,
				new JLabel(Language.getBundle().getString("carac.nombreEpisode")),
				nbEpisodeField,
				new JLabel(Language.getBundle().getString("carac.dureeMoyenne")),
				dureeMoyenneField,
				new JLabel(Language.getBundle().getString("carac.dateSortieSaison1")),
				datePicker,
				new JLabel(Language.getBundle().getString("carac.dateSortieSaison2")),
				datePicker2,
				new JLabel(Language.getBundle().getString("carac.plateforme")),
				scrollPanePlatform,
				new JLabel(Language.getBundle().getString("carac.dejaVu")),
				vuPanel,
				new JLabel(Language.getBundle().getString("carac.ajoutePar")),
				addByComboBox,
				new JLabel(Language.getBundle().getString("carac.affiche")),
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
				Language.getBundle().getString("serieCourte.ajouterSerieCourte"),
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE
		);

		if (result == JOptionPane.OK_OPTION) {
			try {
				String titre = titleField.getText();

				if(titre.isEmpty()) {
					JOptionPane.showMessageDialog(this, Language.getBundle().getString("erreur.erreurTitreVide"), Language.getBundle().getString("erreur.titreVide"), JOptionPane.ERROR_MESSAGE);
				}
				else {
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

					int nbSaison = 0; // Valeur par défaut
					if (!nbSaisonField.getText().isEmpty()) {
						nbSaison = Integer.parseInt(nbSaisonField.getText());
					}

					int nbEpisode = 0; // Valeur par défaut
					if (!nbEpisodeField.getText().isEmpty()) {
						nbEpisode = Integer.parseInt(nbEpisodeField.getText());
					}

					int dureeMoyenne = 0; // Valeur par défaut
					if (!dureeMoyenneField.getText().isEmpty()) {
						dureeMoyenne = Integer.parseInt(dureeMoyenneField.getText());
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
						File destinationDir = new File("DATA/posters/shortSerie");
						if (!destinationDir.exists()) destinationDir.mkdirs();

						// Pour éviter les doublons, tu peux renommer selon le titre du film
						String fileExtension = selectedPosterFile[0].getName().substring(selectedPosterFile[0].getName().lastIndexOf('.'));
						File destinationFile = new File(destinationDir, titre.replaceAll("\\s+", "_") + fileExtension);

						try {
							Files.copy(selectedPosterFile[0].toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
							imagePath = destinationFile.getPath();
						} catch (IOException e) {
							JOptionPane.showMessageDialog(this, Language.getBundle().getString("erreur.erreurCopieImage") + e.getMessage(), Language.getBundle().getString("erreur.image"), JOptionPane.ERROR_MESSAGE);
						}
					}

					boolean canBeAdd = true;

					java.util.List<SerieCourte> listSerieCourte = gestionnaireSerieCourte.getSerieCourte();
					for (SerieCourte serieCourte : listSerieCourte) {
						if (serieCourte.getTitre().equalsIgnoreCase(titre)) {
							canBeAdd = false;
							if (Objects.equals(serieCourte.getAddBy().getName(), "Tous") || serieCourte.getAddBy() == addBy) {
								JOptionPane.showMessageDialog(this, Language.getBundle().getString("erreur.erreurDoublonPartie1Serie") + titre + Language.getBundle().getString("erreur.erreurDoublonPartie2Feminin"), Language.getBundle().getString("erreur.doublon"), JOptionPane.ERROR_MESSAGE);
							} else {
								gestionnaireSerieCourte.updateSerieCourteAddBy(serieCourte, new User("Tous"));
								JOptionPane.showMessageDialog(this, Language.getBundle().getString("erreur.erreurDejaAjoutePartie1Serie") + titre + Language.getBundle().getString("erreur.erreurDejaAjoutePartie2Feminin"), Language.getBundle().getString("erreur.dejaAjouteSerie"), JOptionPane.INFORMATION_MESSAGE);
							}
							break;
						}
					}

					if (canBeAdd) {
						SerieCourte newSerieCourte = new SerieCourte(titre, actorsArray, desc, genresArray, nbSaison, nbEpisode, dureeMoyenne, dateSortie, dateSortie2, platformsArray, dejaVu, addBy, imagePath);
						gestionnaireSerieCourte.addSerieCourte(newSerieCourte); // Ajouter la serie courte à votre gestionnaire de series
						JOptionPane.showMessageDialog(this, Language.getBundle().getString("serie.annonceSerieAjoute") + titre, Language.getBundle().getString("serie.serieAjoute"), JOptionPane.INFORMATION_MESSAGE);
					}
				}

				// Mettre à jour le modèle de tableau après l'ajout de la serie courte
				java.util.List<SerieCourte> updatedSerieCourte = gestionnaireSerieCourte.getSerieCourte(); // Récupérer la liste mise à jour des series
				tableModel.setSerieCourte(updatedSerieCourte); // Mettre à jour le modèle du tableau

			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, Language.getBundle().getString("erreur.erreurFormatDate"), Language.getBundle().getString("erreur.erreurFormat"), JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void backMenu() {
		serieCourteFrame.dispose();
		new SerieCourteFrame();
	}

	public void deleteSerieCourte(SerieCourte serieCourte) {
		gestionnaireSerieCourte.deleteSerieCourte(serieCourte);

		java.util.List<SerieCourte> updatedSerieCourte = gestionnaireSerieCourte.getSerieCourte(); // Récupérer la liste mise à jour des series
		tableModel.setSerieCourte(updatedSerieCourte); // Mettre à jour le modèle du tableau
	}

	public void editSerieCourte(SerieCourte serieCourte) {
		final File[] selectedPosterFile = {null};
		String oldImagePath = serieCourte.getImagePath();

		String oldTitle = serieCourte.getTitre();

		JTextField titleField = new JTextField(serieCourte.getTitre());
		titleField.setEnabled(false);

		ArrayList<Actor> allActors = DataManager.loadActor(); // Tous les acteurs
		DefaultComboBoxModel<Actor> actorComboModel = new DefaultComboBoxModel<>();
		for (Actor actor : allActors) {
			actorComboModel.addElement(actor);
		}
		JComboBox<Actor> actorComboBox = new JComboBox<>(actorComboModel);

		JPanel selectedActorsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		selectedActorsPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		Actor[] actorSerieCourte = serieCourte.getActeur();
		List<Actor> selectedActors;
		if (actorSerieCourte != null) {
			selectedActors = new ArrayList<>(List.of(actorSerieCourte));
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

		JTextField descriptionField = new JTextField(serieCourte.getDescription());

		ArrayList<Genre> genres = DataManager.loadGenre();
		JPanel genrePanel = new JPanel(new GridLayout(0, 4, 4, 4));
		java.util.List<JCheckBox> genreCheckBoxes = new ArrayList<>();
		for (Genre genre : genres) {
			JCheckBox checkBox = new JCheckBox(genre.getName());
			if(serieCourte.getGenre() != null && check(serieCourte.getGenre(), genre.getName())) {
				checkBox.setSelected(true);
			}
			genreCheckBoxes.add(checkBox);
			genrePanel.add(checkBox);
		}
		JScrollPane scrollPaneGenre = new JScrollPane(genrePanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);


		JTextField nbSaisonField = new JTextField();
		((AbstractDocument) nbSaisonField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
		nbSaisonField.setText(String.valueOf(serieCourte.getNombreSaison())); // Préremplir avec le nombre de saisons

		JTextField nbEpisodeField = new JTextField();
		((AbstractDocument) nbEpisodeField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
		nbEpisodeField.setText(String.valueOf(serieCourte.getNombreEpisode())); // Préremplir avec le nombre d'épisodes par saison

		JTextField dureeMoyenneField = new JTextField();
		((AbstractDocument) dureeMoyenneField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
		dureeMoyenneField.setText(String.valueOf(serieCourte.getDureeMoyenne())); // Préremplir avec la durée moyenne des épisodes

		UtilDateModel model = new UtilDateModel();
		Date oldDateSortie = serieCourte.getDateSortiePremiereSaison(); // Supposons que serie.getDateSortiePremiereSaison() renvoie la date de sortie de la première saison

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

		UtilDateModel model2 = new UtilDateModel();
		Date oldDateSortie2 = serieCourte.getDateSortieDerniereSaison(); // Supposons que serie.getDateSortieDerniereSaison() renvoie la date de sortie de la dernière saison

		if (oldDateSortie2 != null) {
			model2.setValue(oldDateSortie2);
		}

		Properties p2 = new Properties();
		p2.put("text.today", "Today");
		p2.put("text.month", "Month");
		p2.put("text.year", "Year");
		JDatePanelImpl datePanel2 = new JDatePanelImpl(model2, p2);
		JDatePickerImpl datePicker2 = new JDatePickerImpl(datePanel2, new DateLabelFormatter());
		datePicker2.setPreferredSize(new Dimension(120, 25));

		ArrayList<Platform> platforms = DataManager.loadPlatform();
		JPanel platformPanel = new JPanel(new GridLayout(0, 4, 4, 4));
		java.util.List<JCheckBox> platformCheckBoxes = new ArrayList<>();
		for (Platform platform : platforms) {
			JCheckBox checkBox = new JCheckBox(platform.getName());
			if(serieCourte.getPlateforme() != null && check(serieCourte.getPlateforme(), platform.getName())) {
				checkBox.setSelected(true);
			}
			platformCheckBoxes.add(checkBox);
			platformPanel.add(checkBox);
		}
		JScrollPane scrollPanePlatform = new JScrollPane(platformPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		// Créer les boutons radio pour "Déjà vu" et "Pas encore vu"
		JRadioButton dejaVuButton = new JRadioButton(Language.getBundle().getString("filtre.dejaVu"));
		JRadioButton pasEncoreVuButton = new JRadioButton(Language.getBundle().getString("filtre.pasEncoreVu"));
		ButtonGroup vuGroup = new ButtonGroup();
		vuGroup.add(dejaVuButton);
		vuGroup.add(pasEncoreVuButton);

		// Sélectionner le bouton approprié en fonction de l'état actuel de la série courte
		if (serieCourte.getDejaVu()) {
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
			if(Objects.equals(user.getName(), serieCourte.getAddBy().getName())) {
				addByModel.addElement(serieCourte.getAddBy());
			} else {
				addByModel.addElement(user);
			}
		}
		JComboBox<Object> addByComboBox = new JComboBox<>(addByModel);
		addByComboBox.setSelectedItem(serieCourte.getAddBy());

		JButton chooseImageButton = new JButton(Language.getBundle().getString("affiche.changerAffiche"));
		JLabel imagePathLabel = new JLabel((oldImagePath != null) ? new File(oldImagePath).getName() : Language.getBundle().getString("affiche.aucuneImage"));

		chooseImageButton.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle(Language.getBundle().getString("affiche.choisirNouvelleAffiche"));

			// ✅ Filtrer les fichiers pour n'afficher que les images
			FileNameExtensionFilter imageFilter = new FileNameExtensionFilter(
					Language.getBundle().getString("affiche.imageExtension"), "jpg", "jpeg", "png"
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
						if (original == null) throw new IOException(Language.getBundle().getString("erreur.imageInvalide"));

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
								Language.getBundle().getString("erreur.erreurImageInvalide") + selectedFile.getName(),
								Language.getBundle().getString("erreur.imageInvalide"), JOptionPane.ERROR_MESSAGE);
						selectedPosterFile[0] = null;
						imagePathLabel.setText(Language.getBundle().getString("affiche.aucuneImage"));
						ex.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null,
							Language.getBundle().getString("erreur.erreurFormatAffiche"),
							Language.getBundle().getString("erreur.erreurFormat"), JOptionPane.ERROR_MESSAGE);
					selectedPosterFile[0] = null;
					imagePathLabel.setText(Language.getBundle().getString("affiche.aucuneImage"));
				}
			}
		});

		final JComponent[] inputs = new JComponent[] {
				new JLabel(Language.getBundle().getString("carac.titre")),
				titleField,
				new JLabel(Language.getBundle().getString("carac.acteurs")),
				actorComboBox,
				selectedActorsPanel,
				new JLabel(Language.getBundle().getString("carac.description")),
				descriptionField,
				new JLabel(Language.getBundle().getString("carac.genres")),
				scrollPaneGenre,
				new JLabel(Language.getBundle().getString("carac.nombreSaison")),
				nbSaisonField,
				new JLabel(Language.getBundle().getString("carac.nombreEpisode")),
				nbEpisodeField,
				new JLabel(Language.getBundle().getString("carac.dureeMoyenne")),
				dureeMoyenneField,
				new JLabel(Language.getBundle().getString("carac.dateSortieSaison1")),
				datePicker,
				new JLabel(Language.getBundle().getString("carac.dateSortieSaison2")),
				datePicker2,
				new JLabel(Language.getBundle().getString("carac.plateforme")),
				scrollPanePlatform,
				new JLabel(Language.getBundle().getString("carac.dejaVu")),
				vuPanel,
				new JLabel(Language.getBundle().getString("carac.ajoutePar")),
				addByComboBox,
				new JLabel(Language.getBundle().getString("carac.affiche")),
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
				Language.getBundle().getString("serieCourte.modifierSerieCourte"),
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE
		);

		if (result == JOptionPane.OK_OPTION) {
			try {
				String titre = titleField.getText();

				if(titre.isEmpty()) {
					JOptionPane.showMessageDialog(this, Language.getBundle().getString("erreur.erreurTitreVide"), Language.getBundle().getString("erreur.titreVide"), JOptionPane.ERROR_MESSAGE);
				}
				else {
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

					int nbSaison = 0; // Valeur par défaut
					if (!nbSaisonField.getText().isEmpty()) {
						nbSaison = Integer.parseInt(nbSaisonField.getText());
					}

					int nbEpisode = 0; // Valeur par défaut
					if (!nbEpisodeField.getText().isEmpty()) {
						nbEpisode = Integer.parseInt(nbEpisodeField.getText());
					}


					int dureeMoyenne = 0; // Valeur par défaut
					if (!dureeMoyenneField.getText().isEmpty()) {
						dureeMoyenne = Integer.parseInt(dureeMoyenneField.getText());
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
						File destinationDir = new File("DATA/posters/shortSerie");
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
							JOptionPane.showMessageDialog(this, Language.getBundle().getString("erreur.erreurCopieImage") + e.getMessage(), Language.getBundle().getString("erreur.image"), JOptionPane.ERROR_MESSAGE);
						}
					}

					SerieCourte newSerieCourte = new SerieCourte(titre, actorsArray, desc, genresArray, nbSaison, nbEpisode, dureeMoyenne, dateSortie, dateSortie2, platformsArray, dejaVu, addBy, imagePath);
					gestionnaireSerieCourte.editSerieCourte(oldTitle, newSerieCourte); // Ajouter la serie courte à votre gestionnaire de séries
				}

				// Mettre à jour le modèle de tableau après l'ajout de la serie courte
				List<SerieCourte> updatedSerieCourte = gestionnaireSerieCourte.getSerieCourte(); // Récupérer la liste mise à jour des séries
				tableModel.setSerieCourte(updatedSerieCourte); // Mettre à jour le modèle du tableau

				JOptionPane.showMessageDialog(this, Language.getBundle().getString("serie.annonceSerieModifie") + titre, Language.getBundle().getString("serie.serieModifie"), JOptionPane.INFORMATION_MESSAGE);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, Language.getBundle().getString("erreur.erreurFormatDate"), Language.getBundle().getString("erreur.erreurFormat"), JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	public void detailsSerieCourte(SerieCourte serieCourte) {
		StringBuilder acteurs = new StringBuilder();
		if(serieCourte.getActeur() != null) {
			serieCourte.getActeur();
			for (Actor actor : serieCourte.getActeur()) {
				acteurs.append(actor.getName()).append(", ");
			}
		}

		StringBuilder genres = new StringBuilder();
		if(serieCourte.getGenre() != null) {
			serieCourte.getGenre();
			for (Genre genre : serieCourte.getGenre()) {
				genres.append(genre.getName()).append(", ");
			}
		}

		StringBuilder plateforme = new StringBuilder();
		if(serieCourte.getPlateforme() != null) {
			serieCourte.getPlateforme();
			for (Platform platform : serieCourte.getPlateforme()) {
				plateforme.append(platform.getName()).append(", ");
			}
		}

		// Affiche ou texte de remplacement
		JLabel imageLabel = new JLabel();
		String imagePath = serieCourte.getImagePath();

		if (imagePath != null && new File(imagePath).exists()) {
			imageLabel.setIcon(resizeImage(imagePath, 150, 200));
		} else {
			imageLabel.setText(Language.getBundle().getString("affiche.aucuneAfficheDisponible"));
			imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		}

		// Style HTML pour les titres
		String titreStyle = "<html><span style='font-family:Arial; font-size:14pt; font-weight:bold; text-decoration: underline;'>";

		final JComponent[] inputs = new JComponent[] {
				imageLabel,

				new JLabel(titreStyle + Language.getBundle().getString("filtre.titre") + "</span></html>"),
				new JLabel(serieCourte.getTitre()),
				new JSeparator(SwingConstants.HORIZONTAL),

				new JLabel(titreStyle + Language.getBundle().getString("filtre.acteur") + "</span></html>"),
				new JLabel("<html>" + acteurs.toString().replaceAll(", ", "<br>") + "</html>"),
				new JSeparator(SwingConstants.HORIZONTAL),

				new JLabel(titreStyle + Language.getBundle().getString("filtre.description") + "</span></html>"),
				new JLabel("<html><div style='width:300px'>" + serieCourte.getDescription() + "</div></html>"),
				new JSeparator(SwingConstants.HORIZONTAL),

				new JLabel(titreStyle + Language.getBundle().getString("filtre.genre") + "</span></html>"),
				new JLabel("<html>" + genres.toString().replaceAll(", ", "<br>") + "</html>"),
				new JSeparator(SwingConstants.HORIZONTAL),

				new JLabel(titreStyle + Language.getBundle().getString("filtre.nombreSaison") + "</span></html>"),
				new JLabel(String.valueOf(serieCourte.getNombreSaison())),
				new JSeparator(SwingConstants.HORIZONTAL),

				new JLabel(titreStyle + Language.getBundle().getString("filtre.nombreEpisode") + "</span></html>"),
				new JLabel(String.valueOf(serieCourte.getNombreEpisode())),
				new JSeparator(SwingConstants.HORIZONTAL),

				new JLabel(titreStyle + Language.getBundle().getString("filtre.dureeEpisode") + "</span></html>"),
				new JLabel(serieCourte.getDureeMoyenne() + Language.getBundle().getString("filtre.min")),
				new JSeparator(SwingConstants.HORIZONTAL),

				new JLabel(titreStyle + Language.getBundle().getString("filtre.dateSortieSaison1") + "</span></html>"),
				new JLabel(String.valueOf(serieCourte.getDateSortiePremiereSaison())),
				new JSeparator(SwingConstants.HORIZONTAL),

				new JLabel(titreStyle + Language.getBundle().getString("filtre.dateSortieSaison2") + "</span></html>"),
				new JLabel(String.valueOf(serieCourte.getDateSortieDerniereSaison())),
				new JSeparator(SwingConstants.HORIZONTAL),

				new JLabel(titreStyle + Language.getBundle().getString("filtre.plateforme") + "</span></html>"),
				new JLabel("<html>" + plateforme.toString().replaceAll(", ", "<br>") + "</html>"),
				new JSeparator(SwingConstants.HORIZONTAL),

				new JLabel(titreStyle + Language.getBundle().getString("filtre.dejaVu") + "</span></html>"),
				new JLabel(serieCourte.getDejaVu() ? Language.getBundle().getString("app.oui") : Language.getBundle().getString("app.non")),
				new JSeparator(SwingConstants.HORIZONTAL),

				new JLabel(titreStyle + Language.getBundle().getString("filtre.ajoutePar") + "</span></html>"),
				new JLabel(serieCourte.getAddBy().getName())
		};

		JOptionPane.showMessageDialog(this, inputs, Language.getBundle().getString("serieCourte.detailSerieCourte"), JOptionPane.PLAIN_MESSAGE);
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

	public GestionnaireSerieCourte getGestionnaire() {
		return gestionnaireSerieCourte;
	}

	private ImageIcon resizeImage(String path, int width, int height) {
		ImageIcon icon = new ImageIcon(path);
		Image img = icon.getImage();
		Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return new ImageIcon(newImg);
	}
}
