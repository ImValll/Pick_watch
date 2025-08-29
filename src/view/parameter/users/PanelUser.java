package view.parameter.users;

import model.ButtonEditor;
import model.ButtonRenderer;
import model.Language;
import model.parameter.users.User;
import model.parameter.users.GestionnaireUser;
import model.parameter.users.UserTableModel;
import view.parameter.ParameterFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
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

		JButton searchButton = ButtonEditor.createButton(Language.getBundle().getString("user.btnSearch"), new Color(70, 130, 180));
		searchButton.addActionListener(this::searchUser);

		JButton addUserButton = ButtonEditor.createButton(Language.getBundle().getString("user.ajouterUser"), new Color(70, 130, 180));

		JLabel titleLabel = new JLabel(Language.getBundle().getString("param.nom"));
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

		JButton btnBack = ButtonEditor.createButton(Language.getBundle().getString("app.retour"), new Color(70, 130, 180));
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
			if (!columnName.equals(Language.getBundle().getString("app.modifier")) && !columnName.equals(Language.getBundle().getString("app.supprimer"))) {
				tableArea.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
			}
		}

		tableArea.getColumn(Language.getBundle().getString("app.modifier")).setCellRenderer(new ButtonRenderer());
		tableArea.getColumn(Language.getBundle().getString("app.modifier")).setCellEditor(new ButtonEditor(this));

		tableArea.getColumn(Language.getBundle().getString("app.supprimer")).setCellRenderer(new ButtonRenderer());
		tableArea.getColumn(Language.getBundle().getString("app.supprimer")).setCellEditor(new ButtonEditor(this));

		JScrollPane scrollPane = new JScrollPane(tableArea);
		scrollPane.getViewport().setBackground(new Color(60, 63, 65));
		scrollPane.setBorder(BorderFactory.createEmptyBorder());

		return scrollPane;
	}

	private void searchUser(ActionEvent e) {
		String titre = searchTitleField.getText();
		java.util.List<User> result = gestionnaireUser.searchUser(titre);
		if (result.isEmpty()) {
			JOptionPane.showMessageDialog(this, Language.getBundle().getString("erreur.erreurPasUserTrouveTitre"), Language.getBundle().getString("app.erreur"), JOptionPane.ERROR_MESSAGE);
		} else {
			tableModel.setUser(result); // Mettre à jour le modèle du tableau
		}
	}

	private void addUser() {
		JTextField titleField = new JTextField();

		final JComponent[] inputs = new JComponent[] {
				new JLabel(Language.getBundle().getString("param.nomEtoile")),
				titleField,
		};

		int result = JOptionPane.showConfirmDialog(this, inputs, Language.getBundle().getString("user.ajouterNouveauUser"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			String titre = titleField.getText();

			if(titre.isEmpty()) {
				JOptionPane.showMessageDialog(this, Language.getBundle().getString("erreur.erreurNomVide"), Language.getBundle().getString("erreur.nomVide"), JOptionPane.ERROR_MESSAGE);
			}
			else {

				boolean canBeAdd = true;

				java.util.List<User> listUser = gestionnaireUser.getUser();
				for (User user : listUser) {
					if (user.getName().equalsIgnoreCase(titre)) {
						canBeAdd = false;
						JOptionPane.showMessageDialog(this, Language.getBundle().getString("erreur.erreurDoublonPartie1User") + titre + Language.getBundle().getString("erreur.erreurDoublonPartie2Masculin"), Language.getBundle().getString("erreur.doublon"), JOptionPane.ERROR_MESSAGE);
						break;
					}
				}

				if (canBeAdd) {
					User newUser = new User(titre);
					gestionnaireUser.addUser(newUser); // Ajouter l'utilisateur à votre gestionnaire d'utilisateur
					JOptionPane.showMessageDialog(this, Language.getBundle().getString("user.annonceUserAjoute") + titre, Language.getBundle().getString("user.userAjoute"), JOptionPane.INFORMATION_MESSAGE);
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
				new JLabel(Language.getBundle().getString("param.ancienNom")),
				oldTitleField,
				new JLabel(Language.getBundle().getString("param.nomEtoile")),
				titleField,
		};

		int result = JOptionPane.showConfirmDialog(this, inputs, Language.getBundle().getString("user.modifierUser"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			String titre = titleField.getText();

			if(titre.isEmpty()) {
				JOptionPane.showMessageDialog(this, Language.getBundle().getString("erreur.erreurNomVide"), Language.getBundle().getString("erreur.nomVide"), JOptionPane.ERROR_MESSAGE);
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
