/*
 * Copyright (c) 2019 SpaceStuffMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// Package Declaration
package com.spacestuffmc.space.wgen.planets;

import org.bukkit.material.MaterialData;

import java.io.Serializable;
import java.util.Set;

/**
 * Holder class for an individual planetoid.
 * 
 * @author Canis85
 * @author iffa
 */
@SuppressWarnings("deprecation")
public class Planetoid implements Serializable {
    // Variables
    private static final long serialVersionUID = 1L;
    Set<MaterialData> coreBlkIds;
    Set<MaterialData> shellBlkIds;
    int shellThickness;
    int radius;
    int xPos;
    int yPos;
    int zPos;

    /**
     * Constructor of Planetoid.
     */
    Planetoid() {
    }

    /**
     * Constructor of Planetoid.
     * 
     * @param coreID Core material
     * @param shellID Shell material
     * @param shellThick Shell thickness
     * @param radius Radius
     * @param x X-coord
     * @param y Y-coord
     * @param z Z-coord
     */
    public Planetoid(Set<MaterialData> coreID, Set<MaterialData> shellID, int shellThick, int radius, int x, int y, int z) {
        this.coreBlkIds = coreID;
        this.shellBlkIds = shellID;
        this.shellThickness = shellThick;
        this.radius = radius;
        this.xPos = x;
        this.yPos = y;
        this.zPos = z;
    }
}
