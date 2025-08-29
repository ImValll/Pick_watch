package view.movie;

import model.ButtonEditor;
import model.Language;
import model.movie.GestionnaireMovie;
import view.Frame;

import javax.swing.*;
import java.awt.*;

public class MovieFrame extends JFrame {

	public MovieFrame() {

		super(Language.getBundle().getString("movie.title"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1200, 700); // Taille augmentée
		setLocationRelativeTo(null);

		JPanel contentPanel = new JPanel();
		contentPanel.setBackground(new Color(50, 50, 50));
		contentPanel.setLayout(new GridBagLayout());

		JButton btnTotalRandom = ButtonEditor.createButton(Language.getBundle().getString("movie.btnTotalRandom"), new Color(70, 130, 180));
		JButton btnFilterRandom = ButtonEditor.createButton(Language.getBundle().getString("movie.btnFilterRandom"), new Color(70, 130, 180));
		JButton btnShowMovie = ButtonEditor.createButton(Language.getBundle().getString("movie.btnShowMovie"), new Color(220, 20, 60));
		JButton btnMenu = ButtonEditor.createButton(Language.getBundle().getString("app.btnMenu"), new Color(220, 20, 60));


		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(15, 30, 15, 30);

		contentPanel.add(btnTotalRandom, gbc);
		contentPanel.add(btnFilterRandom, gbc);
		contentPanel.add(btnShowMovie, gbc);
		contentPanel.add(btnMenu, gbc);

		btnTotalRandom.addActionListener(e -> chooseMovie());
		btnFilterRandom.addActionListener(e -> selectFilter());
		btnShowMovie.addActionListener(e -> showMovies());
		btnMenu.addActionListener(e -> backToMenu());

		JScrollPane scrollPane = new JScrollPane(contentPanel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16); // défilement fluide

		setContentPane(scrollPane);

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
