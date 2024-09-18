package com.example.examplemod.proxy;

import com.example.examplemod.cities.CityManager;
import com.example.examplemod.events.ChunkEventHandler;
import com.example.examplemod.events.ServerChunkEventHandler;
import net.minecraftforge.common.MinecraftForge;
import com.example.examplemod.ExampleMod;

public class ProxyServer extends ProxyCommon {

    private CityManager cityManager;

    public ProxyServer(CityManager cityManager) {
        this.cityManager = cityManager;
    }

    @Override
    public void registerEventHandlers() {
        super.registerEventHandlers();
        if (cityManager != null) {
            // Регистрация серверных обработчиков событий
            MinecraftForge.EVENT_BUS.register(new ChunkEventHandler(cityManager));
            MinecraftForge.EVENT_BUS.register(new ServerChunkEventHandler(cityManager));
        } else {
            ExampleMod.LOGGER.warn("CityManager ещё не инициализирован. Не удалось зарегистрировать ChunkEventHandler и ServerChunkEventHandler.");
        }
    }


}
