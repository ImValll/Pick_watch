package model.serie;

import model.Genre;
import model.Plateforme;
import model.Utilisateur;

import java.io.Serializable;
import java.util.Date;

public class Serie implements Serializable {
	private static final long serialVersionUID = 1L;
	private String titre;
	private String description;
	private Genre[] genre;
	private int nombreSaison;
	private int nombreEpisode;
	private int dureeMoyenne;
	private Date dateSortiePremiereSaison;
	private Date dateSortieDerniereSaison;
	private Plateforme[] plateforme;
	private Boolean dejaVu;
	private Utilisateur addBy;

	public Serie(String titre, String description, Genre[] genre, int nombreSaison, int nombreEpisode, int dureeMoyenne, Date dateSortiePremiereSaison, Date dateSortieDerniereSaison, Plateforme[] plateforme, Boolean dejaVu, Utilisateur addBy) {
		this.titre = titre;
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
