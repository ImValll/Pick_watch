package model.parameter.users;

import model.DataManager;
import model.Language;
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
		System.out.println(Language.getBundle().getString("user.userAjoute2Point") + user.getName());
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
					movie.setAddBy(newUser);
					movies.remove(movie);
					movies.add(movie);
				}
				DataManager.saveMovie(movies);
			}
			if(!sagaUtilise.isEmpty()) {
				for(Saga saga : sagaUtilise) {
					saga.setAddBy(newUser);
					sagas.remove(saga);
					sagas.add(saga);
				}
				DataManager.saveSaga(sagas);
			}
			if(!serieUtilise.isEmpty()) {
				for(Serie serie : serieUtilise) {
					serie.setAddBy(newUser);
					series.remove(serie);
					series.add(serie);
				}
				DataManager.saveSerie(series);
			}
			if(!serieCourteUtilise.isEmpty()) {
				for(SerieCourte serieCourte : serieCourteUtilise) {
					serieCourte.setAddBy(newUser);
					seriesCourtes.remove(serieCourte);
					seriesCourtes.add(serieCourte);
				}
				DataManager.saveSerieCourte(seriesCourtes);
			}
		}

		if (user != null) {
			user.setName(newUser.getName());
			DataManager.saveUser(users);
			message[0] = "i";
			message[1] = Language.getBundle().getString("user.userModifie");
			message[2] = Language.getBundle().getString("user.annonceUserPartie1") + user.getName() + Language.getBundle().getString("param.annoncePartie2ModifieMasculin");
		} else {
			message[0] = "e";
			message[1] = Language.getBundle().getString("user.userNonTrouve");
			message[2] = Language.getBundle().getString("user.erreurUserNonTrouve");
			System.out.println(Language.getBundle().getString("user.userNonTrouve"));
		}
	}

	public ArrayList<User> getUser() {
		return users;
	}

	public void deleteUser(User user) {
		boolean peutSupprimer = true;
		String utilise = "";

		for (Movie movie : movies) {
			if (user.getName().equals(movie.getAddBy().getName())) {
				peutSupprimer = false;
				utilise = Language.getBundle().getString("movie.unPlusieursFilm");
				break;
			}
		}
		for (Saga saga : sagas) {
			if (user.getName().equals(saga.getAddBy().getName())) {
				peutSupprimer = false;
				utilise = Language.getBundle().getString("saga.unPlusieursSaga");
				break;
			}
		}
		for (Serie serie : series) {
			if (user.getName().equals(serie.getAddBy().getName())) {
				peutSupprimer = false;
				utilise = Language.getBundle().getString("serie.unPlusieursSerie");
				break;
			}
		}
		for (SerieCourte serieCourte : seriesCourtes) {
			if (user.getName().equals(serieCourte.getAddBy().getName())) {
				peutSupprimer = false;
				utilise = Language.getBundle().getString("serieCourte.unPlusieursSerieCourte");
				break;
			}
		}

		if (user != null) {
			if(peutSupprimer) {
				users.remove(user);
				DataManager.saveUser(users); // Mettre à jour la liste des users
				System.out.println(Language.getBundle().getString("user.userSupprime") + user.getName());
				message[0] = "i";
				message[1] = Language.getBundle().getString("user.userSupprime");
				message[2] = Language.getBundle().getString("user.annonceUserPartie1") + user.getName() + Language.getBundle().getString("param.annoncePartie2SupprimeMasculin");
			} else {
				message[0] = "e";
				message[1] = Language.getBundle().getString("user.erreurUserUtilise");
				message[2] = Language.getBundle().getString("user.impossibleSupprimerPartie1") + user.getName() + Language.getBundle().getString("user.impossibleSupprimerPartie2") + utilise + ".";
			}
		} else {
			message[0] = "e";
			message[1] = Language.getBundle().getString("user.userNonTrouve");
			message[2] = Language.getBundle().getString("user.erreurUserNonTrouve");
			System.out.println(Language.getBundle().getString("user.userNonTrouve"));
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
