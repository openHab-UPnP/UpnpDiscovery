package org.openhab.binding.upnpcamera.handler;

import static org.openhab.binding.upnpcamera.upnpcameraBindingConstants.THING_TYPE_CAMERA;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Discovering implements UpnpDiscoveryParticipant {
    private Logger logger = LoggerFactory.getLogger(upnpcameraHandler.class);
    public static String UDN = "udn";// the
    public static String IP = "ip";// the adresse IP of the camera
    public static String NOM = "name";// Name of the camera

    @Override
    public Set<ThingTypeUID> getSupportedThingTypeUIDs() {
        // TODO Auto-generated method stub
        return Collections.singleton(THING_TYPE_CAMERA);
    }

    @Override // discover the camera
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
            // add the properties of the cameras to the properties of the thing
            properties.put(UDN, device.getIdentity().getUdn().getIdentifierString());
            properties.put(IP, device.getIdentity().getDescriptorURL().getHost());
            properties.put(NOM, label);

            DiscoveryResult result = DiscoveryResultBuilder.create(uid).withProperties(properties).withLabel(label)
                    .build();
            logger.debug("Created a DiscoveryResult for device '{}' with UDN '{}'",
                    device.getDetails().getFriendlyName(), device.getIdentity().getUdn().getIdentifierString());
            // returning the result
            return result;
        } else {
            return null;
        }
    }

    @Override
    public ThingUID getThingUID(RemoteDevice device) {
        if (device != null) {
            if (device.getDetails().getManufacturerDetails().getManufacturer() != null) {// detect all the D-Link
                                                                                         // cameras
                if (device.getDetails().getManufacturerDetails().getManufacturer().toUpperCase().contains("D-LINK")) {
                    logger.debug("Discovered a camera thing with UDN '{}'",
                            device.getIdentity().getUdn().getIdentifierString());

                    return new ThingUID(THING_TYPE_CAMERA, device.getIdentity().getUdn().getIdentifierString());
                }
            }
        }
        return null;
    }

}
