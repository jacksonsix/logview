package offline;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONML;
import org.json.JSONObject;



/*JSONObject comp =new JSONObject(company);
String lab = comp.getString("name");
comp.put("label", lab);
JSONObject json = new JSONObject();	
JSONArray array = new JSONArray();
JSONObject item = new JSONObject();

String reqbody = json.toString();	
String jsonResult = HttpsClient.getContentUsingPost(addr, reqbody,"application/json","UTF-8", 10000);
JsonObject jsonObject = new JsonParser().parse(jsonResult).getAsJsonObject();
String status = jsonObject.getAsJsonObject("result").get("status").getAsString();
String msg = jsonObject.getAsJsonObject("result").get("message").getAsString();*/


class helper{
	JSONObject network;
	OfflineLog newJava; 
}

public class HarData {
	
	public static void main(String[] args){
		HarData d = new HarData();
		d.readFromFile("\\httpoffline\\a.har");
		
	}
	
	private  void readFromFile(String filename){
		StringBuilder all = new StringBuilder();
		try {
			  File file = new File(filename);		 
			  BufferedReader br = new BufferedReader(new FileReader(file));		 
			  
			  String st;
			  while ((st = br.readLine()) != null){
			    all.append(st);
			  }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			JSONObject network =new JSONObject(all.toString());
			OfflineLog newJava = new OfflineLog();
			helper h = new helper();
			h.network = network;
			h.newJava = newJava;
			copyData(h);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public boolean fromFile(String file){

		return true;
	}
	
	private  void copyData(helper h){
		JSONObject jobj = h.network;
		OfflineLog aobj = h.newJava;
		try {
			//objs =  jobj.getJSONObject("log").getJSONArray("entries");
			jobj =  jobj.getJSONObject("log");
			
			copyEach(jobj,aobj);
/*			Iterator<?> keys = jobj.keys();
			while( keys.hasNext() ) {
			    String key = (String)keys.next();
			    Field dest = findElementByName(key,aobj);	
			    Object view = jobj.get(key);
			    if(view instanceof String || view instanceof Number){
					dest.setAccessible(true);
					dest.set(aobj, view.toString());
				}else{
				    dest.setAccessible(true);
				    Object o = dest.get(aobj);			  
				    copyEach(jobj.getJSONObject(key),o);
				}

			}*/
			
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		System.out.println("finished");
	}
	
	private void copyEach(JSONObject obj,Object ob){
		Iterator<?> keys = obj.keys();
		while( keys.hasNext() ) {
		    String key = (String)keys.next();
		    Field dest = findElementByName(key,ob);		   
		   // boolean s = dest.getType().isAssignableFrom(Response.class);
		   // Object o = createElementByName(dest.getType());
		   
		    try {		    
		    	Object view = obj.get(key);
				if(view instanceof String || view instanceof Number){
					dest.setAccessible(true);
					dest.set(ob, obj.get(key).toString());
				}else if(JSONObject.NULL.equals(view) ){
					dest.setAccessible(true);
					dest.set(ob, null);
				}else if(view instanceof Boolean){
					dest.setAccessible(true);
					dest.set(ob, obj.get(key).toString());
				}
				else if ( view instanceof JSONObject ) {					 
                     System.out.println("object copy");
                     dest.setAccessible(true);
                     Object o = dest.get(ob);
                     copyEach( obj.getJSONObject(key),o);
				}else if ( view instanceof JSONArray){
					JSONArray ja = obj.getJSONArray(key);
					 dest.setAccessible(true);
                     Object o = dest.get(ob);
                    final Object refArray = Array.newInstance(
                    		dest.getType().getComponentType(), ja.length());
                   
					for(int i=0;i<ja.length();i++){
						//Object o = Array.get(view, i);
						JSONObject item = ja.getJSONObject(i);						
						Object oo = createElementByName(dest.getType().getComponentType());
	                    Array.set(refArray, i, oo);   	                       
						copyEach(item,Array.get(refArray, i));
					}
					dest.setAccessible(true);
					dest.set(ob, refArray);
					dest.setAccessible(false);
				}
				else{
					throw new JSONException("error");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private Field findElementByName(String name,Object parent){
		
		try {
			Field f = parent.getClass().getDeclaredField(name);
			return f;
		} catch (NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private Object createElementByName(Class<?> class1){
		Class<?> clazz;
		try {
			clazz = Class.forName(class1.getName());
			Constructor<?> ctor = clazz.getConstructor();
			Object object = ctor.newInstance();
			return object;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		return null;

	}
}
