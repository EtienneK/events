package com.etiennek.events.rabbitmq;

import java.io.Serializable;

public class Command implements Serializable {

	private static final long serialVersionUID = -3091499400755631604L;
	
	private String id;
	private String className;
	private String json;

	Command() {
	}

	Command(String className, String id, String json) {
		this.className = className;
		this.id = id;
		this.json = json;
	}

	String getId() {
		return id;
	}

	String getClassName() {
		return className;
	}

	String getJson() {
		return json;
	}

}
