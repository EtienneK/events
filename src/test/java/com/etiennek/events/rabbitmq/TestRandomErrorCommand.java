package com.etiennek.events.rabbitmq;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import com.etiennek.events.api.Command;

public class TestRandomErrorCommand implements Command {
	private static final long serialVersionUID = -2222745320855928690L;
	
	public static final AtomicInteger COUNT = new AtomicInteger(0);
	
	@Override
	public void handle() {
		if (new Random().nextInt(2) == 0) {
			throw new RuntimeException("Kaboom!");
		}
		COUNT.incrementAndGet();
	}
}
