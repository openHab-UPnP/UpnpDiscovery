package org.openhab.binding.upnpcamera.handler;

import static org.openhab.binding.upnpcamera.upnpcameraBindingConstants.THING_TYPE_CAMERA;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.config.discovery.UpnpDiscoveryParticipant;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.jupnp.model.meta.RemoteDevice;
import org.openhab.binding.upnpcamera.dataBase.CameraGetPropertyValues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Multimap;

public class Discovering implements UpnpDiscoveryParticipant {
    private Logger logger = LoggerFactory.getLogger(upnpcameraHandler.class);
    public static String UDN = "udn";
    public static String IP = "ip";
    public static String NOM = "nom";

    @Override
    public Set<ThingTypeUID> getSupportedThingTypeUIDs() {
        // TODO Auto-generated method stub
        return Collections.singleton(THING_TYPE_CAMERA);
    }

    @Override
    public DiscoveryResult createResult(RemoteDevice device) {
        ThingUID uid = getThingUID(device);
        if (uid != null) {
            Map<String, Object> properties = new HashMap<>(3);
            String label = "camera device";
            try {
                label = device.getDetails().getModelDetails().getModelName();
            } catch (Exception e) {
                // ignore and use default label
            }
            properties.put(UDN, device.getIdentity().getUdn().getIdentifierString());
            properties.put(IP, device.getIdentity().getDescriptorURL().getHost());
            properties.put(NOM, label);
            DiscoveryResult result = DiscoveryResultBuilder.create(uid).withProperties(properties).withLabel(label)
                    .build();
            // ArrayList<String> temp = (ArrayList<String>) getvideoUrl(label);
            String temp = getvideoUrl(label).toString();
            String imageUrl = getImageUrl(label).toString();
            if (temp != null && imageUrl != null) {
                temp = temp.substring(temp.indexOf("[") + 1, temp.lastIndexOf("]"));
                temp = temp.replaceAll("IPADDRESS", device.getIdentity().getDescriptorURL().getHost());

                imageUrl = imageUrl.substring(imageUrl.indexOf("[") + 1, imageUrl.lastIndexOf("]"));
                imageUrl = imageUrl.replaceAll("IPADDRESS", device.getIdentity().getDescriptorURL().getHost());
                logger.debug(temp + " " + imageUrl);
                try {
                    addOnServer(label, temp, imageUrl, "activated");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                logger.debug("Camera non connue");
            }
            logger.debug("Created a DiscoveryResult for device '{}' with UDN '{}'",
                    device.getDetails().getFriendlyName(), device.getIdentity().getUdn().getIdentifierString());
            return result;
        } else {
            return null;
        }
    }

    @Override
    public ThingUID getThingUID(RemoteDevice device) {
        if (device != null) {
            if (device.getDetails().getManufacturerDetails().getManufacturer() != null) {
                if (device.getDetails().getManufacturerDetails().getManufacturer().toUpperCase().contains("D-LINK")) {
                    logger.debug("Discovered a camera thing with UDN '{}'",
                            device.getIdentity().getUdn().getIdentifierString());

                    return new ThingUID(THING_TYPE_CAMERA, device.getIdentity().getUdn().getIdentifierString());
                }
            }
        }
        return null;
    }

    // HTTP GET request
    private void addOnServer(String name, String ip, String imageUrl, String status) throws Exception {

        String url = "http://localhost/enregistrer.php?ip=" + ip + "&nom=" + name + "&status=" + status + "&image="
                + imageUrl;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // print result
        System.out.println(response.toString());

    }

    private void readProperties() {
        CameraGetPropertyValues properties = new CameraGetPropertyValues();
        Multimap<String, String> myMultimap;
        try {
            myMultimap = properties.getPropValues();

            for (String key : myMultimap.keys()) {
                System.out.println(key);
                System.out.println(myMultimap.get(key));
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private Collection<String> getvideoUrl(String model) {
        CameraGetPropertyValues properties = new CameraGetPropertyValues();
        Multimap<String, String> myMultimap;
        try {
            myMultimap = properties.getPropValues();
            for (String key : myMultimap.keys()) {
                if (myMultimap.get(key).contains(model)) {
                    String camera = key.substring(0, key.lastIndexOf("_") + 1);
                    String camera_video = camera + "video";
                    return myMultimap.get(camera_video);

                }
                System.out.println(key);
                System.out.println(myMultimap.get(key));
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private Collection<String> getImageUrl(String model) {
        CameraGetPropertyValues properties = new CameraGetPropertyValues();
        Multimap<String, String> myMultimap;
        try {
            myMultimap = properties.getPropValues();
            for (String key : myMultimap.keys()) {
                if (myMultimap.get(key).contains(model)) {
                    String camera = key.substring(0, key.lastIndexOf("_") + 1);
                    String camera_image = camera + "image";
                    return myMultimap.get(camera_image);

                }
                System.out.println(key);
                System.out.println(myMultimap.get(key));
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
