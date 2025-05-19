package view.parameter.platforms;

import model.ButtonEditor;
import model.ButtonRenderer;
import model.DataManager;
import model.genre.Platform;
import model.parameter.platforms.GestionnairePlatform;
import model.parameter.platforms.PlatformTableModel;
import view.parameter.ParameterFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class PanelPlatform extends JPanel {

	DataManager dataManager = new DataManager();

	ParameterFrame platformFrame;

	private CardLayout cardLayout = new CardLayout();
	private JPanel cards = new JPanel(cardLayout); // Panel that uses CardLayout
	private JTable tableArea; // Display book information
	private PlatformTableModel tableModel;
	GestionnairePlatform gestionnairePlatform;
	private JTextField searchTitleField;

	public PanelPlatform(GestionnairePlatform gestionnairePlatform, ParameterFrame platformFrame) {
		this.gestionnairePlatform = gestionnairePlatform;
		this.platformFrame = platformFrame;
		initializeUI();
	}

	private void initializeUI() {
		setLayout(new BorderLayout());
		add(cards, BorderLayout.CENTER);

		JPanel platformGrid = createPlatformPanel();

		add(platformGrid);
	}

	private JPanel createPlatformPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		// Top panel for search
		JPanel topPanel = new JPanel();
		searchTitleField = new JTextField(20);

		JButton searchButton = createButton("Rechercher une plateforme", new Color(70, 130, 180));
		searchButton.addActionListener(this::searchPlatform);

		JButton addPlatformButton = createButton("Ajouter une plateforme", new Color(70, 130, 180));

		JLabel titleLabel = new JLabel("Titre : ");
		titleLabel.setForeground(Color.WHITE);
		topPanel.add(titleLabel);

		topPanel.add(searchTitleField);
		topPanel.add(searchButton);
		topPanel.add(addPlatformButton);

		addPlatformButton.addActionListener(e -> addPlatform());

		// Get JScrollPane from showAllPlatform method
		JScrollPane scrollPane = showAllPlatform();

		topPanel.setBackground(new Color(50, 50, 50));
		scrollPane.setBackground(new Color(50, 50, 50));
		panel.add(topPanel, BorderLayout.NORTH);
		panel.add(scrollPane, BorderLayout.CENTER);

		JButton btnBack = createButton("Retour", new Color(70, 130, 180));
		btnBack.addActionListener(e -> backMenu());

		JPanel bottomPanel = new JPanel();
		bottomPanel.setBackground(new Color(50, 50, 50));
		bottomPanel.add(btnBack);
		panel.add(bottomPanel, BorderLayout.SOUTH);

		return panel;
	}

	private JScrollPane showAllPlatform() {
		java.util.List<Platform> listPlatform = gestionnairePlatform.getPlatform();
		tableModel = new PlatformTableModel(listPlatform);
		tableArea = new JTable(tableModel);
		tableArea.setBackground(Color.LIGHT_GRAY);

		// Ajout de rendus personnalisés pour les colonnes "Modifier" et "Supprimer"
		tableArea.getColumn("Modifier").setCellRenderer(new ButtonRenderer());
		tableArea.getColumn("Modifier").setCellEditor(new ButtonEditor(this));

		tableArea.getColumn("Supprimer").setCellRenderer(new ButtonRenderer());
		tableArea.getColumn("Supprimer").setCellEditor(new ButtonEditor(this));

		return new JScrollPane(tableArea);
	}

	private void searchPlatform(ActionEvent e) {
		String titre = searchTitleField.getText();
		java.util.List<Platform> result = gestionnairePlatform.searchPlatform(titre);
		if (result.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Erreur: Aucune plateforme trouvée pour ce titre.", "Erreur", JOptionPane.ERROR_MESSAGE);
		} else {
			tableModel.setPlatform(result); // Mettre à jour le modèle du tableau
		}
	}

	private void addPlatform() {
		JTextField titleField = new JTextField();

		final JComponent[] inputs = new JComponent[] {
				new JLabel("Titre*"),
				titleField,
		};

		int result = JOptionPane.showConfirmDialog(this, inputs, "Ajouter une nouvelle plateforme", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			String titre = titleField.getText();

			if(titre.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Erreur: Le titre doit être entré.", "Erreur titre vide", JOptionPane.ERROR_MESSAGE);
			}
			else {

				boolean canBeAdd = true;

				java.util.List<Platform> listPlatform = gestionnairePlatform.getPlatform();
				for (int i = 0; i < listPlatform.size(); i++) {
					Platform platform = listPlatform.get(i);
					if (platform.getName().equalsIgnoreCase(titre)) {
						canBeAdd = false;
						JOptionPane.showMessageDialog(this, "Erreur: La plateforme " + titre + " a déjà été ajoutée", "Erreur doublons", JOptionPane.ERROR_MESSAGE);
						break;
					}
				}

				if (canBeAdd) {
					Platform newPlatform = new Platform(titre);
					gestionnairePlatform.addPlatform(newPlatform); // Ajouter la plateforme à votre gestionnaire de plateforme
					JOptionPane.showMessageDialog(this, "Plateforme ajoutée avec succès: " + titre, "Plateforme Ajoutée", JOptionPane.INFORMATION_MESSAGE);
				}
			}

			// Mettre à jour le modèle de tableau après l'ajout du platform
			java.util.List<Platform> updatedPlatform = gestionnairePlatform.getPlatform(); // Récupérer la liste mise à jour des series
			tableModel.setPlatform(updatedPlatform); // Mettre à jour le modèle du tableau
		}
	}

	public void backMenu() {
		platformFrame.dispose();
		new ParameterFrame();
	}

	public void deletePlatform(Platform platform) {
		gestionnairePlatform.deletePlatform(platform);

		java.util.List<Platform> updatedPlatform = gestionnairePlatform.getPlatform(); // Récupérer la liste mise à jour des series
		tableModel.setPlatform(updatedPlatform); // Mettre à jour le modèle du tableau
	}

	public void editPlatform(Platform platform) {

		String oldTitle = platform.getName();
		JTextField oldTitleField = new JTextField(platform.getName());
		oldTitleField.setEnabled(false);

		JTextField titleField = new JTextField(platform.getName());



		final JComponent[] inputs = new JComponent[] {
				new JLabel("Ancien titre"),
				oldTitleField,
				new JLabel("Titre*"),
				titleField,
		};

		int result = JOptionPane.showConfirmDialog(this, inputs, "Modifier une plateforme", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			String titre = titleField.getText();

			if(titre.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Erreur: Le titre doit être entré.", "Erreur titre vide", JOptionPane.ERROR_MESSAGE);
			} else {
				Platform newPlatform = new Platform(titre);
				gestionnairePlatform.editPlatform(oldTitle, newPlatform); // Ajouter la plateforme à votre gestionnaire de plateforme
			}

			// Mettre à jour le modèle de tableau après l'ajout du platform
			List<Platform> updatedPlatform = gestionnairePlatform.getPlatform(); // Récupérer la liste mise à jour des plateformes
			tableModel.setPlatform(updatedPlatform); // Mettre à jour le modèle du tableau

			JOptionPane.showMessageDialog(this, "Plateforme modifiée avec succès: " + titre, "Plateforme modifiée", JOptionPane.INFORMATION_MESSAGE);
		}

	}



	public static boolean check(Object[] tab, Object val) {
		boolean b = false;

		for(Object i : tab){
			if(i.toString().equals(val.toString())){
				b = true;
				break;
			}
		}
		return b;
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

	public JTable getTableArea() {
		return tableArea;
	}

	public GestionnairePlatform getGestionnaire() {
		return gestionnairePlatform;
	}
}
