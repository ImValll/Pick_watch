package model.saga;

import model.DataManager;
import model.Language;
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
		System.out.println(Language.getBundle().getString("saga.sagaAjoute2Point") + saga.getTitre());
	}

	public List<Saga> searchSaga(String title) {
		return sagas.stream()
				.filter(saga -> saga.getTitre().toLowerCase().contains(title.toLowerCase()))
				.collect(Collectors.toList());
	}

	public void editSaga(String titre, Saga newSaga) {
		Saga saga = findSagaByTitle(titre);
		if (saga != null) {
			saga.setTitre(newSaga.getTitre());
			saga.setRealistateur(newSaga.getRealistateur());
			saga.setActeur(newSaga.getActeur());
			saga.setDescription(newSaga.getDescription());
			saga.setGenre(newSaga.getGenre());
			saga.setNombreFilms(newSaga.getNombreFilms());
			saga.setDateSortiePremier(newSaga.getDateSortiePremier());
			saga.setDateSortieDernier(newSaga.getDateSortieDernier());
			saga.setPlateforme(newSaga.getPlateforme());
			saga.setAddBy(newSaga.getAddBy());
			saga.setImagePath(newSaga.getImagePath());
			DataManager.saveSaga(sagas);
			System.out.println(Language.getBundle().getString("saga.sagaModifie2Point") + saga.getTitre());
		} else {
			System.out.println(Language.getBundle().getString("saga.sagaNonTrouve"));
		}
	}

	public void updateSagaAddBy(Saga saga, User newAddBy) {
		saga.setAddBy(newAddBy);
	}

	public List<Saga> getSaga() {
		return sagas;
	}

	public void deleteSaga(Saga saga) {
		if (saga != null) {
			// Supprimer l'image si elle existe
			String imagePath = saga.getImagePath();
			if (imagePath != null) {
				File imageFile = new File(imagePath);
				if (imageFile.exists()) {
					boolean deleted = imageFile.delete();
					if (deleted) {
						System.out.println(Language.getBundle().getString("affiche.afficheSupprime") + imagePath);
					} else {
						System.out.println(Language.getBundle().getString("affiche.echecAfficheSupprime") + imagePath);
					}
				}
			}

			// Supprimer la saga de la liste
			sagas.remove(saga);
			DataManager.saveSaga(sagas); // Mettre à jour la liste des sagas
			System.out.println(Language.getBundle().getString("saga.sagaSupprime2Point") + saga.getTitre());
		} else {
			System.out.println(Language.getBundle().getString("saga.sagaNonTrouve"));
		}
	}

	public Saga findSagaByTitle(String titre) {
		return sagas.stream()
				.filter(saga -> saga.getTitre().equals(titre))
				.findFirst()
				.orElse(null);
	}

	public Saga pickRandomSaga(String rea, Actor[] acteurs, Genre[] genres, int nbFilm, Date dateSortie, Date dateSortie2, Platform[] plateformes, int dejaVu, User addBy) {
		List<Saga> filteredSaga = new ArrayList<>();

		for (Saga saga : sagas) {
			boolean matches = true;

			if (rea != null && !saga.getRealistateur().equalsIgnoreCase(rea)) {
				matches = false;
			}
			else if (acteurs != null && acteurs.length > 0) {
				boolean acteurMatch = false;
				for (Actor acteur : acteurs) {
					Actor[] actorSaga = saga.getActeur();
					if(actorSaga != null) {
						for (Actor sagaActeur : saga.getActeur()) {
							if (sagaActeur.getName().equals(acteur.getName())) {
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
					for (Genre sagaGenre : saga.getGenre()) {
						if (sagaGenre.equals(genre)) {
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
			else if (dateSortie != null && saga.getDateSortiePremier() == null || dateSortie != null && saga.getDateSortiePremier().getYear() != dateSortie.getYear()) {
				matches = false;
			}
			else if (dateSortie2 != null && saga.getDateSortieDernier() == null || dateSortie2 != null && saga.getDateSortieDernier().getYear() != dateSortie2.getYear()) {
				matches = false;
			}
			else if (plateformes != null && plateformes.length > 0) {
				boolean plateformeMatch = false;
				for (Platform plateforme : plateformes) {
					for (Platform sagaPlateforme : saga.getPlateforme()) {
						if (sagaPlateforme.equals(plateforme)) {
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
