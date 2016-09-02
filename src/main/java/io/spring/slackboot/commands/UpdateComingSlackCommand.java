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

import io.spring.slackboot.core.AbstractSlackCommand;
import io.spring.slackboot.core.domain.MessageEvent;

import org.springframework.stereotype.Component;

/**
 * @author Greg Turnquist
 */
@Component
public class UpdateComingSlackCommand extends AbstractSlackCommand {

	public static final String GITHUB_REPO = "https://github.com/spring-io/slackboot";
	public static final String TRAVIS_REPO = "https://travis-ci.com/spring-io/slackboot";

	@Override
	public boolean match(MessageEvent message) {

		return message.getAttachments().stream()
			.filter(attachment ->
				attachment.getText().contains("<" + GITHUB_REPO + "/commit")
					||
				attachment.getText().contains("Build <" + TRAVIS_REPO)
			)
			.findAny()
			.isPresent();
	}

	@Override
	public void handle(MessageEvent message) {

		message.getAttachments().stream()
			.filter(attachment -> attachment.getText().contains("<" + GITHUB_REPO + "/commit"))
			.findAny()
			.ifPresent(attachment -> {
				getSlackService().sendMessage(getToken(), "Ooh! Has someone made a change?", message.getChannel(), true);
			});

		message.getAttachments().stream()
			.filter(attachment ->
				attachment.getText().contains("Build <" + TRAVIS_REPO + "/builds")
				&&
				attachment.getText().contains("passed in")
			)
			.findAny()
			.ifPresent(attachment -> {
				getSlackService().sendMessage(getToken(), "Yipee! Looks like a new upgrade for me.", message.getChannel(), true);
			});

		message.getAttachments().stream()
			.filter(attachment ->
				attachment.getText().contains("Build <" + TRAVIS_REPO + "/builds")
					&&
					attachment.getText().contains("errored in")
			)
			.findAny()
			.ifPresent(attachment -> {
				getSlackService().sendMessage(getToken(), ":cry: Sorry that build job failed.", message.getChannel(), true);
			});

	}
}
