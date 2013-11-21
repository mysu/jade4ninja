package co.leugim.jade4ninja.template;

import ninja.Result;
import ninja.Route;
import ninja.template.TemplateEngineHelper;

public class TemplateEngineJadeHelper extends TemplateEngineHelper {
    @Override
    public String getTemplateForResult(Route route, Result result, String suffix) {
        return super.getTemplateForResult(route, result, suffix).replace(".ftl.html", TemplateEngineJade4J.SUFFIX);
    }
}
