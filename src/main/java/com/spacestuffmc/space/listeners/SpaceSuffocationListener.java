/*
 * Copyright (c) 2019 SpaceStuffMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// Package Declaration
package com.spacestuffmc.space.listeners;

import com.spacestuffmc.space.SpaceMain;
import com.spacestuffmc.space.api.event.area.AreaEnterEvent;
import com.spacestuffmc.space.api.event.area.AreaLeaveEvent;
import com.spacestuffmc.space.api.event.area.SpaceEnterEvent;
import com.spacestuffmc.space.api.event.area.SpaceLeaveEvent;
import com.spacestuffmc.space.handlers.ConfigHandler;
import com.spacestuffmc.space.handlers.MessageHandler;
import com.spacestuffmc.space.handlers.PlayerHandler;
import com.spacestuffmc.space.runnables.SuffacationRunnable;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 *
 * @author Jack
 */
public class SpaceSuffocationListener implements Listener {
    // Variables
    public static Map<Player, Integer> taskid = new HashMap<Player, Integer>();
    private static SpaceMain plugin;

    /**
     * Constructor of SpaceSuffocationListener.
     * 
     * @param plugin SpaceMain instance
     */
    public SpaceSuffocationListener(SpaceMain plugin) {
        SpaceSuffocationListener.plugin = plugin;
    }

    /**
     * Called when someone enters an area.
     * 
     * @param event Event data 
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onAreaEnter(AreaEnterEvent event) {
        stopSuffocating(event.getPlayer());
    }

    /**
     * Called when someone leaves an area.
     * 
     * @param event Event data 
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onAreaLeave(AreaLeaveEvent event) {
        startSuffocating(event.getPlayer());
    }

    /**
     * Called when someone leaves space.
     * 
     * @param event Event data 
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onSpaceLeave(SpaceLeaveEvent event) {
        stopSuffocating(event.getPlayer());
    }

    /**
     * Called when someone enters space.
     * 
     * @param event Event data 
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onSpaceEnter(SpaceEnterEvent event) {
        if (!PlayerHandler.insideArea(event.getTo())) {
            startSuffocating(event.getPlayer(), event.getTo().getWorld());
        }
    }

    /**
     * Starts suffocation for a player.
     * This is only a convience method.
     * This should NOT be used on cross-world teleportation.
     * 
     * @param player Player to suffocate
     */
    public static void startSuffocating(Player player) {
        startSuffocating(player, player.getWorld());
    }
    
    /**
     * Starts suffocation for a player.
     * 
     * @param player Player to suffocate
     * @param world the world
     */
    public static void startSuffocating(Player player, World world) {
        if (player.hasPermission("Space.ignoresuitchecks")) {
            return;
        }
        String id = ConfigHandler.getID(world);
        boolean suffocatingOn = (ConfigHandler.getRequireHelmet(id) || ConfigHandler.getRequireSuit(id));
        MessageHandler.debugPrint(Level.INFO, "Started SuffocationRunnable for " + player.getName());
        if (suffocatingOn) {
            SuffacationRunnable task = new SuffacationRunnable(player);
            int taskInt = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, task, 20L, 20L);
            taskid.put(player, taskInt);
        }
    }

    /**
     * Stops a player from suffocating.
     * 
     * @param player Player to stop suffocating
     * 
     * @return True if suffocating stopped
     */
    public static boolean stopSuffocating(Player player) {
        if (!taskid.containsKey(player)) {
            return false;
        }
        if (Bukkit.getScheduler().isQueued(taskid.get(player))) {
            Bukkit.getScheduler().cancelTask(taskid.get(player));
            taskid.remove(player);
            return true;
        }
        return false;
    }
}
