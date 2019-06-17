/*
 * Copyright (c) 2019 SpaceStuffMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// Package Declaration
package com.spacestuffmc.space.listeners.misc;

import com.spacestuffmc.space.handlers.WorldHandler;
import com.spacestuffmc.space.wgen.populators.SpaceBlackHolePopulator;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Player listener to trigger black hole sucking.
 *
 * @author iffa
 * @author jflory7
 */
@SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "unused", "ConstantConditions"})
public class BlackHolePlayerListener implements Listener {
    // Variables
    private static Map<Player, Integer> runnables = new HashMap<>();
    private static Map<Chunk, Boolean> scannedNonSpout = new HashMap<>();
    private static final int SIZE = 20;
    private static long lastTime = System.currentTimeMillis();
    private static List<Block> nonSpoutBlocks = new ArrayList<>(); //To be used if not using Spout and black holes is on

    /**
     * Called when a player attempts to move.
     *
     * @param event Event data
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.isCancelled() || !WorldHandler.isInAnySpace(event.getPlayer()) || event.getPlayer().getHealth() == 0 || event.getPlayer().hasPermission("Space.ignoreblackholes")) return;
        long currentTime = System.currentTimeMillis();
        if (!(lastTime + 200 <= currentTime)) return;
        lastTime = System.currentTimeMillis();
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event){// Check if breaking non-spout black hole
	if (event.isCancelled() || !WorldHandler.isInAnySpace(event.getPlayer()) || event.getPlayer().getHealth() == 0 || !event.getPlayer().hasPermission("Space.ignoreblackholes")) {
            return;
        }
	String id = WorldHandler.getID(event.getPlayer().getWorld());
	if(Action.LEFT_CLICK_BLOCK != event.getAction() || event.getClickedBlock().getTypeId() != SpaceBlackHolePopulator.ID_TO_USE){
	    return;
	}
	event.getClickedBlock().setTypeId(0);
    }
    /**
     * Gets all running suck tasks.
     *
     * @return All tasks
     */
    public static Map<Player, Integer> getRunningTasks() {
        return runnables;
    }

    public static void stopRunnable(Player player) {
        Bukkit.getScheduler().cancelTask(runnables.get(player));
        runnables.remove(player);
    }
    
    private static void checkBlocksNonSpout(Location loc) {
        Chunk center = loc.getChunk();
        int chunks = (int) Math.ceil((SIZE - 1) / 16);
        chunks = chunks > Bukkit.getViewDistance() ? Bukkit.getViewDistance() : chunks;
        for (int chunkX = -chunks; chunkX <= chunks; chunkX++) {
            for (int chunkZ = -chunks; chunkZ <= chunks; chunkZ++) {
                Chunk chunk = loc.getWorld().getChunkAt(center.getX() + chunkX, center.getZ() + chunkZ);
                if (scannedNonSpout.containsKey(chunk) && scannedNonSpout.get(chunk)) {
                    continue;
                }
                for (int x = 0; x < 16; x++) {
                    for (int y = 0; y < 128; y++) {
                        for (int z = 0; z < 16; z++) {
			    Block block = chunk.getBlock(x, y, z);
			    if(block.getTypeId() == SpaceBlackHolePopulator.ID_TO_USE){
				nonSpoutBlocks.add(block);
			    }

                        }
                    }
                }
                scannedNonSpout.put(chunk, true);
            }
        }
    }
}
