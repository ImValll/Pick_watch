package model.parameter.actors;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ActorTableModel extends AbstractTableModel {
	private List<Actor> actors;
	private final String[] columnNames = {"Titre", "Modifier", "Supprimer"};

	public ActorTableModel(List<Actor> actors) {
		this.actors = actors;
	}

	public void setActor(List<Actor> actors) {
		this.actors = actors;
		fireTableDataChanged(); // Notifier que les données du tableau ont changé
	}

	@Override
	public int getRowCount() {
		return actors.size();
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
		Actor actor = actors.get(rowIndex);
		switch (columnIndex) {
			case 0:
				if (actor.getName() != null) {
					return actor.getName();
				} else {
					return "";
				}
			case 1:
				return "Modifier";
			case 2:
				return "Supprimer";
			default:
				return null;
		}
	}
}
