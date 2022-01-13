package dynamiclabs.immersivefx.specialfx;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;

import net.minecraft.world.World;
import net.minecraft.world.IWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;

import java.util.Map;
import java.util.HashMap;

import dynamiclabs.immersivefx.dsurround.DynamicSurroundings;

public class DropItemSounds {
	@Mod.EventBusSubscriber
	private static class GlobalTrigger {
		@SubscribeEvent
		public static void onGemDropped(ItemTossEvent event) {
			PlayerEntity entity = event.getPlayer();
			double i = entity.getX();
			double j = entity.getY();
			double k = entity.getZ();
			World world = entity.level;
			ItemStack itemstack = event.getEntityItem().getItem();
			Map<String, Object> dependencies = new HashMap<>();
			dependencies.put("x", i);
			dependencies.put("y", j);
			dependencies.put("z", k);
			dependencies.put("world", world);
			dependencies.put("entity", entity);
			dependencies.put("itemstack", itemstack);
			dependencies.put("event", event);
			executeDropItemSound(dependencies);
		}
	}

	public static void executeDropItemSound(Map<String, Object> dependencies) {

		IWorld world = (IWorld) dependencies.get("world");
		Entity entity = (Entity) dependencies.get("entity");
		if (world instanceof World && !world.isClientSide()) {
			((World) world).playSound(null, new BlockPos((int) (entity.getX()), (int) (entity.getY()), (int) (entity.getZ())),
					(net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("immersivefx:throwitem")),
					SoundCategory.BLOCKS, (float) 1.00, (float) 1);
		} else {
			((World) world).playLocalSound((entity.getX()), (entity.getY()), (entity.getZ()),
					(net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("immersivefx:throwitem")),
					SoundCategory.BLOCKS, (float) 1.00, (float) 1, false);
		}
	}
}
