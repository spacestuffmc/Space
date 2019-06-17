/*
 * Copyright (c) 2019 SpaceStuffMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.jnbt;

/**
 * Represents a single NBT tag.
 * @author Graham Edgecombe
 *
 */
public abstract class Tag {
	
	/**
	 * The name of this tag.
	 */
	private final String name;
	
	/**
	 * Creates the tag with the specified name.
	 * @param name The name.
	 */
	public Tag(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the name of this tag.
	 * @return The name of this tag.
	 */
	public final String getName() {
		return name;
	}
	
	/**
	 * Gets the value of this tag.
	 * @return The value of this tag.
	 */
	public abstract Object getValue();

}
