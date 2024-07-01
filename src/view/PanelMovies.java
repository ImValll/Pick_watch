package view;

import model.*;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class PanelMovies extends JPanel {

	Frame frame;

	private CardLayout cardLayout = new CardLayout();
	private JPanel cards = new JPanel(cardLayout); // Panel that uses CardLayout
	private JTable tableArea; // Display book information
	private MoviesTableModel tableModel;
	Gestionnaire gestionnaire;
	private JTextField searchTitleField;
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	public PanelMovies(Gestionnaire gestionnaire, Frame frame) {
		this.gestionnaire = gestionnaire;
		this.frame = frame;
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

		topPanel.add(new JLabel("Titre : "));
		topPanel.add(searchTitleField);
		topPanel.add(searchButton);
		topPanel.add(addMovieButton);

		addMovieButton.addActionListener(e -> addMovie());

		// Get JScrollPane from showAllMovies method
		JScrollPane scrollPane = showAllMovies();

		panel.add(topPanel, BorderLayout.NORTH);
		panel.add(scrollPane, BorderLayout.CENTER);

		JButton btnBack = createButton("Retour", new Color(70, 130, 180));
		btnBack.addActionListener(e -> backMenu());

		JPanel bottomPanel = new JPanel();
		bottomPanel.add(btnBack);
		panel.add(bottomPanel, BorderLayout.SOUTH);

		return panel;
	}

	private JScrollPane showAllMovies() {
		List<Movie> listMovies = gestionnaire.getMovies();
		tableModel = new MoviesTableModel(listMovies);
		tableArea = new JTable(tableModel);

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

		JCheckBox actionButon = new JCheckBox("Action");
		callerGender.addItem(actionButon);
		JCheckBox aventureButon = new JCheckBox("Aventure");
		callerGender.addItem(aventureButon);
		JCheckBox anticipationButon = new JCheckBox("Anticipation");
		callerGender.addItem(anticipationButon);
		JCheckBox noelButon = new JCheckBox("Noel");
		callerGender.addItem(noelButon);
		JCheckBox comedieButon = new JCheckBox("Comedie");
		callerGender.addItem(comedieButon);
		JCheckBox drameButon = new JCheckBox("Drame");
		callerGender.addItem(drameButon);
		JCheckBox fantastiqueButon = new JCheckBox("Fantastique");
		callerGender.addItem(fantastiqueButon);
		JCheckBox fantasyButon = new JCheckBox("Fantasy");
		callerGender.addItem(fantasyButon);
		JCheckBox horreurButon = new JCheckBox("Horreur");
		callerGender.addItem(horreurButon);
		JCheckBox historiqueButon = new JCheckBox("Historique");
		callerGender.addItem(historiqueButon);
		JCheckBox SFButon = new JCheckBox("SF");
		callerGender.addItem(SFButon);
		JCheckBox thrillerButon = new JCheckBox("Thriller");
		callerGender.addItem(thrillerButon);
		JCheckBox westernButon = new JCheckBox("Western");
		callerGender.addItem(westernButon);
		JCheckBox otherGenderButon = new JCheckBox("Autre");
		callerGender.addItem(otherGenderButon);

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

		JCheckBox disneyButon = new JCheckBox("Disney");
		callerPlateforme.addItem(disneyButon);
		JCheckBox netflixButon = new JCheckBox("Netflix");
		callerPlateforme.addItem(netflixButon);
		JCheckBox primeButon = new JCheckBox("Prime");
		callerPlateforme.addItem(primeButon);
		JCheckBox canalButon = new JCheckBox("Canal");
		callerPlateforme.addItem(canalButon);
		JCheckBox crunchyrollButon = new JCheckBox("Crunchyroll");
		callerPlateforme.addItem(crunchyrollButon);
		JCheckBox maxButon = new JCheckBox("Max");
		callerPlateforme.addItem(maxButon);
		JCheckBox OCSButon = new JCheckBox("OCS");
		callerPlateforme.addItem(OCSButon);
		JCheckBox aucuneIdeeButon = new JCheckBox("Aucune Idée");
		callerPlateforme.addItem(aucuneIdeeButon);
		JCheckBox illegaleButon = new JCheckBox("Illégale");
		callerPlateforme.addItem(illegaleButon);

		JComboBox<Utilisateur> addByComboBox = new JComboBox<>(Utilisateur.values());

		final JComponent[] inputs = new JComponent[] {
			new JLabel("Titre"),
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
				String rea = reaField.getText();
				String desc = descriptionField.getText();

				// Gather selected genres
				java.util.List<Genre> selectedGenres = new java.util.ArrayList<>();
				if (actionButon.isSelected()) selectedGenres.add(Genre.Action);
				if (aventureButon.isSelected()) selectedGenres.add(Genre.Aventure);
				if (anticipationButon.isSelected()) selectedGenres.add(Genre.Anticipation);
				if (noelButon.isSelected()) selectedGenres.add(Genre.Noel);
				if (comedieButon.isSelected()) selectedGenres.add(Genre.Comedie);
				if (drameButon.isSelected()) selectedGenres.add(Genre.Drame);
				if (fantastiqueButon.isSelected()) selectedGenres.add(Genre.Fantastique);
				if (fantasyButon.isSelected()) selectedGenres.add(Genre.Fantasy);
				if (horreurButon.isSelected()) selectedGenres.add(Genre.Horreur);
				if (historiqueButon.isSelected()) selectedGenres.add(Genre.Historique);
				if (SFButon.isSelected()) selectedGenres.add(Genre.SF);
				if (thrillerButon.isSelected()) selectedGenres.add(Genre.Thriller);
				if (westernButon.isSelected()) selectedGenres.add(Genre.Western);
				if (otherGenderButon.isSelected()) selectedGenres.add(Genre.Autre);

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
				if (disneyButon.isSelected()) selectedPlateformes.add(Plateforme.Disney);
				if (netflixButon.isSelected()) selectedPlateformes.add(Plateforme.Netflix);
				if (primeButon.isSelected()) selectedPlateformes.add(Plateforme.Prime);
				if (canalButon.isSelected()) selectedPlateformes.add(Plateforme.Canal);
				if (crunchyrollButon.isSelected()) selectedPlateformes.add(Plateforme.Crunchyroll);
				if (maxButon.isSelected()) selectedPlateformes.add(Plateforme.Max);
				if (OCSButon.isSelected()) selectedPlateformes.add(Plateforme.OCS);
				if (aucuneIdeeButon.isSelected()) selectedPlateformes.add(Plateforme.AucuneIdee);
				if (illegaleButon.isSelected()) selectedPlateformes.add(Plateforme.Illegale);

				Plateforme[] plateformesArray = new Plateforme[selectedPlateformes.size()];
				selectedPlateformes.toArray(plateformesArray);

				Utilisateur addBy = Utilisateur.valueOf(addByComboBox.getSelectedItem().toString());

				Movie newMovie = new Movie(titre, rea, desc, genresArray, duree, dateSortie, plateformesArray, addBy);
				gestionnaire.addMovie(newMovie); // Ajouter le film à votre gestionnaire de films

				// Mettre à jour le modèle de tableau après l'ajout du film
				List<Movie> updatedMovies = gestionnaire.getMovies(); // Récupérer la liste mise à jour des films
				tableModel.setMovies(updatedMovies); // Mettre à jour le modèle du tableau

				JOptionPane.showMessageDialog(this, "Film ajouté avec succès: " + titre, "Film Ajouté", JOptionPane.INFORMATION_MESSAGE);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "Erreur: L'année de sortie doit être un nombre valide.", "Erreur de Format", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void backMenu() {
		frame.dispose();
		new Frame();
	}

	public void deleteMovie(Movie movie) {
		gestionnaire.deleteMovie(movie);

		List<Movie> updatedMovies = gestionnaire.getMovies(); // Récupérer la liste mise à jour des films
		tableModel.setMovies(updatedMovies); // Mettre à jour le modèle du tableau
	}

	public void editMovie(Movie movie) {

		String oldTitle = movie.getTitre();

		JTextField titleField = new JTextField(movie.getTitre());
		JTextField reaField = new JTextField(movie.getRealistateur());
		JTextField descriptionField = new JTextField(movie.getDescription());

		Caller callerGender = new Caller();
		JScrollPane scrollPaneGender = new JScrollPane(callerGender, JScrollPane.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		JCheckBox actionButon = new JCheckBox("Action");
		if(check(movie.getGenre(), "Action")) {
			actionButon.setSelected(true);
		}
		callerGender.addItem(actionButon);
		JCheckBox aventureButon = new JCheckBox("Aventure");
		if(check(movie.getGenre(), "Aventure")) {
			aventureButon.setSelected(true);
		}
		callerGender.addItem(aventureButon);
		JCheckBox anticipationButon = new JCheckBox("Anticipation");
		if(check(movie.getGenre(), "Anticipation")) {
			anticipationButon.setSelected(true);
		}
		callerGender.addItem(anticipationButon);
		JCheckBox noelButon = new JCheckBox("Noel");
		if(check(movie.getGenre(), "Noel")) {
			noelButon.setSelected(true);
		}
		callerGender.addItem(noelButon);
		JCheckBox comedieButon = new JCheckBox("Comedie");
		if(check(movie.getGenre(), "Comedie")) {
			comedieButon.setSelected(true);
		}
		callerGender.addItem(comedieButon);
		JCheckBox drameButon = new JCheckBox("Drame");
		if(check(movie.getGenre(), "Drame")) {
			drameButon.setSelected(true);
		}
		callerGender.addItem(drameButon);
		JCheckBox fantastiqueButon = new JCheckBox("Fantastique");
		if(check(movie.getGenre(), "Fantastique")) {
			fantastiqueButon.setSelected(true);
		}
		callerGender.addItem(fantastiqueButon);
		JCheckBox fantasyButon = new JCheckBox("Fantasy");
		if(check(movie.getGenre(), "Fantasy")) {
			fantasyButon.setSelected(true);
		}
		callerGender.addItem(fantasyButon);
		JCheckBox horreurButon = new JCheckBox("Horreur");
		if(check(movie.getGenre(), "Horreur")) {
			horreurButon.setSelected(true);
		}
		callerGender.addItem(horreurButon);
		JCheckBox historiqueButon = new JCheckBox("Historique");
		if(check(movie.getGenre(), "Historique")) {
			historiqueButon.setSelected(true);
		}
		callerGender.addItem(historiqueButon);
		JCheckBox SFButon = new JCheckBox("SF");
		if(check(movie.getGenre(), "SF")) {
			SFButon.setSelected(true);
		}
		callerGender.addItem(SFButon);
		JCheckBox thrillerButon = new JCheckBox("Thriller");
		if(check(movie.getGenre(), "Thriller")) {
			thrillerButon.setSelected(true);
		}
		callerGender.addItem(thrillerButon);
		JCheckBox westernButon = new JCheckBox("Western");
		if(check(movie.getGenre(), "Western")) {
			westernButon.setSelected(true);
		}
		callerGender.addItem(westernButon);
		JCheckBox otherGenderButon = new JCheckBox("Autre");
		if(check(movie.getGenre(), "Autre")) {
			otherGenderButon.setSelected(true);
		}
		callerGender.addItem(otherGenderButon);

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

		JCheckBox disneyButon = new JCheckBox("Disney");
		if(check(movie.getPlateforme(), "Disney")) {
			actionButon.setSelected(true);
		}
		callerPlateforme.addItem(disneyButon);
		JCheckBox netflixButon = new JCheckBox("Netflix");
		if(check(movie.getPlateforme(), "Netflix")) {
			netflixButon.setSelected(true);
		}
		callerPlateforme.addItem(netflixButon);
		JCheckBox primeButon = new JCheckBox("Prime");
		if(check(movie.getPlateforme(), "Prime")) {
			primeButon.setSelected(true);
		}
		callerPlateforme.addItem(primeButon);
		JCheckBox canalButon = new JCheckBox("Canal");
		if(check(movie.getPlateforme(), "Canal")) {
			canalButon.setSelected(true);
		}
		callerPlateforme.addItem(canalButon);
		JCheckBox crunchyrollButon = new JCheckBox("Crunchyroll");
		if(check(movie.getPlateforme(), "Crunchyroll")) {
			crunchyrollButon.setSelected(true);
		}
		callerPlateforme.addItem(crunchyrollButon);
		JCheckBox maxButon = new JCheckBox("Max");
		if(check(movie.getPlateforme(), "Max")) {
			maxButon.setSelected(true);
		}
		callerPlateforme.addItem(maxButon);
		JCheckBox OCSButon = new JCheckBox("OCS");
		if(check(movie.getPlateforme(), "OCS")) {
			OCSButon.setSelected(true);
		}
		callerPlateforme.addItem(OCSButon);
		JCheckBox aucuneIdeeButon = new JCheckBox("Aucune Idée");
		if(check(movie.getPlateforme(), "Aucune Idée")) {
			aucuneIdeeButon.setSelected(true);
		}
		callerPlateforme.addItem(aucuneIdeeButon);
		JCheckBox illegaleButon = new JCheckBox("Illégale");
		if(check(movie.getPlateforme(), "Illégale")) {
			illegaleButon.setSelected(true);
		}
		callerPlateforme.addItem(illegaleButon);

		JComboBox<Utilisateur> addByComboBox = new JComboBox<>(Utilisateur.values());
		addByComboBox.setSelectedItem(movie.getAddBy());

		final JComponent[] inputs = new JComponent[] {
				new JLabel("Titre"),
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
				String rea = reaField.getText();
				String desc = descriptionField.getText();

				// Gather selected genres
				java.util.List<Genre> selectedGenres = new java.util.ArrayList<>();
				if (actionButon.isSelected()) selectedGenres.add(Genre.Action);
				if (aventureButon.isSelected()) selectedGenres.add(Genre.Aventure);
				if (anticipationButon.isSelected()) selectedGenres.add(Genre.Anticipation);
				if (noelButon.isSelected()) selectedGenres.add(Genre.Noel);
				if (comedieButon.isSelected()) selectedGenres.add(Genre.Comedie);
				if (drameButon.isSelected()) selectedGenres.add(Genre.Drame);
				if (fantastiqueButon.isSelected()) selectedGenres.add(Genre.Fantastique);
				if (fantasyButon.isSelected()) selectedGenres.add(Genre.Fantasy);
				if (horreurButon.isSelected()) selectedGenres.add(Genre.Horreur);
				if (historiqueButon.isSelected()) selectedGenres.add(Genre.Historique);
				if (SFButon.isSelected()) selectedGenres.add(Genre.SF);
				if (thrillerButon.isSelected()) selectedGenres.add(Genre.Thriller);
				if (westernButon.isSelected()) selectedGenres.add(Genre.Western);
				if (otherGenderButon.isSelected()) selectedGenres.add(Genre.Autre);

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
				if (disneyButon.isSelected()) selectedPlateformes.add(Plateforme.Disney);
				if (netflixButon.isSelected()) selectedPlateformes.add(Plateforme.Netflix);
				if (primeButon.isSelected()) selectedPlateformes.add(Plateforme.Prime);
				if (canalButon.isSelected()) selectedPlateformes.add(Plateforme.Canal);
				if (crunchyrollButon.isSelected()) selectedPlateformes.add(Plateforme.Crunchyroll);
				if (maxButon.isSelected()) selectedPlateformes.add(Plateforme.Max);
				if (OCSButon.isSelected()) selectedPlateformes.add(Plateforme.OCS);
				if (aucuneIdeeButon.isSelected()) selectedPlateformes.add(Plateforme.AucuneIdee);
				if (illegaleButon.isSelected()) selectedPlateformes.add(Plateforme.Illegale);

				Plateforme[] plateformesArray = new Plateforme[selectedPlateformes.size()];
				selectedPlateformes.toArray(plateformesArray);

				Utilisateur addBy = Utilisateur.valueOf(addByComboBox.getSelectedItem().toString());

				Movie newMovie = new Movie(titre, rea, desc, genresArray, duree, dateSortie, plateformesArray, addBy);
				gestionnaire.editMovie(oldTitle, newMovie); // Ajouter le film à votre gestionnaire de films

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
