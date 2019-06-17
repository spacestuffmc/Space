/*
 * Copyright (c) 2019 SpaceStuffMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// Package Declaration
package com.spacestuffmc.space.listeners;

import com.spacestuffmc.space.api.event.area.AreaEnterEvent;
import com.spacestuffmc.space.api.event.area.AreaLeaveEvent;
import com.spacestuffmc.space.api.event.area.SpaceEnterEvent;
import com.spacestuffmc.space.api.event.area.SpaceLeaveEvent;
import com.spacestuffmc.space.handlers.MessageHandler;
import com.spacestuffmc.space.handlers.PlayerHandler;
import com.spacestuffmc.space.handlers.WorldHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

/**
 * PlayerListener for general space related actions.
 * 
 * @author iffa
 */
public class SpacePlayerListener implements Listener {
    // Variables
    private final Map<Player, Boolean> inArea = new HashMap<>();
    //private final Map<Player, Boolean> fixDupe = new HashMap<Player, Boolean>();


    /**
     * Called when a player attempts to teleport.
     * 
     * @param event Event data
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();
        if (WorldHandler.isSpaceWorld(Objects.requireNonNull(Objects.requireNonNull(event.getTo()).getWorld())) && event.getTo().getWorld() != event.getFrom().getWorld()) {
            /* Notify listeners start */
            SpaceEnterEvent e = new SpaceEnterEvent(event.getPlayer(), event.getFrom(), event.getTo());
            Bukkit.getServer().getPluginManager().callEvent(e);
            if (e.isCancelled()) {
                event.setCancelled(true);
                return;
            }
            /* Notify listeners end */
            MessageHandler.debugPrint(Level.INFO, "Player '" + event.getPlayer().getName() + "' teleported to space.");
            PlayerHandler.giveSuitOrHelmet(player);
        } else if (!WorldHandler.isSpaceWorld(Objects.requireNonNull(event.getTo().getWorld()))
                && WorldHandler.isSpaceWorld(Objects.requireNonNull(event.getFrom().getWorld()))) {
            /* Notify listeners start */
            SpaceLeaveEvent e = new SpaceLeaveEvent(event.getPlayer(), event.getFrom(), event.getTo());
            Bukkit.getServer().getPluginManager().callEvent(e);
            if (e.isCancelled()) {
                event.setCancelled(true);
                return;
            }
            /* Notify listeners end */
            PlayerHandler.removeSuitOrHelmet(player);
        }
    }

    /**
     * Called when a player attempts to move.
     * 
     * @param event Event data
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (WorldHandler.isInAnySpace(event.getPlayer())) {
            if(!inArea.containsKey(event.getPlayer())) inArea.put(event.getPlayer(), false);
            boolean insideArea=PlayerHandler.insideArea(event.getPlayer());
            if (insideArea) {
                if (!inArea.get(event.getPlayer())) {
                    inArea.put(event.getPlayer(), true);
                    /* Notify listeners start */
                    AreaEnterEvent e = new AreaEnterEvent(event.getPlayer());
                    Bukkit.getServer().getPluginManager().callEvent(e);
                    /* Notify listeners end */
                    MessageHandler.debugPrint(Level.INFO, "Player '" + event.getPlayer().getName() + "' entered an area.");
                }
            } else {
                if (inArea.get(event.getPlayer())) {
                    inArea.put(event.getPlayer(), false);
                    /* Notify listeners start */
                    AreaLeaveEvent e = new AreaLeaveEvent(event.getPlayer());
                    Bukkit.getServer().getPluginManager().callEvent(e);
                    /* Notify listeners end */
                    MessageHandler.debugPrint(Level.INFO, "Player '" + event.getPlayer().getName() + "' left an area.");
                }
            }
        }
    }


    /**
     * Called when a player quits the game.
     * 
     * @param event Event data
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if(SpaceSuffocationListener.stopSuffocating(event.getPlayer())){
            MessageHandler.debugPrint(Level.INFO, "Cancelled suffocation task for player '" + event.getPlayer().getName() + "'. (reason: left server)");
        }
        inArea.remove(event.getPlayer());
    }
    
    /**
     * Called when a player quits the game.
     * 
     * @param event Event data
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(!WorldHandler.isSpaceWorld(event.getPlayer().getWorld())) {
            return;
        }
        boolean insideArea = PlayerHandler.insideArea(event.getPlayer());
        inArea.put(event.getPlayer(), insideArea);
        SpaceEnterEvent e = new SpaceEnterEvent(event.getPlayer(),null,event.getPlayer().getLocation());
        Bukkit.getServer().getPluginManager().callEvent(e);
    }
    /**
     * Called on player respawn
     * 
     * @param event Event data
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if(WorldHandler.isSpaceWorld(Objects.requireNonNull(event.getRespawnLocation().getWorld()))){
            PlayerHandler.giveSuitOrHelmet(event.getPlayer());
            boolean insideArea = PlayerHandler.insideArea(event.getPlayer());
            inArea.put(event.getPlayer(), insideArea);
            SpaceEnterEvent e = new SpaceEnterEvent(event.getPlayer(),null,event.getRespawnLocation());
            Bukkit.getServer().getPluginManager().callEvent(e);
        }
        else{
            SpaceSuffocationListener.stopSuffocating(event.getPlayer());
        }
    }
    
    
}