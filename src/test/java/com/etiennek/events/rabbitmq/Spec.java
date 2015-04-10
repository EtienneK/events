package com.etiennek.events.rabbitmq;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.jayway.awaitility.Awaitility.*;
import static java.util.concurrent.TimeUnit.*;

import com.etiennek.events.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class Spec {
	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private RabbitMqCommandBus commandBus;

	@Autowired
	private TestCommandHandler testCommandHandler;

	@Before
	public void init() {
		while (rabbitTemplate.receive(RabbitMqConfiguration.QUEUE_NAME) != null) {
			System.out.println("clearing queue");
		}
		testCommandHandler.reset();
	}

	@Test
	public void enqueue_Should_put_a_Command_Entity_in_the_repository() {
		// Arrange
		String id = UUID.randomUUID().toString();
		com.etiennek.events.api.Command command = new TestCommand(id);
		// Act
		commandBus.enqueue(command);
		// Assert
		await().atMost(5, SECONDS).until(
				() -> Assert.assertEquals(1, testCommandHandler.getCount()));
	}

	@Test
	public void enqueue_When_a_Command_was_enqueued_a_handler_should_be_called_that_will_handle_the_command() {
		final int NUMBER_OF_COMMANDS = 100;
		for (int i = 0; i < NUMBER_OF_COMMANDS; i++) {
			// Arrange
			String id = UUID.randomUUID().toString();
			com.etiennek.events.api.Command command = new TestCommand(id);
			// Act
			commandBus.enqueue(command);
		}
		// Assert
		await().atMost(5, SECONDS).until(
				() -> Assert.assertEquals(NUMBER_OF_COMMANDS,
						testCommandHandler.getCount()));
	}
	
	@Test
	public void enqueue_When_a_Command_was_enqueued_a_handler_should_be_called_that_will_handle_the_command_and_be_able_to_replay_when_failures_happen() {
		final int NUMBER_OF_COMMANDS = 100;
		for (int i = 0; i < NUMBER_OF_COMMANDS; i++) {
			// Arrange
			String id = UUID.randomUUID().toString();
			com.etiennek.events.api.Command command = new TestCommand(id);
			// Act
			commandBus.enqueue(command);
		}
		// Assert
		await().atMost(5, SECONDS).until(
				() -> Assert.assertEquals(NUMBER_OF_COMMANDS,
						testCommandHandler.getCount()));
	}

}
