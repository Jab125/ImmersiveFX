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

import dynamiclabs.immersivefx.mobeffects.MobEffects;
import dynamiclabs.immersivefx.mobeffects.library.ItemData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import dynamiclabs.immersivefx.mobeffects.library.ItemLibrary;
import dynamiclabs.immersivefx.sndctrl.api.effects.AbstractEntityEffect;

@OnlyIn(Dist.CLIENT)
public class EntityBowEffect extends AbstractEntityEffect {

    private static final ResourceLocation NAME = new ResourceLocation(MobEffects.MOD_ID, "bow");
    public static final FactoryHandler FACTORY = new FactoryHandler(
            EntityBowEffect.NAME,
            entity -> new EntityBowEffect());

    protected ItemStack lastActiveStack = ItemStack.EMPTY;

    public EntityBowEffect() {
        super(NAME);
    }

    @Override
    public void update() {
        final LivingEntity entity = getEntity();
        final ItemStack currentStack = entity.getUseItem();
        if (!currentStack.isEmpty()) {
            if (!ItemStack.matches(currentStack, this.lastActiveStack)) {
                final ItemData data = ItemLibrary.getItemData(currentStack);
                if (data == ItemLibrary.BOW || data == ItemLibrary.CROSSBOW || data == ItemLibrary.SHIELD) {
                    if (isActivePlayer(entity))
                        data.playUseSound();
                    else
                        data.playUseSound(entity.blockPosition());
                }

                this.lastActiveStack = currentStack;
            }

        } else {
            this.lastActiveStack = ItemStack.EMPTY;
        }
    }

}