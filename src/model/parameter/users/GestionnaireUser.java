package model.parameter.users;

import model.DataManager;
import model.movie.Movie;
import model.saga.Saga;
import model.serie.Serie;
import model.serie_courte.SerieCourte;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GestionnaireUser {
	private ArrayList<User> users;

	private List<Movie> movies;
	private List<Saga> sagas;
	private List<Serie> series;
	private List<SerieCourte> seriesCourtes;

	private String[] message = new String[3];

	public GestionnaireUser() {
		users = DataManager.loadUser();
		if (users == null) {
			users = new ArrayList<>();
		}

		movies = DataManager.loadMovie();
		if (movies == null) {
			movies = new ArrayList<>();
		}

		sagas = DataManager.loadSaga();
		if (sagas == null) {
			sagas = new ArrayList<>();
		}

		series = DataManager.loadSerie();
		if (series == null) {
			series = new ArrayList<>();
		}

		seriesCourtes = DataManager.loadSerieCourte();
		if (seriesCourtes == null) {
			seriesCourtes = new ArrayList<>();
		}
	}

	public void addUser(User user) {
		users.add(user);
		DataManager.saveUser(users);
		System.out.println("Utilisateur ajouté : " + user.getName());
	}

	public List<User> searchUser(String title) {
		return users.stream()
				.filter(user -> user.getName().toLowerCase().contains(title.toLowerCase()))
				.collect(Collectors.toList());
	}

	public void editUser(String titre, User newUser) {
		User user = findUserByTitle(titre);

		boolean utilise = false;
		ArrayList<Movie> movieUtilise = new ArrayList<>();
		ArrayList<Saga> sagaUtilise = new ArrayList<>();
		ArrayList<Serie> serieUtilise = new ArrayList<>();
		ArrayList<SerieCourte> serieCourteUtilise = new ArrayList<>();

		for (Movie movie : movies) {
			if(user.getName().equals(movie.getAddBy().getName())) {
				utilise = true;
				movieUtilise.add(movie);
			}
		}
		for (Saga saga : sagas) {
			if(user.getName().equals(saga.getAddBy().getName())) {
				utilise = true;
				sagaUtilise.add(saga);
			}
		}
		for (Serie serie : series) {
			if(user.getName().equals(serie.getAddBy().getName())) {
				utilise = true;
				serieUtilise.add(serie);
			}
		}
		for (SerieCourte serieCourte : seriesCourtes) {
			if(user.getName().equals(serieCourte.getAddBy().getName())) {
				utilise = true;
				serieCourteUtilise.add(serieCourte);
			}
		}

		if(utilise) {
			if(!movieUtilise.isEmpty()) {
				for(Movie movie : movieUtilise) {
					Movie newMovie = movie;
					newMovie.setAddBy(newUser);
					movies.remove(movie);
					movies.add(newMovie);
				}
				DataManager.saveMovie(movies);
			}
			if(!sagaUtilise.isEmpty()) {
				for(Saga saga : sagaUtilise) {
					Saga newSaga = saga;
					newSaga.setAddBy(newUser);
					sagas.remove(saga);
					sagas.add(newSaga);
				}
				DataManager.saveSaga(sagas);
			}
			if(!serieUtilise.isEmpty()) {
				for(Serie serie : serieUtilise) {
					Serie newSerie = serie;
					newSerie.setAddBy(newUser);
					series.remove(serie);
					series.add(newSerie);
				}
				DataManager.saveSerie(series);
			}
			if(!serieCourteUtilise.isEmpty()) {
				for(SerieCourte serieCourte : serieCourteUtilise) {
					SerieCourte newSerieCourte = serieCourte;
					newSerieCourte.setAddBy(newUser);
					seriesCourtes.remove(serieCourte);
					seriesCourtes.add(newSerieCourte);
				}
				DataManager.saveSerieCourte(seriesCourtes);
			}
		}

		if (user != null) {
			user.setName(newUser.getName());
			DataManager.saveUser(users);
			message[0] = "i";
			message[1] = "Utilisateur modifié";
			message[2] = "L'utilisateur " + user.getName() + " a été modifié avec succès.";
		} else {
			message[0] = "e";
			message[1] = "Utilisateur non trouvé";
			message[2] = "Erreur, l'utilisateur n'a pas été trouvé.";
			System.out.println("Utilisateur non trouvé.");
		}
	}

	public ArrayList<User> getUser() {
		return users;
	}

	public void deleteUser(User user) {
		Boolean peutSupprimer = true;
		String utilise = "";

		for (Movie movie : movies) {
			if(user.getName().equals(movie.getAddBy().getName())) {
				peutSupprimer = false;
				utilise = "un ou plusieurs films";
			}
		}
		for (Saga saga : sagas) {
			if(user.getName().equals(saga.getAddBy().getName())) {
				peutSupprimer = false;
				utilise = "une ou plusieurs sagas";
			}
		}
		for (Serie serie : series) {
			if(user.getName().equals(serie.getAddBy().getName())) {
				peutSupprimer = false;
				utilise = "une ou plusieurs séries";
			}
		}
		for (SerieCourte serieCourte : seriesCourtes) {
			if(user.getName().equals(serieCourte.getAddBy().getName())) {
				peutSupprimer = false;
				utilise = "une ou plusieurs séries courtes";
			}
		}

		if (user != null) {
			if(peutSupprimer) {
				users.remove(user);
				DataManager.saveUser(users); // Mettre à jour la liste des users
				System.out.println("Utilisateur supprimé : " + user.getName());
				message[0] = "i";
				message[1] = "Utilisateur supprimé";
				message[2] = "L'utilisateur " + user.getName() + " a été supprimé avec succès.";
			} else {
				message[0] = "e";
				message[1] = "Erreur utilisateur utilisé";
				message[2] = "Impossible de supprimer l'utilisateur " + user.getName() + ", il est utilisé dans " + utilise + ".";
			}
		} else {
			message[0] = "e";
			message[1] = "Utilisateur non trouvé";
			message[2] = "Erreur, l'utilisateur n'a pas été trouvé.";
			System.out.println("Utilisateur non trouvé.");
		}
	}

	public User findUserByTitle(String titre) {
		return users.stream()
				.filter(user -> user.getName().equals(titre))
				.findFirst()
				.orElse(null);
	}

	public String[] getMessage() {
		String[] tempMessage = message;
		message = new String[3];
		return tempMessage;
	}
}
