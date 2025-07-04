package model.movie;


import model.parameter.actors.Actor;
import model.parameter.genres.Genre;
import model.parameter.platforms.Platform;
import model.parameter.users.User;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

public class Movie implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	private String titre;
	private String realistateur;
	private Actor[] acteur;
	private String description;
	private Genre[] genre;
	private int duree;
	private Date dateSortie;
	private Platform[] plateforme;
	private boolean dejaVu;
	private User addBy;
	private String imagePath;

	public Movie(String titre, String realistateur, Actor[] acteur, String description, Genre[] genre, int duree, Date dateSortie, Platform[] plateforme, boolean dejaVu, User addBy, String imagePath) {
		this.titre = titre;
		this.realistateur = realistateur;
		this.acteur = acteur;
		this.description = description;
		this.genre = genre;
		this.duree = duree;
		this.dateSortie = dateSortie;
		this.plateforme = plateforme;
		this.dejaVu = dejaVu;
		this.addBy = addBy;
		this.imagePath = imagePath;
	}

	public String getTitre() {
		return titre;
	}

	public String getRealistateur() {
		return realistateur;
	}

	public Actor[] getActeur() {return acteur;}

	public String getDescription() {
		return description;
	}

	public Genre[] getGenre() {
		return genre;
	}

	public int getDuree() {
		return duree;
	}

	public Date getDateSortie() {
		return dateSortie;
	}

	public Platform[] getPlateforme() {
		return plateforme;
	}

	public boolean getDejaVu() {
		return dejaVu;
	}

	public User getAddBy() {
		return addBy;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public void setRealistateur(String realistateur) {
		this.realistateur = realistateur;
	}

	public void setActeur(Actor[] acteur) {this.acteur = acteur;}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setGenre(Genre[] genre) {
		this.genre = genre;
	}

	public void setDuree(int duree) {
		this.duree = duree;
	}

	public void setDateSortie(Date dateSortie) {
		this.dateSortie = dateSortie;
	}

	public void setPlateforme(Platform[] plateforme) {
		this.plateforme = plateforme;
	}

	public void setDejaVu(boolean dejaVu) {
		this.dejaVu = dejaVu;
	}

	public void setAddBy(User addBy) {
		this.addBy = addBy;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
}
