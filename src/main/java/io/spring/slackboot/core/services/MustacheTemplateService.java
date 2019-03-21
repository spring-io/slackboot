/*
 * Copyright 2017 the original author or authors.
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
package io.spring.slackboot.core.services;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.mustache.MustacheResourceTemplateLoader;
import org.springframework.stereotype.Service;

import com.samskivert.mustache.Mustache;

/**
 * @author Greg Turnquist
 */
@Service
public class MustacheTemplateService {

	private static final Logger log = LoggerFactory.getLogger(MustacheTemplateService.class);

	private final MustacheResourceTemplateLoader mustacheResourceTemplateLoader;
	private final Mustache.Compiler compiler;

	public MustacheTemplateService(MustacheResourceTemplateLoader mustacheResourceTemplateLoader, Mustache.Compiler compiler) {

		this.mustacheResourceTemplateLoader = mustacheResourceTemplateLoader;
		this.compiler = compiler;
	}

	public String processTemplateIntoString(String templateName, Map model) {

		try {
			String content = compiler.compile(
				mustacheResourceTemplateLoader.getTemplate(templateName)).execute(model);
			return content;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return "Unable to render " + templateName + ". Please check logs for details";
		}
	}
}
