package view;

import model.Gestionnaire;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {

	public Frame() {

		super("MOVIE SELECTOR");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1200, 700); // Taille augmentée
		setLocationRelativeTo(null);
		getContentPane().setBackground(new Color(50, 50, 50)); // Fond sombre pour le contraste
		setLayout(new GridBagLayout()); // Utilisation de GridBagLayout pour un meilleur contrôle

		JButton btnTotalRandom = createButton("Choisir un film aléatoire", new Color(70, 130, 180));
		JButton btnFilterRandom = createButton("Appliquer des filtres au choix aléatoire", new Color(70, 130, 180));
		JButton btnShowMovie = createButton("Consulter la liste des films", new Color(220, 20, 60));


		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(15, 30, 15, 30);

		add(btnTotalRandom, gbc);
		add(btnFilterRandom, gbc);
		add(btnShowMovie, gbc);

		btnTotalRandom.addActionListener(e -> chooseMovie());
		btnFilterRandom.addActionListener(e -> selectFilter());
		btnShowMovie.addActionListener(e -> showMovies());

		this.setVisible(true);
	}

	private void chooseMovie() {
		// Initialisation du Panel utilisateur
		PanelMovies panelMovies = new PanelMovies(new Gestionnaire(), this);
		setContentPane(panelMovies);
		validate();
	}

	private void selectFilter() {
		// Initialisation du Panel utilisateur
		PanelMovies panelMovies = new PanelMovies(new Gestionnaire(), this);
		setContentPane(panelMovies);
		validate();
	}

	private void showMovies() {
		// Initialisation du Panel utilisateur
		PanelMovies panelMovies = new PanelMovies(new Gestionnaire(), this);
		setContentPane(panelMovies);
		validate();
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
