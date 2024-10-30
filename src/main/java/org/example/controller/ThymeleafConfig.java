package org.example.controller;

import jakarta.servlet.ServletException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.FileTemplateResolver;

public class ThymeleafConfig {

    private TemplateEngine engine;

    public void init(String templatesPath) throws ServletException {
        engine = new TemplateEngine();

        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix(templatesPath);
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        System.out.println(resolver);
        engine.addTemplateResolver(resolver);
    }

    public TemplateEngine getEngine() {
        return engine;
    }

}
