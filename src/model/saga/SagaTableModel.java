package model.saga;

import model.movie.Movie;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class SagaTableModel extends AbstractTableModel {
	private List<Saga> sagas;
	private final String[] columnNames = {"Titre", "Réalisateur", "Description", "Genre", "Nombre de films", "Date de sortie du premier", "Date de sortie du dernier", "Plateforme", "Déjà vu", "Ajouté par", "Modifier", "Supprimer"};

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
		if (columnIndex == 10 || columnIndex == 11) { // colonnes "Modifier" et "Supprimer"
			return JButton.class;
		}
		return super.getColumnClass(columnIndex);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 10 || columnIndex == 11; // rend les colonnes "Modifier" et "Supprimer" éditables
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
				if (saga.getGenre() != null) {
					return Arrays.toString(saga.getGenre());
				} else {
					return "";
				}
			case 4:
				if (saga.getNombreFilms() != 0) {
					return saga.getNombreFilms();
				} else {
					return "";
				}
			case 5:
				if (saga.getDateSortiePremier() != null) {
					return new SimpleDateFormat("dd/MM/yyyy").format(saga.getDateSortiePremier());
				} else {
					return "";
				}
			case 6:
				if (saga.getDateSortieDernier() != null) {
					return new SimpleDateFormat("dd/MM/yyyy").format(saga.getDateSortieDernier());
				} else {
					return "";
				}
			case 7:
				if (saga.getPlateforme() != null) {
					return Arrays.toString(saga.getPlateforme());
				} else {
					return "";
				}
			case 8:
				if (saga.getDejaVu() == false) {
					return "NON";
				} else {
					return "OUI";
				}
			case 9:
				if (saga.getAddBy() != null) {
					return saga.getAddBy();
				} else {
					return "";
				}
			case 10:
				return "Modifier";
			case 11:
				return "Supprimer";
			default:
				return null;
		}
	}

	public void addSaga(Saga saga) {
		sagas.add(saga);
		fireTableRowsInserted(sagas.size() - 1, sagas.size() - 1);
	}
}