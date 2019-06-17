/*
 * Copyright (c) 2016 CrystalCraftMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.jnbt;

/**
 * The <code>TAG_Float</code> tag.
 * @author Graham Edgecombe
 *
 */
public final class FloatTag extends Tag {

	/**
	 * The value.
	 */
	private final float value;
	
	/**
	 * Creates the tag.
	 * @param name The name.
	 * @param value The value.
	 */
	public FloatTag(String name, float value) {
		super(name);
		this.value = value;
	}
	
	@Override
	public Float getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		String name = getName();
		String append = "";
		if(name != null && !name.equals("")) {
			append = "(\"" + this.getName() + "\")";
		}
		return "TAG_Float" + append + ": " + value;
	}

}
