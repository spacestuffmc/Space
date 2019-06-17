/*
 * Copyright (c) 2016 CrystalCraftMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// Package Declaration
package com.spacestuffmc.space.listeners;

import com.spacestuffmc.space.api.SpacePlayerHandler;
import com.spacestuffmc.space.handlers.ConfigHandler;
import com.spacestuffmc.space.handlers.MessageHandler;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.logging.Level;

/**
 * EntityListener.
 * 
 * @author iffa
 */
public class SpaceEntityListener implements Listener {

    /**
     * Called when an entity (attempts) to take damage.
     * 
     * @param event Event data
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && event.getCause() == DamageCause.DROWNING) {
            Player player = (Player) event.getEntity();
            if (ConfigHandler.getStopDrowning()) {
                for (World world : ConfigHandler.getStopDrowningWorlds()) {
                    if (world == player.getWorld() && player.getInventory().getHelmet() != null && player.getInventory().getHelmet() == SpacePlayerHandler.toItemStack(ConfigHandler.getHelmet())) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    /**
     * Called when an entity dies.
     * 
     * @param event Event data
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            if(SpaceSuffocationListener.stopSuffocating(p)){
                MessageHandler.debugPrint(Level.INFO, "Cancelled suffocating task for player '" + p.getName() + "' because (s)he died.");
            }
        }
    }
}
