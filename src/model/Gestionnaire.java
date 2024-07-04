package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
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

	public void updateMovieAddBy(Movie movie, Utilisateur newAddBy) {
		movie.setAddBy(newAddBy);
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

	public Movie pickRandomMovie(String rea, Genre[] genres, int duree, Date dateSortie, Plateforme[] plateformes, Utilisateur addBy) {
		List<Movie> filteredMovies = new ArrayList<>();

		for (Movie movie : movies) {
			boolean matches = true;

			if (rea != null && !movie.getRealistateur().equalsIgnoreCase(rea)) {
				matches = false;
			}
			if (genres != null && genres.length > 0) {
				boolean genreMatch = false;
				for (Genre genre : genres) {
					for (Genre movieGenre : movie.getGenre()) {
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
			if (duree != 0 && movie.getDuree() > duree) {
				matches = false;
			}
			if (dateSortie != null && movie.getDateSortie() == null || dateSortie != null && movie.getDateSortie().getYear() != dateSortie.getYear()) {
				matches = false;
			}
			if (plateformes != null && plateformes.length > 0) {
				boolean plateformeMatch = false;
				for (Plateforme plateforme : plateformes) {
					for (Plateforme moviePlateforme : movie.getPlateforme()) {
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
			if (addBy != null && !movie.getAddBy().equals(addBy)) {
				matches = false;
			}

			if (matches) {
				filteredMovies.add(movie);
			}
		}

		if (filteredMovies.isEmpty()) {
			return null;
		}

		Random rand = new Random();
		return filteredMovies.get(rand.nextInt(filteredMovies.size()));
	}
}
