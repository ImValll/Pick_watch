package model.parameter.genres;

import model.parameter.Enum;

import java.io.Serializable;

public class Genre implements Enum, Serializable {
	private String name;

	public Genre(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
