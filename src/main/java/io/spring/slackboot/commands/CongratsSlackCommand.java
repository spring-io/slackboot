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
package io.spring.slackboot.commands;

import java.util.Arrays;
import java.util.Random;

import io.spring.slackboot.core.SelfAwareSlackCommand;
import io.spring.slackboot.core.domain.MessageEvent;

import org.springframework.stereotype.Component;

/**
 * @author Greg Turnquist
 */
@Component
public class CongratsSlackCommand extends SelfAwareSlackCommand {

	private String[] compliments = new String[]{"good job", "awesome", "nice work", "thanks"};
	private String[] thanks = new String []{"Thanks!", "Glad to be of help"};

	private final Random random;

	public CongratsSlackCommand() {
		this.random = new Random();
	}

	@Override
	protected boolean also(MessageEvent message) {

		return Arrays.stream(compliments)
			.anyMatch(s -> message.getText().toLowerCase().contains(s));
	}

	@Override
	public void handle(MessageEvent message) {
		
		if (message.getText().toLowerCase().contains("thanks")) {
			getSlackService().sendMessage(getToken(), "You're welcome.", message.getChannel(), true);
		} else {
			getSlackService().sendMessage(getToken(), nextMessage(), message.getChannel(), true);
		}

		getCounterService().increment("slack.boot.executed." + this.getClass().getSimpleName());
	}

	/**
	 * Using the {@link Random}, find the next entry in the {@literal thanks} message.
	 * 
	 * @return
	 */
	private String nextMessage() {
		return thanks[random.nextInt(thanks.length)];
	}
}
