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

package dynamiclabs.immersivefx.mobeffects.effects;

import dynamiclabs.immersivefx.lib.GameUtils;
import dynamiclabs.immersivefx.lib.TickCounter;
import dynamiclabs.immersivefx.mobeffects.MobEffects;
import dynamiclabs.immersivefx.mobeffects.library.ItemData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import dynamiclabs.immersivefx.mobeffects.library.ItemLibrary;
import dynamiclabs.immersivefx.sndctrl.api.effects.AbstractEntityEffect;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = MobEffects.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntitySwingEffect extends AbstractEntityEffect {

    // Grace for time between the right click being triggered and when the effect update happens
    private static final long RIGHT_CLICK_GRACE = 5;

    private static final ResourceLocation NAME = new ResourceLocation(MobEffects.MOD_ID, "swing");
    public static final FactoryHandler FACTORY = new FactoryHandler(
            EntitySwingEffect.NAME,
            entity -> new EntitySwingEffect());

    private static long lastRightClick;
    protected int swingProgress = 0;
    protected boolean isSwinging = false;

    public EntitySwingEffect() {
        super(NAME);
    }

    @Override
    public void update() {

        final LivingEntity entity = getEntity();

        // Boats are strange - ignore them for now
        if (entity.getVehicle() instanceof BoatEntity)
            return;

        // Is the swing in motion
        if (entity.swingTime > this.swingProgress && entity.swingingArm != null) {
            if (!this.isSwinging) {
                if (isClickOK(entity)) {
                    if (freeSwing(entity)) {
                        final ItemStack currentItem = entity.getItemInHand(entity.swingingArm);
                        final ItemData data = ItemLibrary.getItemData(currentItem);
                        if (isActivePlayer(entity))
                            data.playSwingSound();
                        else
                            data.playSwingSound(entity.blockPosition());
                    }
                }
            }

            this.isSwinging = true;

        } else {
            this.isSwinging = false;
        }

        this.swingProgress = entity.swingTime;
    }

    protected static boolean freeSwing(@Nonnull final LivingEntity entity) {
        final BlockRayTraceResult result = rayTraceBlock(entity);
        return result.getType() == RayTraceResult.Type.MISS;
    }

    protected static double getReach(@Nonnull final LivingEntity entity) {
        if (entity instanceof PlayerEntity)
            return entity.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue();

        // From EntityAIAttackMelee::getAttackReachSqr - approximate
        return entity.getBbWidth() * 2F + 0.6F; // 0.6 == default entity width
    }

    protected static BlockRayTraceResult rayTraceBlock(@Nonnull final LivingEntity entity) {
        double range = getReach(entity);
        final Vector3d eyes = entity.getEyePosition(1F);
        final Vector3d look = entity.getViewVector(1F);
        final Vector3d rangedLook = eyes.add(look.x * range, look.y * range, look.z * range);
        return entity.getCommandSenderWorld().clip(new RayTraceContext(eyes, rangedLook, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.SOURCE_ONLY, entity));
    }

    private boolean isClickOK(@Nonnull final LivingEntity entity) {
        return entity == GameUtils.getPlayer() && (lastRightClick < (TickCounter.getTickCount() - RIGHT_CLICK_GRACE));
    }

    @SubscribeEvent
    public static void onRightClick(@Nonnull final PlayerInteractEvent.RightClickBlock event) {
        if (event.getSide() == LogicalSide.CLIENT) {
            lastRightClick = TickCounter.getTickCount();
        }
    }

}