package com.bakiatmaca.research.jbm.agent;

import java.lang.instrument.Instrumentation;

public class JbmAgent {

	private static final String DEFAULT_PUSH_METRIC_ENDPOINT = "http://192.168.2.212:9171";
	
	public static void premain(String args, Instrumentation instrumentation) {
		System.out.println("JbmAgent is started with using argument '-javaagent'");
		
		transform(args, instrumentation);
	}
	
	public static void agentmain(String args, Instrumentation instrumentation) {
		System.out.println("JbmAgent is started with using Attach API");
		
		transform(args, instrumentation);
	}
	
	public static void transform(String args, Instrumentation instrumentation) {
		String params = args == null ? DEFAULT_PUSH_METRIC_ENDPOINT : args;
		System.out.println(String.format("Endpoint [%s]\n",  params));
		
		try {	        
			instrumentation.addTransformer(new ClassTransformer(params, "com.bakiatmaca.research.jbm.demoapp.workers.", "*"), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}