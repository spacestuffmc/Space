/*
 * Copyright (c) 2019 SpaceStuffMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.jnbt;

/**
 * The <code>TAG_Double</code> tag.
 * @author Graham Edgecombe
 *
 */
public final class DoubleTag extends Tag {
	
	/**
	 * The value.
	 */
	private final double value;

	/**
	 * Creates the tag.
	 * @param name The name.
	 * @param value The value.
	 */
	public DoubleTag(String name, double value) {
		super(name);
		this.value = value;
	}
	
	@Override
	public Double getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		String name = getName();
		String append = "";
		if(name != null && !name.equals("")) {
			append = "(\"" + this.getName() + "\")";
		}
		return "TAG_Double" + append + ": " + value;
	}

}
