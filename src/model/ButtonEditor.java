package model;

import model.parameter.actors.Actor;
import model.parameter.genres.Genre;
import model.parameter.platforms.Platform;
import model.parameter.users.User;
import model.movie.Movie;
import model.saga.Saga;
import model.serie.Serie;
import model.serie_courte.SerieCourte;
import view.movie.PanelMovies;
import view.parameter.actors.PanelActor;
import view.parameter.genres.PanelGenre;
import view.parameter.platforms.PanelPlatform;
import view.parameter.users.PanelUser;
import view.saga.PanelSaga;
import view.serie.PanelSerie;
import view.serie_courte.PanelSerieCourte;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {

	private final JButton button;
	private String label;
	private int row;

	public ButtonEditor(PanelMovies panel) {
		super();
		button = new JButton();
		button.setOpaque(true);
		button.addActionListener(e -> {
			fireEditingStopped();
			// Get the movie from the table model using the row index
			Movie movie = panel.getGestionnaire().findMovieByTitle(panel.getTableArea().getValueAt(row, 0).toString()); // Assuming the first column holds Movie objects
			if (Language.getBundle().getString("app.visualiser").equals(label)) {
				panel.detailsMovie(movie);
			} else if (Language.getBundle().getString("app.modifier").equals(label)) {
				panel.editMovie(movie);
			} else if (Language.getBundle().getString("app.supprimer").equals(label)) {
				panel.deleteMovie(movie);
			}
		});
	}

	public ButtonEditor(PanelSaga panel) {
		super();
		button = new JButton();
		button.setOpaque(true);
		button.addActionListener(e -> {
			fireEditingStopped();
			// Get the saga from the table model using the row index
			Saga saga = panel.getGestionnaire().findSagaByTitle(panel.getTableArea().getValueAt(row, 0).toString()); // Assuming the first column holds Saga objects
			if (Language.getBundle().getString("app.visualiser").equals(label)) {
				panel.detailsSaga(saga);
			} else if (Language.getBundle().getString("app.modifier").equals(label)) {
				panel.editSaga(saga);
			} else if (Language.getBundle().getString("app.supprimer").equals(label)) {
				panel.deleteSaga(saga);
			}
		});
	}

	public ButtonEditor(PanelSerie panel) {
		super();
		button = new JButton();
		button.setOpaque(true);
		button.addActionListener(e -> {
			fireEditingStopped();
			// Get the serie from the table model using the row index
			Serie serie = panel.getGestionnaire().findSerieByTitle(panel.getTableArea().getValueAt(row, 0).toString()); // Assuming the first column holds Serie objects
			if (Language.getBundle().getString("app.visualiser").equals(label)) {
				panel.detailsSerie(serie);
			} else if (Language.getBundle().getString("app.modifier").equals(label)) {
				panel.editSerie(serie);
			} else if (Language.getBundle().getString("app.supprimer").equals(label)) {
				panel.deleteSerie(serie);
			}
		});
	}

	public ButtonEditor(PanelSerieCourte panel) {
		super();
		button = new JButton();
		button.setOpaque(true);
		button.addActionListener(e -> {
			fireEditingStopped();
			// Get the serie from the table model using the row index
			SerieCourte serieCourte = panel.getGestionnaire().findSerieCourteByTitle(panel.getTableArea().getValueAt(row, 0).toString()); // Assuming the first column holds Serie objects
			if (Language.getBundle().getString("app.visualiser").equals(label)) {
				panel.detailsSerieCourte(serieCourte);
			} else if (Language.getBundle().getString("app.modifier").equals(label)) {
				panel.editSerieCourte(serieCourte);
			} else if (Language.getBundle().getString("app.supprimer").equals(label)) {
				panel.deleteSerieCourte(serieCourte);
			}
		});
	}

	public ButtonEditor(PanelGenre panel) {
		super();
		button = new JButton();
		button.setOpaque(true);
		button.addActionListener(e -> {
			fireEditingStopped();
			// Get the genre from the table model using the row index
			Genre genre = panel.getGestionnaire().findGenreByTitle(panel.getTableArea().getValueAt(row, 0).toString()); // Assuming the first column holds Serie objects
			if (Language.getBundle().getString("app.modifier").equals(label)) {
				panel.editGenre(genre);
			} else if (Language.getBundle().getString("app.supprimer").equals(label)) {
				panel.deleteGenre(genre);
			}
		});
	}

	public ButtonEditor(PanelPlatform panel) {
		super();
		button = new JButton();
		button.setOpaque(true);
		button.addActionListener(e -> {
			fireEditingStopped();
			// Get the platform from the table model using the row index
			Platform platform = panel.getGestionnaire().findPlatformByTitle(panel.getTableArea().getValueAt(row, 0).toString()); // Assuming the first column holds Plateform objects
			if (Language.getBundle().getString("app.modifier").equals(label)) {
				panel.editPlatform(platform);
			} else if (Language.getBundle().getString("app.supprimer").equals(label)) {
				panel.deletePlatform(platform);
			}
		});
	}

	public ButtonEditor(PanelUser panel) {
		super();
		button = new JButton();
		button.setOpaque(true);
		button.addActionListener(e -> {
			fireEditingStopped();
			// Get the user from the table model using the row index
			User user = panel.getGestionnaire().findUserByTitle(panel.getTableArea().getValueAt(row, 0).toString()); // Assuming the first column holds User objects
			if (Language.getBundle().getString("app.modifier").equals(label)) {
				panel.editUser(user);
			} else if (Language.getBundle().getString("app.supprimer").equals(label)) {
				panel.deleteUser(user);
			}
		});
	}

	public ButtonEditor(PanelActor panel) {
		super();
		button = new JButton();
		button.setOpaque(true);
		button.addActionListener(e -> {
			fireEditingStopped();
			// Get the actor from the table model using the row index
				Actor actor = panel.getGestionnaire().findActorByTitle(panel.getTableArea().getValueAt(row, 0).toString()); // Assuming the first column holds User objects
			if (Language.getBundle().getString("app.modifier").equals(label)) {
				panel.editActor(actor);
			} else if (Language.getBundle().getString("app.supprimer").equals(label)) {
				panel.deleteActor(actor);
			}
		});
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		this.row = row;
		label = value.toString();
		button.setText(label);
		return button;
	}

	@Override
	public Object getCellEditorValue() {
		return label;
	}

	@Override
	public boolean stopCellEditing() {
		return super.stopCellEditing();
	}

	public static JButton createButton(String title, Color color) {
		JButton button = new JButton(title);

		button.setBackground(color); // Bleu foncé
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Arial", Font.BOLD, 18));
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

		return button;
	}
}
