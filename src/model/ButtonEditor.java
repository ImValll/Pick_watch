package model;

import model.genre.Genre;
import model.genre.Platform;
import model.genre.User;
import model.movie.Movie;
import model.saga.Saga;
import model.serie.Serie;
import model.serie_courte.SerieCourte;
import view.movie.PanelMovies;
import view.parameter.genres.PanelGenre;
import view.parameter.platforms.PanelPlatform;
import view.parameter.users.PanelUser;
import view.saga.PanelSaga;
import view.serie.PanelSerie;
import view.serie_courte.PanelSerieCourte;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {

	private JButton button;
	private String label;
	private boolean isClicked;
	private int row;
	private JPanel panel;

	public ButtonEditor(PanelMovies panel) {
		super();
		this.panel = panel;
		button = new JButton();
		button.setOpaque(true);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireEditingStopped();
				// Get the movie from the table model using the row index
				Movie movie = panel.getGestionnaire().findMovieByTitle(panel.getTableArea().getValueAt(row, 0).toString()); // Assuming the first column holds Movie objects
				if ("Modifier".equals(label)) {
					panel.editMovie(movie);
				} else if ("Supprimer".equals(label)) {
					panel.deleteMovie(movie);
				}
			}
		});
	}

	public ButtonEditor(PanelSaga panel) {
		super();
		this.panel = panel;
		button = new JButton();
		button.setOpaque(true);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireEditingStopped();
				// Get the saga from the table model using the row index
				Saga saga = panel.getGestionnaire().findSagaByTitle(panel.getTableArea().getValueAt(row, 0).toString()); // Assuming the first column holds Saga objects
				if ("Modifier".equals(label)) {
					panel.editSaga(saga);
				} else if ("Supprimer".equals(label)) {
					panel.deleteSaga(saga);
				}
			}
		});
	}

	public ButtonEditor(PanelSerie panel) {
		super();
		this.panel = panel;
		button = new JButton();
		button.setOpaque(true);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireEditingStopped();
				// Get the serie from the table model using the row index
				Serie serie = panel.getGestionnaire().findSerieByTitle(panel.getTableArea().getValueAt(row, 0).toString()); // Assuming the first column holds Serie objects
				if ("Modifier".equals(label)) {
					panel.editSerie(serie);
				} else if ("Supprimer".equals(label)) {
					panel.deleteSerie(serie);
				}
			}
		});
	}

	public ButtonEditor(PanelSerieCourte panel) {
		super();
		this.panel = panel;
		button = new JButton();
		button.setOpaque(true);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireEditingStopped();
				// Get the serie from the table model using the row index
				SerieCourte serieCourte = panel.getGestionnaire().findSerieCourteByTitle(panel.getTableArea().getValueAt(row, 0).toString()); // Assuming the first column holds Serie objects
				if ("Modifier".equals(label)) {
					panel.editSerieCourte(serieCourte);
				} else if ("Supprimer".equals(label)) {
					panel.deleteSerieCourte(serieCourte);
				}
			}
		});
	}

	public ButtonEditor(PanelGenre panel) {
		super();
		this.panel = panel;
		button = new JButton();
		button.setOpaque(true);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireEditingStopped();
				// Get the genre from the table model using the row index
				Genre genre = panel.getGestionnaire().findGenreByTitle(panel.getTableArea().getValueAt(row, 0).toString()); // Assuming the first column holds Serie objects
				if ("Modifier".equals(label)) {
					panel.editGenre(genre);
				} else if ("Supprimer".equals(label)) {
					panel.deleteGenre(genre);
				}
			}
		});
	}

	public ButtonEditor(PanelPlatform panel) {
		super();
		this.panel = panel;
		button = new JButton();
		button.setOpaque(true);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireEditingStopped();
				// Get the platform from the table model using the row index
				Platform platform = panel.getGestionnaire().findPlatformByTitle(panel.getTableArea().getValueAt(row, 0).toString()); // Assuming the first column holds Plateform objects
				if ("Modifier".equals(label)) {
					panel.editPlatform(platform);
				} else if ("Supprimer".equals(label)) {
					panel.deletePlatform(platform);
				}
			}
		});
	}

	public ButtonEditor(PanelUser panel) {
		super();
		this.panel = panel;
		button = new JButton();
		button.setOpaque(true);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireEditingStopped();
				// Get the user from the table model using the row index
				User user = panel.getGestionnaire().findUserByTitle(panel.getTableArea().getValueAt(row, 0).toString()); // Assuming the first column holds User objects
				if ("Modifier".equals(label)) {
					panel.editUser(user);
				} else if ("Supprimer".equals(label)) {
					panel.deleteUser(user);
				}
			}
		});
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		this.row = row;
		label = value.toString();
		button.setText(label);
		isClicked = true;
		return button;
	}

	@Override
	public Object getCellEditorValue() {
		return label;
	}

	@Override
	public boolean stopCellEditing() {
		isClicked = false;
		return super.stopCellEditing();
	}
}
