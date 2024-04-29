package dev.tildejustin.stateoutput.mixin;

import dev.tildejustin.stateoutput.ScreenHolder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    @Dynamic
    @ModifyArg(
            // method_54133 -> startWorldLoading
            // class_434$class_9678 -> DownloadingTerrainScreen$WorldEntryReason, 1.20.5+
            method = { /* 1.18.2-1.20.2 */ "onGameJoin", /* 1.20.3-1.20.4 */ "startWorldLoading", /* 1.20.5+ */ "Lnet/minecraft/client/MinecraftClient;method_54133(Lnet/minecraft/client/network/ClientPlayerEntity;Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/class_434$class_9678;)V"},
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V"), require = 1, allow = 1
    )
    private @Nullable Screen captureScreenInstance(@Nullable Screen screen) {
        ScreenHolder.screen = screen;
        return screen;
    }
}
