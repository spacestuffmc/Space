/*
 * Copyright (c) 2019 SpaceStuffMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// Package Declaration
package com.spacestuffmc.space.wgen.populators;

import com.spacestuffmc.space.api.schematic.Schematic;
import com.spacestuffmc.space.api.schematic.SpaceSchematicHandler;
import com.spacestuffmc.space.handlers.ConfigHandler;
import com.spacestuffmc.space.handlers.MessageHandler;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;
import java.util.logging.Level;

/**
 * Populates a world with schematics.
 * 
 * @author iffa
 */
public class SpaceSchematicPopulator extends BlockPopulator {

    /**
     * Populates a chunk with schematics.
     * 
     * @param world World
     * @param random Random
     * @param chunk Source chunk
     */
    @Override
    public void populate(World world, Random random, Chunk chunk) {
        if (SpaceSchematicHandler.getSchematics().isEmpty()) {
            return;
        }
        int y = new Random().nextInt(world.getMaxHeight());
        String id = ConfigHandler.getID(world);
        Schematic randomSchematic = SpaceSchematicHandler.getSchematics().get(new Random().nextInt(SpaceSchematicHandler.getSchematics().size()));
        if (new Random().nextInt(200) <= ConfigHandler.getSchematicChance(id)) {
            MessageHandler.debugPrint(Level.INFO, "Starting schematic population process with schematic '" + randomSchematic.getName() + "'.");
            SpaceSchematicHandler.placeSchematic(randomSchematic, new Location(world, (chunk.getX() << 4) + new Random().nextInt(10), y, (chunk.getZ() << 4) + new Random().nextInt(10)));
        }
    }
}
