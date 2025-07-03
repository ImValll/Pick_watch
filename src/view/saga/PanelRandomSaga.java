package view.saga;

import model.ButtonEditor;
import model.parameter.actors.Actor;
import model.parameter.genres.Genre;
import model.parameter.platforms.Platform;
import model.parameter.users.User;
import model.saga.GestionnaireSaga;
import model.saga.Saga;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class PanelRandomSaga extends JPanel {

	private final GestionnaireSaga gestionnaireSaga;
	private final SagaFrame sagaFrame;

	private String rea;
	private Actor[] actors;
	private Genre[] genres;
	private int nbFilm;
	private Date dateSortie;
	private Date dateSortie2;
	private Platform[] plateformes;
	private int dejaVu = -1;
	private User addBy;

	private Saga saga;
	private JPanel sagaPanel;
	private JLabel sagaLabel;

	public PanelRandomSaga(GestionnaireSaga gestionnaireSaga, SagaFrame sagaFrame) {
		this.gestionnaireSaga = gestionnaireSaga;
		this.sagaFrame = sagaFrame;
		initializeUI();
	}

	public PanelRandomSaga(GestionnaireSaga gestionnaireSaga, SagaFrame sagaFrame, String rea, Actor[] actors, Genre[] genres, int nbFilm, Date dateSortie, Date dateSortie2, Platform[] plateformes, int dejaVu, User addBy) {
		this.gestionnaireSaga = gestionnaireSaga;
		this.sagaFrame = sagaFrame;
		this.rea = rea;
		this.actors = actors;
		this.genres = genres;
		this.nbFilm = nbFilm;
		this.dateSortie = dateSortie;
		this.dateSortie2 = dateSortie2;
		this.plateformes = plateformes;
		this.dejaVu = dejaVu;
		this.addBy = addBy;

		initializeUI();
	}

	private void initializeUI() {
		setLayout(new BorderLayout());

		JPanel sagaSelectedPanel = createSagaPanel();

		add(sagaSelectedPanel);
	}

	private JPanel createSagaPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		sagaPanel = new JPanel();
		sagaLabel = new JLabel();
		sagaPanel.add(sagaLabel);

		sagaPanel.setBackground(new Color(50, 50, 50));
		panel.add(sagaPanel, BorderLayout.CENTER);

		Saga sagaSelected = gestionnaireSaga.pickRandomSaga(rea, actors, genres, nbFilm, dateSortie, dateSortie2, plateformes, dejaVu, addBy);

		if (sagaSelected == null) {
			JOptionPane.showMessageDialog(this, "Erreur: Il n'y a aucune saga dans la base de données ou aucune saga ne correspond à votre recherche.", "Erreur", JOptionPane.ERROR_MESSAGE);
			backMenu();
		} else {
			this.saga = sagaSelected;
			updateSagaInfo(sagaSelected);
		}

		JButton btnBack = ButtonEditor.createButton("MENU", new Color(70, 130, 180));
		btnBack.addActionListener(e -> backMenu());

		JButton btnGen = ButtonEditor.createButton("Générer à nouveau", new Color(70, 130, 180));
		btnGen.addActionListener(e -> generateAgain());

		JButton btnDelete = ButtonEditor.createButton("Supprimer la saga de la liste", new Color(70, 130, 180));
		btnDelete.addActionListener(e -> deleteSaga());

		JPanel bottomPanel = new JPanel();
		bottomPanel.add(btnBack);
		bottomPanel.add(btnGen);
		bottomPanel.add(btnDelete);

		bottomPanel.setBackground(new Color(50, 50, 50));
		panel.add(bottomPanel, BorderLayout.SOUTH);

		return panel;
	}

	private void updateSagaInfo(Saga sagaSelected) {
		String date;
		if (saga.getDateSortiePremier() != null) {
			date = new SimpleDateFormat("yyyy").format(sagaSelected.getDateSortiePremier());
		}
		else {
			date = "";
		}

		String date2;
		if (saga.getDateSortieDernier() != null) {
			date2 = new SimpleDateFormat("yyyy").format(sagaSelected.getDateSortieDernier());
		}
		else {
			date2 = "";
		}

		String sagaInfo = "<html>La saga choisie est : " + sagaSelected.getTitre() + " réalisée par " +
				sagaSelected.getRealistateur() + ".<br>" + Arrays.toString(sagaSelected.getActeur()) +
				" a/ont joué dedans.<br>La saga appartient à/aux genre(s) " +
				Arrays.toString(sagaSelected.getGenre()) + ".<br>Elle possède " + sagaSelected.getNombreFilms() +
				" films.<br>Le premier film est sorti en " + date + " et le dernier en " + date2 +
				".<br>Ils sont disponible sur " + Arrays.toString(sagaSelected.getPlateforme()) +
				".<br>Elle a été ajoutée par " + sagaSelected.getAddBy() + "</html>";

		sagaLabel.setForeground(Color.WHITE);

		sagaLabel.setText(sagaInfo);
		sagaPanel.revalidate();
		sagaPanel.repaint();
	}

	public void backMenu() {
		sagaFrame.dispose();
		new SagaFrame();
	}

	public void generateAgain() {
		Saga newSaga = gestionnaireSaga.pickRandomSaga(rea, actors, genres, nbFilm, dateSortie, dateSortie2, plateformes, dejaVu, addBy);
		if (newSaga == null) {
			JOptionPane.showMessageDialog(this, "Erreur: Il n'y a aucune saga dans la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
			backMenu();
		} else {
			this.saga = newSaga;
			updateSagaInfo(newSaga);
		}

	}

	public void deleteSaga() {
		gestionnaireSaga.deleteSaga(saga);

		backMenu();
	}
}
