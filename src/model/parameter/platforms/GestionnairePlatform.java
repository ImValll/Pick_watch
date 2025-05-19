package model.parameter.platforms;

import model.DataManager;
import model.genre.Platform;
import model.movie.Movie;
import model.saga.Saga;
import model.serie.Serie;
import model.serie_courte.SerieCourte;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GestionnairePlatform {
	private ArrayList<Platform> platforms;
	
	private List<Movie> movies;
	private List<Saga> sagas;
	private List<Serie> series;
	private List<SerieCourte> seriesCourtes;
	
	public GestionnairePlatform() {
		platforms = DataManager.loadPlatform();
		if (platforms == null) {
			platforms = new ArrayList<>();
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
	
	public void addPlatform(Platform platform) {
		platforms.add(platform);
		DataManager.savePlatform(platforms);
		System.out.println("Plateforme ajoutée: " + platform.getName());
	}
	
	public List<Platform> searchPlatform(String title) {
		return platforms.stream()
				.filter(platform -> platform.getName().toLowerCase().contains(title.toLowerCase()))
				.collect(Collectors.toList());
	}
	
	public void showPlatform() {
		for (Platform platform : platforms) {
			System.out.println(platform.getName());
		}
	}
	
	public void editPlatform(String titre, Platform newPlatform) {
		Platform platform = findPlatformByTitle(titre);
	
		Boolean utilise = true;
		ArrayList<Movie> movieUtilise = new ArrayList<>();
		ArrayList<Saga> sagaUtilise = new ArrayList<>();
		ArrayList<Serie> serieUtilise = new ArrayList<>();
		ArrayList<SerieCourte> serieCourteUtilise = new ArrayList<>();
	
		for (Movie movie : movies) {
			for(Platform platformMovie : movie.getPlateforme()) {
				if(platformMovie.equals(platform)) {
					utilise = false;
					movieUtilise.add(movie);
				}
			}
		}
		for (Saga saga : sagas) {
			for(Platform platformSaga : saga.getPlateforme()) {
				if(platformSaga.equals(platform)) {
					utilise = false;
					sagaUtilise.add(saga);
				}
			}
		}
		for (Serie serie : series) {
			for(Platform platformSerie : serie.getPlateforme()) {
				if(platformSerie.equals(platform)) {
					utilise = false;
					serieUtilise.add(serie);
				}
			}
		}
		for (SerieCourte serieCourte : seriesCourtes) {
			for(Platform platformSerieCourte : serieCourte.getPlateforme()) {
				if(platformSerieCourte.equals(platform)) {
					utilise = false;
					serieCourteUtilise.add(serieCourte);
				}
			}
		}
	
		if(utilise) {
			if(!movieUtilise.isEmpty()) {
				for(Movie movie : movieUtilise) {
					ArrayList<Platform> platforms = new ArrayList<>();
					for(Platform platformMovie : movie.getPlateforme()) {
						if(!platformMovie.equals(platform)) {
							platforms.add(platformMovie);
						}
					}
					platforms.add(platform);
					Platform[] platformArray = platforms.toArray(new Platform[0]);
					Movie newMovie = movie;
					newMovie.setPlateforme(platformArray);
					movies.remove(movie);
					movies.add(newMovie);
				}
				DataManager.saveMovie(movies);
			}
			if(!sagaUtilise.isEmpty()) {
				for(Saga saga : sagaUtilise) {
					ArrayList<Platform> platforms = new ArrayList<>();
					for(Platform platformSaga : saga.getPlateforme()) {
						if(!platformSaga.equals(platform)) {
							platforms.add(platformSaga);
						}
					}
					platforms.add(platform);
					Platform[] platformArray = platforms.toArray(new Platform[0]);
					Saga newSaga = saga;
					newSaga.setPlateforme(platformArray);
					sagas.remove(saga);
					sagas.add(newSaga);
				}
				DataManager.saveSaga(sagas);
			}
			if(!serieUtilise.isEmpty()) {
				for(Serie serie : serieUtilise) {
					ArrayList<Platform> platforms = new ArrayList<>();
					for(Platform platformSerie : serie.getPlateforme()) {
						if(!platformSerie.equals(platform)) {
							platforms.add(platformSerie);
						}
					}
					platforms.add(platform);
					Platform[] platformArray = platforms.toArray(new Platform[0]);
					Serie newSerie = serie;
					newSerie.setPlateforme(platformArray);
					series.remove(serie);
					series.add(newSerie);
				}
				DataManager.saveSerie(series);
			}
			if(!serieCourteUtilise.isEmpty()) {
				for(SerieCourte serieCourte : serieCourteUtilise) {
					ArrayList<Platform> platforms = new ArrayList<>();
					for(Platform platformSerieCourte : serieCourte.getPlateforme()) {
						if(!platformSerieCourte.equals(platform)) {
							platforms.add(platformSerieCourte);
						}
					}
					platforms.add(platform);
					Platform[] platformArray = platforms.toArray(new Platform[0]);
					SerieCourte newSerieCourte = serieCourte;
					newSerieCourte.setPlateforme(platformArray);
					seriesCourtes.remove(serieCourte);
					seriesCourtes.add(newSerieCourte);
				}
				DataManager.saveSerieCourte(seriesCourtes);
			}
		}
	
		if (platform != null) {
			platform.setName(newPlatform.getName());
			DataManager.savePlatform(platforms);
			System.out.println("Plateforme modifiée : " + platform.getName());
		} else {
			System.out.println("Plateforme non trouvée.");
		}
	}
	
	public ArrayList<Platform> getPlatform() {
		return platforms;
	}
	
	public void deletePlatform(Platform platform) {
		Boolean peutSupprimer = true;
		String utilise = "";
	
		for (Movie movie : movies) {
			for(Platform platformMovie : movie.getPlateforme()) {
				if(platformMovie.equals(platform)) {
					peutSupprimer = false;
					utilise = "un ou plusieurs films";
				}
			}
		}
		for (Saga saga : sagas) {
			for(Platform platformSaga : saga.getPlateforme()) {
				if(platformSaga.equals(platform)) {
					peutSupprimer = false;
					utilise = "une ou plusieurs sagas";
				}
			}
		}
		for (Serie serie : series) {
			for(Platform platformSerie : serie.getPlateforme()) {
				if(platformSerie.equals(platform)) {
					peutSupprimer = false;
					utilise = "une ou plusieurs séries";
				}
			}
		}
		for (SerieCourte serieCourte : seriesCourtes) {
			for(Platform platformSerieCourte : serieCourte.getPlateforme()) {
				if(platformSerieCourte.equals(platform)) {
					peutSupprimer = false;
					utilise = "une ou plusieurs séries courtes";
				}
			}
		}
	
		if (platform != null) {
			if(peutSupprimer) {
				platforms.remove(platform);
				DataManager.savePlatform(platforms); // Mettre à jour la liste des platforms
				System.out.println("Plateforme supprimée : " + platform.getName());
			} else {
				System.out.println("Impossible de supprimer le plateforme " + platform.getName() + ", elle est utilisée dans " + utilise + ".");
			}
		} else {
			System.out.println("Plateforme non trouvée.");
		}
	}
	
	public Platform findPlatformByTitle(String titre) {
		return platforms.stream()
				.filter(platform -> platform.getName().equals(titre))
				.findFirst()
				.orElse(null);
	}
}
