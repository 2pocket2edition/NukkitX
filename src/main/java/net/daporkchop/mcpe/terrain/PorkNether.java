package net.daporkchop.mcpe.terrain;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.level.generator.noise.Simplex;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;
import net.daporkchop.mcpe.terrain.noise.NoiseGeneratorOctaves3D;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PorkNether extends Generator {
    private NukkitRandom nukkitRandom;
    private Random random;
    private ChunkManager level;
    private Simplex simplex1;
    private Simplex simplex2;

    public PorkNether() {
        this(new HashMap<>());
    }

    public PorkNether(Map<String, Object> options) {
        //Nothing here. Just used for future update.
    }

    @Override
    public int getId() {
        return TYPE_NETHER;
    }

    @Override
    public int getDimension() {
        return Level.DIMENSION_NETHER;
    }

    @Override
    public void init(ChunkManager level, NukkitRandom random) {
        this.level = level;
        this.nukkitRandom = random;
        this.random = new Random(level.getSeed());
        this.simplex1 = new Simplex(random, 16, 0.5, 1 / 64);
        this.simplex2 = new Simplex(random, 4, 0.5, 1 / 16);
    }

    @Override
    public void generateChunk(int chunkX, int chunkZ) {
        FullChunk chunk = level.getChunk(chunkX, chunkZ);
        int x = chunkX << 4;
        int z = chunkZ << 4;
        /*noise1 = gen1.generateNoiseArray(noise1,
                x, 0, z,
                16, 256, 16,
                684.412, 684.412 / 4, 684.412);*/
        double[][][] noise1 = getFastNoise3D(simplex1, 16, 128, 16, 4, 1, 4, x, 0, z);
        double[][][] noise2 = getFastNoise3D(simplex2, 16, 128, 16, 4, 4, 4, x, 0, z);
        for (int relX = 0; relX < 16; relX++) {
            for (int relZ = 0; relZ < 16; relZ++) {
                for (int relY = 1; relY < 128; relY++) {
                    if (noise1[relX][relZ][relY] - (noise2[relX][relZ][relY] / 4) < 0) {
                        chunk.setBlockId(relX, relY, relZ, Block.NETHERRACK);
                    }
                }
                chunk.setBlockId(relX, 0, relZ, Block.BEDROCK);
                chunk.setBlockId(relX, 255, relZ, Block.BEDROCK);
            }
        }
    }

    @Override
    public void populateChunk(int chunkX, int chunkZ) {

    }

    @Override
    public Map<String, Object> getSettings() {
        return null;
    }

    @Override
    public String getName() {
        return "porknether";
    }

    @Override
    public Vector3 getSpawn() {
        return new Vector3(0.5, 128, 0.5);
    }

    @Override
    public ChunkManager getChunkManager() {
        return level;
    }
}
