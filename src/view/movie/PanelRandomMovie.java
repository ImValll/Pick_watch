package view.movie;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class PanelRandomMovie extends JPanel {

	private Gestionnaire gestionnaire;
	private MovieFrame movieFrame;

	private String rea;
	private Genre[] genres;
	private int duree;
	private Date dateSortie;
	private Plateforme[] plateformes;
	private Utilisateur addBy;

	private Movie movie;
	private JPanel moviePanel;
	private JLabel movieLabel;

	public PanelRandomMovie(Gestionnaire gestionnaire, MovieFrame movieFrame) {
		this.gestionnaire = gestionnaire;
		this.movieFrame = movieFrame;
		initializeUI();
	}

	public PanelRandomMovie(Gestionnaire gestionnaire, MovieFrame movieFrame, Utilisateur addBy) {
		this.gestionnaire = gestionnaire;
		this.movieFrame = movieFrame;
		this.addBy = addBy;

		initializeUI();
	}

	public PanelRandomMovie(Gestionnaire gestionnaire, MovieFrame movieFrame, String rea, Genre[] genres, int duree, Date dateSortie, Plateforme[] plateformes, Utilisateur addBy) {
		this.gestionnaire = gestionnaire;
		this.movieFrame = movieFrame;
		this.rea = rea;
		this.genres = genres;
		this.duree = duree;
		this.dateSortie = dateSortie;
		this.plateformes = plateformes;
		this.addBy = addBy;

		initializeUI();
	}

	private void initializeUI() {
		setLayout(new BorderLayout());

		JPanel movieSelectedPanel = createMoviePanel();

		add(movieSelectedPanel);
	}

	private JPanel createMoviePanel() {
		JPanel panel = new JPanel(new BorderLayout());

		moviePanel = new JPanel();
		movieLabel = new JLabel();
		moviePanel.add(movieLabel);

		moviePanel.setBackground(new Color(50, 50, 50));
		panel.add(moviePanel, BorderLayout.CENTER);

		Movie movieSelected = gestionnaire.pickRandomMovie(rea, genres, duree, dateSortie, plateformes, addBy);

		if (movieSelected == null) {
			JOptionPane.showMessageDialog(this, "Erreur: Il n'y a aucun film dans la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
			backMenu();
		} else {
			this.movie = movieSelected;
			updateMovieInfo(movieSelected);
		}

		JButton btnBack = createButton("MENU", new Color(70, 130, 180));
		btnBack.addActionListener(e -> backMenu());

		JButton btnGen = createButton("Générer à nouveau", new Color(70, 130, 180));
		btnGen.addActionListener(e -> generateAgain());

		JButton btnDelete = createButton("Supprimer le film de la liste", new Color(70, 130, 180));
		btnDelete.addActionListener(e -> deleteMovie());

		JPanel bottomPanel = new JPanel();
		bottomPanel.add(btnBack);
		bottomPanel.add(btnGen);
		bottomPanel.add(btnDelete);

		bottomPanel.setBackground(new Color(50, 50, 50));
		panel.add(bottomPanel, BorderLayout.SOUTH);

		return panel;
	}

	private void updateMovieInfo(Movie movieSelected) {
		String date;
		if (movie.getDateSortie() != null) {
			date = new SimpleDateFormat("yyyy").format(movieSelected.getDateSortie());
		}
		else {
			date = "";
		}

		String movieInfo = "<html>Le film choisi est : " + movieSelected.getTitre() + " réalisé par " +
				movieSelected.getRealistateur() + ".<br>Le film appartient à/aux genre(s) " +
				Arrays.toString(movieSelected.getGenre()) + ".<br>Il dure " + movieSelected.getDuree() +
				" et est sorti en " + date + ".<br>Il est disponible sur " + Arrays.toString(movieSelected.getPlateforme()) +
				".<br>Il a été ajouté par " + movieSelected.getAddBy() + "</html>";

		movieLabel.setForeground(Color.WHITE);

		movieLabel.setText(movieInfo);
		moviePanel.revalidate();
		moviePanel.repaint();
	}

	public void backMenu() {
		movieFrame.dispose();
		new MovieFrame();
	}

	public void generateAgain() {
		Movie newMovie = gestionnaire.pickRandomMovie(rea, genres, duree, dateSortie, plateformes, addBy);
		if (newMovie == null) {
			JOptionPane.showMessageDialog(this, "Erreur: Il n'y a aucun film dans la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
			backMenu();
		} else {
			this.movie = newMovie;
			updateMovieInfo(newMovie);
		}

	}

	public void deleteMovie() {
		gestionnaire.deleteMovie(movie);

		backMenu();
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
