/*
 * Copyright (c) 2019 SpaceStuffMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// Package Declaration
package com.spacestuffmc.space.api.schematic;

import com.spacestuffmc.space.handlers.MessageHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.material.MaterialData;
import org.bukkit.util.BlockVector;
import org.jnbt.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.logging.Level;

/**
 * Schematic-file loading class. To be used for populators
 * 
 * @author iffa
 * @author kitskub
 * @author DrAgonmoray (original NBT loading code)
 * @author sk89q and other people who accidently a WE commit (schematic handling)
 */
@SuppressWarnings("deprecation")
public class SpaceSchematicHandler {
    // Variables
    public static File schematicFolder = new File("plugins" + File.separator + "Space" + File.separator + "schematics");
    private static List<Schematic> schematics = new ArrayList<>();

    /**
     * Gets the list of schematics loaded.
     * 
     * @return List of schematics loaded
     */
    public static List<Schematic> getSchematics() {
        return Collections.unmodifiableList(schematics);
    }

    /**
     * Places a schematic to a location.
     * 
     * @param schematic Schematic
     * @param origin Location the schematic should be placed to
     */
    public static void placeSchematic(Schematic schematic, Location origin) {
        Map<Location, Map<Material, MaterialData>> blocksMap = getBlocksMap(schematic, origin);
        Map<BlockVector, Map<String, Tag>> tileEntitiesMap = getTileEntitiesMap(schematic.getTileEntities());

        // Builds the schematic.
        MessageHandler.debugPrint(Level.INFO, "Now going inside buildSchematic.");
        buildSchematic(origin, blocksMap, tileEntitiesMap);
    }

    /**
     * Internal buildSchematic that places the schematic (hopefully succesfully!).
     * 
     * @param origin Origin location
     * @param blocksMap Blocks Map
     * @param tileEntitiesMap Tile Entities Map
     */
    private static void buildSchematic(Location origin,
            Map<Location, Map<Material, MaterialData>> blocksMap,
            Map<BlockVector, Map<String, Tag>> tileEntitiesMap) {
        
        // Variables
        MessageHandler.debugPrint(Level.INFO, "Inside buildSchematic with origin location: '" + origin.toString() + "'.");
        World world = origin.getWorld();

        // Setting blocks
        for (Location location : blocksMap.keySet()) {
            int originX = origin.getBlockX() + location.getBlockX();
            int originY = origin.getBlockY() + location.getBlockY();
            int originZ = origin.getBlockZ() + location.getBlockZ();

            for (Material material : blocksMap.get(location).keySet()) {
                Objects.requireNonNull(world).getBlockAt(originX, originY, originZ).setType(material);
                //TODO: testing required
                world.getBlockAt(originX, originY, originZ).getData();
            }
        }
    }

    /**
     * Loads all schematics from plugins/Space/schematics.
     */
    public static void loadSchematics() {
        List<File> files = Arrays.asList(Objects.requireNonNull(new File("plugins" + File.separator + "Space" + File.separator + "schematics").listFiles()));
        if (files.isEmpty()) {
            return;
        }
        for (File file : files) {
            if (file.isFile() && file.getName().toLowerCase().endsWith(".schematic")) {
                // It is for sure a .schematic-file, but now let's see if it's valid.
                loadSchematic(file);
            }
        }
    }
    
    private static Schematic loadSchematic(String name, NBTInputStream nbtStream) throws Exception{
            // Schematic tag
            CompoundTag schematicTag = (CompoundTag) nbtStream.readTag();
            if (!schematicTag.getName().equals("Schematic")) {
                throw new Exception("Tag \"Schematic\" does not exist or is not first");
            }

            // Check
            Map<String, Tag> tagCollection = schematicTag.getValue();
            if (!tagCollection.containsKey("Blocks")) {
                throw new Exception("Schematic file is missing a \"Blocks\" tag");
            }

            // Get information
            short width = getChildTag(tagCollection, "Width", ShortTag.class).getValue();
            short length = getChildTag(tagCollection, "Length", ShortTag.class).getValue();
            short height = getChildTag(tagCollection, "Height", ShortTag.class).getValue();

            // Get blocks
            byte[] blocks = getChildTag(tagCollection, "Blocks", ByteArrayTag.class).getValue();
            byte[] data = getChildTag(tagCollection, "Data", ByteArrayTag.class).getValue();
            
            List<Tag> entities = getChildTag(tagCollection, "Entities", ListTag.class).getValue();

            // Check type of Schematic
            String materials = getChildTag(tagCollection, "Materials", StringTag.class).getValue();
            if (!materials.equals("Alpha")) {
                throw new Exception("Schematic file is not an Alpha schematic");
            }

            // Need to pull out tile entities
            List<Tag> tileEntities = getChildTag(tagCollection, "TileEntities", ListTag.class).getValue();
            return new Schematic(name, blocks, data, width, height, length, entities, tileEntities);
    }

