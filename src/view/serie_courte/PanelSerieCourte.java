package view.serie_courte;

import model.*;
import model.serie.GestionnaireSerie;
import model.serie.Serie;
import model.serie.SerieTableModel;
import model.serie_courte.GestionnaireSerieCourte;
import model.serie_courte.SerieCourte;
import model.serie_courte.SerieCourteTableModel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import view.serie.SerieFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class PanelSerieCourte extends JPanel {

	SerieCourteFrame serieCourteFrame;

	private CardLayout cardLayout = new CardLayout();
	private JPanel cards = new JPanel(cardLayout); // Panel that uses CardLayout
	private JTable tableArea; // Display book information
	private SerieCourteTableModel tableModel;
	GestionnaireSerieCourte gestionnaireSerieCourte;
	private JTextField searchTitleField;
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

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

		JButton searchButton = createButton("Rechercher une série courte", new Color(70, 130, 180));
		searchButton.addActionListener(this::searchSerieCourte);

		JButton addSerieCourteButton = createButton("Ajouter une série courte", new Color(70, 130, 180));

		JLabel titleLabel = new JLabel("Titre : ");
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

		JButton btnBack = createButton("Retour", new Color(70, 130, 180));
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
		tableArea.setBackground(Color.LIGHT_GRAY);

		// Ajout de rendus personnalisés pour les colonnes "Modifier" et "Supprimer"
		tableArea.getColumn("Modifier").setCellRenderer(new ButtonRenderer());
		tableArea.getColumn("Modifier").setCellEditor(new ButtonEditor(this));

		tableArea.getColumn("Supprimer").setCellRenderer(new ButtonRenderer());
		tableArea.getColumn("Supprimer").setCellEditor(new ButtonEditor(this));

		return new JScrollPane(tableArea);
	}

	private void searchSerieCourte(ActionEvent e) {
		String titre = searchTitleField.getText();
		java.util.List<SerieCourte> result = gestionnaireSerieCourte.searchSerieCourte(titre);
		if (result.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Erreur: Aucune série trouvé pour ce titre.", "Erreur", JOptionPane.ERROR_MESSAGE);
		} else {
			tableModel.setSerieCourte(result); // Mettre à jour le modèle du tableau
		}
	}

	private void addSerieCourte() {
		JTextField titleField = new JTextField();
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

		JTextField nbSaisonField = new JTextField();
		JTextField nbEpisodeField = new JTextField();
		JTextField dureeMoyenneField = new JTextField();

		//Date première saison
		UtilDateModel model = new UtilDateModel();
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

		//Date dernière saison
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
				new JLabel("Description"),
				descriptionField,
				new JLabel("Genres"),
				scrollPaneGenre,
				new JLabel("Nombre de saisons"),
				nbSaisonField,
				new JLabel("Nombre d'épisodes par saison"),
				nbEpisodeField,
				new JLabel("Durée moyenne des épisodes"),
				dureeMoyenneField,
				new JLabel("Date de sortie de la première saison"),
				datePicker,
				new JLabel("Date de sortie de la dernière saison"),
				datePicker2,
				new JLabel("Plateforme"),
				scrollPanePlatform,
				new JLabel("Déjà vu"),
				vuPanel,
				new JLabel("Ajouté par"),
				addByComboBox
		};

		int result = JOptionPane.showConfirmDialog(this, inputs, "Ajouter une nouvelle série", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			try {
				String titre = titleField.getText();

				if(titre.isEmpty()) {
					JOptionPane.showMessageDialog(this, "Erreur: Le titre doit être entré.", "Erreur titre vide", JOptionPane.ERROR_MESSAGE);
				}
				else {
					String desc = descriptionField.getText();

					// Get genres
					java.util.List<Genre> selectedGenres = new ArrayList<>();
					for (JCheckBox checkBox : genreCheckBoxes) {
						if (checkBox.isSelected()) {
							selectedGenres.add(Genre.valueOf(checkBox.getText()));
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

					java.util.List<SerieCourte> listSerieCourte = gestionnaireSerieCourte.getSerieCourte();
					for (int i = 0; i < listSerieCourte.size(); i++) {
						SerieCourte serieCourte = listSerieCourte.get(i);
						if (serieCourte.getTitre().equalsIgnoreCase(titre)) {
							canBeAdd = false;
							if (serieCourte.getAddBy() == Utilisateur.Nous2 || serieCourte.getAddBy() == addBy) {
								JOptionPane.showMessageDialog(this, "Erreur: La série " + titre + " a déjà été ajoutée", "Erreur doublons", JOptionPane.ERROR_MESSAGE);
							} else {
								gestionnaireSerieCourte.updateSerieCourteAddBy(serieCourte, Utilisateur.Nous2);
								JOptionPane.showMessageDialog(this, "La série " + titre + " a déjà été ajouté par un autre utilisateur son attribut de personne qui a ajoutée passe donc à Nous2.", "Erreur série déjà ajoutée par un utilisateur", JOptionPane.INFORMATION_MESSAGE);
							}
							break;
						}
					}

					if (canBeAdd) {
						SerieCourte newSerieCourte = new SerieCourte(titre, desc, genresArray, nbSaison, nbEpisode, dureeMoyenne, dateSortie, dateSortie2, platformsArray, dejaVu, addBy);
						gestionnaireSerieCourte.addSerieCourte(newSerieCourte); // Ajouter la serie courte à votre gestionnaire de series
						JOptionPane.showMessageDialog(this, "Série ajoutée avec succès: " + titre, "Série Ajoutée", JOptionPane.INFORMATION_MESSAGE);
					}
				}

				// Mettre à jour le modèle de tableau après l'ajout de la serie courte
				java.util.List<SerieCourte> updatedSerieCourte = gestionnaireSerieCourte.getSerieCourte(); // Récupérer la liste mise à jour des series
				tableModel.setSerieCourte(updatedSerieCourte); // Mettre à jour le modèle du tableau

			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "Erreur: L'année de sortie doit être un nombre valide.", "Erreur de Format", JOptionPane.ERROR_MESSAGE);
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

		String oldTitle = serieCourte.getTitre();

		JTextField titleField = new JTextField(serieCourte.getTitre());
		titleField.setEnabled(false);
		JTextField descriptionField = new JTextField(serieCourte.getDescription());

		Genre[] genres = Genre.values();
		JPanel genrePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		java.util.List<JCheckBox> genreCheckBoxes = new ArrayList<>();
		for (Genre genre : genres) {
			JCheckBox checkBox = new JCheckBox(genre.name());
			if(serieCourte.getGenre() != null && check(serieCourte.getGenre(), genre.name())) {
				checkBox.setSelected(true);
			}
			genreCheckBoxes.add(checkBox);
			genrePanel.add(checkBox);
		}
		JScrollPane scrollPaneGenre = new JScrollPane(genrePanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);


		JTextField nbSaisonField = new JTextField();
		nbSaisonField.setText(String.valueOf(serieCourte.getNombreSaison())); // Préremplir avec le nombre de saisons

		JTextField nbEpisodeField = new JTextField();
		nbEpisodeField.setText(String.valueOf(serieCourte.getNombreEpisode())); // Préremplir avec le nombre d'épisodes par saison

		JTextField dureeMoyenneField = new JTextField();
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

		Plateforme[] platforms = Plateforme.values();
		JPanel platformPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		java.util.List<JCheckBox> platformCheckBoxes = new ArrayList<>();
		for (Plateforme platform : platforms) {
			JCheckBox checkBox = new JCheckBox(platform.name());
			if(serieCourte.getPlateforme() != null && check(serieCourte.getPlateforme(), platform.name())) {
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
		if (serieCourte.getDejaVu()) {
			dejaVuButton.setSelected(true);
		} else {
			pasEncoreVuButton.setSelected(true);
		}

		// Ajouter les boutons radio à un panneau
		JPanel vuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		vuPanel.add(dejaVuButton);
		vuPanel.add(pasEncoreVuButton);

		JComboBox<Utilisateur> addByComboBox = new JComboBox<>(Utilisateur.values());
		addByComboBox.setSelectedItem(serieCourte.getAddBy());

		final JComponent[] inputs = new JComponent[] {
				new JLabel("Titre*"),
				titleField,
				new JLabel("Description"),
				descriptionField,
				new JLabel("Genres"),
				scrollPaneGenre,
				new JLabel("Nombre de saisons"),
				nbSaisonField,
				new JLabel("Nombre d'épisodes par saison"),
				nbEpisodeField,
				new JLabel("Durée moyenne des épisodes"),
				dureeMoyenneField,
				new JLabel("Date de sortie de la première saison"),
				datePicker,
				new JLabel("Date de sortie de la dernière saison"),
				datePicker2,
				new JLabel("Plateforme"),
				scrollPanePlatform,
				new JLabel("Déjà vu"),
				vuPanel,
				new JLabel("Ajouté par"),
				addByComboBox
		};

		int result = JOptionPane.showConfirmDialog(this, inputs, "Modifier une série courte", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			try {
				String titre = titleField.getText();

				if(titre.isEmpty()) {
					JOptionPane.showMessageDialog(this, "Erreur: Le titre doit être entré.", "Erreur titre vide", JOptionPane.ERROR_MESSAGE);
				}
				else {
					String desc = descriptionField.getText();

					// Get genres
					java.util.List<Genre> selectedGenres = new ArrayList<>();
					for (JCheckBox checkBox : genreCheckBoxes) {
						if (checkBox.isSelected()) {
							selectedGenres.add(Genre.valueOf(checkBox.getText()));
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

					SerieCourte newSerieCourte = new SerieCourte(titre, desc, genresArray, nbSaison, nbEpisode, dureeMoyenne, dateSortie, dateSortie2, platformsArray, dejaVu, addBy);
					gestionnaireSerieCourte.editSerieCourte(oldTitle, newSerieCourte); // Ajouter la serie courte à votre gestionnaire de séries
				}

				// Mettre à jour le modèle de tableau après l'ajout de la serie courte
				List<SerieCourte> updatedSerieCourte = gestionnaireSerieCourte.getSerieCourte(); // Récupérer la liste mise à jour des séries
				tableModel.setSerieCourte(updatedSerieCourte); // Mettre à jour le modèle du tableau

				JOptionPane.showMessageDialog(this, "Série modifiée avec succès: " + titre, "Série modifiée", JOptionPane.INFORMATION_MESSAGE);
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

	public GestionnaireSerieCourte getGestionnaire() {
		return gestionnaireSerieCourte;
	}
}
