package com.fraynn.utilitary;

import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utilitary {
	public static final String ID = "utilitary";
	public static final String NAME = "Utilitary";
	public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

	public static Identifier of(String name) {
		return Identifier.fromNamespaceAndPath(ID, name);
	}

	public static void init() {}
}