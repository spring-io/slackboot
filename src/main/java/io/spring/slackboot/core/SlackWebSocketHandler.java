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
package io.spring.slackboot.core;

import io.spring.slackboot.core.domain.SlackBootProperties;
import io.spring.slackboot.core.handlers.SlackEventHandler;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Listen to the WebSocket and consume messages.
 *
 * @author Greg Turnquist
 */
@Component
class SlackWebSocketHandler extends TextWebSocketHandler {

	private final static Logger log = LoggerFactory.getLogger(SlackWebSocketHandler.class);

	private final ObjectMapper objectMapper;
	private final SlackBootProperties slackBootProperties;

	private DeadmanSwitch deadmanSwitch;
	private List<SlackEventHandler> slackEventHandlers;
	private final PingingService pingingService;

	private final static Random RANDOM = new Random();

	public SlackWebSocketHandler(ObjectMapper objectMapper, SlackBootProperties slackBootProperties,
			DeadmanSwitch deadmanSwitch, @Autowired(required = false) List<SlackEventHandler> slackEventHandlers,
			PingingService pingingService) {

		this.objectMapper = objectMapper;
		this.slackBootProperties = slackBootProperties;
		this.deadmanSwitch = deadmanSwitch;
		this.slackEventHandlers = slackEventHandlers;
		this.pingingService = pingingService;
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

		if (this.pingingService.getSession() == null) {
			this.pingingService.setSession(session);
		}

		try {
			deadmanSwitch.reset("WebSocket", Duration.ofMinutes(slackBootProperties.getDeadmanLimitMinutes()));
			pingingService.reset();

			if (slackBootProperties.isRandomNap()) {
				if (RANDOM.nextBoolean()) {
					log.info("I think I'll just take a little nap...");
					Thread.sleep(120000L);
				}
			}

			Map<String, Object> jsonMessage = objectMapper.readValue(message.getPayload(),
					new TypeReference<Map<String, Object>>() {});

			slackEventHandlers.stream() //
					.filter(slackEventHandler -> slackEventHandler.handles(jsonMessage)) //
					.forEach(slackEventHandler -> slackEventHandler.handle(message.getPayload()));
		} catch (Exception e) {
			// Swallow all exceptions to avoid breaking the event loop.
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	public List<SlackEventHandler> getSlackEventHandlers() {
		return slackEventHandlers;
	}
}
