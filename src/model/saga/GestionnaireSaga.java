package model.saga;

import model.DataManager;
import model.genre.Genre;
import model.genre.Platform;
import model.genre.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GestionnaireSaga {
	private List<Saga> sagas;

	public GestionnaireSaga() {
		sagas = DataManager.loadSaga();
		if (sagas == null) {
			sagas = new ArrayList<>();
		}
	}

	public void addSaga(Saga saga) {
		sagas.add(saga);
		DataManager.saveSaga(sagas);
		System.out.println("Saga ajoutée: " + saga.getTitre());
	}

	public List<Saga> searchSaga(String title) {
		return sagas.stream()
				.filter(saga -> saga.getTitre().toLowerCase().contains(title.toLowerCase()))
				.collect(Collectors.toList());
	}

	public void showSaga() {
		for (Saga saga : sagas) {
			System.out.println(saga.getTitre() + " par " + saga.getRealistateur());
		}
	}

	public void editSaga(String titre, Saga newSaga) {
		Saga saga = findSagaByTitle(titre);
		if (saga != null) {
			saga.setTitre(newSaga.getTitre());
			saga.setRealistateur(newSaga.getRealistateur());
			saga.setDescription(newSaga.getDescription());
			saga.setGenre(newSaga.getGenre());
			saga.setNombreFilms(newSaga.getNombreFilms());
			saga.setDateSortiePremier(newSaga.getDateSortiePremier());
			saga.setDateSortiePremier(newSaga.getDateSortiePremier());
			saga.setPlateforme(newSaga.getPlateforme());
			saga.setAddBy(newSaga.getAddBy());
			DataManager.saveSaga(sagas);
			System.out.println("Saga modifiée: " + saga.getTitre());
		} else {
			System.out.println("Saga non trouvée.");
		}
	}

	public void updateSagaAddBy(Saga saga, User newAddBy) {
		saga.setAddBy(newAddBy);
	}

	public List<Saga> getSaga() {
		return sagas;
	}

//	public void sauvegarderModifications() {
//		DataManager.sauvegarderLivres(livres);
//		DataManager.sauvegarderUtilisateurs(utilisateurs);
//	}

	public void deleteSaga(Saga saga) {
		if (saga != null) {
			sagas.remove(saga);
			DataManager.saveSaga(sagas); // Mettre à jour la liste des sagas
			System.out.println("Saga supprimée: " + saga.getTitre());
		} else {
			System.out.println("Saga non trouvée.");
		}
	}

	public Saga findSagaByTitle(String titre) {
		return sagas.stream()
				.filter(saga -> saga.getTitre().equals(titre))
				.findFirst()
				.orElse(null);
	}

	public Saga pickRandomSaga(String rea, Genre[] genres, int nbFilm, Date dateSortie, Date dateSortie2, Platform[] plateformes, int dejaVu, User addBy) {
		List<Saga> filteredSaga = new ArrayList<>();

		for (Saga saga : sagas) {
			boolean matches = true;

			if (rea != null && !saga.getRealistateur().equalsIgnoreCase(rea)) {
				matches = false;
			}
			if (genres != null && genres.length > 0) {
				boolean genreMatch = false;
				for (Genre genre : genres) {
					for (Genre movieGenre : saga.getGenre()) {
						if (movieGenre.equals(genre)) {
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
			if (nbFilm != 0 && saga.getNombreFilms() == nbFilm) {
				matches = false;
			}
			if (dateSortie != null && saga.getDateSortiePremier() == null || dateSortie != null && saga.getDateSortiePremier().getYear() != dateSortie.getYear()) {
				matches = false;
			}
			if (dateSortie2 != null && saga.getDateSortieDernier() == null || dateSortie2 != null && saga.getDateSortieDernier().getYear() != dateSortie2.getYear()) {
				matches = false;
			}
			if (plateformes != null && plateformes.length > 0) {
				boolean plateformeMatch = false;
				for (Platform plateforme : plateformes) {
					for (Platform moviePlateforme : saga.getPlateforme()) {
						if (moviePlateforme.equals(plateforme)) {
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
				if ((dejaVu == 0 && saga.getDejaVu()) || (dejaVu == 1 && !saga.getDejaVu())) {
					matches = false;
				}
			}
			if (addBy != null && !saga.getAddBy().equals(addBy)) {
				matches = false;
			}

			if (matches) {
				filteredSaga.add(saga);
			}
		}

		if (filteredSaga.isEmpty()) {
			return null;
		}

		Random rand = new Random();
		return filteredSaga.get(rand.nextInt(filteredSaga.size()));
	}
}
