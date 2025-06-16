package view.serie;

import model.ButtonEditor;
import model.serie.GestionnaireSerie;
import view.Frame;

import javax.swing.*;
import java.awt.*;

public class SerieFrame extends JFrame {
	public SerieFrame() {

		super("SERIE SELECTOR");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1200, 700); // Taille augmentée
		setLocationRelativeTo(null);
		getContentPane().setBackground(new Color(50, 50, 50)); // Fond sombre pour le contraste
		setLayout(new GridBagLayout()); // Utilisation de GridBagLayout pour un meilleur contrôle

		JButton btnTotalRandom = ButtonEditor.createButton("Choisir une série aléatoire", new Color(70, 130, 180));
		JButton btnFilterRandom = ButtonEditor.createButton("Appliquer des filtres au choix aléatoire", new Color(70, 130, 180));
		JButton btnShowSerie = ButtonEditor.createButton("Consulter la liste des séries", new Color(220, 20, 60));
		JButton btnMenu = ButtonEditor.createButton("Retour au menu", new Color(220, 20, 60));


		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(15, 30, 15, 30);

		add(btnTotalRandom, gbc);
		add(btnFilterRandom, gbc);
		add(btnShowSerie, gbc);
		add(btnMenu, gbc);

		btnTotalRandom.addActionListener(e -> chooseSerie());
		btnFilterRandom.addActionListener(e -> selectFilter());
		btnShowSerie.addActionListener(e -> showSerie());
		btnMenu.addActionListener(e -> backToMenu());

		this.setVisible(true);
	}

	private void chooseSerie() {
		// Initialisation du Panel utilisateur
		PanelRandomSerie panelRandomSerie = new PanelRandomSerie(new GestionnaireSerie(), this);
		setContentPane(panelRandomSerie);
		validate();
	}

	private void selectFilter() {
		// Initialisation du Panel utilisateur
		PanelFilterRandomSerie panelFilterRandomSerie = new PanelFilterRandomSerie(new GestionnaireSerie(), this);
		setContentPane(panelFilterRandomSerie);
		validate();
	}

	private void showSerie() {
		// Initialisation du Panel utilisateur
		PanelSerie panelSerie = new PanelSerie(new GestionnaireSerie(), this);
		setContentPane(panelSerie);
		validate();
	}

	private void backToMenu() {
		this.dispose();
		new Frame();
	}
}
