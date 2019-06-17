/*
 * Copyright (c) 2019 SpaceStuffMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.jnbt;

/**
 * The <code>TAG_Long</code> tag.
 * @author Graham Edgecombe
 *
 */
public final class LongTag extends Tag {

	/**
	 * The value.
	 */
	private final long value;
	
	/**
	 * Creates the tag.
	 * @param name The name.
	 * @param value The value.
	 */
	public LongTag(String name, long value) {
		super(name);
		this.value = value;
	}
	
	@Override
	public Long getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		String name = getName();
		String append = "";
		if(name != null && !name.equals("")) {
			append = "(\"" + this.getName() + "\")";
		}
		return "TAG_Long" + append + ": " + value;
	}

}
