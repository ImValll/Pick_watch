package model.saga;

import model.Genre;
import model.Plateforme;
import model.Utilisateur;

import java.io.Serializable;
import java.util.Date;

public class Saga implements Serializable {
	private static final long serialVersionUID = 1L;
	private String titre;
	private String realistateur;
	private String description;
	private Genre[] genre;
	private int nombreFilms;
	private Date dateSortiePremier;
	private Date dateSortieDernier;
	private Plateforme[] plateforme;
	private Boolean dejaVu;
	private Utilisateur addBy;

	public Saga(String titre, String realistateur, String description, Genre[] genre, int nombreFilms, Date dateSortiePremier, Date dateSortieDernier, Plateforme[] plateforme, Boolean dejaVu, Utilisateur addBy) {
		this.titre = titre;
		this.realistateur = realistateur;
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

	public Plateforme[] getPlateforme() {
		return plateforme;
	}

	public void setPlateforme(Plateforme[] plateforme) {
		this.plateforme = plateforme;
	}

	public Boolean getDejaVu() {
		return dejaVu;
	}

	public void setDejaVu(Boolean dejaVu) {
		this.dejaVu = dejaVu;
	}

	public Utilisateur getAddBy() {
		return addBy;
	}

	public void setAddBy(Utilisateur addBy) {
		this.addBy = addBy;
	}
}
