package view;

import model.ButtonEditor;
import model.DataManager;
import model.Language;
import view.movie.MovieFrame;
import view.parameter.ParameterFrame;
import view.saga.SagaFrame;
import view.serie.SerieFrame;
import view.serie_courte.SerieCourteFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

public class Frame extends JFrame {
	public Frame() {

		super(Language.getBundle().getString("menu.title"));
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

		JButton btnMovie = ButtonEditor.createButton(Language.getBundle().getString("menu.btnMovie"), new Color(70, 130, 180));
		JButton btnSaga = ButtonEditor.createButton(Language.getBundle().getString("menu.btnSaga"), new Color(70, 130, 180));
		JButton btnSerie = ButtonEditor.createButton(Language.getBundle().getString("menu.btnSerie"), new Color(70, 130, 180));
		JButton btnSerieCourte = ButtonEditor.createButton(Language.getBundle().getString("menu.btnSerieCourte"), new Color(70, 130, 180));
		JButton btnParameter = ButtonEditor.createButton(Language.getBundle().getString("menu.btnParametre"), new Color(220, 20, 60));
		JButton btnQuitter = ButtonEditor.createButton(Language.getBundle().getString("menu.btnQuitter"), new Color(220, 20, 60));


		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(15, 30, 15, 30);

		centerPanel.add(btnMovie, gbc);
		centerPanel.add(btnSaga, gbc);
		centerPanel.add(btnSerie, gbc);
		centerPanel.add(btnSerieCourte, gbc);
		centerPanel.add(btnParameter, gbc);
		centerPanel.add(btnQuitter, gbc);

		add(centerPanel, BorderLayout.CENTER);

		btnMovie.addActionListener(e -> movieFunction());
		btnSaga.addActionListener(e -> sagaFunction());
		btnSerie.addActionListener(e -> serieFunction());
		btnSerieCourte.addActionListener(e -> serieCourteFunction());
		btnParameter.addActionListener(e -> parameterFunction());
		btnQuitter.addActionListener(e -> quitter());

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

	private void movieFunction() {
		dispose();
		new MovieFrame();
	}

	private void sagaFunction() {
		dispose();
		new SagaFrame();
	}

	private void serieFunction() {
		dispose();
		new SerieFrame();
	}

	private void serieCourteFunction() {
		dispose();
		new SerieCourteFrame();
	}

	private void parameterFunction() {
		dispose();
		new ParameterFrame();
	}

	private void quitter() {
		dispose();
	}

	private void changeLanguage(Locale language) {
		DataManager.saveLanguage(language);
		Language.getInstance();
		dispose();
		new Frame();
	}
}
