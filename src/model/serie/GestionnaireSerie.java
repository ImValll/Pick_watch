package model.serie;

import model.DataManager;
import model.parameter.actors.Actor;
import model.parameter.genres.Genre;
import model.parameter.platforms.Platform;
import model.parameter.users.User;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GestionnaireSerie {
	private List<Serie> series;

	public GestionnaireSerie() {
		series = DataManager.loadSerie();
		if (series == null) {
			series = new ArrayList<>();
		}
	}

	public void addSerie(Serie serie) {
		series.add(serie);
		DataManager.saveSerie(series);
		System.out.println("Série ajoutée: " + serie.getTitre());
	}

	public List<Serie> searchSerie(String title) {
		return series.stream()
				.filter(serie -> serie.getTitre().toLowerCase().contains(title.toLowerCase()))
				.collect(Collectors.toList());
	}

	public void editSerie(String titre, Serie newSerie) {
		Serie serie = findSerieByTitle(titre);
		if (serie != null) {
			serie.setTitre(newSerie.getTitre());
			serie.setActeur(newSerie.getActeur());
			serie.setDescription(newSerie.getDescription());
			serie.setGenre(newSerie.getGenre());
			serie.setNombreSaison(newSerie.getNombreSaison());
			serie.setNombreEpisode(newSerie.getNombreEpisode());
			serie.setDureeMoyenne(newSerie.getDureeMoyenne());
			serie.setDateSortiePremiereSaison(newSerie.getDateSortiePremiereSaison());
			serie.setDateSortieDerniereSaison(newSerie.getDateSortieDerniereSaison());
			serie.setPlateforme(newSerie.getPlateforme());
			serie.setAddBy(newSerie.getAddBy());
			serie.setImagePath(newSerie.getImagePath());
			DataManager.saveSerie(series);
			System.out.println("Série modifiée: " + serie.getTitre());
		} else {
			System.out.println("Série non trouvée.");
		}
	}

	public void updateSerieAddBy(Serie serie, User newAddBy) {
		serie.setAddBy(newAddBy);
	}

	public List<Serie> getSerie() {
		return series;
	}

	public void deleteSerie(Serie serie) {
		if (serie != null) {
			// Supprimer l'image si elle existe
			String imagePath = serie.getImagePath();
			if (imagePath != null) {
				File imageFile = new File(imagePath);
				if (imageFile.exists()) {
					boolean deleted = imageFile.delete();
					if (deleted) {
						System.out.println("Affiche supprimée : " + imagePath);
					} else {
						System.out.println("Échec de suppression de l'affiche : " + imagePath);
					}
				}
			}

			// Supprimer la série de la liste
			series.remove(serie);
			DataManager.saveSerie(series); // Mettre à jour la liste des séries
			System.out.println("Série supprimée: " + serie.getTitre());
		} else {
			System.out.println("Série non trouvée.");
		}
	}

	public Serie findSerieByTitle(String titre) {
		return series.stream()
				.filter(serie -> serie.getTitre().equals(titre))
				.findFirst()
				.orElse(null);
	}

	public Serie pickRandomSerie(Actor[] acteurs, Genre[] genres, int nbSaison, int nbEpidose, int dureeMoyene, Date dateSortie, Date dateSortie2, Platform[] plateformes, int dejaVu, User addBy) {
		List<Serie> filteredSerie = new ArrayList<>();

		for (Serie serie : series) {
			boolean matches = true;

			if (acteurs != null && acteurs.length > 0) {
				boolean acteurMatch = false;
				for (Actor acteur : acteurs) {
					Actor[] actorSerie = serie.getActeur();
					if(actorSerie != null) {
						for (Actor serieActeur : serie.getActeur()) {
							if (serieActeur.getName().equals(acteur.getName())) {
								acteurMatch = true;
								break;
							}
						}
					}
					if (acteurMatch) break;
				}
				if (!acteurMatch) {
					matches = false;
				}
			}
			if (genres != null && genres.length > 0) {
				boolean genreMatch = false;
				for (Genre genre : genres) {
					for (Genre serieGenre : serie.getGenre()) {
						if (serieGenre.equals(genre)) {
							genreMatch = true;
							break;
						}
					}
					if (genreMatch) break;
				}
				if (!genreMatch) {
					matches = false;
				}
			}
			if (nbSaison != 0 && serie.getNombreSaison() == nbSaison) {
				matches = false;
			}
			if (nbEpidose != 0 && serie.getNombreEpisode() == nbEpidose) {
				matches = false;
			}
			if (dureeMoyene != 0 && serie.getDureeMoyenne() == dureeMoyene) {
				matches = false;
			}
			if (dateSortie != null && serie.getDateSortiePremiereSaison() == null || dateSortie != null && serie.getDateSortiePremiereSaison().getYear() != dateSortie.getYear()) {
				matches = false;
			}
			if (dateSortie2 != null && serie.getDateSortieDerniereSaison() == null || dateSortie2 != null && serie.getDateSortieDerniereSaison().getYear() != dateSortie2.getYear()) {
				matches = false;
			}
			if (plateformes != null && plateformes.length > 0) {
				boolean plateformeMatch = false;
				for (Platform plateforme : plateformes) {
					for (Platform seriePlateforme : serie.getPlateforme()) {
						if (seriePlateforme.equals(plateforme)) {
							plateformeMatch = true;
							break;
						}
					}
					if (plateformeMatch) break;
				}
				if (!plateformeMatch) {
					matches = false;
				}
			}
			if (dejaVu != -1) {
				if ((dejaVu == 0 && serie.getDejaVu()) || (dejaVu == 1 && !serie.getDejaVu())) {
					matches = false;
				}
			}
			if (addBy != null && !serie.getAddBy().equals(addBy)) {
				matches = false;
			}

			if (matches) {
				filteredSerie.add(serie);
			}
		}

		if (filteredSerie.isEmpty()) {
			return null;
		}

		Random rand = new Random();
		return filteredSerie.get(rand.nextInt(filteredSerie.size()));
	}
}
