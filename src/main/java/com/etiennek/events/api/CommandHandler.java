package com.etiennek.events.api;

public interface CommandHandler<C extends Command> {
	public void handle(C command);
}
