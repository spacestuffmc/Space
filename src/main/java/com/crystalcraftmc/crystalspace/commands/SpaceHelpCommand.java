/*
 * Copyright (c) 2016 CrystalCraftMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// Package Declaration
package com.crystalcraftmc.crystalspace.commands;

import com.crystalcraftmc.crystalspace.SpaceMain;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Represents "/space help".
 * 
 * @author iffa
 */
public class SpaceHelpCommand extends SpaceCommand {
    /**
     * Constructor of SpaceHelpCommand.
     * 
     * @param plugin CrystalSpace instance
     * @param sender Command sender
     * @param args Command arguments
     */
    public SpaceHelpCommand(SpaceMain plugin, CommandSender sender, String[] args) {
        super(plugin, sender, args);
    }

    /**
     * Does the command.
     */
    @Override
    public void command() {
        getSender().sendMessage(ChatColor.GOLD + "[CrystalSpace] Usage:");
        getSender().sendMessage(ChatColor.GRAY + " /space enter [world] - Go to space (default world or given one)");
        getSender().sendMessage(ChatColor.GRAY + " /space back - Leave space or go back where you were in space");
        getSender().sendMessage(ChatColor.GRAY + " /space list - Brings up a list of all space worlds");
        getSender().sendMessage(ChatColor.GRAY + " /space help - Brings up this help message");
        getSender().sendMessage(ChatColor.GRAY + " /space about [credits] - About CrystalSpace");
        getSender().sendMessage(ChatColor.GRAY + "If you have questions, please visit " + ChatColor.GOLD + "bit.ly/banspace" + ChatColor.GRAY + "!");
        //getSender().sendMessage(ChatColor.GRAY + "...or if you prefer IRC, #iffa or #bananacode (Espernet)");
    }
}
