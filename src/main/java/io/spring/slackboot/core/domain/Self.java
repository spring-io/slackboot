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
package io.spring.slackboot.core.domain;

import java.util.Objects;

/**
 * Captures Slack bot's {@code id} and {@code name}. Avoids hardcoding the name of the bot, allowing commands to "@" the
 * bot dynamically.
 *
 * @author Greg Turnquist
 */
public class Self {

	private String id;
	private String name;

	public Self() {}

	public Self(String id, String name) {

		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Self self = (Self) o;
		return Objects.equals(id, self.id) && Objects.equals(name, self.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	@Override
	public String toString() {
		return "Self{" + "id='" + id + '\'' + ", name='" + name + '\'' + '}';
	}
}
