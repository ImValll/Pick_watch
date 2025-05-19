package model.parameter.users;

import model.DataManager;
import model.genre.User;
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

	public void showUser() {
		for (User user : users) {
			System.out.println(user.getName());
		}
	}

	public void editUser(String titre, User newUser) {
		User user = findUserByTitle(titre);

		Boolean utilise = true;
		ArrayList<Movie> movieUtilise = new ArrayList<>();
		ArrayList<Saga> sagaUtilise = new ArrayList<>();
		ArrayList<Serie> serieUtilise = new ArrayList<>();
		ArrayList<SerieCourte> serieCourteUtilise = new ArrayList<>();

		for (Movie movie : movies) {
			if(user.equals(movie.getAddBy())) {
				utilise = false;
				movieUtilise.add(movie);
			}
		}
		for (Saga saga : sagas) {
			if(user.equals(saga.getAddBy())) {
				utilise = false;
				sagaUtilise.add(saga);
			}
		}
		for (Serie serie : series) {
			if(user.equals(serie.getAddBy())) {
				utilise = false;
				serieUtilise.add(serie);
			}
		}
		for (SerieCourte serieCourte : seriesCourtes) {
			if(user.equals(serieCourte.getAddBy())) {
				utilise = false;
				serieCourteUtilise.add(serieCourte);
			}
		}

		if(utilise) {
			if(!movieUtilise.isEmpty()) {
				for(Movie movie : movieUtilise) {
					User userMovie = user;
					Movie newMovie = movie;
					newMovie.setAddBy(userMovie);
					movies.remove(movie);
					movies.add(newMovie);
				}
				DataManager.saveMovie(movies);
			}
			if(!sagaUtilise.isEmpty()) {
				for(Saga saga : sagaUtilise) {
					User userSaga = user;
					Saga newSaga = saga;
					newSaga.setAddBy(userSaga);
					sagas.remove(saga);
					sagas.add(newSaga);
				}
				DataManager.saveSaga(sagas);
			}
			if(!serieUtilise.isEmpty()) {
				for(Serie serie : serieUtilise) {
					User userSerie = user;
					Serie newSerie = serie;
					newSerie.setAddBy(userSerie);
					series.remove(serie);
					series.add(newSerie);
				}
				DataManager.saveSerie(series);
			}
			if(!serieCourteUtilise.isEmpty()) {
				for(SerieCourte serieCourte : serieCourteUtilise) {
					User userSerieCourte = user;
					SerieCourte newSerieCourte = serieCourte;
					newSerieCourte.setAddBy(userSerieCourte);
					seriesCourtes.remove(serieCourte);
					seriesCourtes.add(newSerieCourte);
				}
				DataManager.saveSerieCourte(seriesCourtes);
			}
		}

		if (user != null) {
			user.setName(newUser.getName());
			DataManager.saveUser(users);
			System.out.println("Utilisateur modifié : " + user.getName());
		} else {
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
			if(user.equals(movie.getAddBy())) {
				peutSupprimer = false;
				utilise = "un ou plusieurs films";
			}
		}
		for (Saga saga : sagas) {
			if(user.equals(saga.getAddBy())) {
				peutSupprimer = false;
				utilise = "une ou plusieurs sagas";
			}
		}
		for (Serie serie : series) {
			if(user.equals(serie.getAddBy())) {
				peutSupprimer = false;
				utilise = "une ou plusieurs séries";
			}
		}
		for (SerieCourte serieCourte : seriesCourtes) {
			if(user.equals(serieCourte.getAddBy())) {
				peutSupprimer = false;
				utilise = "une ou plusieurs séries courtes";
			}
		}

		if (user != null) {
			if(peutSupprimer) {
				users.remove(user);
				DataManager.saveUser(users); // Mettre à jour la liste des users
				System.out.println("Utilisateur supprimé : " + user.getName());
			} else {
				System.out.println("Impossible de supprimer l'utilisateur " + user.getName() + ", il est utilisé dans " + utilise + ".");
			}
		} else {
			System.out.println("Utilisateur non trouvé.");
		}
	}

	public User findUserByTitle(String titre) {
		return users.stream()
				.filter(user -> user.getName().equals(titre))
				.findFirst()
				.orElse(null);
	}
}
