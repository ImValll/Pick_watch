package model.parameter.actors;

import model.DataManager;
import model.movie.Movie;
import model.saga.Saga;
import model.serie.Serie;
import model.serie_courte.SerieCourte;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GestionnaireActor {
	private ArrayList<Actor> actors;

	private List<Movie> movies;
	private List<Saga> sagas;
	private List<Serie> series;
	private List<SerieCourte> seriesCourtes;

	private String[] message = new String[3];

	public GestionnaireActor() {

		actors = DataManager.loadActor();
		if (actors == null) {
			actors = new ArrayList<>();
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

	public void addActor(Actor actor) {
		actors.add(actor);
		DataManager.saveActor(actors);
		System.out.println("Acteur ajouté: " + actor.getName());
	}

	public List<Actor> searchActor(String title) {
		return actors.stream()
				.filter(actor -> actor.getName().toLowerCase().contains(title.toLowerCase()))
				.collect(Collectors.toList());
	}

	public void editActor(String titre, Actor newActor) {
		Actor actor = findActorByTitle(titre);

		boolean utilise = false;
		ArrayList<Movie> movieUtilise = new ArrayList<>();
		ArrayList<Saga> sagaUtilise = new ArrayList<>();
		ArrayList<Serie> serieUtilise = new ArrayList<>();
		ArrayList<SerieCourte> serieCourteUtilise = new ArrayList<>();

		for (Movie movie : movies) {
			if(movie.getActeur() != null) {
				for (Actor actorMovie : movie.getActeur()) {
					if (Objects.equals(actorMovie.getName(), actor.getName())) {
						utilise = true;
						movieUtilise.add(movie);
					}
				}
			}
		}
		for (Saga saga : sagas) {
			if(saga.getActeur() != null) {
				for (Actor actorSaga : saga.getActeur()) {
					if (Objects.equals(actorSaga.getName(), actor.getName())) {
						utilise = true;
						sagaUtilise.add(saga);
					}
				}
			}
		}
		for (Serie serie : series) {
			if(serie.getActeur() != null) {
				for (Actor actorSerie : serie.getActeur()) {
					if (Objects.equals(actorSerie.getName(), actor.getName())) {
						utilise = true;
						serieUtilise.add(serie);
					}
				}
			}
		}
		for (SerieCourte serieCourte : seriesCourtes) {
			if(serieCourte.getActeur() != null) {
				for (Actor actorSerieCourte : serieCourte.getActeur()) {
					if (Objects.equals(actorSerieCourte.getName(), actor.getName())) {
						utilise = true;
						serieCourteUtilise.add(serieCourte);
					}
				}
			}
		}

		if(utilise) {
			if(!movieUtilise.isEmpty()) {
				for(Movie movie : movieUtilise) {
					ArrayList<Actor> actors = new ArrayList<>();
					for(Actor actorMovie : movie.getActeur()) {
						if(!actorMovie.getName().equals(actor.getName())) {
							actors.add(actorMovie);
						}
					}
					actors.add(newActor);
					Actor[] actorArray = actors.toArray(new Actor[0]);
					Movie newMovie = movie;
					newMovie.setActeur(actorArray);
					movies.remove(movie);
					movies.add(newMovie);
				}
				DataManager.saveMovie(movies);
			}
			if(!sagaUtilise.isEmpty()) {
				for(Saga saga : sagaUtilise) {
					ArrayList<Actor> actors = new ArrayList<>();
					for(Actor actorSaga : saga.getActeur()) {
						if(!actorSaga.getName().equals(actor.getName())) {
							actors.add(actorSaga);
						}
					}
					actors.add(newActor);
					Actor[] actorArray = actors.toArray(new Actor[0]);
					Saga newSaga = saga;
					newSaga.setActeur(actorArray);
					sagas.remove(saga);
					sagas.add(newSaga);
				}
				DataManager.saveSaga(sagas);
			}
			if(!serieUtilise.isEmpty()) {
				for(Serie serie : serieUtilise) {
					ArrayList<Actor> actors = new ArrayList<>();
					for(Actor actorSerie : serie.getActeur()) {
						if(!actorSerie.getName().equals(actor.getName())) {
							actors.add(actorSerie);
						}
					}
					actors.add(newActor);
					Actor[] actorArray = actors.toArray(new Actor[0]);
					Serie newSerie = serie;
					newSerie.setActeur(actorArray);
					series.remove(serie);
					series.add(newSerie);
				}
				DataManager.saveSerie(series);
			}
			if(!serieCourteUtilise.isEmpty()) {
				for(SerieCourte serieCourte : serieCourteUtilise) {
					ArrayList<Actor> actors = new ArrayList<>();
					for(Actor actorSerieCourte : serieCourte.getActeur()) {
						if(!actorSerieCourte.getName().equals(actor.getName())) {
							actors.add(actorSerieCourte);
						}
					}
					actors.add(newActor);
					Actor[] actorArray = actors.toArray(new Actor[0]);
					SerieCourte newSerieCourte = serieCourte;
					newSerieCourte.setActeur(actorArray);
					seriesCourtes.remove(serieCourte);
					seriesCourtes.add(newSerieCourte);
				}
				DataManager.saveSerieCourte(seriesCourtes);
			}
		}

		if (actor != null) {
			actor.setName(newActor.getName());
			DataManager.saveActor(actors);
			message[0] = "i";
			message[1] = "Actor modifié";
			message[2] = "Le actor " + actor.getName() + " a été modifié avec succès.";
		} else {
			message[0] = "e";
			message[1] = "Actor non trouvé";
			message[2] = "Erreur, le actor n'a pas été trouvé.";
			System.out.println("Actor non trouvé.");
		}
	}

	public ArrayList<Actor> getActor() {
		return actors;
	}

	public void deleteActor(Actor actor) {
		Boolean peutSupprimer = true;
		String utilise = "";

		for (Movie movie : movies) {
			if(movie.getActeur() != null) {
				for (Actor actorMovie : movie.getActeur()) {
					if (actorMovie.getName().equals(actor.getName())) {
						peutSupprimer = false;
						utilise = "un ou plusieurs films";
					}
				}
			}
		}
		for (Saga saga : sagas) {
			if(saga.getActeur() != null) {
				for (Actor actorSaga : saga.getActeur()) {
					if (actorSaga.getName().equals(actor.getName())) {
						peutSupprimer = false;
						utilise = "une ou plusieurs sagas";
					}
				}
			}
		}
		for (Serie serie : series) {
			if(serie.getActeur() != null) {
				for (Actor actorSerie : serie.getActeur()) {
					if (actorSerie.getName().equals(actor.getName())) {
						peutSupprimer = false;
						utilise = "une ou plusieurs séries";
					}
				}
			}
		}
		for (SerieCourte serieCourte : seriesCourtes) {
			if(serieCourte.getActeur() != null) {
				for (Actor actorSerieCourte : serieCourte.getActeur()) {
					if (actorSerieCourte.getName().equals(actor.getName())) {
						peutSupprimer = false;
						utilise = "une ou plusieurs séries courtes";
					}
				}
			}
		}

		if (actor != null) {
			if(peutSupprimer) {
				actors.remove(actor);
				DataManager.saveActor(actors); // Mettre à jour la liste des actors
				message[0] = "i";
				message[1] = "Actor supprimé";
				message[2] = "Le actor " + actor.getName() + " a été supprimé avec succès.";
			} else {
				message[0] = "e";
				message[1] = "Erreur actor utilisé";
				message[2] = "Impossible de supprimer le actor " + actor.getName() + ", il est utilisé dans " + utilise + ".";
			}
		} else {
			message[0] = "e";
			message[1] = "Actor non trouvé";
			message[2] = "Erreur, le actor n'a pas été trouvé.";
			System.out.println("Actor non trouvé.");
		}
	}

	public Actor findActorByTitle(String titre) {
		return actors.stream()
				.filter(actor -> actor.getName().equals(titre))
				.findFirst()
				.orElse(null);
	}

	public String[] getMessage() {
		String[] tempMessage = message;
		message = new String[3];
		return tempMessage;
	}
}
