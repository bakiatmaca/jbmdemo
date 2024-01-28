package com.bakiatmaca.research.jbm.agent;

import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class ExpressionEditorPushLog extends ExprEditor {

	private static final String LOG_METHOD_NAME = "java.util.logging.Logger.log";
	private static final String PRINTLN_METHOD_NAME = "java.io.PrintStream.println";
	
	private String params;
	
	public ExpressionEditorPushLog(String params) {
		super();
		this.params = params;
	}

	private static final String baseMethodBody = """
			{
				try {
					java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
					java.time.LocalDateTime now = java.time.LocalDateTime.now();  
					
					String data = {DATA_NOTATION};
					
					java.net.URL url = new java.net.URL("{PUST_ENDPOINT}/" + java.net.URLEncoder.encode(data, "UTF-8"));
					java.net.HttpURLConnection con = (java.net.HttpURLConnection) url.openConnection();
					con.setConnectTimeout(800);
					con.setRequestMethod("GET");
					con.getResponseCode();
					
					//System.out.println("getResponseCode:" + con.getResponseCode());
					
					System.out.println("PUSH Metric-> " + data);
				} catch (Exception e) {
					//e.printStackTrace();
				}
			}
			""";
	
	@Override
	public void edit(MethodCall m) throws CannotCompileException {
		String longName = m.getClassName() + "." + m.getMethodName();
		
		if (longName.equals(LOG_METHOD_NAME)) {			
			System.out.println(String.format("[JBM] Suppressing \"%s\" method for %s called from %s", m.getMethodName(), longName, m.getEnclosingClass().getName()));

			//String data = "legacy;" + dtf.format(now) + ";" + $1 + ";" + $2;
			m.replace(baseMethodBody.replace("{PUST_ENDPOINT}", params)
					.replace("{DATA_NOTATION}", "\"legacy;logapp;ulog;\" + dtf.format(now) + \";\" + $1 + \";\" + $2"));
			
		} else if (longName.equals(PRINTLN_METHOD_NAME)) {
			System.out.println(String.format("[JBM] Suppressing \"%s\" method for %s called from %s", m.getMethodName(), longName, m.getEnclosingClass().getName()));
			
			//String data = "legacy;" + dtf.format(now) + ";" + $1;
			m.replace(baseMethodBody.replace("{PUST_ENDPOINT}", params)
					.replace("{DATA_NOTATION}", "\"legacy;logapp;sout;\" + dtf.format(now) + \";\" + $1"));
		}
	}
}
