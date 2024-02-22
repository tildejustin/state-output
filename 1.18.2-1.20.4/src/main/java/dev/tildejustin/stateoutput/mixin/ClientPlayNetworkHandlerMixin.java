package dev.tildejustin.stateoutput.mixin;

import dev.tildejustin.stateoutput.ScreenHolder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    @ModifyArg(
            method = { /* 1.18.2-1.20.2 */ "onGameJoin", /* 1.20.3+ */ "startWorldLoading"},
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V"), require = 1, allow = 1
    )
    private @Nullable Screen captureScreenInstance(@Nullable Screen screen) {
        ScreenHolder.screen = screen;
        return screen;
    }
}
