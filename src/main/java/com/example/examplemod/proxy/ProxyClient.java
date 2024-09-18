package com.example.examplemod.proxy;

import com.example.examplemod.cities.CityManager;
import com.example.examplemod.cities.render.CityBorderRenderer;
import net.minecraftforge.common.MinecraftForge;

public class ProxyClient extends ProxyCommon {

    @Override
    public void registerEventHandlers() {
        super.registerEventHandlers();
        // Регистрация обработчиков событий только на клиенте
        MinecraftForge.EVENT_BUS.register(new CityBorderRenderer(CityManager.getInstance()));
    }



}
