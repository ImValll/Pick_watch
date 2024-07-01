package model;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class TestDeserialization {
	public static void main(String[] args) {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("movie.ser"))) {
			Movie movie = (Movie) ois.readObject();
			System.out.println("Movie deserialized successfully.");
			System.out.println("Title: " + movie.getTitre());
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
