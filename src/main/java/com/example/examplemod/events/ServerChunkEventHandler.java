// src/main/java/com/example/examplemod/events/ServerChunkEventHandler.java

package com.example.examplemod.events;

import com.example.examplemod.cities.CityManager;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ServerChunkEventHandler {

    private final CityManager cityManager;

    public ServerChunkEventHandler(CityManager cityManager) {
        this.cityManager = cityManager;
    }

    @SubscribeEvent
    public void onChunkLoad(ChunkEvent.Load event) {
        World world = event.getWorld();
        if (!world.isRemote) { // Только на серверной стороне
            Chunk chunk = event.getChunk();
            // Логика при загрузке чанка
            cityManager.updateChunkOwnership(chunk, world);
        }
    }

    @SubscribeEvent
    public void onChunkUnload(ChunkEvent.Unload event) {
        World world = event.getWorld();
        if (!world.isRemote) { // Только на серверной стороне
            Chunk chunk = event.getChunk();
            // Логика при выгрузке чанка
            cityManager.removeChunkFromCityIfNeeded(chunk, world);
        }
    }
}
