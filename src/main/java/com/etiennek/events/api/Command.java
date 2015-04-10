package com.etiennek.events.api;

public abstract class Command {
	private String id;

	public Command(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

}
