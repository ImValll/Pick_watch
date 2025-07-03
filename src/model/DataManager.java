package model;

import model.parameter.actors.Actor;
import model.parameter.genres.Genre;
import model.parameter.platforms.Platform;
import model.parameter.users.User;
import model.movie.Movie;
import model.saga.Saga;
import model.serie.Serie;
import model.serie_courte.SerieCourte;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
	private static final String GENRES_FILE = "DATA/genres.ser";
	private static final String PLATFORMS_FILE = "DATA/platforms.ser";
	private static final String ACTORS_FILE = "DATA/actors.ser";
	private static final String USERS_FILE = "DATA/users.ser";
	private static final String MOVIE_FILE = "DATA/movies.ser";
	private static final String SAGA_FILE = "DATA/sagas.ser";
	private static final String SERIE_FILE = "DATA/series.ser";
	private static final String SERIE_COURTE_FILE = "DATA/shortSeries.ser";

	public static void saveGenre(ArrayList<Genre> genres) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(GENRES_FILE))) {
			oos.writeObject(genres);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<Genre> loadGenre() {
		ArrayList<Genre> genres = new ArrayList<>();

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(GENRES_FILE))) {
			genres = (ArrayList<Genre>) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		return genres;
	}

	public static void savePlatform(ArrayList<Platform> platforms) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PLATFORMS_FILE))) {
			oos.writeObject(platforms);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<Platform> loadPlatform() {
		ArrayList<Platform> platforms = new ArrayList<>();

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PLATFORMS_FILE))) {
			platforms = (ArrayList<Platform>) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		return platforms;
	}

	public static void saveActor(ArrayList<Actor> actors) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ACTORS_FILE))) {
			oos.writeObject(actors);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<Actor> loadActor() {
		ArrayList<Actor> actors = new ArrayList<>();

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ACTORS_FILE))) {
			actors = (ArrayList<Actor>) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		return actors;
	}

	public static void saveUser(ArrayList<User> users) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
			oos.writeObject(users);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<User> loadUser() {
		ArrayList<User> users = new ArrayList<>();

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_FILE))) {
			users = (ArrayList<User>) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		return users;
	}

	public static void saveMovie(List<Movie> movies) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(MOVIE_FILE))) {
			oos.writeObject(movies);
			System.out.println("Movies serialized successfully.");
		} catch (IOException e) {
			System.err.println("Erreur lors de la sauvegarde des films: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static List<Movie> loadMovie() {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(MOVIE_FILE))) {
			List<Movie> movies = (List<Movie>) ois.readObject();
			System.out.println("Movies deserialized successfully.");
			return movies;
		} catch (FileNotFoundException e) {
			System.err.println("Fichier non trouvé: " + e.getMessage());
			return new ArrayList<>(); // Fichier non trouvé, retourne une liste vide pour initialisation
		} catch (ClassNotFoundException e) {
			System.err.println("Classe non trouvée: " + e.getMessage());
			e.printStackTrace();
			return new ArrayList<>();
		} catch (InvalidClassException e) {
			System.err.println("Classe invalide (version incompatible): " + e.getMessage());
			e.printStackTrace();
			return new ArrayList<>();
		} catch (IOException e) {
			System.err.println("Erreur lors du chargement des films: " + e.getMessage());
			e.printStackTrace();
			return new ArrayList<>();
		}
	}



	public static void saveSaga(List<Saga> sagas) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAGA_FILE))) {
			oos.writeObject(sagas);
			System.out.println("Sagas serialized successfully.");
		} catch (IOException e) {
			System.err.println("Erreur lors de la sauvegarde des sagas: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static List<Saga> loadSaga() {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAGA_FILE))) {
			List<Saga> sagas = (List<Saga>) ois.readObject();
			System.out.println("Sagas deserialized successfully.");
			return sagas;
		} catch (FileNotFoundException e) {
			System.err.println("Fichier non trouvé: " + e.getMessage());
			return new ArrayList<>(); // Fichier non trouvé, retourne une liste vide pour initialisation
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Erreur lors du chargement des sagas: " + e.getMessage());
			e.printStackTrace();
			return new ArrayList<>();
		}
	}



	public static void saveSerie(List<Serie> series) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SERIE_FILE))) {
			oos.writeObject(series);
			System.out.println("Series serialized successfully.");
		} catch (IOException e) {
			System.err.println("Erreur lors de la sauvegarde des Séries: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static List<Serie> loadSerie() {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SERIE_FILE))) {
			List<Serie> series = (List<Serie>) ois.readObject();
			System.out.println("Series deserialized successfully.");
			return series;
		} catch (FileNotFoundException e) {
			System.err.println("Fichier non trouvé: " + e.getMessage());
			return new ArrayList<>(); // Fichier non trouvé, retourne une liste vide pour initialisation
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Erreur lors du chargement des séries: " + e.getMessage());
			e.printStackTrace();
			return new ArrayList<>();
		}
	}



	public static void saveSerieCourte(List<SerieCourte> seriesCourte) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SERIE_COURTE_FILE))) {
			oos.writeObject(seriesCourte);
			System.out.println("Series serialized successfully.");
		} catch (IOException e) {
			System.err.println("Erreur lors de la sauvegarde des Séries: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static List<SerieCourte> loadSerieCourte() {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SERIE_COURTE_FILE))) {
			List<SerieCourte> seriesCourte = (List<SerieCourte>) ois.readObject();
			System.out.println("Series deserialized successfully.");
			return seriesCourte;
		} catch (FileNotFoundException e) {
			System.err.println("Fichier non trouvé: " + e.getMessage());
			return new ArrayList<>(); // Fichier non trouvé, retourne une liste vide pour initialisation
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Erreur lors du chargement des séries: " + e.getMessage());
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
}
