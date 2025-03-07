/*
 *  Dynamic Surroundings: Environs
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

package dynamiclabs.immersivefx.environs.effects.particles;

import net.minecraft.block.BlockState;
import net.minecraft.client.particle.DiggingParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import dynamiclabs.immersivefx.lib.WorldUtils;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@OnlyIn(Dist.CLIENT)
public class DustParticle extends DiggingParticle {

	private final BlockPos.Mutable pos = new BlockPos.Mutable();

	public DustParticle(final World world, final double x, final double y, final double z, final BlockState state) {
		this(world, x, y, z, 0, 0, 0, state);
	}

	public DustParticle(final World world, final double x, final double y, final double z, final double dX, final double dY, final double dZ, final BlockState state) {
		super((ClientWorld) world, x, y, z, 0, 0, 0, state);

		this.hasPhysics = false;
		this.xd = dX;
		this.yd = dY;
		this.zd = dZ;

		scale((float) (0.3F + this.random.nextGaussian() / 30.0F));
		setPos(this.x, this.y, this.z);
	}

	@Override
	public void move(final double dX, final double dY, final double dZ) {
		this.x += dX;
		this.y += dY;
		this.z += dZ;
	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		this.yd -= 0.04D * this.gravity;
		move(this.xd, this.yd, this.zd);
		this.xd *= 0.9800000190734863D;
		this.yd *= 0.9800000190734863D;
		this.zd *= 0.9800000190734863D;

		this.pos.set(this.x, this.y, this.z);

		if (this.lifetime-- <= 0) {
			remove();
		} else if (WorldUtils.isBlockSolid(this.level, this.pos)) {
			remove();
		}
	}
}