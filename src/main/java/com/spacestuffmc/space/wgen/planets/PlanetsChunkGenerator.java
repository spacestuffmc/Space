/*
 * Copyright (c) 2016 CrystalCraftMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// Package Declaration
package com.spacestuffmc.space.wgen.planets;

import com.spacestuffmc.space.config.SpaceConfig;
import com.spacestuffmc.space.handlers.ConfigHandler;
import com.spacestuffmc.space.handlers.MessageHandler;
import com.crystalcraftmc.space.wgen.populators.*;
import com.spacestuffmc.space.wgen.populators.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.material.MaterialData;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import org.bukkit.configuration.InvalidConfigurationException;

/**
 * Generates a space world with planets.
 * 
 * @author Jack
 * @author Canis85
 * @author iffa
 */
public class PlanetsChunkGenerator extends ChunkGenerator {
    // Variables
    private Map<Set<MaterialData>, Float> allowedShellIds;
    private Map<Set<MaterialData>, Float> allowedCoreIds;
    private int density = SpaceConfig.getConfig(SpaceConfig.ConfigFile.DEFAULT_PLANETS).getInt("density", (Integer) SpaceConfig.Defaults.DENSITY.getDefault()); // Number of planetoids it will try to create per
    private int minSize = SpaceConfig.getConfig(SpaceConfig.ConfigFile.DEFAULT_PLANETS).getInt("minSize", (Integer) SpaceConfig.Defaults.MIN_SIZE.getDefault()); // Minimum radius
    private int maxSize = SpaceConfig.getConfig(SpaceConfig.ConfigFile.DEFAULT_PLANETS).getInt("maxSize", (Integer) SpaceConfig.Defaults.MAX_SIZE.getDefault()); // Maximum radius
    private int minDistance = SpaceConfig.getConfig(SpaceConfig.ConfigFile.DEFAULT_PLANETS).getInt("minDistance", (Integer) SpaceConfig.Defaults.MIN_DISTANCE.getDefault()); // Minimum distance between planets, in blocks
    private int floorHeight = SpaceConfig.getConfig(SpaceConfig.ConfigFile.DEFAULT_PLANETS).getInt("floorHeight", (Integer) SpaceConfig.Defaults.FLOOR_HEIGHT.getDefault()); // Floor height
    private int maxShellSize = SpaceConfig.getConfig(SpaceConfig.ConfigFile.DEFAULT_PLANETS).getInt("maxShellSize", (Integer) SpaceConfig.Defaults.MAX_SHELL_SIZE.getDefault()); // Maximum shell thickness
    private int minShellSize = SpaceConfig.getConfig(SpaceConfig.ConfigFile.DEFAULT_PLANETS).getInt("minShellSize", (Integer) SpaceConfig.Defaults.MIN_SHELL_SIZE.getDefault()); // Minimum shell thickness, should be at least 3
    private Material floorBlock = Material.matchMaterial(SpaceConfig.getConfig(SpaceConfig.ConfigFile.DEFAULT_PLANETS).getString("floorBlock", (String) SpaceConfig.Defaults.FLOOR_BLOCK.getDefault()));// BlockID for the floor
    private static HashMap<World, List<Planetoid>> planets = new HashMap<World, List<Planetoid>>();
    public final String ID;
    public final boolean GENERATE;

    /**
     * Constructor of PlanetsChunkGenerator.
     * 
     * @param id ID
     */
    public PlanetsChunkGenerator(String id) {
        this(id, ConfigHandler.getGeneratePlanets(id));
    }

    /**
     * Constructor of PlanetsChunkGenerator 2.
     * 
     * @param id ID
     * @param generate ?
     */
    public PlanetsChunkGenerator(String id, boolean generate) {
        this.ID = id.toLowerCase();
        this.GENERATE = generate;
        loadAllowedBlocks();
        loadPlanetSettings();
    }

    /**
     * Generates chunk data for a chunk.
     * 
     * @param world
     * @param random
     * @param x
     * @param z
     * @param biome
     * @return 
     */
    @Override
    public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
        if (!planets.containsKey(world)) {
            planets.put(world, new ArrayList<Planetoid>());
        }
        
        ChunkData cData = this.createChunkData(world);
        
