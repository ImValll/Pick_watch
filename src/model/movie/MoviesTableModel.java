package model.movie;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class MoviesTableModel extends AbstractTableModel {
	private List<Movie> movies;
	private final String[] columnNames = {"Titre", "Réalisateur", "Description", "Visualiser", "Modifier", "Supprimer"};

	public MoviesTableModel(List<Movie> movies) {
		this.movies = movies;
	}

	public void setMovies(List<Movie> movies) {
		this.movies = movies;
		fireTableDataChanged(); // Notifier que les données du tableau ont changé
	}

	@Override
	public int getRowCount() {
		return movies.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 3 || columnIndex == 4 || columnIndex == 5) { // colonnes "Visualiser", "Modifier" et "Supprimer"
			return JButton.class;
		}
		return super.getColumnClass(columnIndex);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 3 || columnIndex == 4 || columnIndex == 5; // rend les colonnes "Visualiser", "Modifier" et "Supprimer" éditables
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Movie movie = movies.get(rowIndex);
		switch (columnIndex) {
			case 0:
				if (movie.getTitre() != null) {
					return movie.getTitre();
				}
				else {
					return "";
				}
			case 1:
				if (movie.getRealistateur() != null) {
					return movie.getRealistateur();
				}
				else {
					return "";
				}
			case 2:
				if (movie.getDescription() != null) {
					return movie.getDescription();
				}
				else {
					return "";
				}
			case 3:
				return "Visualiser";
			case 4:
				return "Modifier";
			case 5:
				return "Supprimer";
			default:
				return null;
		}
	}
}
