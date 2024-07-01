package model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

public class TestSerialization {
	public static void main(String[] args) {
		Movie movie = new Movie(
				"Inception",
				"Christopher Nolan",
				"A mind-bending thriller",
				new Genre[]{Genre.SF},
				148,
				new Date(),
				new Plateforme[]{Plateforme.Netflix},
				Utilisateur.Valentin
		);

		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("movie.ser"))) {
			oos.writeObject(movie);
			System.out.println("Movie serialized successfully.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
