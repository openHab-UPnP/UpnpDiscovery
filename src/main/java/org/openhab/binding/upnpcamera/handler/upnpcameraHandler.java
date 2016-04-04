/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.upnpcamera.handler;

import java.util.concurrent.ScheduledFuture;

import org.eclipse.smarthome.config.discovery.DiscoveryServiceRegistry;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.io.transport.upnp.UpnpIOService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link upnpcameraHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author yacine - Initial contribution
 */
public class upnpcameraHandler extends BaseThingHandler {
    public static String UDN = "udn";
    public static String IP = "ip";
    public static String NOM = "nom";

    private Logger logger = LoggerFactory.getLogger(upnpcameraHandler.class);

    /** Job that runs {@link #updateRunnable}. */
    private ScheduledFuture<?> updateJob;

    public upnpcameraHandler(Thing thing, UpnpIOService upnpIOService,
            DiscoveryServiceRegistry discoveryServiceRegistry) {
        super(thing);

        logger.debug("Creating a ZonePlayerHandler for thing '{}'", getThing().getUID());
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.trace("ChannelUID : " + channelUID);
        logger.trace("command : " + command);
    }

    @Override
    public void initialize() {

        logger.debug("DiscoveryServiceRegistry initialized");
        super.initialize();
        logger.debug("initialized ended");
    }
}
