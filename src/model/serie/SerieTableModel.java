package model.serie;

import model.Language;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class SerieTableModel extends AbstractTableModel {
	private List<Serie> series;
	private final String[] columnNames = {Language.getBundle().getString("carac.titre2"), Language.getBundle().getString("carac.description"), Language.getBundle().getString("app.visualiser"), Language.getBundle().getString("app.modifier"), Language.getBundle().getString("app.supprimer")};

	public SerieTableModel(List<Serie> series) {
		this.series = series;
	}

	public void setSerie(List<Serie> series) {
		this.series = series;
		fireTableDataChanged(); // Notifier que les données du tableau ont changé
	}

	@Override
	public int getRowCount() {
		return series.size();
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
		if (columnIndex == 2 || columnIndex == 3 || columnIndex == 4) { // colonnes "Visualiser", "Modifier" et "Supprimer"
			return JButton.class;
		}
		return super.getColumnClass(columnIndex);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 2 || columnIndex == 3 || columnIndex == 4; // rend les colonnes "Visualiser", "Modifier" et "Supprimer" éditables
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Serie serie = series.get(rowIndex);
		switch (columnIndex) {
			case 0:
				if (serie.getTitre() != null) {
					return serie.getTitre();
				} else {
					return "";
				}
			case 1:
				if (serie.getDescription() != null) {
					return serie.getDescription();
				} else {
					return "";
				}
			case 2:
				return Language.getBundle().getString("app.visualiser");
			case 3:
				return Language.getBundle().getString("app.modifier");
			case 4:
				return Language.getBundle().getString("app.supprimer");
			default:
				return null;
		}
	}
}
