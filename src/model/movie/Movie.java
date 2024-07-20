package model.movie;

import model.Genre;
import model.Plateforme;
import model.Utilisateur;

import java.io.Serializable;
import java.util.Date;

public class Movie implements Serializable {
	private static final long serialVersionUID = 1L;
	private String titre;
	private String realistateur;
	private String description;
	private Genre[] genre;
	private int duree;
	private Date dateSortie;
	private Plateforme[] plateforme;
	private boolean dejaVu;
	private Utilisateur addBy;

	public Movie(String titre, String realistateur, String description, Genre[] genre, int duree, Date dateSortie, Plateforme[] plateforme, boolean dejaVu, Utilisateur addBy) {
		this.titre = titre;
		this.realistateur = realistateur;
		this.description = description;
		this.genre = genre;
		this.duree = duree;
		this.dateSortie = dateSortie;
		this.plateforme = plateforme;
		this.dejaVu = dejaVu;
		this.addBy = addBy;
	}

	public String getTitre() {
		return titre;
	}

	public String getRealistateur() {
		return realistateur;
	}

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

	public Plateforme[] getPlateforme() {
		return plateforme;
	}

	public boolean getDejaVu() {
		return dejaVu;
	}

	public Utilisateur getAddBy() {
		return addBy;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public void setRealistateur(String realistateur) {
		this.realistateur = realistateur;
	}

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

	public void setPlateforme(Plateforme[] plateforme) {
		this.plateforme = plateforme;
	}

	public void setDejaVu(boolean dejaVu) {
		this.dejaVu = dejaVu;
	}

	public void setAddBy(Utilisateur addBy) {
		this.addBy = addBy;
	}
}
