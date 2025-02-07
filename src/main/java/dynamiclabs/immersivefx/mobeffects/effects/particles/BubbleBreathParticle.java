/*
 *  Dynamic Surroundings: Mob Effects
 *  Copyright (C) 2019  OreCruncher
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
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>
 */

package dynamiclabs.immersivefx.mobeffects.effects.particles;

import dynamiclabs.immersivefx.lib.GameUtils;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class BubbleBreathParticle  extends SpriteTexturedParticle {
    public BubbleBreathParticle(@Nonnull final LivingEntity entity, final boolean isDrowning) {
        super((ClientWorld) entity.getCommandSenderWorld(), 0, 0, 0);

        // Reuse the bubble sheet
        final IAnimatedSprite spriteSet = GameUtils.getMC().particleEngine.spriteSets.get(ParticleTypes.BUBBLE.getRegistryName());
        this.pickSprite(spriteSet);

        final Vector3d origin = ParticleUtils.getBreathOrigin(entity);
        final Vector3d trajectory = ParticleUtils.getLookTrajectory(entity);
        final double factor = isDrowning ? 0.02D : 0.005D;

        this.setPos(origin.x, origin.y, origin.z);
        this.xo = origin.x;
        this.yo = origin.y;
        this.zo = origin.z;

        this.xd = trajectory.x * factor;
        this.yd = trajectory.y * 0.002D;
        this.zd = trajectory.z * factor;

        this.gravity = 0F;

        this.setAlpha(0.2F);
        this.setSize(0.02F, 0.02F);
        this.quadSize *= this.random.nextFloat() * 0.6F + 0.2F;
        this.quadSize *= entity.isBaby() ? 0.125F : 0.25F;
        this.lifetime = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.lifetime-- <= 0) {
            this.remove();
        } else {
            this.yd += 0.002D;
            this.move(this.xd, this.yd, this.zd);
            this.xd *= 0.8500000238418579D;
            this.yd *= 0.8500000238418579D;
            this.zd *= 0.8500000238418579D;
            if (!this.level.getFluidState(new BlockPos(this.x, this.y, this.z)).is(FluidTags.WATER)) {
                this.remove();
            }
        }
    }

    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}
