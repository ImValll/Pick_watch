package model;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class MoviesTableModel extends AbstractTableModel {
	private List<Movie> movies;
	private final String[] columnNames = {"Titre", "Réalisateur", "Description", "Genre", "Durée", "Date de sortie", "Plateforme", "Ajouté par", "Modifier", "Supprimer"};

	public MoviesTableModel(List<Movie> movies) {
		this.movies = movies;
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
		if (columnIndex == 8 || columnIndex == 9) { // colonnes "Modifier" et "Supprimer"
			return JButton.class;
		}
		return super.getColumnClass(columnIndex);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 8 || columnIndex == 9; // rend les colonnes "Modifier" et "Supprimer" éditables
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Movie movie = movies.get(rowIndex);
		switch (columnIndex) {
			case 0:
				return movie.getTitre();
			case 1:
				return movie.getRealistateur();
			case 2:
				return movie.getDescription();
			case 3:
				return Arrays.toString(movie.getGenre());
			case 4:
				return movie.getDuree();
			case 5:
				return new SimpleDateFormat("dd/MM/yyyy").format(movie.getDateSortie());
			case 6:
				return Arrays.toString(movie.getPlateforme());
			case 7:
				return movie.getAddBy();
			case 8:
				return "Modifier";
			case 9:
				return "Supprimer";
			default:
				return null;
		}
	}
}
