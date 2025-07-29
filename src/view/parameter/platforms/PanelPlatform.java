package view.parameter.platforms;

import model.ButtonEditor;
import model.ButtonRenderer;
import model.parameter.platforms.Platform;
import model.parameter.platforms.GestionnairePlatform;
import model.parameter.platforms.PlatformTableModel;
import view.parameter.ParameterFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Objects;

public class PanelPlatform extends JPanel {

	ParameterFrame platformFrame;

	private final CardLayout cardLayout = new CardLayout();
	private final JPanel cards = new JPanel(cardLayout); // Panel that uses CardLayout
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

		JButton searchButton = ButtonEditor.createButton("Rechercher une plateforme", new Color(70, 130, 180));
		searchButton.addActionListener(this::searchPlatform);

		JButton addPlatformButton = ButtonEditor.createButton("Ajouter une plateforme", new Color(70, 130, 180));

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

		JButton btnBack = ButtonEditor.createButton("Retour", new Color(70, 130, 180));
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

		tableArea.setFillsViewportHeight(true);
		tableArea.setRowHeight(28);
		tableArea.setIntercellSpacing(new Dimension(1, 1));
		tableArea.setShowGrid(true);
		tableArea.setGridColor(new Color(100, 100, 100));
		tableArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		tableArea.setForeground(Color.WHITE);
		tableArea.setBackground(new Color(60, 63, 65));
		tableArea.setSelectionBackground(new Color(96, 99, 102));
		tableArea.setSelectionForeground(Color.WHITE);
		tableArea.getTableHeader().setReorderingAllowed(false);
		tableArea.getTableHeader().setBackground(new Color(80, 80, 80));
		tableArea.getTableHeader().setForeground(Color.WHITE);
		tableArea.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		for (int i = 0; i < tableArea.getColumnCount(); i++) {
			String columnName = tableArea.getColumnName(i);
			if (!columnName.equals("Modifier") && !columnName.equals("Supprimer")) {
				tableArea.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
			}
		}

		tableArea.getColumn("Modifier").setCellRenderer(new ButtonRenderer());
		tableArea.getColumn("Modifier").setCellEditor(new ButtonEditor(this));

		tableArea.getColumn("Supprimer").setCellRenderer(new ButtonRenderer());
		tableArea.getColumn("Supprimer").setCellEditor(new ButtonEditor(this));

		JScrollPane scrollPane = new JScrollPane(tableArea);
		scrollPane.getViewport().setBackground(new Color(60, 63, 65));
		scrollPane.setBorder(BorderFactory.createEmptyBorder());

		return scrollPane;
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
				for (Platform platform : listPlatform) {
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
		afficheMessage();

		java.util.List<Platform> updatedPlatform = gestionnairePlatform.getPlatform(); // Récupérer la liste mise à jour des series
		tableModel.setPlatform(updatedPlatform); // Mettre à jour le modèle du tableau
	}

	public void editPlatform(Platform platform) {

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
				gestionnairePlatform.editPlatform(oldTitleField.getText(), newPlatform); // Ajouter la plateforme à votre gestionnaire de plateforme
			}

			// Mettre à jour le modèle de tableau après l'ajout du platform
			List<Platform> updatedPlatform = gestionnairePlatform.getPlatform(); // Récupérer la liste mise à jour des plateformes
			tableModel.setPlatform(updatedPlatform); // Mettre à jour le modèle du tableau

			afficheMessage();
		}

	}

	public JTable getTableArea() {
		return tableArea;
	}

	public GestionnairePlatform getGestionnaire() {
		return gestionnairePlatform;
	}

	public void afficheMessage() {
		String[] message = gestionnairePlatform.getMessage();

		if(message[0] != null) {
			if(Objects.equals(message[0], "e")) {
				JOptionPane.showMessageDialog(this, message[2], message[1], JOptionPane.ERROR_MESSAGE);
			} else if(Objects.equals(message[0], "i")) {
				JOptionPane.showMessageDialog(this, message[2], message[1], JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
}
