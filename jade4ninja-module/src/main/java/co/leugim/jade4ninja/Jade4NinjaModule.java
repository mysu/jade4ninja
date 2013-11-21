/**
 * Copyright (C) 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.leugim.jade4ninja;

import ninja.template.TemplateEngineHelper;
import co.leugim.jade4ninja.template.TemplateEngineJade4J;
import co.leugim.jade4ninja.template.TemplateEngineJadeHelper;

import com.google.inject.AbstractModule;

public class Jade4NinjaModule extends AbstractModule{

    @Override
    protected void configure() {
      System.out.println("Initializing Jade4NinjaModule");
      bind(TemplateEngineJade4J.class);
      bind(TemplateEngineHelper.class).to(TemplateEngineJadeHelper.class);
      System.out.println("Jade4NinjaModule initialized");  
    }

}
