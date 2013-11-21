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
