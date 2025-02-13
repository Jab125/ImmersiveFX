/*
 * Dynamic Surroundings
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

package dynamiclabs.immersivefx.dsurround.commands;

import dynamiclabs.immersivefx.dsurround.DynamicSurroundings;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.concurrent.ThreadTaskExecutor;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.common.Mod;
import dynamiclabs.immersivefx.dsurround.commands.dump.DumpCommand;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = DynamicSurroundings.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommandHelpers {
    private CommandHelpers() {

    }

    @SubscribeEvent
    public static void registerCommands(@Nonnull final RegisterCommandsEvent event) {
        // Only register if its an integrated server environment
        if (event.getEnvironment() == Commands.EnvironmentType.INTEGRATED) {
            DumpCommand.register(event.getDispatcher());
        }
    }

    public static void scheduleOnClientThread(Runnable runnable) {
        final ThreadTaskExecutor<?> scheduler = LogicalSidedProvider.WORKQUEUE.get(LogicalSide.CLIENT);
        scheduler.submitAsync(runnable);
    }

    public static void sendSuccess(@Nonnull final CommandSource source, @Nonnull final String command, @Nonnull String operation, @Nonnull String target) {
        final String key = String.format("command.dsurround.%s.success", command);
        source.sendSuccess(new TranslationTextComponent(key, operation, target), true);
    }

    public static void sendFailure(@Nonnull final CommandSource source, @Nonnull final String command) {
        final String key = String.format("command.dsurround.%s.failure", command);
        source.sendSuccess(new TranslationTextComponent(key), true);
    }
}
