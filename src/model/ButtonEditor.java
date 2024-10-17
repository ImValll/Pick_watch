package model;

import model.movie.Movie;
import model.saga.Saga;
import model.serie.Serie;
import view.movie.PanelMovies;
import view.saga.PanelSaga;
import view.serie.PanelSerie;

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
