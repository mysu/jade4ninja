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

package co.leugim.jade4ninja.template;

import ninja.Context;
import ninja.Result;
import ninja.i18n.Messages;

import com.google.common.base.Optional;

public class JadeI18nHandler {

    private Messages messages;
    private Context context;
    private Optional<Result> result;

    public JadeI18nHandler(Messages messages, Context context, Result result) {
        this.messages = messages;
        this.context = context;
        this.result = Optional.of(result);
    }

    public String value(String key, String... args){
        if (key == null || key.isEmpty()) {

            throw new RuntimeException("Using i18n without any key is not possible.");
        }
            Optional<String> optResult = messages.get(key, context, result, args);
            if(optResult.isPresent())
                return optResult.get();
            else
                return key;

    }
}
