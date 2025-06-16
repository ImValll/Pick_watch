package model.serie_courte;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class SerieCourteTableModel extends AbstractTableModel {
	private List<SerieCourte> seriesCourte;
	private final String[] columnNames = {"Titre", "Description", "Genre", "Nombre de saisons", "Nombre d'épisode par saison", "Durée moyenne d'épisode", "Date de sortie de de première saison", "Date de sortie de la dernière saison", "Plateforme", "Déjà vu", "Ajouté par", "Modifier", "Supprimer"};

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
		if (columnIndex == 11 || columnIndex == 12) { // colonnes "Modifier" et "Supprimer"
			return JButton.class;
		}
		return super.getColumnClass(columnIndex);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 11 || columnIndex == 12; // rend les colonnes "Modifier" et "Supprimer" éditables
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
				if (serieCourte.getGenre() != null) {
					return Arrays.toString(serieCourte.getGenre());
				} else {
					return "";
				}
			case 3:
				if (serieCourte.getNombreSaison() != 0) {
					return serieCourte.getNombreSaison();
				} else {
					return "";
				}
			case 4:
				if (serieCourte.getNombreEpisode() != 0) {
					return serieCourte.getNombreEpisode();
				} else {
					return "";
				}
			case 5:
				if (serieCourte.getDureeMoyenne() != 0) {
					return serieCourte.getDureeMoyenne();
				} else {
					return "";
				}
			case 6:
				if (serieCourte.getDateSortiePremiereSaison() != null) {
					return new SimpleDateFormat("dd/MM/yyyy").format(serieCourte.getDateSortiePremiereSaison());
				} else {
					return "";
				}
			case 7:
				if (serieCourte.getDateSortieDerniereSaison() != null) {
					return new SimpleDateFormat("dd/MM/yyyy").format(serieCourte.getDateSortieDerniereSaison());
				} else {
					return "";
				}
			case 8:
				if (serieCourte.getPlateforme() != null) {
					return Arrays.toString(serieCourte.getPlateforme());
				} else {
					return "";
				}
			case 9:
				if (serieCourte.getDejaVu() == false) {
					return "NON";
				} else {
					return "OUI";
				}
			case 10:
				if (serieCourte.getAddBy() != null) {
					return serieCourte.getAddBy();
				} else {
					return "";
				}
			case 11:
				return "Modifier";
			case 12:
				return "Supprimer";
			default:
				return null;
		}
	}
}
