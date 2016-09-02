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

import java.util.List;
import java.util.Map;

import io.spring.slackboot.core.handlers.SlackEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Greg Turnquist
 */
@Component
class SlackWebSocketHandler extends TextWebSocketHandler {

	private final static Logger log = LoggerFactory.getLogger(SlackWebSocketHandler.class);

	private final ObjectMapper objectMapper;

	private List<SlackEventHandler> slackEventHandlers;

	public SlackWebSocketHandler(ObjectMapper objectMapper, @Autowired(required = false) List<SlackEventHandler> slackEventHandlers) {
		this.objectMapper = objectMapper;
		this.slackEventHandlers = slackEventHandlers;
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		try {
			Map<String, String> jsonMessage = objectMapper.readValue(message.getPayload(), Map.class);
			log.info(jsonMessage.toString());
			slackEventHandlers.stream()
				.filter(slackEventHandler -> slackEventHandler.handles(jsonMessage))
				.forEach(slackEventHandler -> slackEventHandler.handle(message.getPayload()));
		} catch (Exception e) {
			// Swallow all exceptions to avoid breaking the event loop.
			log.error(e.getMessage());
		}
	}

	public List<SlackEventHandler> getSlackEventHandlers() {
		return slackEventHandlers;
	}

}
