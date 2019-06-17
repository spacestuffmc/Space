/*
 * Copyright (c) 2016 CrystalCraftMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// Package Declaration
package com.crystalcraftmc.crystalspace.api.event.area;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * Event data for when a player leaves a breathable area.
 * 
 * @author iffa
 */
public class AreaLeaveEvent extends AreaEvent {
    // Variables
    private static final HandlerList handlers = new HandlerList();
    private static final long serialVersionUID = 7604929590186681633L;

    /**
     * Constructor for AreaLeaveEvent.
     * 
     * @param player Player
     */
    public AreaLeaveEvent(Player player) {
        super("AreaLeaveEvent", player);
    }
        
    /**
     * {@inheritDoc}
     * @return Handler list
     */
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
