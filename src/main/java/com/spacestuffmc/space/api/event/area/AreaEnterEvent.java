/*
 * Copyright (c) 2019 SpaceStuffMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// Package Declaration
package com.spacestuffmc.space.api.event.area;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * Event data for when a player enters a breathable area.
 * 
 * @author iffa
 */
@SuppressWarnings("unused")
public class AreaEnterEvent extends AreaEvent {
    // Variables
    private static final HandlerList handlers = new HandlerList();
    private static final long serialVersionUID = 8533622463870713905L;

    /**
     * Constructor for AreaEnterEvent.
     * 
     * @param player Player
     */
    public AreaEnterEvent(Player player) {
        super(player);
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
