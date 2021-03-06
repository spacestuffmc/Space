/*
 * Copyright (c) 2019 SpaceStuffMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// Package Declaration
package com.spacestuffmc.space.runnables;

import com.spacestuffmc.space.api.event.misc.SpaceSuffocationEvent;
import com.spacestuffmc.space.handlers.MessageHandler;
import com.spacestuffmc.space.handlers.PlayerHandler;
import com.spacestuffmc.space.listeners.SpaceSuffocationListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.logging.Level;

/**
 * A runnable class for suffocating.
 * 
 * @author iffa
 */
public class SuffacationRunnable implements Runnable {
    // Variables
    private final Player player;
    private boolean suffocating = false;

    /**
     * Constructor for SuffacationRunnable.
     * 
     * @param player Player
     */
    public SuffacationRunnable(Player player) {
        this.player = player;
    }

    /**
     * Suffocates the player when repeated every second.
     */
    @Override
    public void run() {
        if (!player.isDead()) {
            if(PlayerHandler.checkNeedsSuffocation(player)){
                if(!suffocating){
                    /* Notify listeners start */
                    SpaceSuffocationEvent e = new SpaceSuffocationEvent(player);
                    Bukkit.getServer().getPluginManager().callEvent(e);
                    if (e.isCancelled()) {
                        return;
                    }
                    /* Notify listeners end */
                    suffocating=true;
                    player.sendMessage("You left an area and are now suffocating.");
                    MessageHandler.debugPrint(Level.INFO, "Player '" + player.getName() + "' is now suffocating in space.");
                }
            } else {
                if(suffocating) {
                    suffocating = false;
                }
            }
            
            if(suffocating){
                if (player.getHealth() < 2 && player.getHealth() > 0) {
                    player.setHealth(0);
                    SpaceSuffocationListener.stopSuffocating(player);
                    return;
                } else if (player.getHealth() <= 0) {
                    SpaceSuffocationListener.stopSuffocating(player);
                    return;
                }
                player.setHealth(player.getHealth() - 2);
            }
        } else {
            SpaceSuffocationListener.stopSuffocating(player);
        }
    }
}
