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
package io.spring.slackboot.commands;

import java.util.Arrays;
import java.util.Random;

import io.spring.slackboot.core.SelfAwareSlackCommand;
import io.spring.slackboot.core.domain.MessageEvent;
import io.spring.slackboot.core.domain.SlackBootProperties;
import io.spring.slackboot.core.services.SlackService;

import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.stereotype.Component;

/**
 * @author Greg Turnquist
 */
@Component
public class CongratsSlackCommand extends SelfAwareSlackCommand {

	private String[] compliments = new String[]{"good job", "awesome", "nice work", "thanks"};

	private String[] thanks = new String []{"Thanks!", "Glad to be of help"};

	private final SlackService slackService;

	private final SlackBootProperties slackBootProperties;

	private final CounterService counterService;

	private final Random random;

	public CongratsSlackCommand(SlackService slackService, SlackBootProperties slackBootProperties, CounterService counterService) {

		this.slackService = slackService;
		this.slackBootProperties = slackBootProperties;
		this.counterService = counterService;
		this.random = new Random();
	}

	@Override
	protected boolean also(MessageEvent message) {
		return Arrays.stream(compliments)
			.filter(s -> message.getText().toLowerCase().contains(s))
			.findAny()
			.isPresent();
	}

	@Override
	public void handle(MessageEvent message) {
		if (message.getText().toLowerCase().contains("thanks")) {
			slackService.sendMessage(slackBootProperties.getToken(), "You're welcome.", message.getChannel(), true);
		} else {
			slackService.sendMessage(slackBootProperties.getToken(), thanks[random.nextInt(thanks.length)], message.getChannel(), true);
		}

		counterService.increment("slack.boot.executed." + this.getClass().getSimpleName());
	}
}
