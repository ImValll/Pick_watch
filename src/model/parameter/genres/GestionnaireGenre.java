package model.parameter.genres;

import model.DataManager;
import model.movie.Movie;
import model.saga.Saga;
import model.serie.Serie;
import model.serie_courte.SerieCourte;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GestionnaireGenre {
	private ArrayList<Genre> genres;

	private List<Movie> movies;
	private List<Saga> sagas;
	private List<Serie> series;
	private List<SerieCourte> seriesCourtes;

	private String[] message = new String[3];

	public GestionnaireGenre() {

		genres = DataManager.loadGenre();
		if (genres == null) {
			genres = new ArrayList<>();
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

	public void addGenre(Genre genre) {
		genres.add(genre);
		DataManager.saveGenre(genres);
		System.out.println("Genre ajouté: " + genre.getName());
	}

	public List<Genre> searchGenre(String title) {
		return genres.stream()
				.filter(genre -> genre.getName().toLowerCase().contains(title.toLowerCase()))
				.collect(Collectors.toList());
	}

	public void editGenre(String titre, Genre newGenre) {
		Genre genre = findGenreByTitle(titre);

		boolean utilise = false;
		ArrayList<Movie> movieUtilise = new ArrayList<>();
		ArrayList<Saga> sagaUtilise = new ArrayList<>();
		ArrayList<Serie> serieUtilise = new ArrayList<>();
		ArrayList<SerieCourte> serieCourteUtilise = new ArrayList<>();

		for (Movie movie : movies) {
			if(movie.getGenre() != null) {
				for (Genre genreMovie : movie.getGenre()) {
					if (Objects.equals(genreMovie.getName(), genre.getName())) {
						utilise = true;
						movieUtilise.add(movie);
					}
				}
			}
		}
		for (Saga saga : sagas) {
			if(saga.getGenre() != null) {
				for (Genre genreSaga : saga.getGenre()) {
					if (Objects.equals(genreSaga.getName(), genre.getName())) {
						utilise = true;
						sagaUtilise.add(saga);
					}
				}
			}
		}
		for (Serie serie : series) {
			if(serie.getGenre() != null) {
				for (Genre genreSerie : serie.getGenre()) {
					if (Objects.equals(genreSerie.getName(), genre.getName())) {
						utilise = true;
						serieUtilise.add(serie);
					}
				}
			}
		}
		for (SerieCourte serieCourte : seriesCourtes) {
			if(serieCourte.getGenre() != null) {
				for (Genre genreSerieCourte : serieCourte.getGenre()) {
					if (Objects.equals(genreSerieCourte.getName(), genre.getName())) {
						utilise = true;
						serieCourteUtilise.add(serieCourte);
					}
				}
			}
		}

		if(utilise) {
			if(!movieUtilise.isEmpty()) {
				for(Movie movie : movieUtilise) {
					ArrayList<Genre> genres = new ArrayList<>();
					for(Genre genreMovie : movie.getGenre()) {
						if(!genreMovie.getName().equals(genre.getName())) {
							genres.add(genreMovie);
						}
					}
					genres.add(newGenre);
					Genre[] genreArray = genres.toArray(new Genre[0]);
					Movie newMovie = movie;
					newMovie.setGenre(genreArray);
					movies.remove(movie);
					movies.add(newMovie);
				}
				DataManager.saveMovie(movies);
			}
			if(!sagaUtilise.isEmpty()) {
				for(Saga saga : sagaUtilise) {
					ArrayList<Genre> genres = new ArrayList<>();
					for(Genre genreSaga : saga.getGenre()) {
						if(!genreSaga.getName().equals(genre.getName())) {
							genres.add(genreSaga);
						}
					}
					genres.add(newGenre);
					Genre[] genreArray = genres.toArray(new Genre[0]);
					Saga newSaga = saga;
					newSaga.setGenre(genreArray);
					sagas.remove(saga);
					sagas.add(newSaga);
				}
				DataManager.saveSaga(sagas);
			}
			if(!serieUtilise.isEmpty()) {
				for(Serie serie : serieUtilise) {
					ArrayList<Genre> genres = new ArrayList<>();
					for(Genre genreSerie : serie.getGenre()) {
						if(!genreSerie.getName().equals(genre.getName())) {
							genres.add(genreSerie);
						}
					}
					genres.add(newGenre);
					Genre[] genreArray = genres.toArray(new Genre[0]);
					Serie newSerie = serie;
					newSerie.setGenre(genreArray);
					series.remove(serie);
					series.add(newSerie);
				}
				DataManager.saveSerie(series);
			}
			if(!serieCourteUtilise.isEmpty()) {
				for(SerieCourte serieCourte : serieCourteUtilise) {
					ArrayList<Genre> genres = new ArrayList<>();
					for(Genre genreSerieCourte : serieCourte.getGenre()) {
						if(!genreSerieCourte.getName().equals(genre.getName())) {
							genres.add(genreSerieCourte);
						}
					}
					genres.add(newGenre);
					Genre[] genreArray = genres.toArray(new Genre[0]);
					SerieCourte newSerieCourte = serieCourte;
					newSerieCourte.setGenre(genreArray);
					seriesCourtes.remove(serieCourte);
					seriesCourtes.add(newSerieCourte);
				}
				DataManager.saveSerieCourte(seriesCourtes);
			}
		}

		if (genre != null) {
			genre.setName(newGenre.getName());
			DataManager.saveGenre(genres);
			message[0] = "i";
			message[1] = "Genre modifié";
			message[2] = "Le genre " + genre.getName() + " a été modifié avec succès.";
		} else {
			message[0] = "e";
			message[1] = "Genre non trouvé";
			message[2] = "Erreur, le genre n'a pas été trouvé.";
			System.out.println("Genre non trouvé.");
		}
	}

	public ArrayList<Genre> getGenre() {
		return genres;
	}

	public void deleteGenre(Genre genre) {
		Boolean peutSupprimer = true;
		String utilise = "";

		for (Movie movie : movies) {
			if(movie.getGenre() != null) {
				for (Genre genreMovie : movie.getGenre()) {
					if (genreMovie.getName().equals(genre.getName())) {
						peutSupprimer = false;
						utilise = "un ou plusieurs films";
					}
				}
			}
		}
		for (Saga saga : sagas) {
			if(saga.getGenre() != null) {
				for (Genre genreSaga : saga.getGenre()) {
					if (genreSaga.getName().equals(genre.getName())) {
						peutSupprimer = false;
						utilise = "une ou plusieurs sagas";
					}
				}
			}
		}
		for (Serie serie : series) {
			if(serie.getGenre() != null) {
				for (Genre genreSerie : serie.getGenre()) {
					if (genreSerie.getName().equals(genre.getName())) {
						peutSupprimer = false;
						utilise = "une ou plusieurs séries";
					}
				}
			}
		}
		for (SerieCourte serieCourte : seriesCourtes) {
			if(serieCourte.getGenre() != null) {
				for (Genre genreSerieCourte : serieCourte.getGenre()) {
					if (genreSerieCourte.getName().equals(genre.getName())) {
						peutSupprimer = false;
						utilise = "une ou plusieurs séries courtes";
					}
				}
			}
		}

		if (genre != null) {
			if(peutSupprimer) {
				genres.remove(genre);
				DataManager.saveGenre(genres); // Mettre à jour la liste des genres
				message[0] = "i";
				message[1] = "Genre supprimé";
				message[2] = "Le genre " + genre.getName() + " a été supprimé avec succès.";
			} else {
				message[0] = "e";
				message[1] = "Erreur genre utilisé";
				message[2] = "Impossible de supprimer le genre " + genre.getName() + ", il est utilisé dans " + utilise + ".";
			}
		} else {
			message[0] = "e";
			message[1] = "Genre non trouvé";
			message[2] = "Erreur, le genre n'a pas été trouvé.";
			System.out.println("Genre non trouvé.");
		}
	}

	public Genre findGenreByTitle(String titre) {
		return genres.stream()
				.filter(genre -> genre.getName().equals(titre))
				.findFirst()
				.orElse(null);
	}

	public String[] getMessage() {
		String[] tempMessage = message;
		message = new String[3];
		return tempMessage;
	}
}
