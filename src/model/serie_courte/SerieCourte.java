package model.serie_courte;

import model.parameter.actors.Actor;
import model.parameter.genres.Genre;
import model.parameter.platforms.Platform;
import model.parameter.users.User;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

public class SerieCourte implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	private String titre;
	private Actor[] acteur;
	private String description;
	private Genre[] genre;
	private int nombreSaison;
	private int nombreEpisode;
	private int dureeMoyenne;
	private Date dateSortiePremiereSaison;
	private Date dateSortieDerniereSaison;
	private Platform[] plateforme;
	private Boolean dejaVu;
	private User addBy;

	public SerieCourte(String titre, Actor[] acteur, String description, Genre[] genre, int nombreSaison, int nombreEpisode, int dureeMoyenne, Date dateSortiePremiereSaison, Date dateSortieDerniereSaison, Platform[] plateforme, Boolean dejaVu, User addBy) {
		this.titre = titre;
		this.acteur = acteur;
		this.description = description;
		this.genre = genre;
		this.nombreSaison = nombreSaison;
		this.nombreEpisode = nombreEpisode;
		this.dureeMoyenne = dureeMoyenne;
		this.dateSortiePremiereSaison = dateSortiePremiereSaison;
		this.dateSortieDerniereSaison = dateSortieDerniereSaison;
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

	public Actor[] getActeur() {
		return acteur;
	}

	public void setActeur(Actor[] acteur) {
		this.acteur = acteur;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Genre[] getGenre() {
		return genre;
	}

	public void setGenre(Genre[] genre) {
		this.genre = genre;
	}

	public int getNombreSaison() {
		return nombreSaison;
	}

	public void setNombreSaison(int nombreSaison) {
		this.nombreSaison = nombreSaison;
	}

	public int getNombreEpisode() {
		return nombreEpisode;
	}

	public void setNombreEpisode(int nombreEpisode) {
		this.nombreEpisode = nombreEpisode;
	}

	public int getDureeMoyenne() {
		return dureeMoyenne;
	}

	public void setDureeMoyenne(int dureeMoyenne) {
		this.dureeMoyenne = dureeMoyenne;
	}

	public Date getDateSortiePremiereSaison() {
		return dateSortiePremiereSaison;
	}

	public void setDateSortiePremiereSaison(Date dateSortiePremiereSaison) {
		this.dateSortiePremiereSaison = dateSortiePremiereSaison;
	}

	public Date getDateSortieDerniereSaison() {
		return dateSortieDerniereSaison;
	}

	public void setDateSortieDerniereSaison(Date dateSortieDerniereSaison) {
		this.dateSortieDerniereSaison = dateSortieDerniereSaison;
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
