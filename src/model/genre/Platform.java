package model.genre;

import java.io.Serializable;

public class Platform implements Enum, Serializable {
	private String name;

	public Platform(String name) {
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
