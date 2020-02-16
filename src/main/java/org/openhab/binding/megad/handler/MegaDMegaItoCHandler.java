/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.megad.handler;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.thing.*;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
/**
 * The {@link MegaDMegaItoCHandler} is responsible for i2c fatures of megad
 *
 * @author Petr Shatsillo - Initial contribution
 */
@NonNullByDefault
public class MegaDMegaItoCHandler extends BaseThingHandler {

    private Logger logger = LoggerFactory.getLogger(MegaDMegaItoCHandler.class);

    private @Nullable ScheduledFuture<?> refreshPollingJob;

    @Nullable
    MegaDBridgeDeviceHandler bridgeDeviceHandler;
    boolean startup = true;
    protected long lastRefresh = 0;

    public MegaDMegaItoCHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
    }

    @SuppressWarnings("null")
    @Override
    public void initialize() {
        bridgeDeviceHandler = getBridgeHandler();
        // logger.debug("Thing Handler for {} started", getThing().getUID().getId());
        if (bridgeDeviceHandler != null) {
            registerMegadItoCListener(bridgeDeviceHandler);
        } else {
            logger.debug("Can't register {} at bridge. BridgeHandler is null.", this.getThing().getUID());
        }

        String[] rr = getThing().getConfiguration().get("refresh").toString().split("[.]");
        logger.debug("refresh: {}", rr[0]);

        if (refreshPollingJob == null || refreshPollingJob.isCancelled()) {
            refreshPollingJob = scheduler.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    int pollingPeriod = Integer.parseInt(rr[0]) * 1000;
                    refresh(pollingPeriod);
                }
            }, 0, 1000, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    private synchronized @Nullable MegaDBridgeDeviceHandler getBridgeHandler() {
        Bridge bridge = getBridge();
        if (bridge == null) {
            logger.error("Required bridge not defined for device.");
            // throw new NullPointerException("Required bridge not defined for device");
            return null;
        } else {
            return getBridgeHandler(bridge);
        }
    }

    private synchronized @Nullable MegaDBridgeDeviceHandler getBridgeHandler(Bridge bridge) {
        ThingHandler handler = bridge.getHandler();
        if (handler instanceof MegaDBridgeDeviceHandler) {
            return (MegaDBridgeDeviceHandler) handler;
        } else {
            logger.debug("No available bridge handler found yet. Bridge: {} .", bridge.getUID());
            return null;
        }
    }

    @SuppressWarnings("null")
    public void refresh(int interval) {
        long now = System.currentTimeMillis();
        if (startup) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                logger.warn("{}", e.getLocalizedMessage());
            }
            /*String[] portStatus = bridgeDeviceHandler
                    .getPortsvalues(getThing().getConfiguration().get("port").toString());
            if (portStatus[2].contains("ON")) {
                // updateValues(portStatus, OnOffType.ON);
            } else {
                // updateValues(portStatus, OnOffType.OFF);
            }*/
            startup = false;
        }
        if (interval != 0) {
            if (now >= (lastRefresh + interval)) {
                updateData();
                lastRefresh = now;
            }
        }
    }

    @SuppressWarnings("null")
    protected void updateData() {
        logger.debug("Updating i2c things...");

        /*String result = "http://" + getBridgeHandler().getThing().getConfiguration().get("hostname").toString() + "/"
                + getBridgeHandler().getThing().getConfiguration().get("password").toString() + "/?pt="
                + getThing().getConfiguration().get("port").toString() + "&cmd=get";*/
    }

    @Override
    public void updateStatus(ThingStatus status) {
        super.updateStatus(status);
    }

    @Override
    protected void updateStatus(ThingStatus status, ThingStatusDetail statusDetail, @Nullable String description) {
        super.updateStatus(status, statusDetail, description);
    }

    private void registerMegadItoCListener(@Nullable MegaDBridgeDeviceHandler bridgeHandler) {
        if (bridgeHandler != null) {
            bridgeHandler.registerMegadItoCListener(this);
        }
    }

}
