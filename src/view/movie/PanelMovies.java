package view.movie;

import model.*;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class PanelMovies extends JPanel {

	MovieFrame movieFrame;

	private CardLayout cardLayout = new CardLayout();
	private JPanel cards = new JPanel(cardLayout); // Panel that uses CardLayout
	private JTable tableArea; // Display book information
	private MoviesTableModel tableModel;
	Gestionnaire gestionnaire;
	private JTextField searchTitleField;
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	public PanelMovies(Gestionnaire gestionnaire, MovieFrame movieFrame) {
		this.gestionnaire = gestionnaire;
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

		JButton searchButton = createButton("Rechercher Film", new Color(70, 130, 180));
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
		List<Movie> listMovies = gestionnaire.getMovies();
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
		List<Movie> result = gestionnaire.searchMovie(titre);
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

		Caller callerGender = new Caller();
		JScrollPane scrollPaneGender = new JScrollPane(callerGender, JScrollPane.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		JCheckBox actionButton = new JCheckBox("Action");
		callerGender.addItem(actionButton);
		JCheckBox aventureButton = new JCheckBox("Aventure");
		callerGender.addItem(aventureButton);
		JCheckBox anticipationButton = new JCheckBox("Anticipation");
		callerGender.addItem(anticipationButton);
		JCheckBox noelButton = new JCheckBox("Noel");
		callerGender.addItem(noelButton);
		JCheckBox comedieButton = new JCheckBox("Comedie");
		callerGender.addItem(comedieButton);
		JCheckBox drameButton = new JCheckBox("Drame");
		callerGender.addItem(drameButton);
		JCheckBox fantastiqueButton = new JCheckBox("Fantastique");
		callerGender.addItem(fantastiqueButton);
		JCheckBox fantasyButton = new JCheckBox("Fantasy");
		callerGender.addItem(fantasyButton);
		JCheckBox horreurButton = new JCheckBox("Horreur");
		callerGender.addItem(horreurButton);
		JCheckBox historiqueButton = new JCheckBox("Historique");
		callerGender.addItem(historiqueButton);
		JCheckBox SFButton = new JCheckBox("SF");
		callerGender.addItem(SFButton);
		JCheckBox thrillerButton = new JCheckBox("Thriller");
		callerGender.addItem(thrillerButton);
		JCheckBox westernButton = new JCheckBox("Western");
		callerGender.addItem(westernButton);
		JCheckBox otherGenderButton = new JCheckBox("Autre");
		callerGender.addItem(otherGenderButton);

		JTextField dureeField = new JTextField();
		UtilDateModel model = new UtilDateModel();

		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());


		Caller callerPlateforme = new Caller();
		JScrollPane scrollPanePlateforme = new JScrollPane(callerPlateforme, JScrollPane.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		JCheckBox disneyButton = new JCheckBox("Disney");
		callerPlateforme.addItem(disneyButton);
		JCheckBox netflixButton = new JCheckBox("Netflix");
		callerPlateforme.addItem(netflixButton);
		JCheckBox primeButton = new JCheckBox("Prime");
		callerPlateforme.addItem(primeButton);
		JCheckBox canalButton = new JCheckBox("Canal");
		callerPlateforme.addItem(canalButton);
		JCheckBox crunchyrollButton = new JCheckBox("Crunchyroll");
		callerPlateforme.addItem(crunchyrollButton);
		JCheckBox maxButton = new JCheckBox("Max");
		callerPlateforme.addItem(maxButton);
		JCheckBox OCSButton = new JCheckBox("OCS");
		callerPlateforme.addItem(OCSButton);
		JCheckBox ParamountButton = new JCheckBox("Paramount");
		callerPlateforme.addItem(ParamountButton);
		JCheckBox aucuneIdeeButton = new JCheckBox("Aucune Idée");
		callerPlateforme.addItem(aucuneIdeeButton);
		JCheckBox horsLigneButton = new JCheckBox("Hors ligne");
		callerPlateforme.addItem(horsLigneButton);
		JCheckBox illegaleButton = new JCheckBox("Illégale");
		callerPlateforme.addItem(illegaleButton);

		JComboBox<Utilisateur> addByComboBox = new JComboBox<>(Utilisateur.values());

		final JComponent[] inputs = new JComponent[] {
			new JLabel("Titre*"),
			titleField,
			new JLabel("Réalisateur"),
			reaField,
			new JLabel("Description"),
			descriptionField,
			new JLabel("Genres"),
			scrollPaneGender,

			new JLabel("Durée"),
			dureeField,
			new JLabel("Date de sortie"),
			datePicker,
			new JLabel("Plateforme"),
			scrollPanePlateforme,

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

					// Gather selected genres
					java.util.List<Genre> selectedGenres = new java.util.ArrayList<>();
					if (actionButton.isSelected()) selectedGenres.add(Genre.Action);
					if (aventureButton.isSelected()) selectedGenres.add(Genre.Aventure);
					if (anticipationButton.isSelected()) selectedGenres.add(Genre.Anticipation);
					if (noelButton.isSelected()) selectedGenres.add(Genre.Noel);
					if (comedieButton.isSelected()) selectedGenres.add(Genre.Comedie);
					if (drameButton.isSelected()) selectedGenres.add(Genre.Drame);
					if (fantastiqueButton.isSelected()) selectedGenres.add(Genre.Fantastique);
					if (fantasyButton.isSelected()) selectedGenres.add(Genre.Fantasy);
					if (horreurButton.isSelected()) selectedGenres.add(Genre.Horreur);
					if (historiqueButton.isSelected()) selectedGenres.add(Genre.Historique);
					if (SFButton.isSelected()) selectedGenres.add(Genre.SF);
					if (thrillerButton.isSelected()) selectedGenres.add(Genre.Thriller);
					if (westernButton.isSelected()) selectedGenres.add(Genre.Western);
					if (otherGenderButton.isSelected()) selectedGenres.add(Genre.Autre);

					Genre[] genresArray = new Genre[selectedGenres.size()];
					selectedGenres.toArray(genresArray);

					int duree = 0; // Valeur par défaut
					if (!dureeField.getText().isEmpty()) {
						duree = Integer.parseInt(dureeField.getText());
					}

					Date dateSortie = null; // Valeur par défaut
					Object value = datePicker.getModel().getValue();
					if (value instanceof Date) {
						dateSortie = (Date) value;
					}

					// Gather selected platforms
					java.util.List<Plateforme> selectedPlateformes = new java.util.ArrayList<>();
					if (disneyButton.isSelected()) selectedPlateformes.add(Plateforme.Disney);
					if (netflixButton.isSelected()) selectedPlateformes.add(Plateforme.Netflix);
					if (primeButton.isSelected()) selectedPlateformes.add(Plateforme.Prime);
					if (canalButton.isSelected()) selectedPlateformes.add(Plateforme.Canal);
					if (crunchyrollButton.isSelected()) selectedPlateformes.add(Plateforme.Crunchyroll);
					if (maxButton.isSelected()) selectedPlateformes.add(Plateforme.Max);
					if (OCSButton.isSelected()) selectedPlateformes.add(Plateforme.OCS);
					if (ParamountButton.isSelected()) selectedPlateformes.add(Plateforme.Paramount);
					if (aucuneIdeeButton.isSelected()) selectedPlateformes.add(Plateforme.AucuneIdee);
					if (horsLigneButton.isSelected()) selectedPlateformes.add(Plateforme.HorsLigne);
					if (illegaleButton.isSelected()) selectedPlateformes.add(Plateforme.Illegale);

					Plateforme[] plateformesArray = new Plateforme[selectedPlateformes.size()];
					selectedPlateformes.toArray(plateformesArray);

					Utilisateur addBy = Utilisateur.valueOf(addByComboBox.getSelectedItem().toString());

					boolean canBeAdd = true;

					List<Movie> listMovies = gestionnaire.getMovies();
					for (int i = 0; i < listMovies.size(); i++) {
						Movie movie = listMovies.get(i);
						if (movie.getTitre().equalsIgnoreCase(titre)) {
							canBeAdd = false;
							if (movie.getAddBy() == Utilisateur.Nous2 || movie.getAddBy() == addBy) {
								JOptionPane.showMessageDialog(this, "Erreur: Le film " + titre + " a déjà été ajouté", "Erreur doublons", JOptionPane.ERROR_MESSAGE);
							} else {
								gestionnaire.updateMovieAddBy(movie, Utilisateur.Nous2);
								JOptionPane.showMessageDialog(this, "Le film " + titre + " a déjà été ajouté par un autre utilisateur son attribut de personne qui a ajouté passe donc à Nous2.", "Erreur film déjà ajouté par un utilisateur", JOptionPane.INFORMATION_MESSAGE);
							}
							break;
						}
					}

					if (canBeAdd) {
						Movie newMovie = new Movie(titre, rea, desc, genresArray, duree, dateSortie, plateformesArray, addBy);
						gestionnaire.addMovie(newMovie); // Ajouter le film à votre gestionnaire de films
						JOptionPane.showMessageDialog(this, "Film ajouté avec succès: " + titre, "Film Ajouté", JOptionPane.INFORMATION_MESSAGE);
					}
				}

				// Mettre à jour le modèle de tableau après l'ajout du film
				List<Movie> updatedMovies = gestionnaire.getMovies(); // Récupérer la liste mise à jour des films
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
		gestionnaire.deleteMovie(movie);

		List<Movie> updatedMovies = gestionnaire.getMovies(); // Récupérer la liste mise à jour des films
		tableModel.setMovies(updatedMovies); // Mettre à jour le modèle du tableau
	}

	public void editMovie(Movie movie) {

		String oldTitle = movie.getTitre();

		JTextField titleField = new JTextField(movie.getTitre());
		titleField.setEnabled(false);
		JTextField reaField = new JTextField(movie.getRealistateur());
		JTextField descriptionField = new JTextField(movie.getDescription());

		Caller callerGender = new Caller();
		JScrollPane scrollPaneGender = new JScrollPane(callerGender, JScrollPane.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		JCheckBox actionButton = new JCheckBox("Action");
		if(check(movie.getGenre(), "Action")) {
			actionButton.setSelected(true);
		}
		callerGender.addItem(actionButton);
		JCheckBox aventureButton = new JCheckBox("Aventure");
		if(check(movie.getGenre(), "Aventure")) {
			aventureButton.setSelected(true);
		}
		callerGender.addItem(aventureButton);
		JCheckBox anticipationButton = new JCheckBox("Anticipation");
		if(check(movie.getGenre(), "Anticipation")) {
			anticipationButton.setSelected(true);
		}
		callerGender.addItem(anticipationButton);
		JCheckBox noelButton = new JCheckBox("Noel");
		if(check(movie.getGenre(), "Noel")) {
			noelButton.setSelected(true);
		}
		callerGender.addItem(noelButton);
		JCheckBox comedieButton = new JCheckBox("Comedie");
		if(check(movie.getGenre(), "Comedie")) {
			comedieButton.setSelected(true);
		}
		callerGender.addItem(comedieButton);
		JCheckBox drameButton = new JCheckBox("Drame");
		if(check(movie.getGenre(), "Drame")) {
			drameButton.setSelected(true);
		}
		callerGender.addItem(drameButton);
		JCheckBox fantastiqueButton = new JCheckBox("Fantastique");
		if(check(movie.getGenre(), "Fantastique")) {
			fantastiqueButton.setSelected(true);
		}
		callerGender.addItem(fantastiqueButton);
		JCheckBox fantasyButton = new JCheckBox("Fantasy");
		if(check(movie.getGenre(), "Fantasy")) {
			fantasyButton.setSelected(true);
		}
		callerGender.addItem(fantasyButton);
		JCheckBox horreurButton = new JCheckBox("Horreur");
		if(check(movie.getGenre(), "Horreur")) {
			horreurButton.setSelected(true);
		}
		callerGender.addItem(horreurButton);
		JCheckBox historiqueButton = new JCheckBox("Historique");
		if(check(movie.getGenre(), "Historique")) {
			historiqueButton.setSelected(true);
		}
		callerGender.addItem(historiqueButton);
		JCheckBox SFButton = new JCheckBox("SF");
		if(check(movie.getGenre(), "SF")) {
			SFButton.setSelected(true);
		}
		callerGender.addItem(SFButton);
		JCheckBox thrillerButton = new JCheckBox("Thriller");
		if(check(movie.getGenre(), "Thriller")) {
			thrillerButton.setSelected(true);
		}
		callerGender.addItem(thrillerButton);
		JCheckBox westernButton = new JCheckBox("Western");
		if(check(movie.getGenre(), "Western")) {
			westernButton.setSelected(true);
		}
		callerGender.addItem(westernButton);
		JCheckBox otherGenderButton = new JCheckBox("Autre");
		if(check(movie.getGenre(), "Autre")) {
			otherGenderButton.setSelected(true);
		}
		callerGender.addItem(otherGenderButton);

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


		Caller callerPlateforme = new Caller();
		JScrollPane scrollPanePlateforme = new JScrollPane(callerPlateforme, JScrollPane.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		JCheckBox disneyButton = new JCheckBox("Disney");
		if(check(movie.getPlateforme(), "Disney")) {
			disneyButton.setSelected(true);
		}
		callerPlateforme.addItem(disneyButton);
		JCheckBox netflixButton = new JCheckBox("Netflix");
		if(check(movie.getPlateforme(), "Netflix")) {
			netflixButton.setSelected(true);
		}
		callerPlateforme.addItem(netflixButton);
		JCheckBox primeButton = new JCheckBox("Prime");
		if(check(movie.getPlateforme(), "Prime")) {
			primeButton.setSelected(true);
		}
		callerPlateforme.addItem(primeButton);
		JCheckBox canalButton = new JCheckBox("Canal");
		if(check(movie.getPlateforme(), "Canal")) {
			canalButton.setSelected(true);
		}
		callerPlateforme.addItem(canalButton);
		JCheckBox crunchyrollButton = new JCheckBox("Crunchyroll");
		if(check(movie.getPlateforme(), "Crunchyroll")) {
			crunchyrollButton.setSelected(true);
		}
		callerPlateforme.addItem(crunchyrollButton);
		JCheckBox maxButton = new JCheckBox("Max");
		if(check(movie.getPlateforme(), "Max")) {
			maxButton.setSelected(true);
		}
		callerPlateforme.addItem(maxButton);
		JCheckBox OCSButton = new JCheckBox("OCS");
		if(check(movie.getPlateforme(), "OCS")) {
			OCSButton.setSelected(true);
		}
		callerPlateforme.addItem(OCSButton);
		JCheckBox ParamountButton = new JCheckBox("Paramount");
		if(check(movie.getPlateforme(), "Paramount")) {
			ParamountButton.setSelected(true);
		}
		callerPlateforme.addItem(ParamountButton);
		JCheckBox aucuneIdeeButton = new JCheckBox("Aucune Idée");
		if(check(movie.getPlateforme(), "AucuneIdee")) {
			aucuneIdeeButton.setSelected(true);
		}
		callerPlateforme.addItem(aucuneIdeeButton);
		JCheckBox horsLigneButton = new JCheckBox("Hors ligne");
		if(check(movie.getPlateforme(), "HorsLigne")) {
			horsLigneButton.setSelected(true);
		}
		callerPlateforme.addItem(horsLigneButton);
		JCheckBox illegaleButton = new JCheckBox("Illégale");
		if(check(movie.getPlateforme(), "Illegale")) {
			illegaleButton.setSelected(true);
		}
		callerPlateforme.addItem(illegaleButton);

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
				scrollPaneGender,

				new JLabel("Durée"),
				dureeField,
				new JLabel("Date de sortie"),
				datePicker,
				new JLabel("Plateforme"),
				scrollPanePlateforme,

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

					// Gather selected genres
					java.util.List<Genre> selectedGenres = new java.util.ArrayList<>();
					if (actionButton.isSelected()) selectedGenres.add(Genre.Action);
					if (aventureButton.isSelected()) selectedGenres.add(Genre.Aventure);
					if (anticipationButton.isSelected()) selectedGenres.add(Genre.Anticipation);
					if (noelButton.isSelected()) selectedGenres.add(Genre.Noel);
					if (comedieButton.isSelected()) selectedGenres.add(Genre.Comedie);
					if (drameButton.isSelected()) selectedGenres.add(Genre.Drame);
					if (fantastiqueButton.isSelected()) selectedGenres.add(Genre.Fantastique);
					if (fantasyButton.isSelected()) selectedGenres.add(Genre.Fantasy);
					if (horreurButton.isSelected()) selectedGenres.add(Genre.Horreur);
					if (historiqueButton.isSelected()) selectedGenres.add(Genre.Historique);
					if (SFButton.isSelected()) selectedGenres.add(Genre.SF);
					if (thrillerButton.isSelected()) selectedGenres.add(Genre.Thriller);
					if (westernButton.isSelected()) selectedGenres.add(Genre.Western);
					if (otherGenderButton.isSelected()) selectedGenres.add(Genre.Autre);

					Genre[] genresArray = new Genre[selectedGenres.size()];
					selectedGenres.toArray(genresArray);

					int duree = 0; // Valeur par défaut
					if (!dureeField.getText().isEmpty()) {
						duree = Integer.parseInt(dureeField.getText());
					}

					Date dateSortie = null; // Valeur par défaut
					Object value = datePicker.getModel().getValue();
					if (value instanceof Date) {
						dateSortie = (Date) value;
					}

					// Gather selected platforms
					java.util.List<Plateforme> selectedPlateformes = new java.util.ArrayList<>();
					if (disneyButton.isSelected()) selectedPlateformes.add(Plateforme.Disney);
					if (netflixButton.isSelected()) selectedPlateformes.add(Plateforme.Netflix);
					if (primeButton.isSelected()) selectedPlateformes.add(Plateforme.Prime);
					if (canalButton.isSelected()) selectedPlateformes.add(Plateforme.Canal);
					if (crunchyrollButton.isSelected()) selectedPlateformes.add(Plateforme.Crunchyroll);
					if (maxButton.isSelected()) selectedPlateformes.add(Plateforme.Max);
					if (OCSButton.isSelected()) selectedPlateformes.add(Plateforme.OCS);
					if (ParamountButton.isSelected()) selectedPlateformes.add(Plateforme.Paramount);
					if (aucuneIdeeButton.isSelected()) selectedPlateformes.add(Plateforme.AucuneIdee);
					if (horsLigneButton.isSelected()) selectedPlateformes.add(Plateforme.HorsLigne);
					if (illegaleButton.isSelected()) selectedPlateformes.add(Plateforme.Illegale);

					Plateforme[] plateformesArray = new Plateforme[selectedPlateformes.size()];
					selectedPlateformes.toArray(plateformesArray);

					Utilisateur addBy = Utilisateur.valueOf(addByComboBox.getSelectedItem().toString());

					Movie newMovie = new Movie(titre, rea, desc, genresArray, duree, dateSortie, plateformesArray, addBy);
					gestionnaire.editMovie(oldTitle, newMovie); // Ajouter le film à votre gestionnaire de films
				}

				// Mettre à jour le modèle de tableau après l'ajout du film
				List<Movie> updatedMovies = gestionnaire.getMovies(); // Récupérer la liste mise à jour des films
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

	public Gestionnaire getGestionnaire() {
		return gestionnaire;
	}
}
