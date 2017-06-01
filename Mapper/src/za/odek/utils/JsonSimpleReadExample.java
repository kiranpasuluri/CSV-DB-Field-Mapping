package za.odek.utils;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class JsonSimpleReadExample {

    public static void main(String[] args) {

        JSONParser parser = new JSONParser();

        try {

        	JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("C:\\kiran\\map1494357604205.json"));

            
            
            
            //System.out.println(jsonObject.toJSONString());
            Set<Entry<String, String>> vEntrySet = jsonObject.entrySet();
            HashMap<String, String> map = new HashMap<String, String>();
            for(Entry<String, String> entry : vEntrySet)
            {
                System.out.println(entry.getKey()+","+ entry.getValue());
                map.put(entry.getKey(), entry.getValue());
            }
            /*for (Iterator vIterator = vEntrySet.iterator(); vIterator.hasNext();) {
				Object vObject = (Object) vIterator.next();
				System.out.println("vObject::"+vObject);
			}*/
            /*jsonObject.
            Map<String, Object> vMap = jsonObject.toMap();*/
            /*for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            	System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
            }*/
            /*System.out.println(jsonObject);

            String name = (String) jsonObject.get("name");
            System.out.println(name);

            long age = (Long) jsonObject.get("age");
            System.out.println(age);

            // loop array
            JSONArray msg = (JSONArray) jsonObject.get("messages");
            Iterator<String> iterator = msg.iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }
*/
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
