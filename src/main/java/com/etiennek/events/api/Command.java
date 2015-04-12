package com.etiennek.events.api;

import java.io.Serializable;

public interface Command extends Serializable {
	void handle();
}
