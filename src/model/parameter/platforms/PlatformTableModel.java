package model.parameter.platforms;

import model.genre.Platform;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class PlatformTableModel extends AbstractTableModel {
	private List<Platform> platforms;
	private final String[] columnNames = {"Titre", "Modifier", "Supprimer"};

	public PlatformTableModel(List<Platform> platforms) {
		this.platforms = platforms;
	}

	public void setPlatform(List<Platform> platforms) {
		this.platforms = platforms;
		fireTableDataChanged(); // Notifier que les données du tableau ont changé
	}

	@Override
	public int getRowCount() {
		return platforms.size();
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
		Platform platform = platforms.get(rowIndex);
		switch (columnIndex) {
			case 0:
				if (platform.getName() != null) {
					return platform.getName();
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

	public void addPlatform(Platform platform) {
		platforms.add(platform);
		fireTableRowsInserted(platforms.size() - 1, platforms.size() - 1);
	}
}
