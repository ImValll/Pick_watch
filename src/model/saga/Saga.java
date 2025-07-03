package model.saga;

import model.parameter.actors.Actor;
import model.parameter.genres.Genre;
import model.parameter.platforms.Platform;
import model.parameter.users.User;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

public class Saga implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	private String titre;
	private String realistateur;
	private Actor[] acteur;
	private String description;
	private Genre[] genre;
	private int nombreFilms;
	private Date dateSortiePremier;
	private Date dateSortieDernier;
	private Platform[] plateforme;
	private Boolean dejaVu;
	private User addBy;

	public Saga(String titre, String realistateur, Actor[] acteur, String description, Genre[] genre, int nombreFilms, Date dateSortiePremier, Date dateSortieDernier, Platform[] plateforme, Boolean dejaVu, User addBy) {
		this.titre = titre;
		this.realistateur = realistateur;
		this.acteur = acteur;
		this.description = description;
		this.genre = genre;
		this.nombreFilms = nombreFilms;
		this.dateSortiePremier = dateSortiePremier;
		this.dateSortieDernier = dateSortieDernier;
		this.plateforme = plateforme;
		this.dejaVu = dejaVu;
		this.addBy = addBy;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getRealistateur() {
		return realistateur;
	}

	public void setRealistateur(String realistateur) {
		this.realistateur = realistateur;
	}

	public Actor[] getActeur() {
		return acteur;
	}

	public void setActeur(Actor[] acteur) {
		this.acteur = acteur;
	}

	public Genre[] getGenre() {
		return genre;
	}

	public void setGenre(Genre[] genre) {
		this.genre = genre;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getNombreFilms() {
		return nombreFilms;
	}

	public void setNombreFilms(int nombreFilms) {
		this.nombreFilms = nombreFilms;
	}

	public Date getDateSortiePremier() {
		return dateSortiePremier;
	}

	public void setDateSortiePremier(Date dateSortiePremier) {
		this.dateSortiePremier = dateSortiePremier;
	}

	public Date getDateSortieDernier() {
		return dateSortieDernier;
	}

	public void setDateSortieDernier(Date dateSortieDernier) {
		this.dateSortieDernier = dateSortieDernier;
	}

	public Platform[] getPlateforme() {
		return plateforme;
	}

	public void setPlateforme(Platform[] plateforme) {
		this.plateforme = plateforme;
	}

	public Boolean getDejaVu() {
		return dejaVu;
	}

	public void setDejaVu(Boolean dejaVu) {
		this.dejaVu = dejaVu;
	}

	public User getAddBy() {
		return addBy;
	}

	public void setAddBy(User addBy) {
		this.addBy = addBy;
	}
}
