package model.parameter.users;

import model.Language;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class UserTableModel extends AbstractTableModel {
	private List<User> users;
	private final String[] columnNames = {Language.getBundle().getString("param.nom2"), Language.getBundle().getString("app.modifier"), Language.getBundle().getString("app.supprimer")};

	public UserTableModel(List<User> users) {
		this.users = users;
	}

	public void setUser(List<User> users) {
		this.users = users;
		fireTableDataChanged(); // Notifier que les données du tableau ont changé
	}

	@Override
	public int getRowCount() {
		return users.size();
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
		User user = users.get(rowIndex);
		switch (columnIndex) {
			case 0:
				if (user.getName() != null) {
					return user.getName();
				} else {
					return "";
				}
			case 1:
				if (!user.getName().equals("Tous")) {
					return Language.getBundle().getString("app.modifier");
				} else {
					return "";
				}
			case 2:
				if (!user.getName().equals("Tous")) {
					return Language.getBundle().getString("app.supprimer");
				} else {
					return "";
				}
			default:
				return null;
		}
	}
}
