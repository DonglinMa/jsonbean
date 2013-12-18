package com.qunar.donglinma.util;

import java.util.Map;
import java.util.Map.Entry;
import com.qunar.donglinma.util.model.*;
import java.util.Set;

public class JavabeanGenerator
{
  protected static String nl;
  public static synchronized JavabeanGenerator create(String lineSeparator)
  {
    nl = lineSeparator;
    JavabeanGenerator result = new JavabeanGenerator();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "package ";
  protected final String TEXT_2 = ";";
  protected final String TEXT_3 = NL;
  protected final String TEXT_4 = NL + "import ";
  protected final String TEXT_5 = ";";
  protected final String TEXT_6 = NL;
  protected final String TEXT_7 = NL + NL + "public class ";
  protected final String TEXT_8 = "{" + NL + "\t";
  protected final String TEXT_9 = NL + "\tprivate ";
  protected final String TEXT_10 = " ";
  protected final String TEXT_11 = ";" + NL + "\t";
  protected final String TEXT_12 = NL + "\t" + NL + "\tpublic void set";
  protected final String TEXT_13 = "(";
  protected final String TEXT_14 = " ";
  protected final String TEXT_15 = "){" + NL + "\t\tthis.";
  protected final String TEXT_16 = " = ";
  protected final String TEXT_17 = ";" + NL + "\t}" + NL + "\t" + NL + "\tpublic ";
  protected final String TEXT_18 = " get";
  protected final String TEXT_19 = "(){" + NL + "\t\treturn this.";
  protected final String TEXT_20 = ";" + NL + "\t}";
  protected final String TEXT_21 = NL + "}";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
ClassInfo info = (ClassInfo) argument;
String packageName = info.getPackageName();
String className = info.getClassName();
Map<String,String> arg = info.getProperties();
Set<String> importList = info.getImportSet();

    stringBuffer.append(TEXT_1);
    stringBuffer.append(packageName);
    stringBuffer.append(TEXT_2);
    if(importList != null){
    stringBuffer.append(TEXT_3);
    for(String tempImport : importList){
    stringBuffer.append(TEXT_4);
    stringBuffer.append(tempImport);
    stringBuffer.append(TEXT_5);
    }
    stringBuffer.append(TEXT_6);
    }
    stringBuffer.append(TEXT_7);
    stringBuffer.append(className);
    stringBuffer.append(TEXT_8);
    for(Entry<String, String> entry : arg.entrySet()){
    stringBuffer.append(TEXT_9);
    stringBuffer.append(entry.getValue());
    stringBuffer.append(TEXT_10);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(TEXT_11);
    }
	for(Entry<String, String> entry : arg.entrySet()){
    stringBuffer.append(TEXT_12);
    stringBuffer.append(entry.getKey().substring(0,1).toUpperCase() + entry.getKey().substring(1));
    stringBuffer.append(TEXT_13);
    stringBuffer.append(entry.getValue());
    stringBuffer.append(TEXT_14);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(TEXT_15);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(TEXT_16);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(TEXT_17);
    stringBuffer.append(entry.getValue());
    stringBuffer.append(TEXT_18);
    stringBuffer.append(entry.getKey().substring(0,1).toUpperCase() + entry.getKey().substring(1));
    stringBuffer.append(TEXT_19);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(TEXT_20);
    }
    stringBuffer.append(TEXT_21);
    return stringBuffer.toString();
  }
}
