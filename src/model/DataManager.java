package model;

import model.movie.Movie;
import model.saga.Saga;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
	private static final String MOVIE_FILE = "movies.ser";
	private static final String SAGA_FILE = "sagas.ser";

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
}
