package co.leugim.jade4ninja.template;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import ninja.Context;
import ninja.Result;
import ninja.i18n.Lang;
import ninja.i18n.Messages;
import ninja.template.TemplateEngine;
import ninja.template.TemplateEngineHelper;
import ninja.utils.NinjaProperties;
import ninja.utils.ResponseStreams;

import org.slf4j.Logger;

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

    public static final String SUFFIX = ".jade";
    private static final String CONTENT_TYPE = "text/html";

    private final Logger logger;
    private final NinjaProperties ninjaProperties;
    private JadeConfiguration jadeConfiguration;
    private TemplateEngineHelper templateEngineHelper;
    private Jade4NinjaExceptionHandler exceptionHandler;
    private Messages messages;
    private Lang lang;

    @Inject
    public TemplateEngineJade4J(Logger logger,
                                TemplateEngineHelper templateEngineHelper,
                                NinjaProperties ninjaProperties,
                                Jade4NinjaExceptionHandler exceptionHandler,
                                Messages messages,
                                Lang lang) {
        this.logger = logger;
        this.ninjaProperties = ninjaProperties;
        this.templateEngineHelper = templateEngineHelper;
        this.exceptionHandler = exceptionHandler;
        this.messages = messages;
        this.lang = lang;

        configureJade4J();
    }

    private void configureJade4J() {
        jadeConfiguration = new JadeConfiguration();
        String srcDir = buildSrcDir();

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

    private String buildSrcDir() {
        return System.getProperty("user.dir") + File.separator + "src"
                + File.separator + "main" + File.separator + "java"
                + File.separator;
    }

    @Override
    public void invoke(Context context, Result result) {

        JadeModelParamsBuilder modelBuilder = new JadeModelParamsBuilder();
        modelBuilder.with(context).with(messages).with(result).with(lang);
        Map<String, Object> model = modelBuilder.build();

        render(context, result, model);
    }

    protected final void render(Context context,
                                Result result,
                                Map<String, Object> model) {
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
