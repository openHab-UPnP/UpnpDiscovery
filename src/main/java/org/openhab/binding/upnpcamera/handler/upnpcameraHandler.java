/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.upnpcamera.handler;

import java.util.Collection;
import java.util.concurrent.ScheduledFuture;

import org.eclipse.smarthome.config.discovery.DiscoveryListener;
import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryServiceRegistry;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.io.transport.upnp.UpnpIOParticipant;
import org.eclipse.smarthome.io.transport.upnp.UpnpIOService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * The {@link upnpcameraHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author yacine - Initial contribution
 */
public class upnpcameraHandler extends BaseThingHandler implements UpnpIOParticipant, DiscoveryListener {
    public static String UDN = "udn";
    public static String IP = "ip";
    public static String NOM = "nom";
    protected final static int SUBSCRIPTION_DURATION = 600;
    private final static Collection<String> SERVICE_SUBSCRIPTIONS = Lists.newArrayList("DeviceProperties",
            "AVTransport", "ZoneGroupTopology", "GroupManagement", "RenderingControl", "AudioIn");
    private ScheduledFuture<?> pollingJob;

    private UpnpIOService service;
    private DiscoveryServiceRegistry discoveryServiceRegistry;
    private Logger logger = LoggerFactory.getLogger(upnpcameraHandler.class);

    public upnpcameraHandler(Thing thing, UpnpIOService upnpIOService,
            DiscoveryServiceRegistry discoveryServiceRegistry) {
        super(thing);

        logger.debug("Creating a ZonePlayerHandler for thing '{}'", getThing().getUID());
        if (upnpIOService != null) {
            this.service = upnpIOService;
        }
        if (discoveryServiceRegistry != null) {
            this.discoveryServiceRegistry = discoveryServiceRegistry;
        }
    }

    /*
     * @Override
     * public void dispose() {
     * logger.debug("Handler disposed.");
     *
     * this.discoveryServiceRegistry.removeDiscoveryListener(this);
     * removeSubscription();
     * }
     */
    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.trace("ChannelUID : " + channelUID);
        logger.trace("command : " + command);
    }

    @Override
    public void initialize() {

        // this.discoveryServiceRegistry.addDiscoveryListener(this);
        logger.debug("DiscoveryServiceRegistry initialized");
        onSubscription();
        super.initialize();
        logger.debug("initialized ended");
    }

    @Override
    public void thingDiscovered(DiscoveryService source, DiscoveryResult result) {
        // TODO Auto-generated method stub
        logger.debug("result 1" + source);
        // logger.debug("result 2" + getThing().getConfiguration().get(UDN) + " " + result.getProperties().get(UDN));
        if (result.getThingUID().equals(this.getThing().getUID())) {
            if (getThing().getConfiguration().get(UDN).equals(result.getProperties().get(UDN))) {
                logger.debug("Discovered UDN '{}' for thing '{}'", result.getProperties().get(UDN),
                        getThing().getUID());

                updateStatus(ThingStatus.ONLINE);
                onSubscription();
            }
        }
    }

    private synchronized void onSubscription() {
        // Set up GENA Subscriptions
        logger.debug("onSubscription started");
        if (service == null) {
            logger.debug("service null");
        } else {
            logger.debug("service not null");
        }
        if (service.isRegistered(this)) {
            for (String subscription : SERVICE_SUBSCRIPTIONS) {
                logger.debug(subscription);
                service.addSubscription(this, subscription, SUBSCRIPTION_DURATION);
            }
        }
        logger.debug("onSubscription ended");
    }

    private synchronized void removeSubscription() {
        // Set up GENA Subscriptions
        if (service.isRegistered(this)) {
            for (String subscription : SERVICE_SUBSCRIPTIONS) {
                service.removeSubscription(this, subscription);
            }
            service.unregisterParticipant(this);
        }
    }

    @Override
    public void thingRemoved(DiscoveryService source, ThingUID thingUID) {
        // TODO Auto-generated method stub
        if (thingUID.equals(this.getThing().getUID())) {
            logger.debug("Setting status for thing '{}' to OFFLINE", getThing().getUID());
            logger.debug("desactivation de la cam");
            updateStatus(ThingStatus.OFFLINE);

        }
    }

    @Override
    public Collection<ThingUID> removeOlderResults(DiscoveryService source, long timestamp,
            Collection<ThingTypeUID> thingTypeUIDs) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getUDN() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onValueReceived(String variable, String value, String service) {
        // TODO Auto-generated method stub
        logger.trace("variable : " + variable);
        logger.trace("value : " + value);
        logger.trace("service : " + service);
    }

    @Override
    public void onStatusChanged(boolean status) {
        // TODO Auto-generated method stub

    }

}