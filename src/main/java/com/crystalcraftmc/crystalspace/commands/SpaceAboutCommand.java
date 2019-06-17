/*
 * Copyright (c) 2016 CrystalCraftMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// Package Declaration
package com.crystalcraftmc.crystalspace.commands;

import com.crystalcraftmc.crystalspace.Space;
import com.crystalcraftmc.crystalspace.handlers.WorldHandler;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Represents "/space about".
 * 
 * @author iffa
 * @author jflory7
 */
public class SpaceAboutCommand extends SpaceCommand {
    /**
     * Constructor of SpaceAboutCommand.
     * 
     * @param plugin CrystalSpace instance
     * @param sender the command sender
     * @param args command arguments
     */
    public SpaceAboutCommand(Space plugin, CommandSender sender, String[] args) {
        super(plugin, sender, args);
    }

    /**
     * Executes <code>/space about</code> command.
     */
    @Override
    public void command() {
        if (getArgs().length < 2) {
            getSender().sendMessage(ChatColor.GOLD + "About:");
            getSender().sendMessage(ChatColor.GOLD + "-" + ChatColor.GRAY + " You're running version " +
                    ChatColor.GOLD + getPlugin().getDescription().getVersion());
            getSender().sendMessage(ChatColor.GOLD + "-" + ChatColor.GRAY + " There are currently " +
                    ChatColor.GOLD + WorldHandler.getSpaceWorlds().size() + ChatColor.GRAY + " space worlds loaded.");
        } else if (getArgs().length < 3 && getArgs()[1].equals("developers")) {
            getSender().sendMessage(ChatColor.GOLD + "-" + ChatColor.GRAY + " Core Developers:");
            getSender().sendMessage(ChatColor.GOLD + "    jflory7, iffa");
        }
    }
}
