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

import io.spring.slackboot.commands.domain.GitHubHookDetails;
import io.spring.slackboot.commands.domain.Guide;
import io.spring.slackboot.core.SelfAwareSlackCommand;
import io.spring.slackboot.core.domain.MessageEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.github.api.impl.GitHubTemplate;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author Greg Turnquist
 */
@Component
public class GuideCacheSlackCommand extends SelfAwareSlackCommand {

	private static final Logger log = LoggerFactory.getLogger(GuideCacheSlackCommand.class);

	private final GitHubTemplate gitHubTemplate;

	public GuideCacheSlackCommand(GitHubTemplate gitHubTemplate) {

		this.gitHubTemplate = gitHubTemplate;
	}

	@Override
	protected boolean also(MessageEvent message) {
		return all(message) || justOne(message);
	}

	@Override
	public void handle(MessageEvent message) {

		if (justOne(message)) {

			Arrays.stream(message.getText().split("\\s+"))
				.filter(token -> token.startsWith("gs-") || token.startsWith("tut-") || token.startsWith("top-"))
				.forEach(guideName -> {
					getSlackService().sendMessage(getToken(), "Ok, I'll try to clear " + guideName, message.getChannel(), true);
					fireHook(guideName, message);
				});

		} else if (all(message)) {

			log.info("Okay, I'll try to reset ALL the guides");
			getSlackService().sendMessage(getToken(), "Ok, I'll try to clear ALL the guides", message.getChannel(), true);

			try {
				Document doc = Jsoup.connect("https://spring.io/guides").get();

				doc.select(ListGuidesSlackCommand.GUIDE_CLASS).stream()
					.map(element -> element.attr("href"))
					.sorted()
					.map(guide -> new Guide(guide))
					.map(guide -> guide.getName())
					.forEach(guideName -> fireHook(guideName, message));

				getSlackService().sendMessage(getToken(), "Done and DONE!", message.getChannel(), true);

			} catch (IOException e) {
				log.error(e.getMessage());
				getSlackService().sendMessage(getToken(), "Hmm. Something went wrong -> " + e.getMessage(), message.getChannel(), true);
			}

		} else {

			getSlackService().sendMessage(getToken(), "Gee, I don't know how to handle that.", message.getChannel(), true);
		}

		getCounterService().increment("slack.boot.executed." + this.getClass().getSimpleName());
	}

	private boolean all(MessageEvent message) {
		return message.getText().toLowerCase().contains("clear all caches");
	}

	private boolean justOne(MessageEvent message) {
		return message.getText().toLowerCase().contains("clear cache");
	}

	private void fireHook(String guide, MessageEvent message) {

		gitHubTemplate.repoOperations().getHooks("spring-guides", guide).stream()
			.map(gitHubHook -> gitHubTemplate.getRestTemplate().getForObject(gitHubHook.getUrl(), GitHubHookDetails.class))
			.filter(gitHubHookDetails -> Optional.ofNullable(gitHubHookDetails.getConfig().getUrl())
				.map(url -> url.contains("spring.io/webhook"))
				.orElse(false))
			.findAny()
			.map(gitHubHookDetails -> Optional.of(gitHubTemplate.getRestTemplate().postForEntity(gitHubHookDetails.getUrl() + "/test", null, Object.class)))
			.orElseGet(() -> {
				getSlackService().sendMessage(getToken(), "Hmm. Looks like you don't have a webhook yet. See https://github.com/spring-guides/getting-started-guides/wiki/Create-a-Repository for help.", message.getChannel(), true);
				return Optional.empty();
			})
			.ifPresent(response -> {
				if (response.getStatusCodeValue() < 300) {
					getSlackService().sendMessage(getToken(), guide + " has been cleared.", message.getChannel(), true);
					getCounterService().increment("slack.boot.guides.cacheCleared.successful");
				} else {
					getSlackService().sendMessage(getToken(),
						"Wow. Something went wrong with " + guide + ", " + response.toString(), message.getChannel(), true);
					getCounterService().increment("slack.boot.guides.cacheCleared.failure");
				}
			});
	}
}
