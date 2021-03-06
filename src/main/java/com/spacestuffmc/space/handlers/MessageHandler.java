/*
 * Copyright (c) 2019 SpaceStuffMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.spacestuffmc.space.handlers;

import com.spacestuffmc.space.api.SpaceMessageHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.logging.Level;

/**
 * Useful methods to send messages to players and console.
 * 
 * Internal use only
 * 
 * @author Jack
 */
@SuppressWarnings("unused")
public class MessageHandler extends SpaceMessageHandler {
    private static String printPrefix = "[Space]";
    
    /**
     * Prints a message to the console.
     * 
     * @param level Logging level (INFO, WARNING etc)
     * @param message Message to print
     */
    public static void print(Level level, String message) {
        log.log(level, printPrefix + " " + message);
    }

    /**
     * Prints a debug message to the console if debugging is enabled.
     * 
     * @param level Logging level (INFO, WARNING etc)
     * @param message Message to print
     */
    public static void debugPrint(Level level, String message) {
        
        if (ConfigHandler.getDebugging()) {
            log.log(level, printPrefix + " " + message);
        }
    }
    
    /**
     * Sends a "You don't have permission!"-message to a player.
     * 
     * @param player Player
     */
    public static void sendNoPermissionMessage(Player player) {
        player.sendMessage(ChatColor.RED + LangHandler.getNoPermissionMessage());
    }

    /**
     * sends a "You don't have enough money!"-message to a player.
     * 
     * @param player Player
     */
    public static void sendNotEnoughMoneyMessage(Player player) {
        player.sendMessage(ChatColor.RED + LangHandler.getNotEnoughMoneyMessage());
    }
     
    private MessageHandler() {
    }
    
    
}
