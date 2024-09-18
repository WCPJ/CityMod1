// src/main/java/com/example/examplemod/cities/render/CityBorderRenderer.java
package com.example.examplemod.cities.render;

import com.example.examplemod.cities.City;
import com.example.examplemod.cities.CityManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CityBorderRenderer {

    private final CityManager cityManager;

    public CityBorderRenderer(CityManager cityManager) {
        this.cityManager = cityManager;
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        // Настройка OpenGL
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771); // GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA
        GlStateManager.color(0.0F, 1.0F, 0.0F, 0.5F); // Зеленый цвет с прозрачностью

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        // Получаем координаты камеры
        double cameraX = mc.getRenderManager().viewerPosX;
        double cameraY = mc.getRenderManager().viewerPosY;
        double cameraZ = mc.getRenderManager().viewerPosZ;

        for (City city : cityManager.getCities().values()) {
            for (String chunkKey : city.getChunks()) {
                String[] parts = chunkKey.split(":");
                int dimension = Integer.parseInt(parts[0]);
                int x = Integer.parseInt(parts[1]);
                int z = Integer.parseInt(parts[2]);

                // Проверяем, находится ли чанк в текущем измерении
                if (dimension != mc.player.dimension) continue;

                // Определяем координаты чанка
                int chunkX = x << 4;
                int chunkZ = z << 4;

                // Создаем AxisAlignedBB для чанка
                AxisAlignedBB boundingBox = new AxisAlignedBB(
                        chunkX - cameraX,
                        0 - cameraY,
                        chunkZ - cameraZ,
                        chunkX + 16 - cameraX,
                        256 - cameraY,
                        chunkZ + 16 - cameraZ
                );

                // Начинаем отрисовку линий
                buffer.begin(3, net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION_COLOR);

                // Отрисовка границ чанка
                drawBoundingBox(buffer, boundingBox);

                tessellator.draw();
            }
        }

        // Восстанавливаем настройки OpenGL
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
    }

    private void drawBoundingBox(BufferBuilder buffer, AxisAlignedBB boundingBox) {
        // Рисуем линии для границы чанка
        // Нижняя граница
        buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(0, 1, 0, 255).endVertex();
        buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(0, 1, 0, 255).endVertex();

        buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(0, 1, 0, 255).endVertex();
        buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(0, 1, 0, 255).endVertex();

        buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(0, 1, 0, 255).endVertex();
        buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(0, 1, 0, 255).endVertex();

        buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(0, 1, 0, 255).endVertex();
        buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(0, 1, 0, 255).endVertex();

        // Верхняя граница
        buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(0, 1, 0, 255).endVertex();
        buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(0, 1, 0, 255).endVertex();

        buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(0, 1, 0, 255).endVertex();
        buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(0, 1, 0, 255).endVertex();

        buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(0, 1, 0, 255).endVertex();
        buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(0, 1, 0, 255).endVertex();

        buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(0, 1, 0, 255).endVertex();
        buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(0, 1, 0, 255).endVertex();

        // Вертикальные линии
        buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(0, 1, 0, 255).endVertex();
        buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(0, 1, 0, 255).endVertex();

        buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(0, 1, 0, 255).endVertex();
        buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(0, 1, 0, 255).endVertex();

        buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(0, 1, 0, 255).endVertex();
        buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(0, 1, 0, 255).endVertex();

        buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(0, 1, 0, 255).endVertex();
        buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(0, 1, 0, 255).endVertex();
    }
}
