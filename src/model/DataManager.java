package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
	private static final String MOVIE_FILE = "movies.ser";

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
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Erreur lors du chargement des films: " + e.getMessage());
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
}
