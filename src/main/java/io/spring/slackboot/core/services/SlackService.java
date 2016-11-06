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
package io.spring.slackboot.core.services;

import io.spring.slackboot.core.domain.RtmStartResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign-powered RPC client to interact with Slack API.
 *
 * @author Greg Turnquist
 */
@FeignClient(name = "slackService", url = "https://slack.com")
public interface SlackService {

	@RequestMapping(method = RequestMethod.GET, value = "/api/rtm.start", params = {"pretty=1"})
	RtmStartResponse rtmStart(@RequestParam("token") String token);

	@RequestMapping(method = RequestMethod.POST, value = "/api/chat.postMessage")
	void sendMessage(@RequestParam("token") String token, @RequestParam("text") String text,
					 @RequestParam("channel") String channel, @RequestParam("as_user") boolean asUser);


}
