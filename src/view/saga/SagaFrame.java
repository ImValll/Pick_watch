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

		JPanel contentPanel = new JPanel();
		contentPanel.setBackground(new Color(50, 50, 50));
		contentPanel.setLayout(new GridBagLayout());

		JButton btnTotalRandom = ButtonEditor.createButton("Choisir une saga aléatoire", new Color(70, 130, 180));
		JButton btnFilterRandom = ButtonEditor.createButton("Appliquer des filtres au choix aléatoire", new Color(70, 130, 180));
		JButton btnShowSaga = ButtonEditor.createButton("Consulter la liste des sagas", new Color(220, 20, 60));
		JButton btnMenu = ButtonEditor.createButton("Retour au menu", new Color(220, 20, 60));


		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(15, 30, 15, 30);

		contentPanel.add(btnTotalRandom, gbc);
		contentPanel.add(btnFilterRandom, gbc);
		contentPanel.add(btnShowSaga, gbc);
		contentPanel.add(btnMenu, gbc);

		btnTotalRandom.addActionListener(e -> chooseSaga());
		btnFilterRandom.addActionListener(e -> selectFilter());
		btnShowSaga.addActionListener(e -> showSaga());
		btnMenu.addActionListener(e -> backToMenu());

		JScrollPane scrollPane = new JScrollPane(contentPanel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16); // défilement fluide

		setContentPane(scrollPane);

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
