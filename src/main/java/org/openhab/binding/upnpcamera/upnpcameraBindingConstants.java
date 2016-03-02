/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.upnpcamera;

import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link UpnpCameraBinding} class defines common constants, which are
 * used across the whole binding.
 *
 * @author yacine - Initial contribution
 */
public class upnpcameraBindingConstants {

    public static final String BINDING_ID = "upnpcamera";

    // List of all Thing Type UIDs
    public final static ThingTypeUID THING_TYPE_CAMERA = new ThingTypeUID(BINDING_ID, "camera");

    // List of all Channel ids

    public final static String PAN = "pan";
    public final static String TILT = "tilt";
    public final static String ZOOM = "zoom";
    public final static String VIDEO_URL = "video_url";

}