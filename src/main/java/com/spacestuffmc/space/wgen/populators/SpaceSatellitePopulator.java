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
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

/**
 * Populates a world with satellites.
 * 
 * @author iffa
 * @author NeonMaster (thanks for the original satellite design, too bad my mathematics blew it up!)
 */
public class SpaceSatellitePopulator extends BlockPopulator {
    /**
     * Populates a world with satellites.
     * 
     * @param world World
     * @param random Random
     * @param source Source chunk
     */
    @Override
    public void populate(World world, Random random, Chunk source) {
        String id = ConfigHandler.getID(world);
        if (random.nextInt(1337) <= ConfigHandler.getSatelliteChance(id)) {
            int height = random.nextInt(world.getMaxHeight());
            buildSatellite(world, height, source);
        }
    }

    /**
     * Builds a satellite. However badly! (but it looks cool)
     * 
     * @param world World
     * @param height Height
     * @param source Source chunk
     */
    private void buildSatellite(World world, int height, Chunk source) {
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 3; y++) {
                source.getBlock(y, height, x).setTypeId(102);
            }
        }
        for (int x = 6; x < 11; x++) {
            for (int y = 0; y < 3; y++) {
                source.getBlock(y, height, x).setTypeId(102);
            }
        }
        for (int y = 0; y < 3; y++) {
            source.getBlock(y, height, 5).setType(Material.IRON_BLOCK);
        }
    }
}
