package dynamiclabs.immersivefx.specialfx;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.World;
import net.minecraft.world.IWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;

import java.util.Map;
import java.util.HashMap;

import dynamiclabs.immersivefx.dsurround.DynamicSurroundings;

public class HungerSound {
	@Mod.EventBusSubscriber
	private static class GlobalTrigger {
		@SubscribeEvent
		public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
			if (event.phase == TickEvent.Phase.END) {
				Entity entity = event.player;
				World world = entity.level;
				double i = entity.getX();
				double j = entity.getY();
				double k = entity.getZ();
				Map<String, Object> dependencies = new HashMap<>();
				dependencies.put("x", i);
				dependencies.put("y", j);
				dependencies.put("z", k);
				dependencies.put("world", world);
				dependencies.put("entity", entity);
				dependencies.put("event", event);
				executeHungerSound(dependencies);
			}
		}
	}

	public static void executeHungerSound(Map<String, Object> dependencies) {
		IWorld world = (IWorld) dependencies.get("world");
		Entity entity = (Entity) dependencies.get("entity");
		if (((entity instanceof PlayerEntity) ? ((PlayerEntity) entity).getFoodData().getFoodLevel() : 0) < 8) {
			if (Math.random() < 0.005) {
				if (world instanceof World && !world.isClientSide()) {
					((World) world).playSound(null, new BlockPos((int) (entity.getX()), (int) (entity.getY()), (int) (entity.getZ())),
							(net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("immersivefx:hunger")),
							SoundCategory.PLAYERS, (float) 1, (float) 1);
				} else {
					((World) world).playLocalSound((entity.getX()), (entity.getY()), (entity.getZ()),
							(net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("immersivefx:hunger")),
							SoundCategory.PLAYERS, (float) 1, (float) 1, false);
				}
			}
		}
	}
}
