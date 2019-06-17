/*
 * Copyright (c) 2016 CrystalCraftMC
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
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Represents "/space back".
 * 
 * @author iffa
 * @author jflory7
 */
public class SpaceExitCommand extends SpaceCommand {
    // Variables
    public static Map<Player, Location> enterDest = new HashMap<Player, Location>();

    /**
     * Constructor of SpaceExitCommand.
     * 
     * @param plugin Space instance
     * @param sender Command sender
     * @param args Command arguments
     */
    public SpaceExitCommand(SpaceMain plugin, CommandSender sender, String[] args) {
        super(plugin, sender, args);
    }

    /**
     * Does the command.
     */
    @Override
    public void command() {
        Player player = (Player) getSender();
        if (WorldHandler.isInAnySpace(player)) {
            if (PlayerHandler.hasPermission("Space.teleport.exit", player)) {
                enterDest.put(player, player.getLocation());
                Location location;
                if (SpaceEnterCommand.exitDest.containsKey(player)) {
                    location = SpaceEnterCommand.exitDest.get(player);
                    MessageHandler.debugPrint(Level.INFO, "Teleported player '" + player.getName() + "' out of space.");
                    player.teleport(location);
                    return;
                } else {
                    SpaceEnterCommand.exitDest.put(player, Bukkit.getServer().getWorlds().get(0).getSpawnLocation());
                    getSender().sendMessage(ChatColor.RED + LangHandler.getNoExitFoundMessage(1));
                    getSender().sendMessage(ChatColor.RED + LangHandler.getNoExitFoundMessage(2));
                    return;
                }
            } else {
                MessageHandler.sendNoPermissionMessage(player);
                return;
            }
        } else {
            player.sendMessage(ChatColor.RED + LangHandler.getNotInSpaceMessage());
            return;
        }
    }
}