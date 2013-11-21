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

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.CaseFormat;
import com.google.common.base.Optional;
import com.google.common.collect.Maps;

import ninja.Context;
import ninja.Result;
import ninja.i18n.Lang;
import ninja.i18n.Messages;

public class JadeModelParamsBuilder {
    private Context context;
    private Result result;
    private Messages messages;
    private Lang lang;

    public final JadeModelParamsBuilder with(Context context) {
        this.context = context;
        return this;
    }

    public final JadeModelParamsBuilder with(Result result) {
        this.result = result;
        return this;
    }

    public final JadeModelParamsBuilder with(Messages messages) {
        this.messages = messages;
        return this;
    }

    public final JadeModelParamsBuilder with(Lang lang) {
        this.lang = lang;
        return this;
    }

    public final Map<String, Object> build() {
        checkParams();
        Map<String, Object> model = getRenderableResult();
        setI18n(model);
        setLanguage(model);
        setSession(model);
        setContext(model);
        setFlash(model);
        return model;
    }

    protected void setFlash(Map<String, Object> model) {
        // /////////////////////////////////////////////////////////////////////
        // Convenience method to translate possible flash scope keys.
        // !!! If you want to set messages with placeholders please do that
        // !!! in your controller. We only can set simple messages.
        // Eg. A message like "errorMessage=my name is: {0}" => translate in
        // controller and pass directly.
        // A message like " errorMessage=An error occurred" => use that as
        // errorMessage.
        //
        // get keys via ${flash.KEYNAME}
        // ////////////////////////////////////////////////////////////////////
        Map<String, String> translatedFlashCookieMap = Maps.newHashMap();
        if (context.getFlashCookie() != null)
            for (Entry<String, String> entry : context.getFlashCookie()
                    .getCurrentFlashCookieData().entrySet()) {

                String messageValue = null;

                Optional<String> messageValueOptional = messages.get(
                        entry.getValue(), context, Optional.of(result));

                if (!messageValueOptional.isPresent()) {
                    messageValue = entry.getValue();
                } else {
                    messageValue = messageValueOptional.get();
                }
                // new way
                translatedFlashCookieMap.put(entry.getKey(), messageValue);
            }

        // now we can retrieve flash cookie messages via ${flash.MESSAGE_KEY}
        model.put("flash", translatedFlashCookieMap);
    }

    protected void setContext(Map<String, Object> model) {
        model.put("contextPath", context.getContextPath());
    }

    protected void setSession(Map<String, Object> model) {
        // put all entries of the session cookie to the map.
        // You can access the values by their key in the cookie
        if (context.getSessionCookie() != null
                && !context.getSessionCookie().isEmpty()) {
            model.put("session", context.getSessionCookie().getData());
        }

    }

    protected void setLanguage(Map<String, Object> model) {
        // set language from framework. You can access
        // it in the templates as ${lang}
        Optional<String> language = lang.getLanguage(context,
                Optional.of(result));
        if (language.isPresent()) {
            model.put("lang", language.get());
        }

    }

    protected void setI18n(Map<String, Object> model) {
        model.put("i18n", new JadeI18nHandler(messages, context, result));
    }

    protected void checkParams() {
        if (context == null || result == null || messages == null) {
            throw new IllegalArgumentException(
                    "Context, result or messages cannot be null");
        }
    }

    protected Map<String, Object> getRenderableResult() {
        Object object = result.getRenderable();

        Map<String, Object> model;
        if (object == null) {
            model = Maps.newHashMap();
        } else if (object instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) object;
            model = map;
        } else {
            model = Maps.newHashMap();
            String realClassNameLowerCamelCase = CaseFormat.UPPER_CAMEL.to(
                    CaseFormat.LOWER_CAMEL, object.getClass().getSimpleName());
            model.put(realClassNameLowerCamelCase, object);
        }
        return model;
    }
}
