/*
 * Copyright 2019 the original author or authors.
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
package io.spring.slackboot.core;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author Greg Turnquist
 */
@Component
@EnableScheduling
public class PingingService {

	private static final Logger log = LoggerFactory.getLogger(PingingService.class);

	private final Duration interval;

	private WebSocketSession session;
	private LocalDateTime lastTimeAMessageCameIn;

	public PingingService() {

		this.interval = Duration.ofMillis(5000);
		this.lastTimeAMessageCameIn = LocalDateTime.now();
	}

	public WebSocketSession getSession() {
		return session;
	}

	public void setSession(WebSocketSession session) {
		this.session = session;
	}

	public void reset() {

		log.debug("Pong...");
		this.lastTimeAMessageCameIn = LocalDateTime.now();
	}

	@Scheduled(fixedRate = 5000L)
	void potentiallySendPing() throws IOException {

		if (this.session != null) {
			Duration between = Duration.between(this.lastTimeAMessageCameIn, LocalDateTime.now());

			if (between.getSeconds() > this.interval.getSeconds()) {
				log.debug("Ping!");
				session.sendMessage(new TextMessage("{\"type\": \"ping\"}"));
			}
		}
	}

}
