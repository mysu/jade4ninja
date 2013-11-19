package co.leugim.jade4ninja;

import co.leugim.jade4ninja.template.TemplateEngineJade4J;

import com.google.inject.AbstractModule;

public class Jade4NinjaModule extends AbstractModule{

    @Override
    protected void configure() {
      System.out.println("Initializing Jade4NinjaModule");
      bind(TemplateEngineJade4J.class);
      System.out.println("Jade4NinjaModule initialized");  
    }

}
