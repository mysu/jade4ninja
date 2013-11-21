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

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import ninja.Context;
import ninja.Result;
import ninja.Route;
import ninja.template.TemplateEngineHelper;
import ninja.utils.NinjaProperties;
import ninja.utils.ResponseStreams;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

@RunWith(MockitoJUnitRunner.class)
public class TemplateEngineJade4JTest {

    private TemplateEngineJade4J engine;

    @Mock
    private NinjaProperties ninjaProperties;
    @Mock
    private TemplateEngineHelper templateEngineHelper;
    @Mock
    private Context context;
    private Result result = null;
    @InjectMocks
    private Jade4NinjaExceptionHandler exceptionHandler;
    @Mock
    private Logger logger;

    @Before
    public void setup() {
        when(ninjaProperties.isTest()).thenReturn(true);
        when(ninjaProperties.isDev()).thenReturn(false);
        when(ninjaProperties.isProd()).thenReturn(false);
    }

    @Test
    public void shouldRenderJade() {
        when(
                templateEngineHelper.getTemplateForResult(any(Route.class),
                        any(Result.class), any(String.class))).thenReturn(
                "../../test/java/views/test.jade");
        final StringWriter writer = new StringWriter();
        when(context.finalizeHeaders(result)).thenReturn(new ResponseStreams() {

            @Override
            public Writer getWriter() throws IOException {
                return writer;
            }

            @Override
            public OutputStream getOutputStream() throws IOException {
                return null;
            }
        });

        engine = new TemplateEngineJade4J(null, templateEngineHelper,
                ninjaProperties, exceptionHandler, null, null);

        Map<String, Object> model = new HashMap<>();
        model.put("key", "Jade");
        engine.render(context, result, model);

        assertEquals("<h1>Hello World Jade</h1>", writer.toString());
    }
}

