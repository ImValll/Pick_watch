package model.movie;

import model.*;
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

public class GestionnaireMovie {
	private List<Movie> movies;

	public GestionnaireMovie() {
		movies = DataManager.loadMovie();
		if (movies == null) {
			movies = new ArrayList<>();
		}
	}

	public void addMovie(Movie movie) {
		movies.add(movie);
		DataManager.saveMovie(movies);
		System.out.println(Language.getBundle().getString("movie.filmAjoute2Point") + movie.getTitre());
	}

	public List<Movie> searchMovie(String title) {
		return movies.stream()
				.filter(movie -> movie.getTitre().toLowerCase().contains(title.toLowerCase()))
				.collect(Collectors.toList());
	}

	public void editMovie(String titre, Movie newMovie) {
		Movie movie = findMovieByTitle(titre);
		if (movie != null) {
			movie.setTitre(newMovie.getTitre());
			movie.setRealistateur(newMovie.getRealistateur());
			movie.setActeur(newMovie.getActeur());
			movie.setDescription(newMovie.getDescription());
			movie.setGenre(newMovie.getGenre());
			movie.setDuree(newMovie.getDuree());
			movie.setDateSortie(newMovie.getDateSortie());
			movie.setPlateforme(newMovie.getPlateforme());
			movie.setAddBy(newMovie.getAddBy());
			movie.setImagePath(newMovie.getImagePath());
			DataManager.saveMovie(movies);
			System.out.println(Language.getBundle().getString("movie.filmModifie2Point") + movie.getTitre());
		} else {
			System.out.println(Language.getBundle().getString("movie.filmNonTrouve"));
		}
	}

	public void updateMovieAddBy(Movie movie, User newAddBy) {
		movie.setAddBy(newAddBy);
	}

	public List<Movie> getMovies() {
		return movies;
	}

	public void deleteMovie(Movie movie) {
		if (movie != null) {
			// Supprimer l'image si elle existe
			String imagePath = movie.getImagePath();
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

			// Supprimer le film de la liste
			movies.remove(movie);
			DataManager.saveMovie(movies); // Mettre à jour la liste des films
			System.out.println(Language.getBundle().getString("movie.filmSupprime2Point") + movie.getTitre());
		} else {
			System.out.println(Language.getBundle().getString("movie.filmNonTrouve"));
		}
	}

	public Movie findMovieByTitle(String titre) {
		return movies.stream()
				.filter(movie -> movie.getTitre().equals(titre))
				.findFirst()
				.orElse(null);
	}

	public Movie pickRandomMovie(String rea, Actor[] acteurs, Genre[] genres, int duree, Date dateSortie, Platform[] plateformes, int dejaVu, User addBy) {
		List<Movie> filteredMovies = new ArrayList<>();

		for (Movie movie : movies) {
			boolean matches = true;

			if (rea != null && !movie.getRealistateur().equalsIgnoreCase(rea)) {
				matches = false;
			}
			else if (acteurs != null && acteurs.length > 0) {
				boolean acteurMatch = false;
				for (Actor acteur : acteurs) {
					Actor[] actorMovie = movie.getActeur();
					if(actorMovie != null) {
						for (Actor movieActeur : movie.getActeur()) {
							if (movieActeur.getName().equals(acteur.getName())) {
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
			else if (dateSortie != null && movie.getDateSortie() == null || dateSortie != null && movie.getDateSortie().getYear() != dateSortie.getYear()) {
				matches = false;
			}
			else if (plateformes != null && plateformes.length > 0) {
				boolean plateformeMatch = false;
				for (Platform plateforme : plateformes) {
					for (Platform moviePlateforme : movie.getPlateforme()) {
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
			if (dejaVu != -1) {
				if ((dejaVu == 0 && movie.getDejaVu()) || (dejaVu == 1 && !movie.getDejaVu())) {
					matches = false;
				}
			}
			if (addBy != null && !movie.getAddBy().getName().equals(addBy.getName())) {
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
