package model.parameter.platforms;

import model.DataManager;
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

	private String[] message = new String[3];
	
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
	
	public void editPlatform(String titre, Platform newPlatform) {
		Platform platform = findPlatformByTitle(titre);
	
		boolean utilise = false;
		ArrayList<Movie> movieUtilise = new ArrayList<>();
		ArrayList<Saga> sagaUtilise = new ArrayList<>();
		ArrayList<Serie> serieUtilise = new ArrayList<>();
		ArrayList<SerieCourte> serieCourteUtilise = new ArrayList<>();
	
		for (Movie movie : movies) {
			if(movie.getPlateforme() != null) {
				for (Platform platformMovie : movie.getPlateforme()) {
					if (platformMovie.getName().equals(platform.getName())) {
						utilise = true;
						movieUtilise.add(movie);
					}
				}
			}
		}
		for (Saga saga : sagas) {
			if(saga.getPlateforme() != null) {
				for (Platform platformSaga : saga.getPlateforme()) {
					if (platformSaga.getName().equals(platform.getName())) {
						utilise = true;
						sagaUtilise.add(saga);
					}
				}
			}
		}
		for (Serie serie : series) {
			if(serie.getPlateforme() != null) {
				for (Platform platformSerie : serie.getPlateforme()) {
					if (platformSerie.getName().equals(platform.getName())) {
						utilise = true;
						serieUtilise.add(serie);
					}
				}
			}
		}
		for (SerieCourte serieCourte : seriesCourtes) {
			if(serieCourte.getPlateforme() != null) {
				for (Platform platformSerieCourte : serieCourte.getPlateforme()) {
					if (platformSerieCourte.getName().equals(platform.getName())) {
						utilise = true;
						serieCourteUtilise.add(serieCourte);
					}
				}
			}
		}
	
		if(utilise) {
			if(!movieUtilise.isEmpty()) {
				for(Movie movie : movieUtilise) {
					ArrayList<Platform> platforms = new ArrayList<>();
					for(Platform platformMovie : movie.getPlateforme()) {
						if(!platformMovie.getName().equals(platform.getName())) {
							platforms.add(platformMovie);
						}
					}
					platforms.add(newPlatform);
					Platform[] platformArray = platforms.toArray(new Platform[0]);
					movie.setPlateforme(platformArray);
					movies.remove(movie);
					movies.add(movie);
				}
				DataManager.saveMovie(movies);
			}
			if(!sagaUtilise.isEmpty()) {
				for(Saga saga : sagaUtilise) {
					ArrayList<Platform> platforms = new ArrayList<>();
					for(Platform platformSaga : saga.getPlateforme()) {
						if(!platformSaga.getName().equals(platform.getName())) {
							platforms.add(platformSaga);
						}
					}
					platforms.add(newPlatform);
					Platform[] platformArray = platforms.toArray(new Platform[0]);
					saga.setPlateforme(platformArray);
					sagas.remove(saga);
					sagas.add(saga);
				}
				DataManager.saveSaga(sagas);
			}
			if(!serieUtilise.isEmpty()) {
				for(Serie serie : serieUtilise) {
					ArrayList<Platform> platforms = new ArrayList<>();
					for(Platform platformSerie : serie.getPlateforme()) {
						if(!platformSerie.getName().equals(platform.getName())) {
							platforms.add(platformSerie);
						}
					}
					platforms.add(newPlatform);
					Platform[] platformArray = platforms.toArray(new Platform[0]);
					serie.setPlateforme(platformArray);
					series.remove(serie);
					series.add(serie);
				}
				DataManager.saveSerie(series);
			}
			if(!serieCourteUtilise.isEmpty()) {
				for(SerieCourte serieCourte : serieCourteUtilise) {
					ArrayList<Platform> platforms = new ArrayList<>();
					for(Platform platformSerieCourte : serieCourte.getPlateforme()) {
						if(!platformSerieCourte.getName().equals(platform.getName())) {
							platforms.add(platformSerieCourte);
						}
					}
					platforms.add(newPlatform);
					Platform[] platformArray = platforms.toArray(new Platform[0]);
					serieCourte.setPlateforme(platformArray);
					seriesCourtes.remove(serieCourte);
					seriesCourtes.add(serieCourte);
				}
				DataManager.saveSerieCourte(seriesCourtes);
			}
		}
	
		if (platform != null) {
			platform.setName(newPlatform.getName());
			DataManager.savePlatform(platforms);
			message[0] = "i";
			message[1] = "Plateforme modifiée";
			message[2] = "La plateforme " + platform.getName() + " a été modifiée avec succès.";
		} else {
			message[0] = "e";
			message[1] = "Plateforme non trouvée";
			message[2] = "Erreur, la plateforme n'a pas été trouvée.";
			System.out.println("Plateforme non trouvée.");
		}
	}
	
	public ArrayList<Platform> getPlatform() {
		return platforms;
	}
	
	public void deletePlatform(Platform platform) {
		boolean peutSupprimer = true;
		String utilise = "";
	
		for (Movie movie : movies) {
			if(movie.getPlateforme() != null) {
				for (Platform platformMovie : movie.getPlateforme()) {
					if (platformMovie.getName().equals(platform.getName())) {
						peutSupprimer = false;
						utilise = "un ou plusieurs films";
						break;
					}
				}
			}
		}
		for (Saga saga : sagas) {
			if(saga.getPlateforme() != null) {
				for (Platform platformSaga : saga.getPlateforme()) {
					if (platformSaga.getName().equals(platform.getName())) {
						peutSupprimer = false;
						utilise = "une ou plusieurs sagas";
						break;
					}
				}
			}
		}
		for (Serie serie : series) {
			if(serie.getPlateforme() != null) {
				for (Platform platformSerie : serie.getPlateforme()) {
					if (platformSerie.getName().equals(platform.getName())) {
						peutSupprimer = false;
						utilise = "une ou plusieurs séries";
						break;
					}
				}
			}
		}
		for (SerieCourte serieCourte : seriesCourtes) {
			if(serieCourte.getPlateforme() != null) {
				for (Platform platformSerieCourte : serieCourte.getPlateforme()) {
					if (platformSerieCourte.getName().equals(platform.getName())) {
						peutSupprimer = false;
						utilise = "une ou plusieurs séries courtes";
						break;
					}
				}
			}
		}
	
		if (platform != null) {
			if(peutSupprimer) {
				platforms.remove(platform);
				DataManager.savePlatform(platforms); // Mettre à jour la liste des platforms
				message[0] = "i";
				message[1] = "Plateforme supprimée";
				message[2] = "La plateforme " + platform.getName() + " a été supprimée avec succès.";
			} else {
				message[0] = "e";
				message[1] = "Erreur genre utilisé";
				message[2] = "Impossible de supprimer le plateforme " + platform.getName() + ", elle est utilisée dans " + utilise + ".";
			}
		} else {
			message[0] = "e";
			message[1] = "Plateforme non trouvée";
			message[2] = "Erreur, la plateforme n'a pas été trouvée.";
			System.out.println("Plateforme non trouvée.");
		}
	}
	
	public Platform findPlatformByTitle(String titre) {
		return platforms.stream()
				.filter(platform -> platform.getName().equals(titre))
				.findFirst()
				.orElse(null);
	}

	public String[] getMessage() {
		String[] tempMessage = message;
		message = new String[3];
		return tempMessage;
	}
}
