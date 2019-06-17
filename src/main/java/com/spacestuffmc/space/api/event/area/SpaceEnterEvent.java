/*
 * Copyright (c) 2019 SpaceStuffMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// Package Declaration
package com.spacestuffmc.space.api.event.area;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * Event data for when a player enters space. 
 * If player is joining server, location from will be null.
 * 
 * @author iffa
 */
@SuppressWarnings({"unused", "NullableProblems"})
public class SpaceEnterEvent extends SpaceWorldAreaEvent{
    // Variables
    private static final HandlerList handlers = new HandlerList();
    private static final long serialVersionUID = 8744071438699676557L;
    private Location to;
    private Location from;

    /**
     * Constructor for SpaceEnterEvent
     * 
     * 
     * @param player Player
     * @param from Where the player teleports from null if from respawn or server join
     * @param to Where the player teleports to
     */
    public SpaceEnterEvent(Player player, Location from, Location to) {
        super("SpaceEnterEvent", player);
        this.from = from;
        this.to = to;
    }

    /**
     * Gets the destination of the teleport.
     * 
     * @return Destination of the teleport
     */
    public Location getTo() {
        return this.to;
    }

    /**
     * Gets where the player is trying to teleport from.
     * 
     * @return Where the player is trying to teleport from
     */
    public Location getFrom() {
        return this.from;
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
