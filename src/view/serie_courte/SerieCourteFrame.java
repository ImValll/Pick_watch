package view.serie_courte;

import model.ButtonEditor;
import model.serie_courte.GestionnaireSerieCourte;
import view.Frame;

import javax.swing.*;
import java.awt.*;

public class SerieCourteFrame extends JFrame {
	public SerieCourteFrame() {

		super("SERIE COURTE SELECTOR");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1200, 700); // Taille augmentée
		setLocationRelativeTo(null);
		getContentPane().setBackground(new Color(50, 50, 50)); // Fond sombre pour le contraste
		setLayout(new GridBagLayout()); // Utilisation de GridBagLayout pour un meilleur contrôle

		JButton btnTotalRandom = ButtonEditor.createButton("Choisir une série courte aléatoire", new Color(70, 130, 180));
		JButton btnFilterRandom = ButtonEditor.createButton("Appliquer des filtres au choix aléatoire", new Color(70, 130, 180));
		JButton btnShowSerieCourte = ButtonEditor.createButton("Consulter la liste des séries courtes", new Color(220, 20, 60));
		JButton btnMenu = ButtonEditor.createButton("Retour au menu", new Color(220, 20, 60));


		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(15, 30, 15, 30);

		add(btnTotalRandom, gbc);
		add(btnFilterRandom, gbc);
		add(btnShowSerieCourte, gbc);
		add(btnMenu, gbc);

		btnTotalRandom.addActionListener(e -> chooseSerieCourte());
		btnFilterRandom.addActionListener(e -> selectFilter());
		btnShowSerieCourte.addActionListener(e -> showSerieCourte());
		btnMenu.addActionListener(e -> backToMenu());

		this.setVisible(true);
	}

	private void chooseSerieCourte() {
		// Initialisation du Panel utilisateur
		PanelRandomSerieCourte panelRandomSerieCourte = new PanelRandomSerieCourte(new GestionnaireSerieCourte(), this);
		setContentPane(panelRandomSerieCourte);
		validate();
	}

	private void selectFilter() {
		// Initialisation du Panel utilisateur
		PanelFilterRandomSerieCourte panelFilterRandomSerieCourte = new PanelFilterRandomSerieCourte(new GestionnaireSerieCourte(), this);
		setContentPane(panelFilterRandomSerieCourte);
		validate();
	}

	private void showSerieCourte() {
		// Initialisation du Panel utilisateur
		PanelSerieCourte panelSerieCourte = new PanelSerieCourte(new GestionnaireSerieCourte(), this);
		setContentPane(panelSerieCourte);
		validate();
	}

	private void backToMenu() {
		this.dispose();
		new Frame();
	}
}
