package com.etiennek.events.api;

public interface CommandBus {
	void enqueue(Command command);
}
