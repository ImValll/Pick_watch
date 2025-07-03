package model.parameter.actors;

import model.parameter.Enum;

import java.io.Serializable;

public class Actor implements Enum, Serializable {
	private String name;

	public Actor(String name) {
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
