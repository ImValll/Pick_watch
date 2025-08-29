package view.serie_courte;

import model.ButtonEditor;
import model.Language;
import model.serie_courte.GestionnaireSerieCourte;
import view.Frame;

import javax.swing.*;
import java.awt.*;

public class SerieCourteFrame extends JFrame {
	public SerieCourteFrame() {

		super(Language.getBundle().getString("serieCourte.title"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1200, 700); // Taille augmentée
		setLocationRelativeTo(null);

		JPanel contentPanel = new JPanel();
		contentPanel.setBackground(new Color(50, 50, 50));
		contentPanel.setLayout(new GridBagLayout());

		JButton btnTotalRandom = ButtonEditor.createButton(Language.getBundle().getString("serieCourte.btnTotalRandom"), new Color(70, 130, 180));
		JButton btnFilterRandom = ButtonEditor.createButton(Language.getBundle().getString("serieCourte.btnFilterRandom"), new Color(70, 130, 180));
		JButton btnShowSerieCourte = ButtonEditor.createButton(Language.getBundle().getString("serieCourte.btnShowSerieCourte"), new Color(220, 20, 60));
		JButton btnMenu = ButtonEditor.createButton(Language.getBundle().getString("app.btnMenu"), new Color(220, 20, 60));


		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(15, 30, 15, 30);

		contentPanel.add(btnTotalRandom, gbc);
		contentPanel.add(btnFilterRandom, gbc);
		contentPanel.add(btnShowSerieCourte, gbc);
		contentPanel.add(btnMenu, gbc);

		btnTotalRandom.addActionListener(e -> chooseSerieCourte());
		btnFilterRandom.addActionListener(e -> selectFilter());
		btnShowSerieCourte.addActionListener(e -> showSerieCourte());
		btnMenu.addActionListener(e -> backToMenu());

		JScrollPane scrollPane = new JScrollPane(contentPanel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16); // défilement fluide

		setContentPane(scrollPane);

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
