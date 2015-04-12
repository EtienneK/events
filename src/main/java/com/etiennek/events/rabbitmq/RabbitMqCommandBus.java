package com.etiennek.events.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

import com.etiennek.events.api.Command;
import com.etiennek.events.api.CommandBus;

@Service
public class RabbitMqCommandBus implements CommandBus {

	private @Autowired AutowireCapableBeanFactory beanFactory;
	private @Autowired RabbitTemplate rabbitTemplate;

	@Override
	public void enqueue(Command command) {
		rabbitTemplate.convertAndSend(RabbitMqConfiguration.QUEUE_NAME,
				SerializationUtils.serialize(command));
	}

	public void route(byte[] commandBytes) {
		Command command = ((Command) SerializationUtils
				.deserialize(commandBytes));
		beanFactory.autowireBean(command);
		command.handle();
	}

}
