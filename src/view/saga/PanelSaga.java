package view.saga;

import model.*;
import model.saga.SagaTableModel;
import model.saga.GestionnaireSaga;
import model.saga.Saga;
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

public class PanelSaga extends JPanel{

	SagaFrame sagaFrame;

	private CardLayout cardLayout = new CardLayout();
	private JPanel cards = new JPanel(cardLayout); // Panel that uses CardLayout
	private JTable tableArea; // Display book information
	private SagaTableModel tableModel;
	GestionnaireSaga gestionnaireSaga;
	private JTextField searchTitleField;
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

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

		JButton searchButton = createButton("Rechercher Saga", new Color(70, 130, 180));
		searchButton.addActionListener(this::searchSaga);

		JButton addSagaButton = createButton("Ajouter une saga", new Color(70, 130, 180));

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

		JButton btnBack = createButton("Retour", new Color(70, 130, 180));
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

		// Ajout de rendus personnalisés pour les colonnes "Modifier" et "Supprimer"
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
		JTextField titleField = new JTextField();
		JTextField reaField = new JTextField();
		JTextField descriptionField = new JTextField();

		Genre[] genres = Genre.values();
		JPanel genrePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		java.util.List<JCheckBox> genreCheckBoxes = new ArrayList<>();
		for (Genre genre : genres) {
			JCheckBox checkBox = new JCheckBox(genre.name());
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


		Plateforme[] platforms = Plateforme.values();
		JPanel platformPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		java.util.List<JCheckBox> platformCheckBoxes = new ArrayList<>();
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
				addByComboBox
		};

		int result = JOptionPane.showConfirmDialog(this, inputs, "Ajouter une nouvelle saga", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					java.util.List<Genre> selectedGenres = new ArrayList<>();
					for (JCheckBox checkBox : genreCheckBoxes) {
						if (checkBox.isSelected()) {
							selectedGenres.add(Genre.valueOf(checkBox.getText()));
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
					java.util.List<Plateforme> selectedPlatforms = new ArrayList<>();
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

					java.util.List<Saga> listSaga = gestionnaireSaga.getSaga();
					for (int i = 0; i < listSaga.size(); i++) {
						Saga saga = listSaga.get(i);
						if (saga.getTitre().equalsIgnoreCase(titre)) {
							canBeAdd = false;
							if (saga.getAddBy() == Utilisateur.Nous2 || saga.getAddBy() == addBy) {
								JOptionPane.showMessageDialog(this, "Erreur: La saga " + titre + " a déjà été ajoutée", "Erreur doublons", JOptionPane.ERROR_MESSAGE);
							} else {
								gestionnaireSaga.updateSagaAddBy(saga, Utilisateur.Nous2);
								JOptionPane.showMessageDialog(this, "La saga " + titre + " a déjà été ajouté par un autre utilisateur son attribut de personne qui a ajoutée passe donc à Nous2.", "Erreur saga déjà ajoutée par un utilisateur", JOptionPane.INFORMATION_MESSAGE);
							}
							break;
						}
					}

					if (canBeAdd) {
						Saga newSaga = new Saga(titre, rea, desc, genresArray, nbFilm, dateSortie, dateSortie2, platformsArray, dejaVu, addBy);
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

		String oldTitle = saga.getTitre();

		JTextField titleField = new JTextField(saga.getTitre());
		titleField.setEnabled(false);
		JTextField reaField = new JTextField(saga.getRealistateur());
		JTextField descriptionField = new JTextField(saga.getDescription());

		Genre[] genres = Genre.values();
		JPanel genrePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		java.util.List<JCheckBox> genreCheckBoxes = new ArrayList<>();
		for (Genre genre : genres) {
			JCheckBox checkBox = new JCheckBox(genre.name());
			if(saga.getGenre() != null && check(saga.getGenre(), genre.name())) {
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

		Plateforme[] platforms = Plateforme.values();
		JPanel platformPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		java.util.List<JCheckBox> platformCheckBoxes = new ArrayList<>();
		for (Plateforme platform : platforms) {
			JCheckBox checkBox = new JCheckBox(platform.name());
			if(saga.getPlateforme() != null && check(saga.getPlateforme(), platform.name())) {
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
		if (saga.getDejaVu()) {
			dejaVuButton.setSelected(true);
		} else {
			pasEncoreVuButton.setSelected(true);
		}

		// Ajouter les boutons radio à un panneau
		JPanel vuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		vuPanel.add(dejaVuButton);
		vuPanel.add(pasEncoreVuButton);

		JComboBox<Utilisateur> addByComboBox = new JComboBox<>(Utilisateur.values());
		addByComboBox.setSelectedItem(saga.getAddBy());

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
				addByComboBox
		};

		int result = JOptionPane.showConfirmDialog(this, inputs, "Modifier une saga", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					java.util.List<Genre> selectedGenres = new ArrayList<>();
					for (JCheckBox checkBox : genreCheckBoxes) {
						if (checkBox.isSelected()) {
							selectedGenres.add(Genre.valueOf(checkBox.getText()));
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
					java.util.List<Plateforme> selectedPlatforms = new ArrayList<>();
					for (JCheckBox checkBox : platformCheckBoxes) {
						if (checkBox.isSelected()) {
							selectedPlatforms.add(Plateforme.valueOf(checkBox.getText()));
						}
					}
					Plateforme[] platformsArray = selectedPlatforms.isEmpty() ? null : selectedPlatforms.toArray(new Plateforme[0]);

					// Get "Déjà vu" status
					boolean dejaVu = dejaVuButton.isSelected();

					Utilisateur addBy = Utilisateur.valueOf(addByComboBox.getSelectedItem().toString());

					Saga newSaga = new Saga(titre, rea, desc, genresArray, nbFilm, dateSortie, dateSortie2, platformsArray, dejaVu, addBy);
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

	public GestionnaireSaga getGestionnaire() {
		return gestionnaireSaga;
	}
}
