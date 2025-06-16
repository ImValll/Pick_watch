package view.saga;

import model.ButtonEditor;
import model.saga.GestionnaireSaga;
import view.Frame;

import javax.swing.*;
import java.awt.*;

public class SagaFrame extends JFrame {

	public SagaFrame() {

		super("SAGA SELECTOR");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1200, 700); // Taille augmentée
		setLocationRelativeTo(null);
		getContentPane().setBackground(new Color(50, 50, 50)); // Fond sombre pour le contraste
		setLayout(new GridBagLayout()); // Utilisation de GridBagLayout pour un meilleur contrôle

		JButton btnTotalRandom = ButtonEditor.createButton("Choisir une saga aléatoire", new Color(70, 130, 180));
		JButton btnFilterRandom = ButtonEditor.createButton("Appliquer des filtres au choix aléatoire", new Color(70, 130, 180));
		JButton btnShowSaga = ButtonEditor.createButton("Consulter la liste des sagas", new Color(220, 20, 60));
		JButton btnMenu = ButtonEditor.createButton("Retour au menu", new Color(220, 20, 60));


		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(15, 30, 15, 30);

		add(btnTotalRandom, gbc);
		add(btnFilterRandom, gbc);
		add(btnShowSaga, gbc);
		add(btnMenu, gbc);

		btnTotalRandom.addActionListener(e -> chooseSaga());
		btnFilterRandom.addActionListener(e -> selectFilter());
		btnShowSaga.addActionListener(e -> showSaga());
		btnMenu.addActionListener(e -> backToMenu());

		this.setVisible(true);
	}

	private void chooseSaga() {
		// Initialisation du Panel utilisateur
		PanelRandomSaga panelRandomSaga = new PanelRandomSaga(new GestionnaireSaga(), this);
		setContentPane(panelRandomSaga);
		validate();
	}

	private void selectFilter() {
		// Initialisation du Panel utilisateur
		PanelFilterRandomSaga panelFilterRandomSaga = new PanelFilterRandomSaga(new GestionnaireSaga(), this);
		setContentPane(panelFilterRandomSaga);
		validate();
	}

	private void showSaga() {
		// Initialisation du Panel utilisateur
		PanelSaga panelSaga = new PanelSaga(new GestionnaireSaga(), this);
		setContentPane(panelSaga);
		validate();
	}

	private void backToMenu() {
		this.dispose();
		new Frame();
	}
}
