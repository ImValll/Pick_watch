package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Gestionnaire {
	private List<Movie> movies;

	public Gestionnaire() {
		movies = DataManager.loadMovie();
		if (movies == null) {
			movies = new ArrayList<>();
		}
	}

	public void addMovie(Movie movie) {
		movies.add(movie);
		DataManager.saveMovie(movies);
		System.out.println("Film ajouté: " + movie.getTitre());
	}

	public List<Movie> searchMovie(String title) {
		return movies.stream()
				.filter(movie -> movie.getTitre().toLowerCase().contains(title.toLowerCase()))
				.collect(Collectors.toList());
	}

	public void showMovie() {
		for (Movie movie : movies) {
			System.out.println(movie.getTitre() + " par " + movie.getRealistateur());
		}
	}

	public void editMovie(String titre, Movie newMovie) {
		Movie movie = findMovieByTitle(titre);
		if (movie != null) {
			movie.setTitre(newMovie.getTitre());
			movie.setRealistateur(newMovie.getRealistateur());
			movie.setDescription(newMovie.getDescription());
			movie.setGenre(newMovie.getGenre());
			movie.setDuree(newMovie.getDuree());
			movie.setDateSortie(newMovie.getDateSortie());
			movie.setPlateforme(newMovie.getPlateforme());
			movie.setAddBy(newMovie.getAddBy());
			DataManager.saveMovie(movies);
			System.out.println("Film modifié: " + movie.getTitre());
		} else {
			System.out.println("Film non trouvé.");
		}
	}

	public List<Movie> getMovies() {
		return movies;
	}

//	public void sauvegarderModifications() {
//		DataManager.sauvegarderLivres(livres);
//		DataManager.sauvegarderUtilisateurs(utilisateurs);
//	}

	public void deleteMovie(Movie movie) {
		if (movie != null) {
			movies.remove(movie);
			DataManager.saveMovie(movies); // Mettre à jour la liste des films
			System.out.println("Film supprimé: " + movie.getTitre());
		} else {
			System.out.println("Film non trouvé.");
		}
	}

	public Movie findMovieByTitle(String titre) {
		return movies.stream()
				.filter(livre -> livre.getTitre().equals(titre))
				.findFirst()
				.orElse(null);
	}
}
