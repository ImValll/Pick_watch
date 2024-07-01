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
		MoviesTableModel model = new MoviesTableModel(listMovies);
		JTable table = new JTable(model);

		// Ajout de rendus personnalisés pour les colonnes "Modifier" et "Supprimer"
		table.getColumn("Modifier").setCellRenderer(new ButtonRenderer());
		table.getColumn("Supprimer").setCellRenderer(new ButtonRenderer());

		return new JScrollPane(table);
	}

	private void searchMovies(ActionEvent e) {
//		String titre = searchTitleField.getText();
//		String auteur = searchAuthorField.getText();
//		List<Livre> resultats = gestionnaire.rechercherLivres(titre, auteur);
//		StringBuilder sb = new StringBuilder();
//		if (resultats.isEmpty()) {
//			sb.append("Aucun livre trouvé pour les critères donnés.");
//		} else {
//			for (Livre livre : resultats) {
//				sb.append(livre.getTitre()).append(" - ").append(livre.getAuteur());
//				if (livre.isReserve()) {
//					sb.append(" (Réservé jusqu'à ").append(livre.getDateFinReservation().toString()).append(")");
//				} else {
//					sb.append(" (Disponible)");
//				}
//				sb.append("\n");
//			}
//		}
//		textArea.setText(sb.toString());
	}

	private void addMovie() {
		JTextField titleField = new JTextField();
		JTextField reaField = new JTextField();
		JTextField descriptionField = new JTextField();

		Caller callerGender = new Caller();
		JScrollPane scrollPaneGender = new JScrollPane(callerGender, JScrollPane.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		JCheckBox actionButon = new JCheckBox("Action");
		callerGender.addBall(actionButon);
		JCheckBox aventureButon = new JCheckBox("Aventure");
		callerGender.addBall(aventureButon);
		JCheckBox anticipationButon = new JCheckBox("Anticipation");
		callerGender.addBall(anticipationButon);
		JCheckBox noelButon = new JCheckBox("Noel");
		callerGender.addBall(noelButon);
		JCheckBox comedieButon = new JCheckBox("Comedie");
		callerGender.addBall(comedieButon);
		JCheckBox drameButon = new JCheckBox("Drame");
		callerGender.addBall(drameButon);
		JCheckBox fantastiqueButon = new JCheckBox("Fantastique");
		callerGender.addBall(fantastiqueButon);
		JCheckBox fantasyButon = new JCheckBox("Fantasy");
		callerGender.addBall(fantasyButon);
		JCheckBox horreurButon = new JCheckBox("Horreur");
		callerGender.addBall(horreurButon);
		JCheckBox historiqueButon = new JCheckBox("Historique");
		callerGender.addBall(historiqueButon);
		JCheckBox SFButon = new JCheckBox("SF");
		callerGender.addBall(SFButon);
		JCheckBox thrillerButon = new JCheckBox("Thriller");
		callerGender.addBall(thrillerButon);
		JCheckBox westernButon = new JCheckBox("Western");
		callerGender.addBall(westernButon);
		JCheckBox otherGenderButon = new JCheckBox("Autre");
		callerGender.addBall(otherGenderButon);

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
		callerPlateforme.addBall(disneyButon);
		JCheckBox netflixButon = new JCheckBox("Netflix");
		callerPlateforme.addBall(netflixButon);
		JCheckBox primeButon = new JCheckBox("Prime");
		callerPlateforme.addBall(primeButon);
		JCheckBox canalButon = new JCheckBox("Canal");
		callerPlateforme.addBall(canalButon);
		JCheckBox crunchyrollButon = new JCheckBox("Crunchyroll");
		callerPlateforme.addBall(crunchyrollButon);
		JCheckBox maxButon = new JCheckBox("Max");
		callerPlateforme.addBall(maxButon);
		JCheckBox OCSButon = new JCheckBox("OCS");
		callerPlateforme.addBall(OCSButon);
		JCheckBox aucuneIdeeButon = new JCheckBox("Aucune Idée");
		callerPlateforme.addBall(aucuneIdeeButon);
		JCheckBox illegaleButon = new JCheckBox("Illégale");
		callerPlateforme.addBall(illegaleButon);

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

				Genre[] genres = new Genre[14];  // Handle number format exception
				int i = 0;

				if(actionButon.isSelected()) {
					genres[i] = Genre.Action;
					i++;
				}
				if(aventureButon.isSelected()) {
					genres[i] = Genre.Aventure;
					i++;
				}
				if(anticipationButon.isSelected()) {
					genres[i] = Genre.Anticipation;
					i++;
				}
				if(noelButon.isSelected()) {
					genres[i] = Genre.Noel;
					i++;
				}
				if(comedieButon.isSelected()) {
					genres[i] = Genre.Comedie;
					i++;
				}
				if(drameButon.isSelected()) {
					genres[i] = Genre.Drame;
					i++;
				}
				if(fantastiqueButon.isSelected()) {
					genres[i] = Genre.Fantastique;
					i++;
				}
				if(fantasyButon.isSelected()) {
					genres[i] = Genre.Fantasy;
					i++;
				}
				if(horreurButon.isSelected()) {
					genres[i] = Genre.Horreur;
					i++;
				}
				if(historiqueButon.isSelected()) {
					genres[i] = Genre.Historique;
					i++;
				}
				if(SFButon.isSelected()) {
					genres[i] = Genre.SF;
					i++;
				}
				if(thrillerButon.isSelected()) {
					genres[i] = Genre.Thriller;
					i++;
				}
				if(westernButon.isSelected()) {
					genres[i] = Genre.Western;
					i++;
				}
				if(otherGenderButon.isSelected()) {
					genres[i] = Genre.Autre;
				}

				int duree = Integer.parseInt(dureeField.getText());
				Date dateSortie = (Date) datePicker.getModel().getValue();;

				Plateforme[] plateforme = new Plateforme[9];  // Handle number format exception
				i = 0;

				if(disneyButon.isSelected()) {
					plateforme[i] = Plateforme.Disney;
					i++;
				}
				if(netflixButon.isSelected()) {
					plateforme[i] = Plateforme.Netflix;
					i++;
				}
				if(primeButon.isSelected()) {
					plateforme[i] = Plateforme.Prime;
					i++;
				}
				if(canalButon.isSelected()) {
					plateforme[i] = Plateforme.Canal;
					i++;
				}
				if(crunchyrollButon.isSelected()) {
					plateforme[i] = Plateforme.Crunchyroll;
					i++;
				}
				if(maxButon.isSelected()) {
					plateforme[i] = Plateforme.Max;
					i++;
				}
				if(OCSButon.isSelected()) {
					plateforme[i] = Plateforme.OCS;
					i++;
				}
				if(aucuneIdeeButon.isSelected()) {
					plateforme[i] = Plateforme.AucuneIdee;
					i++;
				}
				if(illegaleButon.isSelected()) {
					plateforme[i] = Plateforme.Illegale;
				}

				Utilisateur addBy = Utilisateur.valueOf(addByComboBox.getSelectedItem().toString());

				Movie newMovie = new Movie(titre, rea, desc, genres, duree, dateSortie, plateforme, addBy);
				gestionnaire.addMovie(newMovie);
				JOptionPane.showMessageDialog(this, "Film ajouté avec succès: " + titre, "Livre Ajouté", JOptionPane.INFORMATION_MESSAGE);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "Erreur: L'année de sortie doit être un nombre valide.", "Erreur de Format", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void backMenu() {

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
}
