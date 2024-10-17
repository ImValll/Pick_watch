package model.serie_courte;

import model.DataManager;
import model.Genre;
import model.Plateforme;
import model.Utilisateur;
import model.serie.Serie;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GestionnaireSerieCourte {
	private List<SerieCourte> seriesCourte;

	public GestionnaireSerieCourte() {
		seriesCourte = DataManager.loadSerieCourte();
		if (seriesCourte == null) {
			seriesCourte = new ArrayList<>();
		}
	}

	public void addSerieCourte(SerieCourte serieCourte) {
		seriesCourte.add(serieCourte);
		DataManager.saveSerieCourte(seriesCourte);
		System.out.println("Série ajoutée: " + serieCourte.getTitre());
	}

	public List<SerieCourte> searchSerieCourte(String title) {
		return seriesCourte.stream()
				.filter(serieCourte -> serieCourte.getTitre().toLowerCase().contains(title.toLowerCase()))
				.collect(Collectors.toList());
	}

	public void showSerieCourte() {
		for (SerieCourte serieCourte : seriesCourte) {
			System.out.println(serieCourte.getTitre() + " avec " + serieCourte.getNombreSaison() + " saisons");
		}
	}

	public void editSerieCourte(String titre, SerieCourte newSerieCourte) {
		SerieCourte serieCourte = findSerieCourteByTitle(titre);
		if (serieCourte != null) {
			serieCourte.setTitre(newSerieCourte.getTitre());
			serieCourte.setDescription(newSerieCourte.getDescription());
			serieCourte.setGenre(newSerieCourte.getGenre());
			serieCourte.setNombreSaison(newSerieCourte.getNombreSaison());
			serieCourte.setNombreEpisode(newSerieCourte.getNombreEpisode());
			serieCourte.setDureeMoyenne(newSerieCourte.getDureeMoyenne());
			serieCourte.setDateSortiePremiereSaison(newSerieCourte.getDateSortiePremiereSaison());
			serieCourte.setDateSortiePremiereSaison(newSerieCourte.getDateSortiePremiereSaison());
			serieCourte.setPlateforme(newSerieCourte.getPlateforme());
			serieCourte.setAddBy(newSerieCourte.getAddBy());
			DataManager.saveSerieCourte(seriesCourte);
			System.out.println("Série modifiée: " + serieCourte.getTitre());
		} else {
			System.out.println("Série non trouvée.");
		}
	}

	public void updateSerieCourteAddBy(SerieCourte serieCourte, Utilisateur newAddBy) {
		serieCourte.setAddBy(newAddBy);
	}

	public List<SerieCourte> getSerieCourte() {
		return seriesCourte;
	}

//	public void sauvegarderModifications() {
//		DataManager.sauvegarderLivres(livres);
//		DataManager.sauvegarderUtilisateurs(utilisateurs);
//	}

	public void deleteSerieCourte(SerieCourte serieCourte) {
		if (serieCourte != null) {
			seriesCourte.remove(serieCourte);
			DataManager.saveSerieCourte(seriesCourte); // Mettre à jour la liste des séries courtes
			System.out.println("Série supprimée: " + serieCourte.getTitre());
		} else {
			System.out.println("Série non trouvée.");
		}
	}

	public SerieCourte findSerieCourteByTitle(String titre) {
		return seriesCourte.stream()
				.filter(serieCourte -> serieCourte.getTitre().equals(titre))
				.findFirst()
				.orElse(null);
	}

	public SerieCourte pickRandomSerieCourte(Genre[] genres, int nbSaison, int nbEpidose, int dureeMoyene, Date dateSortie, Date dateSortie2, Plateforme[] plateformes, int dejaVu, Utilisateur addBy) {
		List<SerieCourte> filteredSerieCourte = new ArrayList<>();

		for (SerieCourte serieCourte : seriesCourte) {
			boolean matches = true;

			if (genres != null && genres.length > 0) {
				boolean genreMatch = false;
				for (Genre genre : genres) {
					for (Genre serieCourteGenre : serieCourte.getGenre()) {
						if (serieCourteGenre.equals(genre)) {
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
			if (nbSaison != 0 && serieCourte.getNombreSaison() == nbSaison) {
				matches = false;
			}
			if (nbEpidose != 0 && serieCourte.getNombreEpisode() == nbEpidose) {
				matches = false;
			}
			if (dureeMoyene != 0 && serieCourte.getDureeMoyenne() == dureeMoyene) {
				matches = false;
			}
			if (dateSortie != null && serieCourte.getDateSortiePremiereSaison() == null || dateSortie != null && serieCourte.getDateSortiePremiereSaison().getYear() != dateSortie.getYear()) {
				matches = false;
			}
			if (dateSortie2 != null && serieCourte.getDateSortieDerniereSaison() == null || dateSortie2 != null && serieCourte.getDateSortieDerniereSaison().getYear() != dateSortie2.getYear()) {
				matches = false;
			}
			if (plateformes != null && plateformes.length > 0) {
				boolean plateformeMatch = false;
				for (Plateforme plateforme : plateformes) {
					for (Plateforme serieCourtePlateforme : serieCourte.getPlateforme()) {
						if (serieCourtePlateforme.equals(plateforme)) {
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
				if ((dejaVu == 0 && serieCourte.getDejaVu()) || (dejaVu == 1 && !serieCourte.getDejaVu())) {
					matches = false;
				}
			}
			if (addBy != null && !serieCourte.getAddBy().equals(addBy)) {
				matches = false;
			}

			if (matches) {
				filteredSerieCourte.add(serieCourte);
			}
		}

		if (filteredSerieCourte.isEmpty()) {
			return null;
		}

		Random rand = new Random();
		return filteredSerieCourte.get(rand.nextInt(filteredSerieCourte.size()));
	}
}
