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

import io.spring.slackboot.core.domain.BotLoggedInEvent;
import io.spring.slackboot.core.domain.MessageEvent;
import io.spring.slackboot.core.domain.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.ApplicationListener;

/**
 * @author Greg Turnquist
 */
public abstract class SelfAwareSlackCommand extends AbstractSlackCommand implements ApplicationListener<BotLoggedInEvent> {

	private static final Logger log = LoggerFactory.getLogger(SelfAwareSlackCommand.class);

	private Self self;

	@Override
	public void onApplicationEvent(BotLoggedInEvent event) {
		log.info(event.getSelf().getName() + " is now logged in.");
		this.self = event.getSelf();
	}

	@Override
	public boolean match(MessageEvent message) {
		return itsMe(message) && also(message);
	}

	protected boolean itsMe(MessageEvent message) {
		return message.getText().contains("<@" + self.getId() + ">");
	}
	protected abstract boolean also(MessageEvent message);

}
