package com.bakiatmaca.research.jbm.agent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.expr.ExprEditor;

public class ClassTransformer implements ClassFileTransformer {
	
	private String targetClassName;
	private String targetMethodName;
	private String params;
	
	public ClassTransformer(String params, String targetClassName, String targetMethodName) {
		this.params = params;
		this.targetClassName = targetClassName;
		this.targetMethodName = targetMethodName;
	}
	
	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		byte[] bytecode = classfileBuffer;
		
		try {
			
			ClassPool cPool = ClassPool.getDefault();
			////cPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
			
			CtClass ctClass = cPool.makeClass(new ByteArrayInputStream(bytecode));
			CtMethod[] ctClassMethods = ctClass.getDeclaredMethods();
			
			for (CtMethod ctClassMethod : ctClassMethods) {
				if (ctClassMethod.getDeclaringClass().getName().startsWith(targetClassName)
						&& (targetMethodName.equals("*") || ctClassMethod.getName().equals(targetMethodName))) {

					//ctClassMethod.insertBefore("System.out.println(\"[JBM] Entering method\");");
					//ctClassMethod.insertAfter("System.out.println(\"[JBM] Exiting method\");");

					//Suppressing method
					ExprEditor expressionEditor = new ExpressionEditorPushLog(params);
					
					ctClassMethod.instrument(expressionEditor);
					
					bytecode = ctClass.toBytecode();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalClassFormatException(e.getMessage());
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw new IllegalClassFormatException(e.getMessage());
		} catch (CannotCompileException e) {
			e.printStackTrace();
			throw new IllegalClassFormatException(e.getMessage());
		}
		
		return bytecode;
	}
}