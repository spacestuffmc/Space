/*
 * Copyright (c) 2016 CrystalCraftMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.jnbt;

/**
 * The <code>TAG_End</code> tag.
 * @author Graham Edgecombe
 *
 */
public final class EndTag extends Tag {

	/**
	 * Creates the tag.
	 */
	public EndTag() {
		super("");
	}

	@Override
	public Object getValue() {
		return null;
	}
	
	@Override
	public String toString() {
		return "TAG_End";
	}

}
