/**
 * Copyright (c) 2011 Qunar.com. All Rights Reserved.
 */
package com.qunar.donglinma.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qunar.donglinma.util.model.ClassInfo;

/**
 * @author donglinma
 * 
 * @version $Id$ 2013年12月17日 下午2:25:15
 */
public class JsonParser {
    public List<ClassInfo> parseJson(String className, String packageName, String jsonContent) {
        List<ClassInfo> classInfos = new ArrayList<ClassInfo>();
        JSONObject jsonObject = JSON.parseObject(jsonContent);

        fillInfo(className, packageName, jsonObject, classInfos, null);

        return classInfos;
    }

    private void fillInfo(String className, String packagename, JSONObject jsonObject, List<ClassInfo> res,
            ClassInfo previous) {
        ClassInfo info = previous == null ? new ClassInfo() : previous;
        if (previous == null) {
            res.add(info);
        }
        Map<String, String> properties = info.getProperties();
        if (properties == null) {
            properties = new HashMap<String, String>();
            info.setProperties(properties);
        }

        info.setClassName(className);
        info.setPackageName(packagename);

        for (Entry<String, Object> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof JSONObject) {
                String newClassName = getClassName(key);
                fillInfo(newClassName, packagename, (JSONObject) value, res, null);
                properties.put(key, newClassName);
                continue;
            }
            if (value instanceof JSONArray) {
                info.addImport("java.util.List");
                ClassInfo subClassInfo = new ClassInfo();
                boolean isAdd = false;
                @SuppressWarnings("rawtypes")
                Class clazz = null;
                boolean isDiff = false;
                for (Object obj : (JSONArray) value) {
                    if (obj instanceof JSONObject) {
                        isAdd = true;
                        fillInfo(getClassName(key), packagename, (JSONObject) obj, res, subClassInfo);
                    }
                    @SuppressWarnings("rawtypes")
                    Class targetClazz = obj.getClass();
                    if (clazz != null && !targetClazz.equals(clazz)) {
                        isDiff = true;
                    }
                    clazz = targetClazz;
                }

                if (isAdd) {
                    properties.put(key, "List<" + getClassName(key) + ">");
                    res.add(subClassInfo);
                } else {
                    if (clazz == null || isDiff) {
                        properties.put(key, "List");
                    } else {
                        properties.put(key, "List<" + clazz.getSimpleName() + ">");
                    }

                }
                continue;
            }
            System.out.println(value);
            System.out.println(properties);
            properties.put(key, value.getClass().getSimpleName());
        }
    }

    public String getClassName(String instanceName) {
        return instanceName.substring(0, 1).toUpperCase() + instanceName.substring(1);
    }
}
