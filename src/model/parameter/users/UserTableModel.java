package model.parameter.users;

import model.genre.User;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class UserTableModel extends AbstractTableModel {
	private List<User> users;
	private final String[] columnNames = {"Titre", "Modifier", "Supprimer"};

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
					return "Modifier";
				} else {
					return "";
				}
			case 2:
				if (!user.getName().equals("Tous")) {
					return "Supprimer";
				} else {
					return "";
				}
			default:
				return null;
		}
	}

	public void addUser(User user) {
		users.add(user);
		fireTableRowsInserted(users.size() - 1, users.size() - 1);
	}
}
