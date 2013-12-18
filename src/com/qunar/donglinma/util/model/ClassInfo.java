/**
 * Copyright (c) 2011 Qunar.com. All Rights Reserved.
 */
package com.qunar.donglinma.util.model;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author donglinma
 * 
 * @version $Id$ 2013年12月17日 上午11:44:04
 */
public class ClassInfo {
    private String packageName;
    private String className;
    private Set<String> importSet = new HashSet<String>();
    private Map<String, String> properties;

    /**
     * @return the packageName
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * @param packageName the packageName to set
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * @return the className
     */
    public String getClassName() {
        return className;
    }

    /**
     * @param className the className to set
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * @return the properties
     */
    public Map<String, String> getProperties() {
        return properties;
    }

    /**
     * @param properties the properties to set
     */
    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    /**
     * @return the importSet
     */
    public Set<String> getImportSet() {
        return importSet;
    }

    public void addImport(String importContent) {
        this.importSet.add(importContent);
    }
}
