package co.leugim.jade4ninja.template;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import ninja.Context;
import ninja.Result;
import ninja.template.TemplateEngine;
import ninja.template.TemplateEngineHelper;
import ninja.utils.NinjaProperties;
import ninja.utils.ResponseStreams;

import org.slf4j.Logger;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.neuland.jade4j.Jade4J;
import de.neuland.jade4j.Jade4J.Mode;
import de.neuland.jade4j.JadeConfiguration;
import de.neuland.jade4j.exceptions.JadeException;
import de.neuland.jade4j.template.ClasspathTemplateLoader;
import de.neuland.jade4j.template.FileTemplateLoader;
import de.neuland.jade4j.template.JadeTemplate;

@Singleton
public class TemplateEngineJade4J implements TemplateEngine {

    private static final String SUFFIX = ".jade";
    private static final String CONTENT_TYPE = "text/html";

    private final Logger logger;
    private final NinjaProperties ninjaProperties;
    private JadeConfiguration jadeConfiguration;
    private TemplateEngineHelper templateEngineHelper;
    private Jade4NinjaExceptionHandler exceptionHandler;

    @Inject
    public TemplateEngineJade4J(Logger logger,
                                TemplateEngineHelper templateEngineHelper,
                                NinjaProperties ninjaProperties,
                                Jade4NinjaExceptionHandler exceptionHandler) {
        this.logger = logger;
        this.ninjaProperties = ninjaProperties;
        this.templateEngineHelper = templateEngineHelper;
        this.exceptionHandler = exceptionHandler;

        configureJade4J();
    }

    private void configureJade4J() {
        jadeConfiguration = new JadeConfiguration();
        String srcDir = System.getProperty("user.dir") + File.separator + "src"
                + File.separator + "main" + File.separator + "java";

        if ((ninjaProperties.isDev() || ninjaProperties.isTest())
                && new File(srcDir).exists()) {
            jadeConfiguration.setTemplateLoader(new FileTemplateLoader(srcDir,
                    "UTF-8"));
            jadeConfiguration.setCaching(false);
            jadeConfiguration.setPrettyPrint(true);
        } else {
            jadeConfiguration.setTemplateLoader(new ClasspathTemplateLoader());
            jadeConfiguration.setCaching(true);
            jadeConfiguration.setPrettyPrint(false);
        }

        jadeConfiguration.setMode(Mode.HTML);

    }

    @Override
    public void invoke(Context context, Result result) {
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

        // TODO add session, context, cookie objects

        String templateName = templateEngineHelper.getTemplateForResult(
                context.getRoute(), result, SUFFIX);

        ResponseStreams responseStreams = context.finalizeHeaders(result);
        Writer out;
        try {
             out = responseStreams.getWriter();
        } catch (IOException e) {
            logger.error("Error getting writer to render JADE template", e);
            return;
        }
        
        try {
            JadeTemplate template = jadeConfiguration.getTemplate(templateName);
            Jade4J.render(template, model, out);
            responseStreams.getWriter().flush();
            responseStreams.getWriter().close();
        } catch (JadeException | IOException e) {
            exceptionHandler.handleException(out, e);
        }
    }

    @Override
    public String getSuffixOfTemplatingEngine() {
        return SUFFIX;
    }

    @Override
    public String getContentType() {
        return CONTENT_TYPE;
    }

}
