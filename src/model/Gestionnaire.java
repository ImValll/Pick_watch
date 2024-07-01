package model;

import java.time.LocalDate;
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

//	public List<Movie> searchMovie(String titreCritere, String auteurCritere) {
//		return livres.stream()
//				.filter(livre -> livre.getTitre().toLowerCase().contains(titreCritere.toLowerCase()) &&
//						livre.getAuteur().toLowerCase().contains(auteurCritere.toLowerCase()))
//				.collect(Collectors.toList());
//	}

	public void showMovie() {
		for (Movie movie : movies) {
			System.out.println(movie.getTitre() + " par " + movie.getRealistateur());
		}
	}

//	public void editMovie(String titre, String auteur, Livre nouveauLivre) {
//		Livre livre = trouverLivreParTitreEtAuteur(titre, auteur);
//		if (livre != null) {
//			livre.setTitre(nouveauLivre.getTitre());
//			livre.setAuteur(nouveauLivre.getAuteur());
//			livre.setEdition(nouveauLivre.getEdition());
//			livre.setAnneeParution(nouveauLivre.getAnneeParution());
//			livre.setGenre(nouveauLivre.getGenre());
//			livre.setEmplacement(nouveauLivre.getEmplacement());
//			DataManager.sauvegarderLivres(livres);
//			System.out.println("Livre modifié: " + livre.getTitre());
//		} else {
//			System.out.println("Livre non trouvé.");
//		}
//	}

//	public Livre trouverLivreParTitreEtAuteur(String titre, String auteur) {
//		return livres.stream()
//				.filter(livre -> livre.getTitre().equals(titre) && livre.getAuteur().equals(auteur))
//				.findFirst()
//				.orElse(null);
//	}

	public List<Movie> getMovies() {
		return movies;
	}

//	public void sauvegarderModifications() {
//		DataManager.sauvegarderLivres(livres);
//		DataManager.sauvegarderUtilisateurs(utilisateurs);
//	}

//	public void supprimerLivre(String titre, String auteur) {
//		Livre livre = trouverLivreParTitreEtAuteur(titre, auteur);
//		if (livre != null) {
//			livres.remove(livre);
//			DataManager.sauvegarderLivres(livres); // Mettre à jour la liste des livres
//			System.out.println("Livre supprimé: " + livre.getTitre());
//		} else {
//			System.out.println("Livre non trouvé.");
//		}
//	}
}
