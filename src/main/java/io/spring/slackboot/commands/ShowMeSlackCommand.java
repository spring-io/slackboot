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

import java.io.IOException;
import java.util.Arrays;

import io.spring.slackboot.commands.domain.Guide;
import io.spring.slackboot.core.SelfAwareSlackCommand;
import io.spring.slackboot.core.domain.MessageEvent;
import io.spring.slackboot.core.domain.SlackBootProperties;
import io.spring.slackboot.core.services.SlackService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Greg Turnquist
 */
@Component
public class ShowMeSlackCommand extends SelfAwareSlackCommand {

	private static final Logger log = LoggerFactory.getLogger(ShowMeSlackCommand.class);

	private static final String GUIDE_CLASS = "a.guide--title";

	public ShowMeSlackCommand(SlackService slackService, SlackBootProperties slackBootProperties) {
		super(slackService, slackBootProperties);
	}

	@Override
	protected boolean also(MessageEvent message) {
		return message.getText().contains("show me");
	}

	@Override
	public void handle(MessageEvent message) {

		try {
			Document doc = Jsoup.connect("https://spring.io/guides").get();

			doc.select(GUIDE_CLASS).stream().map(element -> element.attr("href")).map(Guide::new)
					.filter(
							guide -> Arrays.stream(message.getText().split("\\s+")).anyMatch(token -> token.equals(guide.getName())))
					.forEach(guide -> getSlackService().sendMessage(getToken(),
							"Click here to see " + guide.getName() + " -> https://spring.io" + guide.getPath(), message.getChannel(),
							true));
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new RuntimeException(e);
		}

	}
}
