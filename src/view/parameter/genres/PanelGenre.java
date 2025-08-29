package view.parameter.genres;

import model.ButtonEditor;
import model.ButtonRenderer;
import model.Language;
import model.parameter.genres.Genre;
import model.parameter.genres.GenreTableModel;
import model.parameter.genres.GestionnaireGenre;
import view.parameter.ParameterFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Objects;

public class PanelGenre extends JPanel {

	ParameterFrame genreFrame;

	private final CardLayout cardLayout = new CardLayout();
	private final JPanel cards = new JPanel(cardLayout); // Panel that uses CardLayout
	private JTable tableArea; // Display book information
	private GenreTableModel tableModel;
	GestionnaireGenre gestionnaireGenre;
	private JTextField searchTitleField;

	public PanelGenre(GestionnaireGenre gestionnaireGenre, ParameterFrame genreFrame) {
		this.gestionnaireGenre = gestionnaireGenre;
		this.genreFrame = genreFrame;
		initializeUI();
	}

	private void initializeUI() {
		setLayout(new BorderLayout());
		add(cards, BorderLayout.CENTER);

		JPanel genreGrid = createGenrePanel();

		add(genreGrid);
	}

	private JPanel createGenrePanel() {
		JPanel panel = new JPanel(new BorderLayout());

		// Top panel for search
		JPanel topPanel = new JPanel();
		searchTitleField = new JTextField(20);

		JButton searchButton = ButtonEditor.createButton(Language.getBundle().getString("genre.btnSearch"), new Color(70, 130, 180));
		searchButton.addActionListener(this::searchGenre);

		JButton addGenreButton = ButtonEditor.createButton(Language.getBundle().getString("genre.ajouterGenre"), new Color(70, 130, 180));

		JLabel titleLabel = new JLabel(Language.getBundle().getString("param.nom"));
		titleLabel.setForeground(Color.WHITE);
		topPanel.add(titleLabel);

		topPanel.add(searchTitleField);
		topPanel.add(searchButton);
		topPanel.add(addGenreButton);

		addGenreButton.addActionListener(e -> addGenre());

		// Get JScrollPane from showAllGenre method
		JScrollPane scrollPane = showAllGenre();

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

	private JScrollPane showAllGenre() {
		java.util.List<Genre> listGenre = gestionnaireGenre.getGenre();
		tableModel = new GenreTableModel(listGenre);
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

	private void searchGenre(ActionEvent e) {
		String titre = searchTitleField.getText();
		java.util.List<Genre> result = gestionnaireGenre.searchGenre(titre);
		if (result.isEmpty()) {
			JOptionPane.showMessageDialog(this, Language.getBundle().getString("erreur.erreurPasGenreTrouveTitre"), Language.getBundle().getString("app.erreur"), JOptionPane.ERROR_MESSAGE);
		} else {
			tableModel.setGenre(result); // Mettre à jour le modèle du tableau
		}
	}

	private void addGenre() {
		JTextField titleField = new JTextField();

		final JComponent[] inputs = new JComponent[] {
				new JLabel(Language.getBundle().getString("param.nomEtoile")),
				titleField,
		};

		int result = JOptionPane.showConfirmDialog(this, inputs, Language.getBundle().getString("genre.ajouterNouveauGenre"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			String titre = titleField.getText();

			if(titre.isEmpty()) {
				JOptionPane.showMessageDialog(this, Language.getBundle().getString("erreur.erreurNomVide"), Language.getBundle().getString("erreur.nomVide"), JOptionPane.ERROR_MESSAGE);
			}
			else {

				boolean canBeAdd = true;

				java.util.List<Genre> listGenre = gestionnaireGenre.getGenre();
				for (Genre genre : listGenre) {
					if (genre.getName().equalsIgnoreCase(titre)) {
						canBeAdd = false;
						JOptionPane.showMessageDialog(this, Language.getBundle().getString("erreur.erreurDoublonPartie1Genre") + titre + Language.getBundle().getString("erreur.erreurDoublonPartie2Masculin"), Language.getBundle().getString("erreur.doublon"), JOptionPane.ERROR_MESSAGE);
						break;
					}
				}

				if (canBeAdd) {
					Genre newGenre = new Genre(titre);
					gestionnaireGenre.addGenre(newGenre); // Ajouter le genre à votre gestionnaire de genre
					JOptionPane.showMessageDialog(this, Language.getBundle().getString("genre.annonceGenreAjoute") + titre, Language.getBundle().getString("genre.genreAjoute"), JOptionPane.INFORMATION_MESSAGE);
				}
			}

			// Mettre à jour le modèle de tableau après l'ajout du genre
			java.util.List<Genre> updatedGenre = gestionnaireGenre.getGenre(); // Récupérer la liste mise à jour des series
			tableModel.setGenre(updatedGenre); // Mettre à jour le modèle du tableau
		}
	}

	public void backMenu() {
		genreFrame.dispose();
		new ParameterFrame();
	}

	public void deleteGenre(Genre genre) {
		gestionnaireGenre.deleteGenre(genre);
		afficheMessage();

		java.util.List<Genre> updatedGenre = gestionnaireGenre.getGenre(); // Récupérer la liste mise à jour des series
		tableModel.setGenre(updatedGenre); // Mettre à jour le modèle du tableau
	}

	public void editGenre(Genre genre) {

		JTextField oldTitleField = new JTextField(genre.getName());
		oldTitleField.setEnabled(false);

		JTextField titleField = new JTextField(genre.getName());



		final JComponent[] inputs = new JComponent[] {
				new JLabel(Language.getBundle().getString("param.ancienNom")),
				oldTitleField,
				new JLabel(Language.getBundle().getString("param.nomEtoile")),
				titleField,
		};

		int result = JOptionPane.showConfirmDialog(this, inputs, Language.getBundle().getString("genre.modifierGenre"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			String titre = titleField.getText();

			if(titre.isEmpty()) {
				JOptionPane.showMessageDialog(this, Language.getBundle().getString("erreur.erreurNomVide"), Language.getBundle().getString("erreur.nomVide"), JOptionPane.ERROR_MESSAGE);
			} else {
				Genre newGenre = new Genre(titre);
				gestionnaireGenre.editGenre(oldTitleField.getText(), newGenre); // Ajouter le genre à votre gestionnaire de genre
			}

			// Mettre à jour le modèle de tableau après l'ajout du genre
			List<Genre> updatedGenre = gestionnaireGenre.getGenre(); // Récupérer la liste mise à jour des genres
			tableModel.setGenre(updatedGenre); // Mettre à jour le modèle du tableau

			afficheMessage();
		}

	}

	public JTable getTableArea() {
		return tableArea;
	}

	public GestionnaireGenre getGestionnaire() {
		return gestionnaireGenre;
	}

	public void afficheMessage() {
		String[] message = gestionnaireGenre.getMessage();

		if(message[0] != null) {
			if(Objects.equals(message[0], "e")) {
				JOptionPane.showMessageDialog(this, message[2], message[1], JOptionPane.ERROR_MESSAGE);
			} else if(Objects.equals(message[0], "i")) {
				JOptionPane.showMessageDialog(this, message[2], message[1], JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
}
