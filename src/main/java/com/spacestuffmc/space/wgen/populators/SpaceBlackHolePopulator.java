/*
 * Copyright (c) 2019 SpaceStuffMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// Package Declaration
package com.spacestuffmc.space.wgen.populators;

import com.spacestuffmc.space.handlers.ConfigHandler;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

/**
 * Populates a world with black holes.
 *
 * @author kitskub
 * @author iffa
 * @author jflory7 
 */
public class SpaceBlackHolePopulator extends BlockPopulator {
    public static final int ID_TO_USE = 120; //for easier changing if needed

    public SpaceBlackHolePopulator() {
    }


    /**
     * Populates a chunk with black holes.
     *
     * @param world World
     * @param random Random
     * @param source Source chunk
     */
    @Override
    public void populate(World world, Random random, Chunk source) {
        String id = ConfigHandler.getID(world);
        
        if (withinSpawn(source)) {
            return;
        }
        
        if (random.nextInt(100) <= ConfigHandler.getBlackHoleChance(id)) {
            int chunkX = source.getX();
            int chunkZ = source.getZ();
            int x = random.nextInt(16);
            int z = random.nextInt(16);
            int y = random.nextInt(world.getMaxHeight());
            Block block = world.getBlockAt((chunkX * 16 + x), y, (chunkZ * 16 + z));
            block.setTypeId(ID_TO_USE);
        }
    }

    /**
     * Checks if the source chunk is in the spawn area.
     * 
     * @param source Source chunk
     * 
     * @return True if within spawn
     */
    private boolean withinSpawn(Chunk source) {
        int x = source.getX();
        int z = source.getZ();

        return x > -2 && x < 2 && z > -2 && z < 2;
    }
}
