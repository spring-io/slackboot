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
package io.spring.slackboot.core.domain;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Bootified property settings.
 *
 * @author Greg Turnquist
 */
@Data
@Component
@ConfigurationProperties(prefix = "slack.boot")
public class SlackBootProperties {

	private String token;
	private String githubToken;
	private boolean debugFeign = false;

	private long deadmanLimitMinutes = 1L;

	// Pure debugging tool, meant to force a deadman switch to kick in.
	private boolean randomNap = false;

}
