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
import java.util.List;
import java.util.stream.Collectors;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.SimpleHash;
import io.spring.slackboot.commands.domain.Guide;
import io.spring.slackboot.core.SelfAwareSlackCommand;
import io.spring.slackboot.core.domain.MessageEvent;
import io.spring.slackboot.core.services.FreemarkerService;
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

	public static final String GUIDE_CLASS = "a.guide--title";

	private final FreemarkerService freemarkerService;

	private final DefaultObjectWrapper wrapper;

	public ListGuidesSlackCommand(FreemarkerService freemarkerService) {

		this.freemarkerService = freemarkerService;
		this.wrapper = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25).build();
	}

	@Override
	protected boolean also(MessageEvent message) {
		return message.getText().toLowerCase().contains("list guides");
	}

	@Override
	public void handle(MessageEvent message) {

		try {
			Document doc = Jsoup.connect("https://spring.io/guides").get();

			List<Guide> guides = doc.select(GUIDE_CLASS).stream()
				.map(element -> element.attr("href"))
				.sorted()
				.map(guide -> new Guide(guide))
				.collect(Collectors.toList());

			SimpleHash model = new SimpleHash(this.wrapper);
			model.put("guides", guides);
			model.put("site", "https://spring.io");
			String helpMessage = freemarkerService.processTemplateIntoString(this.getClass().getSimpleName() + "-message.ftl", model);

			getSlackService().sendMessage(getToken(), helpMessage, message.getChannel(), true);

			getCounterService().increment("slack.boot.executed." + this.getClass().getSimpleName());

		} catch (IOException e) {
			log.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}
}
