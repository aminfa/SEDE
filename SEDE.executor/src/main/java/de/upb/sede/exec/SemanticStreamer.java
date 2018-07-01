package de.upb.sede.exec;

import de.upb.sede.core.SEDEObject;
import de.upb.sede.core.ServiceInstanceHandle;
import de.upb.sede.procedure.ParseConstantProcedure;
import de.upb.sede.util.Streams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.reflect.Method;

public final class SemanticStreamer {


	private final static Logger logger = LogManager.getLogger();

	public static SEDEObject readFrom(InputStream is, String type) {
		Object data;
		if(SEDEObject.isPrimitive(type)){
			/*
			 * parse the primitive data:
			 * data could be: Integer Double String Boolean or null
			 */
			String primitiveStr = Streams.InReadString(is);
			data = castStringToPrimitive(primitiveStr, type).getObject();

		} else if(SEDEObject.isServiceInstanceHandle(type)){
			/*
			 * Parse service instance handle from json string:
			 * data is a serviceinstancehandle.
			 */
			String jsonString = Streams.InReadString(is);
			ServiceInstanceHandle serviceInstanceHandle = new ServiceInstanceHandle();
			serviceInstanceHandle.fromJsonString(jsonString);
			data = serviceInstanceHandle;
		} else if(SEDEObject.isSemantic(type)){
			/*
			 * Read the content from the stream into a byte array:
			 */
			data = Streams.InReadByteArr(is);
		} else {
			throw new RuntimeException("BUG: use read object from method to read real type data.");
		}
		SEDEObject parsedObject = new SEDEObject(type, data);
		return parsedObject;
	}

	private static SEDEObject castStringToPrimitive(String data, String type){
		SEDEObject.PrimitiveType enumType = SEDEObject.PrimitiveType.insensitiveValueOf(type);
		return ParseConstantProcedure.parsePrimitive(data, enumType);
	}

	public static SEDEObject readObjectFrom(SEDEObject semanticObject, String caster, String sourceSemanticType, String targetRealTypeCp) {
		if(semanticObject.isSemantic()){
			ByteArrayInputStream inputStream = new ByteArrayInputStream((byte[]) semanticObject.getObject());
			return readObjectFrom(inputStream, caster, sourceSemanticType, targetRealTypeCp);
		} else{
			throw new RuntimeException("The given SEDEObject\n" + semanticObject.toString() + "\n is not semantic.");
		}
	}

	public static SEDEObject readObjectFrom(InputStream is, String caster, String sourceSemanticType, String targetRealTypeCp) {
		logger.debug("Casting from semantic type '{}' to '{}' using caster class: {}.", sourceSemanticType, targetRealTypeCp, caster);
		String targetRealType = getSimpleNameFromClasspath(targetRealTypeCp);
		String casterMethod = getCastMethod(sourceSemanticType, targetRealType, false);
		Method method = getMethodFor(caster, casterMethod);
		try {
			Object casterInstance = Class.forName(caster).getConstructor().newInstance();
			Object castedObject = method.invoke(casterInstance, is);;
			SEDEObject sedeObject = new SEDEObject(targetRealTypeCp, castedObject);
			return sedeObject;
		} catch (ReflectiveOperationException ex){
			throw new RuntimeException(ex);
		}
	}

	public static void streamInto(OutputStream os, SEDEObject content) {
		try {
			if(content.isPrimitive()){
				/*
				 * semnatic data can be written as string:
				 */
				String stringEncoded = castPrimitiveToString(content.getObject());
				byte [] encodedData = stringEncoded.getBytes();
				os.write(encodedData);
			} else if(content.isSemantic()){
				/**
				 * Semnatic objects hold byte arrray as object:
				 */
				byte [] encodedData = (byte[])content.getObject();
				os.write(encodedData);
			} else if(content.isServiceInstanceHandle()) {
				ServiceInstanceHandle instanceHandle = (ServiceInstanceHandle) content.getObject();
				OutputStreamWriter osWriter = new OutputStreamWriter(os);
				instanceHandle.toJson().writeJSONString(osWriter);
				osWriter.flush();
			} else if(content.isReal()) {
				throw new RuntimeException("BUG: Use streamObjectInto instead: " + content.toString());
			} else{
				throw new RuntimeException("BUG: Cannot handle sede object: " + content.toString());
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}


	public static void streamObjectInto(OutputStream os, SEDEObject content, String caster, String targetSemanticType) {
		logger.debug("Casting from '{}' to semantic type '{}' using caster class: {}.", content.getType(), targetSemanticType, caster);
		String sourceRealType = getSimpleNameFromClasspath(content.getType());
		String casterMethod = getCastMethod(sourceRealType, targetSemanticType, true);
		Method method = getMethodFor(caster, casterMethod);
		try {
			Object casterInstance = Class.forName(caster).getConstructor().newInstance();
			method.invoke(casterInstance, os, content.getObject());
		} catch (ReflectiveOperationException ex){
			throw new RuntimeException(ex);
		}
	}

	public static String getCastMethod(String sourceType, String targetType, boolean toSemantic) {
		String methodName;
		if(toSemantic) {
			methodName = "cts_";
		} else {
			methodName = "cfs_";
		}
		methodName += sourceType + "_" + targetType;
		return methodName;
	}

	private static Method getMethodFor(String caster, String methodName) {
		try {
			Class casterClass = Class.forName(caster);
			Method[] allDefinedMethods = casterClass.getMethods();
			for(Method method : allDefinedMethods){
				if(method.getName().equals(methodName)){
					return method;
				}
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		throw new RuntimeException("Caster class: " + caster + "  doesnt contain method: " + methodName);

	}

	public static String getSimpleNameFromClasspath(String classpath){
		try {
			return Class.forName(classpath).getSimpleName();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private static String castPrimitiveToString(Object primitiveObject){
		if(primitiveObject ==  null) {
			return "null";
		}
		else {
			return primitiveObject.toString();
		}
	}

	public static final <T> T objectDeserialize(InputStream inputStream) {
		ObjectInputStream objectIn = null;
		try {
			objectIn = new ObjectInputStream(inputStream);
			Object input = objectIn.readObject();
			return (T)input;
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public static final void objectSerialize(OutputStream os, Serializable serializable) {
		try {
			ObjectOutputStream objectOut = new ObjectOutputStream(os);
			objectOut.writeObject(serializable);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}