        if (GENERATE) {
            MessageHandler.debugPrint(Level.INFO, "GENERATE == true, generating planet");
            generatePlanetoids(world, x, z);
            // Go through the current system's planetoids and fill in this chunk as
            // needed.
            for (Planetoid curPl : planets.get(world)) {
                // Find planet's center point relative to this chunk.
                int relCenterX = curPl.xPos - x * 16;
                int relCenterZ = curPl.zPos - z * 16;

                for (int curX = -curPl.radius; curX <= curPl.radius; curX++) {//Iterate across every x block
                    boolean xShell = false;//Is part of the x shell
                    int chunkX = curX + relCenterX;
                    if (chunkX >= 0 && chunkX < 16) {
                        int worldX = curX + curPl.xPos;//get the x block number in the world
                        // Figure out radius of this circle
                        int distFromCenter = Math.abs(curX);//Distance from center in the x 
                        if (curPl.radius - distFromCenter < curPl.shellThickness) {//Check if part of xShell
                            xShell = true;
                        }
                        int zHalfLength = (int) Math.ceil(Math.sqrt((curPl.radius * curPl.radius) - (distFromCenter * distFromCenter)));//Half the amount of blocks in the z direction
                        for (int curZ = -zHalfLength; curZ <= zHalfLength; curZ++) {//Iterate over all z blocks 
                            int chunkZ = curZ + relCenterZ;
                            if (chunkZ >= 0 && chunkZ < 16) {
                                int worldZ = curZ + curPl.zPos;//get the z block number in the world
                                boolean zShell = false;//Is part of z shell
                                int zDistFromCenter = Math.abs(curZ);//Distance from center in the z
                                if (zHalfLength - zDistFromCenter < curPl.shellThickness) {//Check if part of zShell
                                    zShell = true;
                                }
                                int yHalfLength = (int) Math.ceil(Math.sqrt((zHalfLength * zHalfLength) - (zDistFromCenter * zDistFromCenter)));
                                for (int curY = -yHalfLength; curY <= yHalfLength; curY++) {
                                    int worldY = curY + curPl.yPos;
                                    boolean yShell = false;
                                    if (yHalfLength - Math.abs(curY) < curPl.shellThickness) {
                                        yShell = true;
                                    }
                                    if (xShell || zShell || yShell) {
                                        ArrayList<MaterialData> list = new ArrayList<MaterialData>(curPl.shellBlkIds);
                                        MaterialData get = list.get(random.nextInt(list.size()));
                                        cData.setBlock(chunkX, worldY, chunkZ, get);
                                        //setBlock(retVal, chunkX, worldY, chunkZ, (byte) get.getItemTypeId());
                                        if (get.getData() != 0) { //Has data
                                            SpaceDataPopulator.addCoords(world, x, z, chunkX, worldY, chunkZ, get.getData());
                                        }
                                    } else {
                                        ArrayList<MaterialData> list = new ArrayList<MaterialData>(curPl.coreBlkIds);
                                        // this confuses me too much. this part is setting core blocks, right? how is it "random"?
                                        MaterialData get = list.get(random.nextInt(list.size()));
                                        cData.setBlock(chunkX, worldY, chunkZ, get);
                                        //setBlock(retVal, chunkX, worldY, chunkZ, (byte) get.getItemTypeId());
                                        if (get.getData() != 0) { //Has data
                                            SpaceDataPopulator.addCoords(world, x, z, chunkX, worldY, chunkZ, get.getData());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
        // Fill in the floor
        for (int floorY = 0; floorY < floorHeight; floorY++) {
            for (int floorX = 0; floorX < 16; floorX++) {
                for (int floorZ = 0; floorZ < 16; floorZ++) {
                    if (floorY == 0) {
                        cData.setBlock(floorX, floorY, floorZ, Material.BEDROCK);
                        //setBlock(retVal, floorX, floorY, floorZ, (byte) Material.BEDROCK.getId());
                    } else {
                        cData.setBlock(floorX, floorY, floorZ, floorBlock);
                        //setBlock(retVal, floorX, floorY, floorZ, (byte) floorBlock.getId());
                    }
                }
            }
        }
        return cData;
    }
    
    

    /**
     * Generates a world.
     * 
     * @param world World
     * @param random Random
     * @param x X-pos
     * @param z Z-pos
     * @param biomes 
     * @return Byte array
     * @deprecated generateChunkData for 1.8+
     */
    @Override
    public byte[][] generateBlockSections(World world, Random random, int x, int z, BiomeGrid biomes){
        if (!planets.containsKey(world)) {
            planets.put(world, new ArrayList<Planetoid>());
        }
        byte[][] retVal = new byte[world.getMaxHeight() / 16][];

        if (GENERATE) {
            generatePlanetoids(world, x, z);
            // Go through the current system's planetoids and fill in this chunk as
            // needed.
            for (Planetoid curPl : planets.get(world)) {
                // Find planet's center point relative to this chunk.
                int relCenterX = curPl.xPos - x * 16;
                int relCenterZ = curPl.zPos - z * 16;

                for (int curX = -curPl.radius; curX <= curPl.radius; curX++) {//Iterate across every x block
                    boolean xShell = false;//Is part of the x shell
                    int chunkX = curX + relCenterX;
                    if (chunkX >= 0 && chunkX < 16) {
                        int worldX = curX + curPl.xPos;//get the x block number in the world
                        // Figure out radius of this circle
                        int distFromCenter = Math.abs(curX);//Distance from center in the x 
                        if (curPl.radius - distFromCenter < curPl.shellThickness) {//Check if part of xShell
                            xShell = true;
                        }
                        int zHalfLength = (int) Math.ceil(Math.sqrt((curPl.radius * curPl.radius) - (distFromCenter * distFromCenter)));//Half the amount of blocks in the z direction
                        for (int curZ = -zHalfLength; curZ <= zHalfLength; curZ++) {//Iterate over all z blocks 
                            int chunkZ = curZ + relCenterZ;
                            if (chunkZ >= 0 && chunkZ < 16) {
                                int worldZ = curZ + curPl.zPos;//get the z block number in the world
                                boolean zShell = false;//Is part of z shell
                                int zDistFromCenter = Math.abs(curZ);//Distance from center in the z
                                if (zHalfLength - zDistFromCenter < curPl.shellThickness) {//Check if part of zShell
                                    zShell = true;
                                }
                                int yHalfLength = (int) Math.ceil(Math.sqrt((zHalfLength * zHalfLength) - (zDistFromCenter * zDistFromCenter)));
                                for (int curY = -yHalfLength; curY <= yHalfLength; curY++) {
                                    int worldY = curY + curPl.yPos;
                                    boolean yShell = false;
                                    if (yHalfLength - Math.abs(curY) < curPl.shellThickness) {
                                        yShell = true;
                                    }
                                    if (xShell || zShell || yShell) {
                                        ArrayList<MaterialData> list = new ArrayList<MaterialData>(curPl.shellBlkIds);
                                        MaterialData get = list.get(random.nextInt(list.size()));
                                        setBlock(retVal, chunkX, worldY, chunkZ, (byte) get.getItemTypeId());
                                        if (get.getData() != 0) { //Has data
                                            SpaceDataPopulator.addCoords(world, x, z, chunkX, worldY, chunkZ, get.getData());
                                        }
                                    } else {
                                        ArrayList<MaterialData> list = new ArrayList<MaterialData>(curPl.coreBlkIds);
                                        // this confuses me too much. this part is setting core blocks, right? how is it "random"?
                                        MaterialData get = list.get(random.nextInt(list.size()));
                                        setBlock(retVal, chunkX, worldY, chunkZ, (byte) get.getItemTypeId());
                                        if (get.getData() != 0) { //Has data
                                            SpaceDataPopulator.addCoords(world, x, z, chunkX, worldY, chunkZ, get.getData());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
        // Fill in the floor
        for (int floorY = 0; floorY < floorHeight; floorY++) {
            for (int floorX = 0; floorX < 16; floorX++) {
                for (int floorZ = 0; floorZ < 16; floorZ++) {
                    if (floorY == 0) {
                        setBlock(retVal, floorX, floorY, floorZ, (byte) Material.BEDROCK.getId());
                    } else {
                        setBlock(retVal, floorX, floorY, floorZ, (byte) floorBlock.getId());
                    }
                }
            }
        }
        return retVal;
    }

    /**
     * 
     * @param result
     * @param x
     * @param y
     * @param z
     * @param blkid 
     * @deprecated ChunkGenerator.ChunkData.setBlock for 1.8+
     */
    static void setBlock(byte[][] result, int x, int y, int z, byte blkid) {
        if (result[y >> 4] == null) {
            result[y >> 4] = new byte[4096];
        }
        result[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = blkid;
    }

    /**
     * Generates planets.
     * 
     * @param world World
     * @param x X-pos
     * @param z Z-pos
     */
    @SuppressWarnings("fallthrough")
    private void generatePlanetoids(World world, int x, int z) {
        long seed = world.getSeed();
        List<Planetoid> planetoids = new ArrayList<Planetoid>();
//        //Seed shift;
//        // if X is negative, left shift seed by one
//        if (x < 0) {
//            seed <<= 1;
//        } // if Z is negative, change sign on seed.
//        if (z < 0) {
//            seed = -seed;
//        }



        // If x and Z are zero, generate a log/leaf planet close to 0,0
        if (x == 0 && z == 0) {
            Planetoid spawnPl = new Planetoid();
            spawnPl.xPos = 7;
            spawnPl.yPos = 70;
            spawnPl.zPos = 7;
            spawnPl.coreBlkIds = new HashSet<MaterialData>(Collections.singleton(new MaterialData(Material.LOG)));
            spawnPl.shellBlkIds = new HashSet<MaterialData>(Collections.singleton(new MaterialData(Material.LEAVES)));
            spawnPl.shellThickness = 3;
            spawnPl.radius = 6;
            planets.get(world).add(spawnPl);
        }

        //x = (x*16) - minDistance;
        //z = (z*16) - minDistance;
        Random rand = new Random(getSeed(world, x, 0, z, 0));
//        for (int i = 0; i < Math.abs(x) + Math.abs(z); i++) {
//            // cycle generator
//            rand.nextInt();
//            rand.nextInt();
//            rand.nextInt();
//            //rand.nextInt();
//            //rand.nextInt();
//            //rand.nextInt();
//            //rand.nextInt();
//        }

        for (int i = 0; i < density; i++) {
            // Try to make a planet
            Planetoid curPl = new Planetoid();
            curPl.shellBlkIds = getBlockTypes(rand, false, true);
            //boolean noHeat = false;
            outer:
            for (MaterialData d : curPl.shellBlkIds) {//Circumstances
                if (d.getItemType() == null) {//Not vanilla
                    continue;//Don't care
                }
                switch (d.getItemType()) {
                    case LEAVES:
                        curPl.coreBlkIds = new HashSet<MaterialData>(Collections.singleton(new MaterialData(Material.LOG)));//If there's any leaves, only use wood
                        break outer;
                    case ICE:
                        curPl.coreBlkIds = new HashSet<MaterialData>(Collections.singleton(new MaterialData(Material.WATER)));// If there's ice, use water? Not final
                        break outer;
                    case WOOL:
                        curPl.coreBlkIds = getBlockTypes(rand, true, true);
                        break outer;
                    default:
                        curPl.coreBlkIds = getBlockTypes(rand, true, false);
                }
            }

            curPl.shellThickness = rand.nextInt(maxShellSize - minShellSize) + minShellSize;
            curPl.radius = rand.nextInt(maxSize - minSize) + minSize;

            // Set position
            curPl.xPos = (x * 16) - minDistance + rand.nextInt(minDistance + 16 + minDistance);
            int randInt = world.getMaxHeight() - curPl.radius * 2 - floorHeight;
            curPl.yPos = rand.nextInt(randInt >= 0 ? randInt : 0) + curPl.radius;
            curPl.zPos = (z * 16) - minDistance + rand.nextInt(minDistance + 16 + minDistance);

            // Created a planet, check for collisions with existing planets
            // If any collision, discard planet
            boolean discard = false;
            for (Planetoid pl : planetoids) {
                // each planetoid has to be at least pl1.radius + pl2.radius +
                // min distance apart
                int distMin = pl.radius + curPl.radius + minDistance;
                if (distanceSquared(pl, curPl) < distMin * distMin) {
                    discard = true;
                    break;
                }
            }
            if (!planets.isEmpty()) {
                if (!planets.get(world).isEmpty()) {
                    List<Planetoid> tempPlanets = planets.get(world);
                    for (Planetoid pl : tempPlanets) {
                        // each planetoid has to be at least pl1.radius + pl2.radius +
                        // min distance apart
                        int distMin = pl.radius + curPl.radius + minDistance;
                        if (distanceSquared(pl, curPl) < distMin * distMin) {
                            discard = true;
                            break;
                        }
                    }
                }
            }
            if (!discard) {
                planetoids.add(curPl);
            }
        }
        planets.get(world).addAll(planetoids);

    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        ArrayList<BlockPopulator> populators = new ArrayList<BlockPopulator>();
        if (ConfigHandler.getSatellitesEnabled(ID)) {
            populators.add(new SpaceSatellitePopulator());
        }
        if (ConfigHandler.getAsteroidsEnabled(ID)) {
            populators.add(new SpaceAsteroidPopulator());
        }
        if (ConfigHandler.getGenerateSchematics(ID)) {
            populators.add(new SpaceSchematicPopulator());
        }
        else if (ConfigHandler.getGenerateBlackHoles(ID)){
            populators.add(new SpaceBlackHolePopulator());
        }
        populators.add(new SpaceDataPopulator());
        
        // Not FPS friendly
        if (false) {
            populators.add(new SpaceEffectPopulator());
        }
        return populators;
    }

    /**
     * Loads allowed blocks
     */
    @SuppressWarnings("unchecked")
    private void loadAllowedBlocks() {
        allowedCoreIds = new HashMap<Set<MaterialData>, Float>();
        allowedShellIds = new HashMap<Set<MaterialData>, Float>();
        for (String s : SpaceConfig.getConfig(SpaceConfig.ConfigFile.DEFAULT_PLANETS).getStringList("blocks.cores")) {
            String[] sSplit = s.replaceAll("\\s","").split("-");
            String[] matList = sSplit[0].split(",");
            Set<MaterialData> matDatas = toSet(matList);
            float value;
            if (sSplit.length == 2) {
                value = Float.valueOf(sSplit[1]);
            } else {
                value = 1.0f;
            }
            allowedCoreIds.put(matDatas, value);
            MessageHandler.debugPrint(Level.INFO, "allowedCoreIds has " + allowedCoreIds.size() + " entries");
        }

        for (String s : SpaceConfig.getConfig(SpaceConfig.ConfigFile.DEFAULT_PLANETS).getStringList("blocks.shells")) {
            String[] sSplit = s.replaceAll("\\s","").split("-");
            String[] matList = sSplit[0].split(",");
            Set<MaterialData> matDatas = toSet(matList);
            float value;
            if (sSplit.length == 2) {
                value = Float.valueOf(sSplit[1]);
            } else {
                value = 1.0f;
            }
            allowedShellIds.put(matDatas, value);
            MessageHandler.debugPrint(Level.INFO, "allowedShellIds has " + allowedShellIds.size() + " entries");
        }
    }

    private static Set<MaterialData> toSet(String[] matList) {
        Set<MaterialData> matDatas = new HashSet<MaterialData>();
        for (String mat : matList) {
            int data = 0;
            String name = "";
            if(mat.split(":").length == 2){
                try {
                    name = mat.split(":")[0];
                    data = Integer.parseInt(mat.split(":")[1]);
                } catch (NumberFormatException numberFormatException) {
                    MessageHandler.print(Level.WARNING, "Invalid core block in planets.yml");
                }
            }
            else{
                name = mat;
            }
            MessageHandler.debugPrint(Level.INFO, "Trying to match material with name: " + name);
            Material newMat = Material.matchMaterial(name);

            if(newMat != null){//Vanilla material
                if (newMat.isBlock()) {
                    // TODO: Non-deprecated alternative? as well as to other MaterialDatas in the source...
                    // https://bukkit.org/threads/so-block-setdata-is-deprecated-whats-the-new-way-to-change-the-data-of-an-existing-block.189076/
                    // No easy way to use a non-dprecated method ^
                    matDatas.add(new MaterialData(newMat, (byte) data));
                } else {
                    MessageHandler.print(Level.WARNING, newMat.toString() + " is not a block");
                }
            }
            else{
                //UNSAFE! Does not check if id represents a block
                try {
                    matDatas.add(new MaterialData(Integer.parseInt(name), (byte) data));
                } catch (NumberFormatException numberFormatException) {
                    MessageHandler.print(Level.WARNING, "Unrecognized id (" + name + ") in planets.yml");
                }
            }
        }
        return matDatas;
    }
    /**
     * Gets the squared distance.
     * @param pl1 Planetoid 1
     * @param pl2 Planetoid 2
     * 
     * @return Distance
     */
    private int distanceSquared(Planetoid pl1, Planetoid pl2) {
        int xDist = pl2.xPos - pl1.xPos;
        int yDist = pl2.yPos - pl1.yPos;
        int zDist = pl2.zPos - pl1.zPos;

        return xDist * xDist + yDist * yDist + zDist * zDist;
    }

    /**
     * Returns a valid block type.
     * 
     * //@param randrandom generator to use
     * @param core if true, searching through allowed cores, otherwise allowed shells
     * //@param heated if true, will not return a block that gives off heat
     * 
     * @return Material
     */
    private Set<MaterialData> getBlockTypes(Random rand, boolean core, boolean noHeat) {
        Set<MaterialData> retVal = null;
        Map<Set<MaterialData>, Float> refMap;
        if (core) {
            refMap = allowedCoreIds;
        } else {
            refMap = allowedShellIds;
        }
        outer:
        while (retVal == null) {
            Set<MaterialData> dataList = new ArrayList<Set<MaterialData>>(refMap.keySet()).get(rand.nextInt(refMap.size()));
            float testVal = rand.nextFloat();
            if (refMap.get(dataList) > testVal) {
                retVal = dataList;
                for (MaterialData d : dataList) {
                    if (noHeat) {
                        if(d.getItemType() == null){//Not a Vanilla Material. Don't care.
                            continue;
                        }
                        switch (d.getItemType()) {
                            case BURNING_FURNACE:
                            case FIRE:
                            case GLOWSTONE:
                            case JACK_O_LANTERN:
                            case STATIONARY_LAVA:
                                retVal = null;//Try again
                                continue outer;
                            default:
                        }
                    }
                }
            }
        }
        return retVal;
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        // TODO: figure out if it's our fault or MV's fault that this is not used...
        return new Location(world, 7, 78, 7);
    }
    
    private final static int HASH_SHIFT = 19;
    private final static long HASH_SHIFT_MASK = (1L << HASH_SHIFT) - 1;

    /**
     * Returns the particular seed a Random should use for a position
     *
     * The meaning of the x, y and z coordinates can be determined by the
     * generator.
     *
     * This gives consistent results for world generation.
     *
     * The extra seed allows multiple Randoms to be returned for the same
     * position for use by populators and different stages of generation.
     *
     * @param world the World
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @param extraSeed the extra seed value
     * @return the seed 
     */
    public static long getSeed(World world, int x, int y, int z, int extraSeed) {
            long hash = world.getSeed();
            hash += (hash << HASH_SHIFT) + (hash >> 64 - HASH_SHIFT & HASH_SHIFT_MASK) + extraSeed;
            hash += (hash << HASH_SHIFT) + (hash >> 64 - HASH_SHIFT & HASH_SHIFT_MASK) + x;
            hash += (hash << HASH_SHIFT) + (hash >> 64 - HASH_SHIFT & HASH_SHIFT_MASK) + y;
            hash += (hash << HASH_SHIFT) + (hash >> 64 - HASH_SHIFT & HASH_SHIFT_MASK) + z;

            return hash;
    }

    private void loadPlanetSettings() {
        if(ID.equals("planets")) return;
        try {
            YamlConfiguration config = new YamlConfiguration();
            config.load(new File(Bukkit.getPluginManager().getPlugin("Space").getDataFolder(), "planets/" + ConfigHandler.getPlanetsFile(ID)));
            density = config.getInt("density", (Integer) SpaceConfig.Defaults.DENSITY.getDefault()); // Number of planetoids it will try to create per
            minSize = config.getInt("minSize", (Integer) SpaceConfig.Defaults.MIN_SIZE.getDefault()); // Minimum radius
            maxSize = config.getInt("maxSize", (Integer) SpaceConfig.Defaults.MAX_SIZE.getDefault()); // Maximum radius
            minDistance = config.getInt("minDistance", (Integer) SpaceConfig.Defaults.MIN_DISTANCE.getDefault()); // Minimum distance between planets, in blocks
            floorHeight = config.getInt("floorHeight", (Integer) SpaceConfig.Defaults.FLOOR_HEIGHT.getDefault()); // Floor height
            maxShellSize = config.getInt("maxShellSize", (Integer) SpaceConfig.Defaults.MAX_SHELL_SIZE.getDefault()); // Maximum shell thickness
            minShellSize = config.getInt("minShellSize", (Integer) SpaceConfig.Defaults.MIN_SHELL_SIZE.getDefault()); // Minimum shell thickness, should be at least 3
            floorBlock = Material.matchMaterial(config.getString("floorBlock", (String) SpaceConfig.Defaults.FLOOR_BLOCK.getDefault()));// BlockID for the floor
        } catch (IOException ex) {
            MessageHandler.debugPrint(Level.WARNING, "IOException when getting info for planets file for id "+ ID);
        } catch (InvalidConfigurationException ex) {
            MessageHandler.debugPrint(Level.WARNING, "InvalidConfigurationException when getting info for planets file for id "+ ID);
        } //Just use defaults if something goes wrong
         //Just use defaults if something goes wrong

    }
}
