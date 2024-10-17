package model.serie;

import model.serie.Serie;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class SerieTableModel extends AbstractTableModel {
	private List<Serie> series;
	private final String[] columnNames = {"Titre", "Description", "Genre", "Nombre de saisons", "Nombre d'épisode par saison", "Durée moyenne d'épisode", "Date de sortie de de première saison", "Date de sortie de la dernière saison", "Plateforme", "Déjà vu", "Ajouté par", "Modifier", "Supprimer"};

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
				if (serie.getGenre() != null) {
					return Arrays.toString(serie.getGenre());
				} else {
					return "";
				}
			case 3:
				if (serie.getNombreSaison() != 0) {
					return serie.getNombreSaison();
				} else {
					return "";
				}
			case 4:
				if (serie.getNombreEpisode() != 0) {
					return serie.getNombreEpisode();
				} else {
					return "";
				}
			case 5:
				if (serie.getDureeMoyenne() != 0) {
					return serie.getDureeMoyenne();
				} else {
					return "";
				}
			case 6:
				if (serie.getDateSortiePremiereSaison() != null) {
					return new SimpleDateFormat("dd/MM/yyyy").format(serie.getDateSortiePremiereSaison());
				} else {
					return "";
				}
			case 7:
				if (serie.getDateSortieDerniereSaison() != null) {
					return new SimpleDateFormat("dd/MM/yyyy").format(serie.getDateSortieDerniereSaison());
				} else {
					return "";
				}
			case 8:
				if (serie.getPlateforme() != null) {
					return Arrays.toString(serie.getPlateforme());
				} else {
					return "";
				}
			case 9:
				if (serie.getDejaVu() == false) {
					return "NON";
				} else {
					return "OUI";
				}
			case 10:
				if (serie.getAddBy() != null) {
					return serie.getAddBy();
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

	public void addSerie(Serie serie) {
		series.add(serie);
		fireTableRowsInserted(series.size() - 1, series.size() - 1);
	}
}
