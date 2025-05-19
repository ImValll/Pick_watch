package view.parameter.users;

import model.ButtonEditor;
import model.ButtonRenderer;
import model.DataManager;
import model.genre.User;
import model.parameter.users.GestionnaireUser;
import model.parameter.users.UserTableModel;
import view.parameter.ParameterFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class PanelUser extends JPanel {

	DataManager dataManager = new DataManager();

	ParameterFrame userFrame;

	private CardLayout cardLayout = new CardLayout();
	private JPanel cards = new JPanel(cardLayout); // Panel that uses CardLayout
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

		JButton searchButton = createButton("Rechercher un utilisateur", new Color(70, 130, 180));
		searchButton.addActionListener(this::searchUser);

		JButton addUserButton = createButton("Ajouter un utilisateur", new Color(70, 130, 180));

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

		JButton btnBack = createButton("Retour", new Color(70, 130, 180));
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
				for (int i = 0; i < listUser.size(); i++) {
					User user = listUser.get(i);
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

		java.util.List<User> updatedUser = gestionnaireUser.getUser(); // Récupérer la liste mise à jour des utilisateurs
		tableModel.setUser(updatedUser); // Mettre à jour le modèle du tableau
	}

	public void editUser(User user) {

		String oldTitle = user.getName();
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
				gestionnaireUser.editUser(oldTitle, newUser); // Ajouter l'utilisateur à votre gestionnaire d'utilisateur
			}

			// Mettre à jour le modèle de tableau après l'ajout de l'utilisateur
			List<User> updatedUser = gestionnaireUser.getUser(); // Récupérer la liste mise à jour des utilisateurs
			tableModel.setUser(updatedUser); // Mettre à jour le modèle du tableau

			JOptionPane.showMessageDialog(this, "Utilisateur modifié avec succès: " + titre, "Utilisateur modifié", JOptionPane.INFORMATION_MESSAGE);
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

	public GestionnaireUser getGestionnaire() {
		return gestionnaireUser;
	}
}
