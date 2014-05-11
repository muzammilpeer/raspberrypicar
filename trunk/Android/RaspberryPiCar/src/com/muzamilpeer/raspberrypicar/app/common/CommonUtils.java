package com.muzamilpeer.raspberrypicar.app.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;

public class CommonUtils {
	public static Object[] reflectionFields(Object entity) {
		Field[] fields = entity.getClass().getFields();
		Map<String, Object> map = new HashMap<String, Object>();
		for (Field f : fields) {
			try {
				map.put(f.getName(), f.get(entity));
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return map.keySet().toArray();
	}

	public static Object[] reflectionKeys(Object entity) {
		Field[] fields = entity.getClass().getFields();
		Map<String, Object> map = new HashMap<String, Object>();
		for (Field f : fields) {
			try {
				map.put(f.getName(), f.get(entity));
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return map.keySet().toArray();
	}

	public static Object[] reflectionValues(Object entity) {
		Field[] fields = entity.getClass().getFields();
		Map<String, Object> map = new HashMap<String, Object>();
		for (Field f : fields) {
			try {
				map.put(f.getName(), f.get(entity));
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return map.values().toArray();
	}

	public static Object[] reflectionGetters(Object entity) {
		Method[] methods = entity.getClass().getMethods();
		Map<String, Object> map = new HashMap<String, Object>();
		for (Method m : methods) {
			if (m.getName().startsWith("get")) {
				Object value = null;
				if (!m.getName().equalsIgnoreCase("getClass")) {
					try {
						value = m.invoke(entity);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					map.put(m.getName(), value);
				}
			}
		}
		return map.values().toArray();
	}

	public static Object refectionSetValue(Object model, String newValue,
			String fieldName) {
		Class<?> c = model.getClass();
		Field field = null;
		try {
			field = c.getField(fieldName);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		field.setAccessible(true);
		try {
			field.set(model, newValue);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e ) {
			e.printStackTrace();
		}
		return model;
	}

	public static void setFinalStatic(Field field, Object newValue)
			throws Exception {
		field.setAccessible(true);

		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

		field.set(null, newValue);
	}
	
	public static byte[] serializeObject(Object o) { 
	    ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
	 
	    try { 
	      ObjectOutput out = new ObjectOutputStream(bos); 
	      out.writeObject(o); 
	      out.close(); 
	 
	      // Get the bytes of the serialized object 
	      byte[] buf = bos.toByteArray(); 
	 
	      return buf; 
	    } catch(IOException ioe) { 
	      Log.e("serializeObject", "error", ioe); 
	 
	      return null; 
	    } 
	  } 
	 public static Object deserializeObject(byte[] b) { 
		    try { 
		      ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(b)); 
		      Object object = in.readObject(); 
		      in.close(); 
		 
		      return object; 
		    } catch(ClassNotFoundException cnfe) { 
		      Log.e("deserializeObject", "class not found error", cnfe); 
		 
		      return null; 
		    } catch(IOException ioe) { 
		      Log.e("deserializeObject", "io error", ioe); 
		 
		      return null; 
		    } 
		  } 

}
