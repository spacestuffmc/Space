/*
 * Copyright (c) 2016 CrystalCraftMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

/*
 */
package com.spacestuffmc.space.api.event.area;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 *
 * @author Jack
 */
public class SpaceWorldAreaEvent extends AreaEvent implements Cancellable{
    private static final HandlerList handlers = new HandlerList();
    private static final long serialVersionUID = -316124428220245924L;
    private boolean cancelled;
    
    /**
     * Constructor for AreaEvent.
     * 
     * @param event Event
     * @param player Player
     */
    public SpaceWorldAreaEvent(String event, Player player) {
        super(event,player);
    }
    
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
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
