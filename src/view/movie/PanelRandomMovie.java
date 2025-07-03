package view.movie;

import model.ButtonEditor;
import model.parameter.actors.Actor;
import model.parameter.genres.Genre;
import model.parameter.platforms.Platform;
import model.parameter.users.User;
import model.movie.GestionnaireMovie;
import model.movie.Movie;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class PanelRandomMovie extends JPanel {

	private final GestionnaireMovie gestionnaireMovie;
	private final MovieFrame movieFrame;

	private String rea;
	private Actor[] actors;
	private Genre[] genres;
	private int duree;
	private Date dateSortie;
	private Platform[] plateformes;
	private int dejaVu = -1;
	private User addBy;

	private Movie movie;
	private JPanel moviePanel;
	private JLabel movieLabel;

	public PanelRandomMovie(GestionnaireMovie gestionnaireMovie, MovieFrame movieFrame) {
		this.gestionnaireMovie = gestionnaireMovie;
		this.movieFrame = movieFrame;
		initializeUI();
	}

	public PanelRandomMovie(GestionnaireMovie gestionnaireMovie, MovieFrame movieFrame, String rea, Actor[] actors, Genre[] genres, int duree, Date dateSortie, Platform[] plateformes, int dejaVu, User addBy) {
		this.gestionnaireMovie = gestionnaireMovie;
		this.movieFrame = movieFrame;
		this.rea = rea;
		this.actors = actors;
		this.genres = genres;
		this.duree = duree;
		this.dateSortie = dateSortie;
		this.plateformes = plateformes;
		this.dejaVu = dejaVu;
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

		Movie movieSelected = gestionnaireMovie.pickRandomMovie(rea, actors, genres, duree, dateSortie, plateformes, dejaVu, addBy);

		if (movieSelected == null) {
			JOptionPane.showMessageDialog(this, "Erreur: Il n'y a aucun film dans la base de données ou aucun film ne correspond à votre recherche.", "Erreur", JOptionPane.ERROR_MESSAGE);
			backMenu();
		} else {
			this.movie = movieSelected;
			updateMovieInfo(movieSelected);
		}

		JButton btnBack = ButtonEditor.createButton("MENU", new Color(70, 130, 180));
		btnBack.addActionListener(e -> backMenu());

		JButton btnGen = ButtonEditor.createButton("Générer à nouveau", new Color(70, 130, 180));
		btnGen.addActionListener(e -> generateAgain());

		JButton btnDelete = ButtonEditor.createButton("Supprimer le film de la liste", new Color(70, 130, 180));
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
				movieSelected.getRealistateur() + ".<br>" + Arrays.toString(movieSelected.getActeur()) +
				" a/ont joué dedans.<br>Le film appartient à/aux genre(s) " +
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
		Movie newMovie = gestionnaireMovie.pickRandomMovie(rea, actors, genres, duree, dateSortie, plateformes, dejaVu, addBy);
		if (newMovie == null) {
			JOptionPane.showMessageDialog(this, "Erreur: Il n'y a aucun film dans la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
			backMenu();
		} else {
			this.movie = newMovie;
			updateMovieInfo(newMovie);
		}

	}

	public void deleteMovie() {
		gestionnaireMovie.deleteMovie(movie);

		backMenu();
	}
}
