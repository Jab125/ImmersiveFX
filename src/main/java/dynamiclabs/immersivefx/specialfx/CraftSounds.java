package dynamiclabs.immersivefx.specialfx;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import net.minecraft.world.World;
import net.minecraft.world.IWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.Entity;

import java.util.Map;
import java.util.HashMap;

import dynamiclabs.immersivefx.dsurround.DynamicSurroundings;

public class CraftSounds {
	@Mod.EventBusSubscriber
	private static class GlobalTrigger {
		@SubscribeEvent
		public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
			Entity entity = event.getPlayer();
			World world = entity.level;
			double i = entity.getX();
			double j = entity.getY();
			double k = entity.getZ();
			ItemStack itemStack = event.getCrafting();
			Map<String, Object> dependencies = new HashMap<>();
			dependencies.put("x", i);
			dependencies.put("y", j);
			dependencies.put("z", k);
			dependencies.put("world", world);
			dependencies.put("entity", entity);
			dependencies.put("itemstack", itemStack);
			dependencies.put("event", event);
			executeCraftSound(dependencies);
		}
	}

	public static void executeCraftSound(Map<String, Object> dependencies) {

		IWorld world = (IWorld) dependencies.get("world");
		double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
		double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
		double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
		if (world instanceof World && !world.isClientSide()) {
			((World) world).playSound(null, new BlockPos((int) x, (int) y, (int) z),
					(net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("immersivefx:crafting")),
					SoundCategory.BLOCKS, (float) 0.5, (float) 1);
		} else {
			((World) world).playLocalSound(x, y, z,
					(net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("immersivefx:crafting")),
					SoundCategory.BLOCKS, (float) 0.5, (float) 1, false);
		}
	}
}
