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

