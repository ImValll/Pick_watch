package view.parameter.genres;

import model.ButtonEditor;
import model.ButtonRenderer;
import model.parameter.genres.Genre;
import model.parameter.genres.GenreTableModel;
import model.parameter.genres.GestionnaireGenre;
import view.parameter.ParameterFrame;

import javax.swing.*;
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

		JButton searchButton = ButtonEditor.createButton("Rechercher un genre", new Color(70, 130, 180));
		searchButton.addActionListener(this::searchGenre);

		JButton addGenreButton = ButtonEditor.createButton("Ajouter un genre", new Color(70, 130, 180));

		JLabel titleLabel = new JLabel("Titre : ");
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

		JButton btnBack = ButtonEditor.createButton("Retour", new Color(70, 130, 180));
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
		tableArea.setBackground(Color.LIGHT_GRAY);

		// Ajout de rendus personnalisés pour les colonnes "Modifier" et "Supprimer"
		tableArea.getColumn("Modifier").setCellRenderer(new ButtonRenderer());
		tableArea.getColumn("Modifier").setCellEditor(new ButtonEditor(this));

		tableArea.getColumn("Supprimer").setCellRenderer(new ButtonRenderer());
		tableArea.getColumn("Supprimer").setCellEditor(new ButtonEditor(this));

		return new JScrollPane(tableArea);
	}

	private void searchGenre(ActionEvent e) {
		String titre = searchTitleField.getText();
		java.util.List<Genre> result = gestionnaireGenre.searchGenre(titre);
		if (result.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Erreur: Aucun genre trouvé pour ce titre.", "Erreur", JOptionPane.ERROR_MESSAGE);
		} else {
			tableModel.setGenre(result); // Mettre à jour le modèle du tableau
		}
	}

	private void addGenre() {
		JTextField titleField = new JTextField();

		final JComponent[] inputs = new JComponent[] {
				new JLabel("Titre*"),
				titleField,
		};

		int result = JOptionPane.showConfirmDialog(this, inputs, "Ajouter un nouveau genre", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			String titre = titleField.getText();

			if(titre.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Erreur: Le titre doit être entré.", "Erreur titre vide", JOptionPane.ERROR_MESSAGE);
			}
			else {

				boolean canBeAdd = true;

				java.util.List<Genre> listGenre = gestionnaireGenre.getGenre();
				for (Genre genre : listGenre) {
					if (genre.getName().equalsIgnoreCase(titre)) {
						canBeAdd = false;
						JOptionPane.showMessageDialog(this, "Erreur: Le genre " + titre + " a déjà été ajouté", "Erreur doublons", JOptionPane.ERROR_MESSAGE);
						break;
					}
				}

				if (canBeAdd) {
					Genre newGenre = new Genre(titre);
					gestionnaireGenre.addGenre(newGenre); // Ajouter le genre à votre gestionnaire de genre
					JOptionPane.showMessageDialog(this, "Genre ajouté avec succès: " + titre, "Genre Ajouté", JOptionPane.INFORMATION_MESSAGE);
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
				new JLabel("Ancien titre"),
				oldTitleField,
				new JLabel("Titre*"),
				titleField,
		};

		int result = JOptionPane.showConfirmDialog(this, inputs, "Modifier un genre", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			String titre = titleField.getText();

			if(titre.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Erreur: Le titre doit être entré.", "Erreur titre vide", JOptionPane.ERROR_MESSAGE);
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
