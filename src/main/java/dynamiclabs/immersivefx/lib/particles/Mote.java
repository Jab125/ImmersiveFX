/*
 * Dynamic Surroundings: Sound Control
 * Copyright (C) 2020  OreCruncher
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>
 */

package dynamiclabs.immersivefx.lib.particles;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import dynamiclabs.immersivefx.lib.GameUtils;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public abstract class Mote implements IParticleMote {

    protected final IBlockReader world;
    protected final IWorldReader lighting;

    protected boolean isAlive = true;
    protected double posX;
    protected double posY;
    protected double posZ;
    protected final BlockPos.Mutable position = new BlockPos.Mutable();

    protected int packedLighting;

    protected float red;
    protected float green;
    protected float blue;
    protected float alpha;

    public Mote(@Nonnull final IBlockReader world, final double x, final double y, final double z) {
        this.world = world;
        this.lighting = world instanceof IWorldReader ? (IWorldReader) world : GameUtils.getWorld();
        setPosition(x, y, z);
        configureColor();
    }

    public void setPosition(final double posX, final double posY, final double posZ) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.position.set(posX, posY, posZ);
    }

    @Nonnull
    public Vector3d getPosition() {
        return new Vector3d(this.posX, this.posY, this.posZ);
    }

    public void configureColor() {
        this.red = this.green = this.blue = this.alpha = 1F;
    }

    @Override
    public boolean isAlive() {
        return this.isAlive;
    }

    @Override
    public void kill() {
        this.isAlive = false;
    }

    @Override
    public boolean tick() {
        if (isAlive()) {

            update();

            // The update() may have killed the mote
            if (isAlive()) {
                setPosition(this.posX, this.posY, this.posZ);
                updateBrightness();
            }
        }
        return isAlive();
    }

    protected void update() {

    }

    public void updateBrightness() {
        this.packedLighting = WorldRenderer.getLightColor(this.lighting, this.position);
    }

    protected final double interpX(ActiveRenderInfo info) {
        return info.getPosition().x;
    }

    protected final double interpY(ActiveRenderInfo info) {
        return info.getPosition().y;
    }

    protected final double interpZ(ActiveRenderInfo info) {
        return info.getPosition().z;
    }

    protected float renderX(ActiveRenderInfo info, final float partialTicks) {
        return (float) (this.posX - interpX(info));
    }

    protected float renderY(ActiveRenderInfo info, final float partialTicks) {
        return (float) (this.posY - interpY(info));
    }

    protected float renderZ(ActiveRenderInfo info, final float partialTicks) {
        return (float) (this.posZ - interpZ(info));
    }

    protected void drawVertex(final IVertexBuilder buffer, final double x, final double y, final double z,
                              final float u, final float v) {
        buffer
                .vertex(x, y, z)
                .uv(u, v)
                .color(this.red, this.green, this.blue, this.alpha)
                .uv2(this.packedLighting)
                .endVertex();
    }

    public abstract void renderParticle(@Nonnull IVertexBuilder buffer, @Nonnull ActiveRenderInfo renderInfo, float partialTicks);
}