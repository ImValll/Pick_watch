package view.parameter;

import model.ButtonEditor;
import model.DataManager;
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
import java.util.Locale;

public class ParameterFrame extends JFrame {

	public ParameterFrame() {

		super(Language.getBundle().getString("param.title"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1200, 700); // Taille augmentée
		setLocationRelativeTo(null);
		getContentPane().setBackground(new Color(50, 50, 50)); // Fond sombre pour le contraste
		setLayout(new BorderLayout());

		// Icônes (place les images drapeau dans ton dossier ressources, par ex: /images/fr.png et /images/uk.png)
		ImageIcon frIcon = new ImageIcon("src/language/imageLanguage/fr.png");
		ImageIcon ukIcon = new ImageIcon("src/language/imageLanguage/uk.png");

		JComboBox<Object> languageCombo = new JComboBox<>();

		// Icônes agrandies
		ImageIcon frBig = new ImageIcon(frIcon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH));
		ImageIcon ukBig = new ImageIcon(ukIcon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH));

		languageCombo.addItem(frBig);
		languageCombo.addItem(ukBig);

		// Taille préférée (ajustée aux drapeaux)
		languageCombo.setPreferredSize(new Dimension(70, 40));

		// Supprimer le rendu par défaut (texte) et n’afficher que l’icône
		languageCombo.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index,
														  boolean isSelected, boolean cellHasFocus) {
				JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof ImageIcon) {
					label.setIcon((ImageIcon) value);
					label.setText(""); // pas de texte
					label.setHorizontalAlignment(CENTER);
				}
				return label;
			}
		});

		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Alignement à droite
		topPanel.setBackground(new Color(50, 50, 50)); // même fond
		topPanel.add(languageCombo);
		add(topPanel, BorderLayout.NORTH);

		// ====== Panneau central pour les boutons ======
		JPanel centerPanel = new JPanel(new GridBagLayout());
		centerPanel.setBackground(new Color(50, 50, 50));

		JButton btnManageUser = ButtonEditor.createButton(Language.getBundle().getString("param.btnUser"), new Color(70, 130, 180));
		JButton btnManageGenre = ButtonEditor.createButton(Language.getBundle().getString("param.btnGenre"), new Color(70, 130, 180));
		JButton btnManagePlatform = ButtonEditor.createButton(Language.getBundle().getString("param.btnPlateforme"), new Color(70, 130, 180));
		JButton btnManageActor = ButtonEditor.createButton(Language.getBundle().getString("param.btnActeur"), new Color(70, 130, 180));
		JButton btnMenu = ButtonEditor.createButton(Language.getBundle().getString("app.btnMenu"), new Color(220, 20, 60));


		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(15, 30, 15, 30);

		centerPanel.add(btnManageUser, gbc);
		centerPanel.add(btnManageGenre, gbc);
		centerPanel.add(btnManagePlatform, gbc);
		centerPanel.add(btnManageActor, gbc);
		centerPanel.add(btnMenu, gbc);

		add(centerPanel, BorderLayout.CENTER);

		btnManageUser.addActionListener(e -> manageUser());
		btnManageGenre.addActionListener(e -> manageGenre());
		btnManagePlatform.addActionListener(e -> managePlatform());
		btnManageActor.addActionListener(e -> manageActor());
		btnMenu.addActionListener(e -> backToMenu());

		// Action lors du changement de langue
		languageCombo.addActionListener(e -> {
			int selected = languageCombo.getSelectedIndex();
			if (selected == 0) {
				changeLanguage(new Locale("fr", "FR"));
			} else {
				changeLanguage(new Locale("en", "GB"));
			}
		});

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

	private void changeLanguage(Locale language) {
		DataManager.saveLanguage(language);
		Language.getInstance();
		dispose();
		new ParameterFrame();
	}
}
