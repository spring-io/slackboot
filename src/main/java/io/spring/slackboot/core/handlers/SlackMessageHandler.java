/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.spring.slackboot.core.handlers;

import java.util.List;

import io.spring.slackboot.core.SlackCommand;
import io.spring.slackboot.core.domain.MessageEvent;
import io.spring.slackboot.core.domain.SlackBootProperties;
import io.spring.slackboot.core.services.SlackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Greg Turnquist
 */
@Component
public class SlackMessageHandler extends SlackEventHandler<MessageEvent> {

	private static final Logger log = LoggerFactory.getLogger(SlackMessageHandler.class);

	private final SlackService slackService;

	private final SlackBootProperties slackBootProperties;

	private final List<SlackCommand> commands;

	public SlackMessageHandler(ObjectMapper objectMapper, SlackService slackService, SlackBootProperties slackBootProperties,
							   @Autowired(required = false) List<SlackCommand> commands) {
		super(objectMapper);
		this.slackService = slackService;
		this.slackBootProperties = slackBootProperties;
		this.commands = commands;
	}

	@Override
	protected void doHandle(MessageEvent message) {
		log.info("Reading '" + message.getText() + "' on channel '" + message.getChannel() + "'");

		commands.stream()
			.filter(slackCommand -> slackCommand.match(message))
			.forEach(slackCommand -> slackCommand.handle(message));
	}

	@Override
	protected boolean doesHandle(String type) {
		return type.equals("message");
	}

	@Override
	protected TypeReference<?> type() {
		return new TypeReference<MessageEvent>() {};
	}
}
