package model.serie_courte;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class SerieCourteTableModel extends AbstractTableModel {
	private List<SerieCourte> seriesCourte;
	private final String[] columnNames = {"Titre", "Description", "Visualiser", "Modifier", "Supprimer"};

	public SerieCourteTableModel(List<SerieCourte> seriesCourte) {
		this.seriesCourte = seriesCourte;
	}

	public void setSerieCourte(List<SerieCourte> seriesCourte) {
		this.seriesCourte = seriesCourte;
		fireTableDataChanged(); // Notifier que les données du tableau ont changé
	}

	@Override
	public int getRowCount() {
		return seriesCourte.size();
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
		SerieCourte serieCourte = seriesCourte.get(rowIndex);
		switch (columnIndex) {
			case 0:
				if (serieCourte.getTitre() != null) {
					return serieCourte.getTitre();
				} else {
					return "";
				}
			case 1:
				if (serieCourte.getDescription() != null) {
					return serieCourte.getDescription();
				} else {
					return "";
				}
			case 2:
				return "Visualiser";
			case 3:
				return "Modifier";
			case 4:
				return "Supprimer";
			default:
				return null;
		}
	}
}
