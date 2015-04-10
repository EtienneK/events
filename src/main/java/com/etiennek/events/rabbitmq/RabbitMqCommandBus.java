package com.etiennek.events.rabbitmq;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etiennek.events.api.CommandBus;
import com.etiennek.events.api.CommandHandler;
import com.google.common.base.Throwables;
import com.google.gson.Gson;

@Service
public class RabbitMqCommandBus implements CommandBus {

	private final Gson GSON = new Gson();

	@Autowired
	RabbitTemplate rabbitTemplate;

	@Autowired
	List<CommandHandler> commandHandlers;

	@Override
	public void enqueue(com.etiennek.events.api.Command command) {
		try {
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			ObjectOutputStream o = new ObjectOutputStream(b);
			o.writeObject(new Command(command.getClass().getName(), command
					.getId(), GSON.toJson(command)));

			rabbitTemplate.convertAndSend(RabbitMqConfiguration.QUEUE_NAME,
					b.toByteArray());
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}

	public void route(byte[] command) throws ClassNotFoundException,
			IOException {
		ByteArrayInputStream b = new ByteArrayInputStream(command);
		ObjectInputStream o = new ObjectInputStream(b);
		route((Command) o.readObject());
	}

	public void route(Command command) {
		for (CommandHandler commandHandler : commandHandlers) {

			Type type = ((ParameterizedType) commandHandler.getClass()
					.getGenericInterfaces()[0]).getActualTypeArguments()[0];

			if (command.getClassName().equals(type.getTypeName())) {
				try {
					Class<?> clazz = Class.forName(command.getClassName());
					commandHandler
							.handle((com.etiennek.events.api.Command) GSON
									.fromJson(command.getJson(), clazz));
				} catch (Exception e) {
					throw Throwables.propagate(e);
				}
			}
			return;
		}
	}
}
