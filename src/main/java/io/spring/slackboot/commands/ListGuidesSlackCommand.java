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
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import io.spring.slackboot.commands.domain.Guide;
import io.spring.slackboot.core.SelfAwareSlackCommand;
import io.spring.slackboot.core.domain.MessageEvent;
import io.spring.slackboot.core.domain.SlackBootProperties;
import io.spring.slackboot.core.services.MustacheTemplateService;
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
public class ListGuidesSlackCommand extends SelfAwareSlackCommand {

	private static final Logger log = LoggerFactory.getLogger(ListGuidesSlackCommand.class);

	private static final String GUIDE_CLASS = "a.guide--title";

	private final MustacheTemplateService mustacheTemplateService;

	public ListGuidesSlackCommand(SlackService slackService, SlackBootProperties slackBootProperties,
			MustacheTemplateService mustacheTemplateService) {

		super(slackService, slackBootProperties);
		this.mustacheTemplateService = mustacheTemplateService;
	}

	@Override
	protected boolean also(MessageEvent message) {
		return message.getText().toLowerCase().contains("list guides");
	}

	@Override
	public void handle(MessageEvent message) {

		try {
			Document doc = Jsoup.connect("https://spring.io/guides").get();

			List<Guide> guides = doc.select(GUIDE_CLASS).stream().map(element -> element.attr("href")).sorted()
					.map(Guide::new).collect(Collectors.toList());

			HashMap<String, Object> model = new HashMap<>();
			model.put("guides", guides);
			model.put("site", "https://spring.io");
			String helpMessage = mustacheTemplateService
					.processTemplateIntoString(this.getClass().getSimpleName() + "-message", model);

			getSlackService().sendMessage(getToken(), helpMessage, message.getChannel(), true);

		} catch (IOException e) {
			log.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}
}
