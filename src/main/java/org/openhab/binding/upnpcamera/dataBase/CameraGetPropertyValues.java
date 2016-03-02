package org.openhab.binding.upnpcamera.dataBase;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class CameraGetPropertyValues {

    String result = "";
    InputStream inputStream;

    public Multimap<String, String> getPropValues() throws IOException {

        Multimap<String, String> myMultimap = ArrayListMultimap.create();

        try {
            Properties prop = new Properties();

            String propFileName = "camera.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            // get the property values

            Set<Object> keys = prop.keySet();

            Iterator<Object> i = keys.iterator();

            String camera_name;
            String str;
            String temp = "camera_";
            String[] strArray;

            while (i.hasNext()) {

                str = (String) i.next();
                // System.out.println(str);

                strArray = str.split("/");
                // System.out.println(strArray.length);
                camera_name = temp.concat(strArray[1]);

                if (strArray[2].equals("model")) {
                    myMultimap.put(camera_name.concat("_model"), prop.getProperty(str));
                } else if (strArray[2].equals("video_url")) {
                    myMultimap.put(camera_name.concat("_video"), prop.getProperty(str));
                } else if (strArray[2].equals("pan_url")) {
                    myMultimap.put(camera_name.concat("_pan"), prop.getProperty(str));
                } else if (strArray[2].equals("image_url")) {
                    myMultimap.put(camera_name.concat("_image"), prop.getProperty(str));
                } else if (strArray[2].equals("tilt_url")) {
                    myMultimap.put(camera_name.concat("_tilt"), prop.getProperty(str));
                } else if (strArray[2].equals("zoom_url")) {
                    myMultimap.put(camera_name.concat("_zoom"), prop.getProperty(str));
                }
            }

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
        return myMultimap;
    }

}
