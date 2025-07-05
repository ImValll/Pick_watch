package model.saga;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class SagaTableModel extends AbstractTableModel {
	private List<Saga> sagas;
	private final String[] columnNames = {"Titre", "Réalisateur", "Description", "Visualiser", "Modifier", "Supprimer"};

	public SagaTableModel(List<Saga> sagas) {
		this.sagas = sagas;
	}

	public void setSaga(List<Saga> sagas) {
		this.sagas = sagas;
		fireTableDataChanged(); // Notifier que les données du tableau ont changé
	}

	@Override
	public int getRowCount() {
		return sagas.size();
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
		Saga saga = sagas.get(rowIndex);
		switch (columnIndex) {
			case 0:
				if (saga.getTitre() != null) {
					return saga.getTitre();
				} else {
					return "";
				}
			case 1:
				if (saga.getRealistateur() != null) {
					return saga.getRealistateur();
				} else {
					return "";
				}
			case 2:
				if (saga.getDescription() != null) {
					return saga.getDescription();
				} else {
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