package com.etiennek.events.rabbitmq;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import com.etiennek.events.api.CommandHandler;

@Component
public class TestCommandHandler implements CommandHandler<TestCommand> {
	private AtomicInteger count = new AtomicInteger(0);

	@Override
	public void handle(TestCommand command) {
		inc();

		// if (getCount() == 55) {
		/*
		 * try { Thread.sleep(4500); } catch (InterruptedException e) {
		 * e.printStackTrace(); }
		 */
		// throw new RuntimeException("BOOOM!");
		// }

		// System.out.println("YESSSSSSSSSS:" + command.getId() + " || "
		// + getCount());
	}

	public void reset() {
		count.set(0);
	}

	public void inc() {
		count.incrementAndGet();
	}

	public int getCount() {
		return count.get();
	}
}
