package model.movie;

import model.DataManager;
import model.Genre;
import model.Plateforme;
import model.Utilisateur;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestSerialization {
	public static void main(String[] args) {
		// Créez des exemples de données pour les tests
		Genre[] genres = {Genre.Action, Genre.Comedie};
		Plateforme[] plateformes = {Plateforme.Netflix, Plateforme.Prime};
		Utilisateur user = Utilisateur.Valentin;

		// Créez des objets Movie
		Movie movie1 = new Movie("Inception", "Christopher Nolan", "A mind-bending thriller",
				genres, 148, new Date(), plateformes, true, user);
		Movie movie2 = new Movie("The Matrix", "The Wachowskis", "A science fiction classic",
				genres, 136, new Date(), plateformes, false, user);

		List<Movie> movies = new ArrayList<>();
		movies.add(movie1);
		movies.add(movie2);

		// Sauvegardez les objets Movie
		DataManager.saveMovie(movies);

		// Chargez les objets Movie
		List<Movie> loadedMovies = DataManager.loadMovie();
		for (Movie movie : loadedMovies) {
			System.out.println("Titre: " + movie.getTitre());
			System.out.println("Réalisateur: " + movie.getRealistateur());
			System.out.println("Description: " + movie.getDescription());
			// Affichez les autres attributs si nécessaire
		}
	}
}