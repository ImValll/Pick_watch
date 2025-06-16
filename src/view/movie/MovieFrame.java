package view.movie;

import model.ButtonEditor;
import model.movie.GestionnaireMovie;
import view.Frame;

import javax.swing.*;
import java.awt.*;

public class MovieFrame extends JFrame {

	public MovieFrame() {

		super("MOVIE SELECTOR");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1200, 700); // Taille augmentée
		setLocationRelativeTo(null);
		getContentPane().setBackground(new Color(50, 50, 50)); // Fond sombre pour le contraste
		setLayout(new GridBagLayout()); // Utilisation de GridBagLayout pour un meilleur contrôle

		JButton btnTotalRandom = ButtonEditor.createButton("Choisir un film aléatoire", new Color(70, 130, 180));
		JButton btnFilterRandom = ButtonEditor.createButton("Appliquer des filtres au choix aléatoire", new Color(70, 130, 180));
		JButton btnShowMovie = ButtonEditor.createButton("Consulter la liste des films", new Color(220, 20, 60));
		JButton btnMenu = ButtonEditor.createButton("Retour au menu", new Color(220, 20, 60));


		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(15, 30, 15, 30);

		add(btnTotalRandom, gbc);
		add(btnFilterRandom, gbc);
		add(btnShowMovie, gbc);
		add(btnMenu, gbc);

		btnTotalRandom.addActionListener(e -> chooseMovie());
		btnFilterRandom.addActionListener(e -> selectFilter());
		btnShowMovie.addActionListener(e -> showMovies());
		btnMenu.addActionListener(e -> backToMenu());

		this.setVisible(true);
	}

	private void chooseMovie() {
		// Initialisation du Panel utilisateur
		PanelRandomMovie panelRandomMovie = new PanelRandomMovie(new GestionnaireMovie(), this);
		setContentPane(panelRandomMovie);
		validate();
	}

	private void selectFilter() {
		// Initialisation du Panel utilisateur
		PanelFilterRandomMovie panelFilterRandomMovie = new PanelFilterRandomMovie(new GestionnaireMovie(), this);
		setContentPane(panelFilterRandomMovie);
		validate();
	}

	private void showMovies() {
		// Initialisation du Panel utilisateur
		PanelMovies panelMovies = new PanelMovies(new GestionnaireMovie(), this);
		setContentPane(panelMovies);
		validate();
	}

	private void backToMenu() {
		this.dispose();
		new Frame();
	}
}
