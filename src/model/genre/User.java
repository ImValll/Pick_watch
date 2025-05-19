package model.genre;

import java.io.Serializable;

public class User implements Enum, Serializable {
	private String name;

	public User(String name) {
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
