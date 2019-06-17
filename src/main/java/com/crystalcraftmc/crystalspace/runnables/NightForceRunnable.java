/*
 * Copyright (c) 2016 CrystalCraftMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// Package Declaration
package com.crystalcraftmc.crystalspace.runnables;

import org.bukkit.World;

/**
 * A runnable class for forcing night.
 * 
 * @author iffa
 */
public class NightForceRunnable implements Runnable {
    // Variables
    private World world;

    /**
     * Constructor for NightForceRunnable.
     * 
     * @param world World
     */
    public NightForceRunnable(World world) {
        this.world = world;
    }

    /**
     * Forces night when repeated every 8399 ticks.
     */
    @Override
    public void run() {
        world.setTime(13801);
    }
}