    /**
     * Loads a schematic file and adds it to the schematics list for later usage.
     * 
     * @param file Schematic file
     */
    private static void loadSchematic(File file) {
        try{
            FileInputStream fis = new FileInputStream(file);
            NBTInputStream nbt = new NBTInputStream(fis);
            Schematic schematic = loadSchematic(file.getName().replaceAll(".schematic", ""), nbt);
            nbt.close();
            fis.close();
            schematics.add(schematic);
            MessageHandler.debugPrint(Level.INFO, "Added Schematic '" + file.getName() + "' to schematics list:\n" + schematic.getWidth() + "\n" + schematic.getHeight() + "\n" + schematic.getLength() + "\n" + Arrays.toString(schematic.getBlocks()) + "\n" + Arrays.toString(schematic.getBlockData()));
        } catch (Exception e) {
            MessageHandler.print(Level.WARNING, "There was a problem while loading schematic file '" + file.getName() + "'! Are you sure it's a schematic: " + e.getMessage());
        }
    }

    /**
     * Gets a child tag.
     * 
     * @param items Tag collection
     * @param key Key
     * @param expected Expected
     * 
     * @return Child tag
     */
    private static <T extends Tag>  T getChildTag(Map<String, Tag> items, String key, Class<T> expected) throws Exception {
        if (!items.containsKey(key)) {
            throw new Exception("Schematic file is missing a \"" + key + "\" tag");
        }
        Tag tag = items.get(key);
        if (!expected.isInstance(tag)) {
            throw new Exception(
                    key + " tag is not of tag type " + expected.getName());
        }
        return expected.cast(tag);
    }

    private static Map<BlockVector, Map<String, Tag>> getTileEntitiesMap(List<Tag> tileEntities){
        Map<BlockVector, Map<String, Tag>> tileEntitiesMap = new HashMap<>();

        for (Tag tag : tileEntities) {
            if (!(tag instanceof CompoundTag)) continue;
            CompoundTag t = (CompoundTag) tag;

            int x = 0;
            int y = 0;
            int z = 0;

            Map<String, Tag> values = new HashMap<>();

            for (Map.Entry<String, Tag> entry : t.getValue().entrySet()) {
                switch (entry.getKey()) {
                    case "x":
                        if (entry.getValue() instanceof IntTag) {
                            x = ((IntTag) entry.getValue()).getValue();
                        }
                        break;
                    case "y":
                        if (entry.getValue() instanceof IntTag) {
                            y = ((IntTag) entry.getValue()).getValue();
                        }
                        break;
                    case "z":
                        if (entry.getValue() instanceof IntTag) {
                            z = ((IntTag) entry.getValue()).getValue();
                        }
                        break;
                }

                values.put(entry.getKey(), entry.getValue());
            }

            BlockVector vec = new BlockVector(x, y, z);
            tileEntitiesMap.put(vec, values);
        }
        return tileEntitiesMap;
    }
    
    private static Map<Location, Map<Material, MaterialData>> getBlocksMap(Schematic schematic, Location origin){
        Map<Location, Map<Material, MaterialData>> toRet = new HashMap<>();
        for (int x = 0; x < schematic.getWidth(); ++x) {
            for (int y = 0; y < schematic.getHeight(); ++y) {
                for (int z = 0; z < schematic.getLength(); ++z) {
                    int index = y * schematic.getWidth() * schematic.getLength() + z * schematic.getWidth() + x;
                    Material block = Material.getMaterial(String.valueOf(schematic.getBlocks()[index]));
                    MaterialData blockData = null;
                    try {
                        blockData = new MaterialData(block, schematic.getBlockData()[index]);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    Map<Material, MaterialData> tempMap = new EnumMap<>(Material.class);
                    tempMap.put(block, blockData);
                    toRet.put(new Location(origin.getWorld(), x, y, z), tempMap);
                }
            }
        }
        return toRet;
    }
    
    /**
     * Constructor of SpaceSchematicHandler.
     */
    private SpaceSchematicHandler() {
    }
}
