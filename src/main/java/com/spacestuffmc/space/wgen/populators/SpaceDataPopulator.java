/*
 * Copyright (c) 2019 SpaceStuffMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.spacestuffmc.space.wgen.populators;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author kitskub
 */
@SuppressWarnings("NullableProblems")
public class SpaceDataPopulator extends BlockPopulator {
    private static Map<World, Map<WrappedCoords, Byte>> coords = new HashMap<>();
    
    public static void addCoords(World world, int chunkX, int chunkZ, int x, int y, int z, byte data) {
        if (coords.get(world) == null) {
            coords.put(world, new HashMap<WrappedCoords, Byte>());
        }
        WrappedCoords key = new WrappedCoords();
        key.chunkX = chunkX;
        key.chunkZ = chunkZ;
        key.x = x;
        key.y = y;
        key.z = z;
        coords.get(world).put(key, data);
    }

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        if (coords.get(world) == null) return;
        for (WrappedCoords c : coords.get(world).keySet()) {
            if (c.chunkX == chunk.getX() && c.chunkZ == chunk.getZ()) {
                //TODO: find replacement methods
                chunk.getBlock(c.x, c.y, c.z).setData(coords.get(world).get(c));
            }
        }
    }
    
    public static class WrappedCoords {
        int chunkX;
        int chunkZ;
        public int x;
        public int y;
        int z;

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final WrappedCoords other = (WrappedCoords) obj;
            if (this.chunkX != other.chunkX) {
                return false;
            }
            if (this.chunkZ != other.chunkZ) {
                return false;
            }
            if (this.x != other.x) {
                return false;
            }
            if (this.y != other.y) {
                return false;
            }
            return this.z == other.z;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 59 * hash + this.chunkX;
            hash = 59 * hash + this.chunkZ;
            hash = 59 * hash + this.x;
            hash = 59 * hash + this.y;
            hash = 59 * hash + this.z;
            return hash;
        }
        
    }
}
