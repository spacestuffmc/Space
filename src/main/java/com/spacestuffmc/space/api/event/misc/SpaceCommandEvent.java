/*
 * Copyright (c) 2016 CrystalCraftMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// Package Declaration
package com.spacestuffmc.space.api.event.misc;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Event data for when a player uses the 'space'-command.
 * 
 * @author iffa
 */
public class SpaceCommandEvent extends Event implements Cancellable {
    // Variables
    private static final HandlerList handlers = new HandlerList();
    private static final long serialVersionUID = -7384621557571433605L;
    private boolean canceled;
    private CommandSender sender;
    private String arguments[];

    /**
     * Constructor for SpaceCommandEvent.
     * 
     * @param event Event
     * @param sender CommandSender
     * @param args Command arguments
     */
    public SpaceCommandEvent(String event, CommandSender sender, String args[]) {
        this.sender = sender;
        this.arguments = args;
    }

    /**
     * Gets the arguments of the command.
     * 
     * @return Given arguments
     */
    public String[] getArgs() {
        return this.arguments;
    }

    /**
     * Gets the command sender.
     * 
     * @return CommandSender
     */
    public CommandSender getSender() {
        return this.sender;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.canceled = cancel;
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
