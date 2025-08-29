package view.parameter;

import model.ButtonEditor;
import model.Language;
import model.parameter.actors.GestionnaireActor;
import model.parameter.genres.GestionnaireGenre;
import model.parameter.platforms.GestionnairePlatform;
import model.parameter.users.GestionnaireUser;
import view.Frame;
import view.parameter.actors.PanelActor;
import view.parameter.genres.PanelGenre;
import view.parameter.platforms.PanelPlatform;
import view.parameter.users.PanelUser;

import javax.swing.*;
import java.awt.*;

public class ParameterFrame extends JFrame {

	public ParameterFrame() {

		super(Language.getBundle().getString("param.title"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1200, 700); // Taille augmentée
		setLocationRelativeTo(null);
		getContentPane().setBackground(new Color(50, 50, 50)); // Fond sombre pour le contraste
		setLayout(new GridBagLayout()); // Utilisation de GridBagLayout pour un meilleur contrôle

		JButton btnManageUser = ButtonEditor.createButton(Language.getBundle().getString("param.btnUser"), new Color(70, 130, 180));
		JButton btnManageGenre = ButtonEditor.createButton(Language.getBundle().getString("param.btnGenre"), new Color(70, 130, 180));
		JButton btnManagePlatform = ButtonEditor.createButton(Language.getBundle().getString("param.btnPlateforme"), new Color(70, 130, 180));
		JButton btnManageActor = ButtonEditor.createButton(Language.getBundle().getString("param.btnActeur"), new Color(70, 130, 180));
		JButton btnMenu = ButtonEditor.createButton(Language.getBundle().getString("app.btnMenu"), new Color(220, 20, 60));


		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(15, 30, 15, 30);

		add(btnManageUser, gbc);
		add(btnManageGenre, gbc);
		add(btnManagePlatform, gbc);
		add(btnManageActor, gbc);
		add(btnMenu, gbc);

		btnManageUser.addActionListener(e -> manageUser());
		btnManageGenre.addActionListener(e -> manageGenre());
		btnManagePlatform.addActionListener(e -> managePlatform());
		btnManageActor.addActionListener(e -> manageActor());
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

	private void manageActor() {
		// Initialisation du Panel actor
		PanelActor panelActor = new PanelActor(new GestionnaireActor(), this);
		setContentPane(panelActor);
		validate();
	}

	private void backToMenu() {
		this.dispose();
		new Frame();
	}
}
