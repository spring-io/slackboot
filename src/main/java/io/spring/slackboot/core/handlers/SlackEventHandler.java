/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.spring.slackboot.core.handlers;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Greg Turnquist
 */
public abstract class SlackEventHandler<T> {

	private static final Logger log = LoggerFactory.getLogger(SlackEventHandler.class);

	private final ObjectMapper objectMapper;

	public SlackEventHandler(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	/**
	 * Does it handles this type of message?
	 *
	 * @param jsonMessage
	 * @return
	 */
	public boolean handles(Map<String, Object> jsonMessage) {

		return Optional.ofNullable(jsonMessage.get("type").toString())
			.map(this::doesHandle)
			.orElseThrow(() -> new IllegalStateException("All Slack messages must have 'type'"));
	}

	/**
	 * Process the incoming message.
	 *
	 * @param message
	 */
	public void handle(String message) {
		convert(message).ifPresent(this::doHandle);
	}

	/**
	 * Transform the message into an {@link Optional}, strongly typed message.
	 *
	 * @param message
	 * @return
	 */
	protected Optional<T> convert(String message) {
		
		try {
			return Optional.of(objectMapper.readValue(message, type()));
		} catch (IOException e) {
			log.error(e.getMessage());
			return Optional.empty();
		}

	}

	/**
	 * Based on the "type" embedded in the message, decide whether or not to handle it.
	 *
	 * @param type
	 * @return
	 */
	protected abstract boolean doesHandle(String type);

	/**
	 * After having converted the message to T, handle it.
	 *
	 * @param convertedMessage
	 */
	protected abstract void doHandle(T convertedMessage);

	/**
	 * What type does this handler process?
	 *
	 * @return
	 */
	protected abstract TypeReference<?> type();
}
