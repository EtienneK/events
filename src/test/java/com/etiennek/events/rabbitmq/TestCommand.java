package com.etiennek.events.rabbitmq;

import java.util.concurrent.atomic.AtomicInteger;

class TestCommand implements com.etiennek.events.api.Command {
	private static final long serialVersionUID = 1924584082886677554L;

	public static final AtomicInteger COUNT = new AtomicInteger(0);

	@Override
	public void handle() {
		if (COUNT.get() == 55) {
			try {
				Thread.sleep(50000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(COUNT.incrementAndGet());
	}
}
