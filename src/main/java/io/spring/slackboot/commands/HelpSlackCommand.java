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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.spring.slackboot.core.SelfAwareSlackCommand;
import io.spring.slackboot.core.SlackCommand;
import io.spring.slackboot.core.domain.MessageEvent;
import io.spring.slackboot.core.domain.SlackBootProperties;
import io.spring.slackboot.core.services.MustacheTemplateService;
import io.spring.slackboot.core.services.SlackService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author Greg Turnquist
 */
@Component
public class HelpSlackCommand extends SelfAwareSlackCommand implements ApplicationContextAware {

	private final MustacheTemplateService mustacheTemplateService;

	private ApplicationContext applicationContext;

	public HelpSlackCommand(SlackService slackService, SlackBootProperties slackBootProperties,
			MustacheTemplateService mustacheTemplateService) {

		super(slackService, slackBootProperties);
		this.mustacheTemplateService = mustacheTemplateService;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	protected boolean also(MessageEvent message) {
		return message.getText().toLowerCase().contains("help");
	}

	@Override
	public void handle(MessageEvent message) {

		List<String> commandHelp = applicationContext
				.getBeansOfType(SlackCommand.class).values().stream().map(command -> mustacheTemplateService
						.processTemplateIntoString(command.getClass().getSimpleName() + "-help", Collections.emptyMap()))
				.collect(Collectors.toList());

		Map<String, Object> model = new HashMap<>();
		model.put("commands", commandHelp);
		String helpMessage = mustacheTemplateService.processTemplateIntoString(this.getClass().getSimpleName() + "-message",
				model);

		getSlackService().sendMessage(getToken(), helpMessage, message.getChannel(), true);
	}

}
