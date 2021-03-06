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
import com.spacestuffmc.space.handlers.LangHandler;
import com.spacestuffmc.space.handlers.MessageHandler;
import com.spacestuffmc.space.handlers.PlayerHandler;
import com.spacestuffmc.space.handlers.WorldHandler;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Represents "/space list".
 * 
 * @author iffa
 */
public class SpaceListCommand extends SpaceCommand {
    /**
     * Constructor of SpaceListCommand.
     * 
     * @param plugin Space instance
     * @param sender Command sender
     * @param args Command arguments
     */
    SpaceListCommand(SpaceMain plugin, CommandSender sender, String[] args) {
        super(plugin, sender, args);
    }

    /**
     * Does the command.
     */
    @Override
    public void command() {
        if (!PlayerHandler.hasPermission("Space.teleport.list", (Player) this.getSender())) {
            MessageHandler.sendNoPermissionMessage((Player) getSender());
            return;
        }
        if (WorldHandler.getSpaceWorlds().isEmpty()) {
            getSender().sendMessage(ChatColor.RED + LangHandler.getNoSpaceLoaded());
            return;
        }
        getSender().sendMessage(ChatColor.GOLD + SpaceMain.getPrefix() + " " + LangHandler.getListOfSpaceMessage());
        List<String> spaceWorlds = new ArrayList<>();
        for (World world : WorldHandler.getSpaceWorlds()) {
            if (world == null) {
                MessageHandler.debugPrint(Level.SEVERE, "world is null in SpaceListCommand! :(");
                continue;
            }
            spaceWorlds.add(world.getName());
        }
        getSender().sendMessage(ChatColor.GRAY + spaceWorlds.toString().replace("]", "").replace("[", ""));
    }
}
