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
				if (movie.getGenre() != null) {
					return Arrays.toString(movie.getGenre());
				}
				else {
					return "";
				}
			case 4:
				if (movie.getDuree() != 0) {
					return movie.getDuree();
				}
				else {
					return "";
				}
			case 5:
				if (movie.getDateSortie() != null) {
					return new SimpleDateFormat("dd/MM/yyyy").format(movie.getDateSortie());
				}
				else {
					return "";
				}
			case 6:
				if (movie.getPlateforme() != null) {
					return Arrays.toString(movie.getPlateforme());
				}
				else {
					return "";
				}
			case 7:
				if (movie.getAddBy() != null) {
					return movie.getAddBy();
				}
				else {
					return "";
				}
			case 8:
				return "Modifier";
			case 9:
				return "Supprimer";
			default:
				return null;
		}
	}

	public void addMovie(Movie movie) {
		movies.add(movie);
		fireTableRowsInserted(movies.size() - 1, movies.size() - 1);
	}
}
