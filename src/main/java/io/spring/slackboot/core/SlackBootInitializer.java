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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import io.spring.slackboot.core.domain.BotLoggedInEvent;
import io.spring.slackboot.core.domain.RtmStartResponse;
import io.spring.slackboot.core.domain.SlackBootProperties;
import io.spring.slackboot.core.services.SlackService;

/**
 * @author Greg Turnquist
 */
@Component
public class SlackBootInitializer implements ApplicationListener<ApplicationReadyEvent>, ApplicationEventPublisherAware {

	private final static Logger log = LoggerFactory.getLogger(SlackBootInitializer.class);

	private final SlackService slackService;
	private final SlackBootProperties slackBootProperties;
	private final SlackWebSocketHandler slackWebSocketHandler;
	private final WebSocketClient webSocketClient;

	private ApplicationEventPublisher applicationEventPublisher;

	public SlackBootInitializer(SlackService slackService, SlackBootProperties slackBootProperties,
								SlackWebSocketHandler slackWebSocketHandler) {

		this.slackService = slackService;
		this.slackBootProperties = slackBootProperties;
		this.slackWebSocketHandler = slackWebSocketHandler;
		this.webSocketClient = 	new StandardWebSocketClient();
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	/**
	 * After finding out that the app is ready, subscribe to Slack's RTM API and await a websocket URL.
	 * Then hook up a custom websocket listener.
	 *
	 * @param applicationReadyEvent
	 */
	@Override
	public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {

		RtmStartResponse response = slackService.rtmStart(slackBootProperties.getToken());

		if (response.isOk()) {

			log.info("My name is " + response.getSelf().getName() + " (<@" + response.getSelf().getId() + ">) and I'm listening on " + response.getUrl());
			this.applicationEventPublisher.publishEvent(new BotLoggedInEvent(response.getSelf()));

			WebSocketConnectionManager webSocketConnectionManager = new WebSocketConnectionManager(
				webSocketClient, slackWebSocketHandler, response.getUrl());

			webSocketConnectionManager.start();
		} else {

			log.error("Connection not ok!");
		}

	}
}
