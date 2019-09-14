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
package io.spring.slackboot.commands.domain;

import java.util.Objects;

/**
 * @author Greg Turnquist
 */
public class Guide {

	private String path;

	public Guide(String path) {
		this.path = path;
	}

	public String getPath() {
		return this.path;
	}

	public String getName() {

		switch (getType()) {
			case "guide":
				return path.replace("/guides", "").replace("/gs/", "gs-").replace("/", "");
			case "topical guide":
				return path.replace("/guides", "").replace("/topicals/", "top-").replace("/", "");
			case "tutorial":
				return path.replace("/guides", "").replace("/tutorials/", "tut-").replace("/", "");
			default:
				return "unknown";
		}
	}

	public String getType() {

		if (path.contains("/gs")) {
			return "guide";
		} else if (path.contains("/topicals")) {
			return "topical guide";
		} else if (path.contains("/tutorials")) {
			return "tutorial";
		} else {
			return "unknown";
		}
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Guide guide = (Guide) o;
		return Objects.equals(path, guide.path);
	}

	@Override
	public int hashCode() {
		return Objects.hash(path);
	}

	@Override
	public String toString() {
		return "Guide{" + "path='" + path + '\'' + '}';
	}
}
