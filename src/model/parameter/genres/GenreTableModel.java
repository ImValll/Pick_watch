package model.parameter.genres;

import model.Language;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class GenreTableModel extends AbstractTableModel {
	private List<Genre> genres;
	private final String[] columnNames = {Language.getBundle().getString("param.nom2"), Language.getBundle().getString("app.modifier"), Language.getBundle().getString("app.supprimer")};

	public GenreTableModel(List<Genre> genres) {
		this.genres = genres;
	}

	public void setGenre(List<Genre> genres) {
		this.genres = genres;
		fireTableDataChanged(); // Notifier que les données du tableau ont changé
	}

	@Override
	public int getRowCount() {
		return genres.size();
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
		if (columnIndex == 1 || columnIndex == 2) { // colonnes "Modifier" et "Supprimer"
			return JButton.class;
		}
		return super.getColumnClass(columnIndex);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 1 || columnIndex == 2; // rend les colonnes "Modifier" et "Supprimer" éditables
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Genre genre = genres.get(rowIndex);
		switch (columnIndex) {
			case 0:
				if (genre.getName() != null) {
					return genre.getName();
				} else {
					return "";
				}
			case 1:
				return Language.getBundle().getString("app.modifier");
			case 2:
				return Language.getBundle().getString("app.supprimer");
			default:
				return null;
		}
	}
}
