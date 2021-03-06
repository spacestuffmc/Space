/*
 * Copyright (c) 2019 SpaceStuffMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// Package Declaration
package com.spacestuffmc.space.commands;

import com.spacestuffmc.space.SpaceMain;
import org.bukkit.command.CommandSender;

/**
 * Represents "/space args".
 * 
 * @author iffa
 */
public abstract class SpaceCommand {
    // Variables
    private SpaceMain plugin;
    private CommandSender sender;
    private String[] args;

    /**
     * Constructor of SpaceCommand.
     * 
     * @param plugin Space instance
     * @param sender Command sender
     * @param args Command arguments
     */
    SpaceCommand(SpaceMain plugin, CommandSender sender, String[] args) {
        this.plugin = plugin;
        this.sender = sender;
        this.args = args;
        command(); // It's bad but fuck that
    }

    /**
     * Does the command.
     */
    public abstract void command();

    /**
     * Gets the plugin.
     * 
     * @return the plugin
     */
    public SpaceMain getPlugin() {
        return plugin;
    }

    /**
     * Gets the sender.
     * 
     * @return the sender
     */
    CommandSender getSender() {
        return sender;
    }

    /**
     * Gets the arguments.
     * 
     * @return the args
     */
    String[] getArgs() {
        return args;
    }
}
