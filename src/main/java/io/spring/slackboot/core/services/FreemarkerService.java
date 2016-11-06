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

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;

/**
 * Service to render HTML Slack messages using Freemarker template engine.
 *
 * @author Greg Turnquist
 */
@Service
public class FreemarkerService {

	private static final Logger log = LoggerFactory.getLogger(FreemarkerService.class);

	private final FreeMarkerConfigurer configurer;

	public FreemarkerService(FreeMarkerConfigurer configurer) {
		this.configurer = configurer;
	}

	public String processTemplateIntoString(String templateName, Object model) {
		try {
			Configuration configuration = this.configurer.getConfiguration();
			Template template = configuration.getTemplate(templateName);
			return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
		} catch (IOException|TemplateException e) {
			log.error(e.getMessage());
			return "Unable to render " + templateName + ". Please check logs for details";
		}
	}


}
