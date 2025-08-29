package view.parameter.actors;

import model.ButtonEditor;
import model.ButtonRenderer;
import model.Language;
import model.parameter.actors.GestionnaireActor;
import model.parameter.actors.Actor;
import model.parameter.actors.ActorTableModel;
import view.parameter.ParameterFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Objects;

public class PanelActor extends JPanel {

	ParameterFrame actorFrame;

	private final CardLayout cardLayout = new CardLayout();
	private final JPanel cards = new JPanel(cardLayout); // Panel that uses CardLayout
	private JTable tableArea; // Display book information
	private ActorTableModel tableModel;
	GestionnaireActor gestionnaireActor;
	private JTextField searchTitleField;

	public PanelActor(GestionnaireActor gestionnaireActor, ParameterFrame actorFrame) {
		this.gestionnaireActor = gestionnaireActor;
		this.actorFrame = actorFrame;
		initializeUI();
	}

	private void initializeUI() {
		setLayout(new BorderLayout());
		add(cards, BorderLayout.CENTER);

		JPanel actorGrid = createActorPanel();

		add(actorGrid);
	}

	private JPanel createActorPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		// Top panel for search
		JPanel topPanel = new JPanel();
		searchTitleField = new JTextField(20);

		JButton searchButton = ButtonEditor.createButton(Language.getBundle().getString("actor.btnSearch"), new Color(70, 130, 180));
		searchButton.addActionListener(this::searchActor);

		JButton addActorButton = ButtonEditor.createButton(Language.getBundle().getString("actor.ajouterActeur"), new Color(70, 130, 180));

		JLabel titleLabel = new JLabel(Language.getBundle().getString("param.nom"));
		titleLabel.setForeground(Color.WHITE);
		topPanel.add(titleLabel);

		topPanel.add(searchTitleField);
		topPanel.add(searchButton);
		topPanel.add(addActorButton);

		addActorButton.addActionListener(e -> addActor());

		// Get JScrollPane from showAllActor method
		JScrollPane scrollPane = showAllActor();

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

	private JScrollPane showAllActor() {
		java.util.List<Actor> listActor = gestionnaireActor.getActor();
		tableModel = new ActorTableModel(listActor);
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

	private void searchActor(ActionEvent e) {
		String titre = searchTitleField.getText();
		java.util.List<Actor> result = gestionnaireActor.searchActor(titre);
		if (result.isEmpty()) {
			JOptionPane.showMessageDialog(this, Language.getBundle().getString("erreur.erreurPasActeurTrouveTitre"), Language.getBundle().getString("app.erreur"), JOptionPane.ERROR_MESSAGE);
		} else {
			tableModel.setActor(result); // Mettre à jour le modèle du tableau
		}
	}

	private void addActor() {
		JTextField titleField = new JTextField();

		final JComponent[] inputs = new JComponent[] {
				new JLabel(Language.getBundle().getString("param.nomEtoile")),
				titleField,
		};

		int result = JOptionPane.showConfirmDialog(this, inputs, Language.getBundle().getString("actor.ajouterNouveauActeur"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			String titre = titleField.getText();

			if(titre.isEmpty()) {
				JOptionPane.showMessageDialog(this, Language.getBundle().getString("erreur.erreurNomVide"), Language.getBundle().getString("erreur.nomVide"), JOptionPane.ERROR_MESSAGE);
			}
			else {

				boolean canBeAdd = true;

				java.util.List<Actor> listActor = gestionnaireActor.getActor();
				for (Actor actor : listActor) {
					if (actor.getName().equalsIgnoreCase(titre)) {
						canBeAdd = false;
						JOptionPane.showMessageDialog(this, Language.getBundle().getString("erreur.erreurDoublonPartie1Acteur") + titre + Language.getBundle().getString("erreur.erreurDoublonPartie2Masculin"), Language.getBundle().getString("erreur.doublon"), JOptionPane.ERROR_MESSAGE);
						break;
					}
				}

				if (canBeAdd) {
					Actor newActor = new Actor(titre);
					gestionnaireActor.addActor(newActor); // Ajouter l'acteur à votre gestionnaire d'acteur
					JOptionPane.showMessageDialog(this, Language.getBundle().getString("actor.annonceActeurAjoute") + titre, Language.getBundle().getString("actor.acteurAjoute"), JOptionPane.INFORMATION_MESSAGE);
				}
			}

			// Mettre à jour le modèle de tableau après l'ajout de l'acteur
			java.util.List<Actor> updatedActor = gestionnaireActor.getActor(); // Récupérer la liste mise à jour des acteurs
			tableModel.setActor(updatedActor); // Mettre à jour le modèle du tableau
		}
	}

	public void backMenu() {
		actorFrame.dispose();
		new ParameterFrame();
	}

	public void deleteActor(Actor actor) {
		gestionnaireActor.deleteActor(actor);
		afficheMessage();

		java.util.List<Actor> updatedActor = gestionnaireActor.getActor(); // Récupérer la liste mise à jour des acteurs
		tableModel.setActor(updatedActor); // Mettre à jour le modèle du tableau
	}

	public void editActor(Actor actor) {

		JTextField oldTitleField = new JTextField(actor.getName());
		oldTitleField.setEnabled(false);

		JTextField titleField = new JTextField(actor.getName());



		final JComponent[] inputs = new JComponent[] {
				new JLabel(Language.getBundle().getString("param.ancienNom")),
				oldTitleField,
				new JLabel(Language.getBundle().getString("param.nomEtoile")),
				titleField,
		};

		int result = JOptionPane.showConfirmDialog(this, inputs, Language.getBundle().getString("actor.modifierActeur"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			String titre = titleField.getText();

			if(titre.isEmpty()) {
				JOptionPane.showMessageDialog(this, Language.getBundle().getString("erreur.erreurNomVide"), Language.getBundle().getString("erreur.nomVide"), JOptionPane.ERROR_MESSAGE);
			} else {
				Actor newActor = new Actor(titre);
				gestionnaireActor.editActor(oldTitleField.getText(), newActor); // Ajouter l'acteur à votre gestionnaire d'acteur
			}

			// Mettre à jour le modèle de tableau après l'ajout de l'acteur
			List<Actor> updatedActor = gestionnaireActor.getActor(); // Récupérer la liste mise à jour des acteurs
			tableModel.setActor(updatedActor); // Mettre à jour le modèle du tableau

			afficheMessage();
		}

	}

	public JTable getTableArea() {
		return tableArea;
	}

	public GestionnaireActor getGestionnaire() {
		return gestionnaireActor;
	}

	public void afficheMessage() {
		String[] message = gestionnaireActor.getMessage();

		if(message[0] != null) {
			if(Objects.equals(message[0], "e")) {
				JOptionPane.showMessageDialog(this, message[2], message[1], JOptionPane.ERROR_MESSAGE);
			} else if(Objects.equals(message[0], "i")) {
				JOptionPane.showMessageDialog(this, message[2], message[1], JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
}
