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
package io.spring.slackboot.core.domain;

import org.springframework.context.ApplicationEvent;

/**
 * When the WebSocket to Slack's RTM interface is successfully opened, transmit this {@link ApplicationEvent}
 * to all interested parties, including the {@link Self} details about the bot.
 *
 * @author Greg Turnquist
 */
public class BotLoggedInEvent extends ApplicationEvent {

	public BotLoggedInEvent(Self source) {
		super(source);
	}

	public Self getSelf() {
		return (Self) this.getSource();
	}
}
