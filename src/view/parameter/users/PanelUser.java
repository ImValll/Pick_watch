package view.parameter.users;

import model.ButtonEditor;
import model.ButtonRenderer;
import model.parameter.users.User;
import model.parameter.users.GestionnaireUser;
import model.parameter.users.UserTableModel;
import view.parameter.ParameterFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Objects;

public class PanelUser extends JPanel {

	ParameterFrame userFrame;

	private final CardLayout cardLayout = new CardLayout();
	private final JPanel cards = new JPanel(cardLayout); // Panel that uses CardLayout
	private JTable tableArea; // Display book information
	private UserTableModel tableModel;
	GestionnaireUser gestionnaireUser;
	private JTextField searchTitleField;

	public PanelUser(GestionnaireUser gestionnaireUser, ParameterFrame userFrame) {
		this.gestionnaireUser = gestionnaireUser;
		this.userFrame = userFrame;
		initializeUI();
	}

	private void initializeUI() {
		setLayout(new BorderLayout());
		add(cards, BorderLayout.CENTER);

		JPanel userGrid = createUserPanel();

		add(userGrid);
	}

	private JPanel createUserPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		// Top panel for search
		JPanel topPanel = new JPanel();
		searchTitleField = new JTextField(20);

		JButton searchButton = ButtonEditor.createButton("Rechercher un utilisateur", new Color(70, 130, 180));
		searchButton.addActionListener(this::searchUser);

		JButton addUserButton = ButtonEditor.createButton("Ajouter un utilisateur", new Color(70, 130, 180));

		JLabel titleLabel = new JLabel("Nom : ");
		titleLabel.setForeground(Color.WHITE);
		topPanel.add(titleLabel);

		topPanel.add(searchTitleField);
		topPanel.add(searchButton);
		topPanel.add(addUserButton);

		addUserButton.addActionListener(e -> addUser());

		// Get JScrollPane from showAllUser method
		JScrollPane scrollPane = showAllUser();

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

	private JScrollPane showAllUser() {
		java.util.List<User> listUser = gestionnaireUser.getUser();
		tableModel = new UserTableModel(listUser);
		tableArea = new JTable(tableModel);
		tableArea.setBackground(Color.LIGHT_GRAY);

		// Ajout de rendus personnalisés pour les colonnes "Modifier" et "Supprimer"
		tableArea.getColumn("Modifier").setCellRenderer(new ButtonRenderer());
		tableArea.getColumn("Modifier").setCellEditor(new ButtonEditor(this));

		tableArea.getColumn("Supprimer").setCellRenderer(new ButtonRenderer());
		tableArea.getColumn("Supprimer").setCellEditor(new ButtonEditor(this));

		return new JScrollPane(tableArea);
	}

	private void searchUser(ActionEvent e) {
		String titre = searchTitleField.getText();
		java.util.List<User> result = gestionnaireUser.searchUser(titre);
		if (result.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Erreur: Aucun utilisateur trouvé pour ce nom.", "Erreur", JOptionPane.ERROR_MESSAGE);
		} else {
			tableModel.setUser(result); // Mettre à jour le modèle du tableau
		}
	}

	private void addUser() {
		JTextField titleField = new JTextField();

		final JComponent[] inputs = new JComponent[] {
				new JLabel("Nom*"),
				titleField,
		};

		int result = JOptionPane.showConfirmDialog(this, inputs, "Ajouter un nouvel utilisateur", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			String titre = titleField.getText();

			if(titre.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Erreur: Le nom doit être entré.", "Erreur nom vide", JOptionPane.ERROR_MESSAGE);
			}
			else {

				boolean canBeAdd = true;

				java.util.List<User> listUser = gestionnaireUser.getUser();
				for (User user : listUser) {
					if (user.getName().equalsIgnoreCase(titre)) {
						canBeAdd = false;
						JOptionPane.showMessageDialog(this, "Erreur: L'utilisateur " + titre + " a déjà été ajouté", "Erreur doublons", JOptionPane.ERROR_MESSAGE);
						break;
					}
				}

				if (canBeAdd) {
					User newUser = new User(titre);
					gestionnaireUser.addUser(newUser); // Ajouter l'utilisateur à votre gestionnaire d'utilisateur
					JOptionPane.showMessageDialog(this, "Utilisateur ajouté avec succès: " + titre, "Utilisateur Ajouté", JOptionPane.INFORMATION_MESSAGE);
				}
			}

			// Mettre à jour le modèle de tableau après l'ajout de l'utilisateur
			java.util.List<User> updatedUser = gestionnaireUser.getUser(); // Récupérer la liste mise à jour des utilisateurs
			tableModel.setUser(updatedUser); // Mettre à jour le modèle du tableau
		}
	}

	public void backMenu() {
		userFrame.dispose();
		new ParameterFrame();
	}

	public void deleteUser(User user) {
		gestionnaireUser.deleteUser(user);
		afficheMessage();

		java.util.List<User> updatedUser = gestionnaireUser.getUser(); // Récupérer la liste mise à jour des utilisateurs
		tableModel.setUser(updatedUser); // Mettre à jour le modèle du tableau
	}

	public void editUser(User user) {

		JTextField oldTitleField = new JTextField(user.getName());
		oldTitleField.setEnabled(false);

		JTextField titleField = new JTextField(user.getName());



		final JComponent[] inputs = new JComponent[] {
				new JLabel("Ancien nom"),
				oldTitleField,
				new JLabel("Nom*"),
				titleField,
		};

		int result = JOptionPane.showConfirmDialog(this, inputs, "Modifier un utilisateur", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			String titre = titleField.getText();

			if(titre.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Erreur: Le titre doit être entré.", "Erreur titre vide", JOptionPane.ERROR_MESSAGE);
			} else {
				User newUser = new User(titre);
				gestionnaireUser.editUser(oldTitleField.getText(), newUser); // Ajouter l'utilisateur à votre gestionnaire d'utilisateur
			}

			// Mettre à jour le modèle de tableau après l'ajout de l'utilisateur
			List<User> updatedUser = gestionnaireUser.getUser(); // Récupérer la liste mise à jour des utilisateurs
			tableModel.setUser(updatedUser); // Mettre à jour le modèle du tableau

			afficheMessage();
		}

	}

	public JTable getTableArea() {
		return tableArea;
	}

	public GestionnaireUser getGestionnaire() {
		return gestionnaireUser;
	}

	public void afficheMessage() {
		String[] message = gestionnaireUser.getMessage();

		if(message[0] != null) {
			if(Objects.equals(message[0], "e")) {
				JOptionPane.showMessageDialog(this, message[2], message[1], JOptionPane.ERROR_MESSAGE);
			} else if(Objects.equals(message[0], "i")) {
				JOptionPane.showMessageDialog(this, message[2], message[1], JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
}
