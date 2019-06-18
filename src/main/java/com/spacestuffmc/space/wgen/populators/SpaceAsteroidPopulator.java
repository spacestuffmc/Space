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
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

/**
 * SpaceAsteroidPopulator, populates a world with Notch's original stone (glowstone) patches.
 *
 * @author Markus 'Notch' Persson
 * @author iffa
 * @author Nightgunner5
 * @author kitskub
 */
@SuppressWarnings("NullableProblems")
public class SpaceAsteroidPopulator extends BlockPopulator {
    // Variables

    private static final BlockFace[] faces = {BlockFace.DOWN, BlockFace.EAST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.UP, BlockFace.WEST};

    /**
     * Populates a world with stone patches.
     * 
     * @param world World
     * @param random Random
     * @param source Source chunk
     */
    @Override
    public void populate(World world, Random random, Chunk source) {
        for (int i = 0; i < 2; i++) {
            Block block = getRandomBlock(source, random);
            String id = ConfigHandler.getID(world);
            //TODO: find replacement methods
            if (random.nextInt(200) <= ConfigHandler.getStoneChance(id) && block.getType() == Material.AIR) {
                block.setType(Material.STONE);
                for (int j = 0; j < 1500; j++) {
                    Block current = block.getRelative(random.nextInt(8) - random.nextInt(8),
                            random.nextInt(12),
                            random.nextInt(8) - random.nextInt(8));
                    if (current.getType() != Material.AIR) {
                        continue;
                    }
                    int count = 0;
                    for (BlockFace face : faces) {
                        if (current.getRelative(face).getType() == Material.STONE) {
                            count++;
                        }
                    }
                    if (count == 1) {
                        current.setType(Material.STONE);
                    }
                }
            }
            block = getRandomBlock(source, random);
            if (random.nextInt(200) <= ConfigHandler.getGlowstoneChance(id) && block.getType() == Material.AIR) {
                block.setType(Material.GLOWSTONE);

                for (int j = 0; j < 1500; j++) {
                    Block current = block.getRelative(random.nextInt(8) - random.nextInt(8),
                            random.nextInt(12),
                            random.nextInt(8) - random.nextInt(8));
                    if (current.getType() != Material.AIR) {
                        continue;
                    }
                    int count = 0;
                    for (BlockFace face : faces) {
                        if (current.getRelative(face).getType() == Material.GLOWSTONE) {
                            count++;
                        }
                    }
                    if (count == 1) {
                        current.setType(Material.GLOWSTONE);
                    }
                }
            }
        }
    }

    /**
     * Gets a random block from the source chunk.
     * 
     * @param source Source chunk
     * @param random Random
     * 
     * @return Random block;
     */
    private Block getRandomBlock(Chunk source, Random random) {
        int x = random.nextInt(16);
        int y = random.nextInt(source.getWorld().getMaxHeight());
        int z = random.nextInt(16);
        return source.getBlock(x, y, z);
    }
}
