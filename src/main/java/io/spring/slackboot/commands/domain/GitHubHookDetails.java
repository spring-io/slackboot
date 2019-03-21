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
package io.spring.slackboot.commands.domain;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Greg Turnquist
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubHookDetails {

	private int id;
	private String url;
	private GitHubHookConfig config;
}
