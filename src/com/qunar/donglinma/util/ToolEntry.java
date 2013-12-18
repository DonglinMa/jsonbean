/**
 * Copyright (c) 2011 Qunar.com. All Rights Reserved.
 */
package com.qunar.donglinma.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qunar.donglinma.util.model.ClassInfo;

/**
 * @author donglinma
 *
 * @version  $Id$ 2013年12月17日 下午3:35:53
 */
public class ToolEntry {
    private JsonParser parser = new JsonParser();
    private JavabeanGenerator gen = new JavabeanGenerator();
    public Map<String,String> generateJavaBean(String className,String packageName,String jsonContent){
        Map<String,String> result = new HashMap<String, String>();
        List<ClassInfo> parseResult = parser.parseJson(className, packageName, jsonContent);
        for(ClassInfo info : parseResult){
            String classContent = gen.generate(info);
            result.put(info.getClassName() + ".java", classContent);
        }
        
        return result;
    }
}
