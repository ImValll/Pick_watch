package view.parameter;

import model.parameter.genres.GestionnaireGenre;
import model.parameter.platforms.GestionnairePlatform;
import model.parameter.users.GestionnaireUser;
import view.Frame;
import view.parameter.genres.PanelGenre;
import view.parameter.platforms.PanelPlatform;
import view.parameter.users.PanelUser;

import javax.swing.*;
import java.awt.*;

public class ParameterFrame extends JFrame {

	public ParameterFrame() {

		super("PARAMETRES");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1200, 700); // Taille augmentée
		setLocationRelativeTo(null);
		getContentPane().setBackground(new Color(50, 50, 50)); // Fond sombre pour le contraste
		setLayout(new GridBagLayout()); // Utilisation de GridBagLayout pour un meilleur contrôle

		JButton btnManageUser = createButton("Gérer les utilisateurs", new Color(70, 130, 180));
		JButton btnManageGenre = createButton("Gérer les genres", new Color(70, 130, 180));
		JButton btnManagePlatform = createButton("Gérer les plateformes", new Color(70, 130, 180));
		JButton btnMenu = createButton("Retour au menu", new Color(220, 20, 60));


		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(15, 30, 15, 30);

		add(btnManageUser, gbc);
		add(btnManageGenre, gbc);
		add(btnManagePlatform, gbc);
		add(btnMenu, gbc);

		btnManageUser.addActionListener(e -> manageUser());
		btnManageGenre.addActionListener(e -> manageGenre());
		btnManagePlatform.addActionListener(e -> managePlatform());
		btnMenu.addActionListener(e -> backToMenu());

		this.setVisible(true);
	}

	private void manageUser() {
		// Initialisation du Panel utilisateur
		PanelUser panelUser = new PanelUser(new GestionnaireUser(), this);
		setContentPane(panelUser);
		validate();
	}

	private void manageGenre() {
		// Initialisation du Panel genre
		PanelGenre panelGenre = new PanelGenre(new GestionnaireGenre(), this);
		setContentPane(panelGenre);
		validate();
	}

	private void managePlatform() {
		// Initialisation du Panel plateforme
		PanelPlatform panelPlatform = new PanelPlatform(new GestionnairePlatform(), this);
		setContentPane(panelPlatform);
		validate();
	}

	private void backToMenu() {
		this.dispose();
		new Frame();
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
}
