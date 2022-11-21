package com.example.authmoduls.common.decorator;

import springfox.documentation.spring.web.paths.AbstractPathProvider;

public class ExtendRelativePathProvider extends AbstractPathProvider {

    public static final String ROOT = "";

    public ExtendRelativePathProvider() {
        System.out.println("Loading The Path Resolver");
    }

    @Override
    protected String applicationPath() {
        return ROOT;
    }

    @Override
    protected String getDocumentationPath() {
        return "/api/docs";
    }
}