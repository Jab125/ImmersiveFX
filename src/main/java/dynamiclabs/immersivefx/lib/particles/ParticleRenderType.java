/*
 * Dynamic Surroundings: Sound Control
 * Copyright (C) 2019  OreCruncher
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

import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class ParticleRenderType implements IParticleRenderType {

    private final ResourceLocation texture;

    public ParticleRenderType(@Nonnull final ResourceLocation texture) {
        this.texture = texture;
    }

    @Nonnull
    protected VertexFormat getVertexFormat() {
        return DefaultVertexFormats.PARTICLE;
    }

    @Override
    public void begin(@Nonnull final BufferBuilder buffer, @Nonnull final TextureManager textureManager) {
        RenderHelper.turnOff();
        textureManager.bind(getTexture());
        buffer.begin(GL11.GL_QUADS, getVertexFormat());
    }

    protected ResourceLocation getTexture() {
        return this.texture;
    }

    @Override
    public void end(@Nonnull final Tessellator tessellator) {
        tessellator.end();
    }

    @Override
    public String toString() {
        return this.texture.toString();
    }
}
