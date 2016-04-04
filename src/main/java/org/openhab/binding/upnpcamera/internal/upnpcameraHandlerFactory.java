/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.upnpcamera.internal;

import static org.openhab.binding.upnpcamera.upnpcameraBindingConstants.THING_TYPE_CAMERA;

import java.util.Collections;
import java.util.Set;

import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.config.discovery.DiscoveryServiceRegistry;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.io.transport.upnp.UpnpIOService;
import org.openhab.binding.upnpcamera.handler.upnpcameraHandler;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link upnpcameraHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author yacine - Initial contribution
 */
public class upnpcameraHandlerFactory extends BaseThingHandlerFactory {
    private Logger logger = LoggerFactory.getLogger(upnpcameraHandlerFactory.class);
    private final static Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Collections.singleton(THING_TYPE_CAMERA);

    private UpnpIOService upnpIOService;
    private DiscoveryServiceRegistry discoveryServiceRegistry;

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {

        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected ThingHandler createHandler(Thing thing) {
        logger.debug("ThingHadle createHandler");
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (thingTypeUID.equals(THING_TYPE_CAMERA)) {
            return new upnpcameraHandler(thing, upnpIOService, discoveryServiceRegistry);
        }

        return null;
    }

    @Override
    protected void activate(ComponentContext componentContext) {
        super.activate(componentContext);
        logger.debug("activate upnpcameraHandler");
    };

    @Override
    public Thing createThing(ThingTypeUID thingTypeUID, Configuration configuration, ThingUID thingUID,
            ThingUID bridgeUID) {
        logger.debug("createThring cool");
        if (THING_TYPE_CAMERA.equals(thingTypeUID)) {
            logger.debug("thingUID " + thingTypeUID);
            logger.debug("thingUID " + thingUID);
            ThingUID sonosPlayerUID = getCameraUID(thingTypeUID, thingUID, configuration);
            logger.debug("Creating a Camera thing with ID '{}'", sonosPlayerUID);
            Thing result = super.createThing(thingTypeUID, configuration, sonosPlayerUID, null);
            if (result != null) {
                logger.debug("Thing not null");
            } else {
                logger.debug("Thing null");
            }
            return result;
        }
        throw new IllegalArgumentException(
                "The thing type " + thingTypeUID + " is not supported by the sonos binding.");
    }

    private ThingUID getCameraUID(ThingTypeUID thingTypeUID, ThingUID thingUID, Configuration configuration) {

        String udn = (String) configuration.get("udn");

        if (thingUID == null) {
            thingUID = new ThingUID(thingTypeUID, udn);
        }

        return thingUID;
    }

    protected void setUpnpIOService(UpnpIOService upnpIOService) {
        this.upnpIOService = upnpIOService;
    }

    protected void unsetUpnpIOService(UpnpIOService upnpIOService) {
        this.upnpIOService = null;
    }

    protected void setDiscoveryServiceRegistry(DiscoveryServiceRegistry discoveryServiceRegistry) {
        this.discoveryServiceRegistry = discoveryServiceRegistry;
    }

    protected void unsetDiscoveryServiceRegistry(DiscoveryServiceRegistry discoveryServiceRegistry) {
        this.discoveryServiceRegistry = null;
    }
}