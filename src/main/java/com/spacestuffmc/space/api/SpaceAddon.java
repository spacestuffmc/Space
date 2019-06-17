/*
 * Copyright (c) 2019 SpaceStuffMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

/*
 */
package com.spacestuffmc.space.api;

/**
 *
 * @author kitskub
 */
public interface SpaceAddon {
    
    /**
     * Called when Space is enabled
    */
    //public void onSpaceEnable();
    
    /**
     * 
     * Called when Space is disabled
     */
    public void onSpaceDisable();
}
