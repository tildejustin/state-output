package dev.tildejustin.stateoutput.mixin;

import dev.tildejustin.stateoutput.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @Shadow
    @Nullable
    public Screen currentScreen;

    @Dynamic
    @Inject(method = {"disconnect(Lnet/minecraft/client/gui/screen/Screen;)V", "method_18096(Lnet/minecraft/client/gui/screen/Screen;Z)V"}, at = @At("TAIL"), require = 1, allow = 1)
    private void outputWaitingState(CallbackInfo ci) {
        // We do this inject after this.player is set to null in the disconnect method.
        // This is because the inworld state output depends on the player being non-null,
        // so it makes more sense to set the state for exiting after the player becomes null.

        // While disconnect is intended for leaving a world, it may also occur before the first world creation,
        // hence the output "waiting" as opposed to "exiting"
        StateOutputHelper.outputState(State.WAITING);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void outputInWorldState(CallbackInfo ci) {
        // If there is no player, there is no world to be in
        if (this.player == null || this.currentScreen == ScreenHolder.screen) return;
        if (this.currentScreen == null) {
            StateOutputHelper.outputState(State.INGAME);
        } else if (this.currentScreen.shouldPause()) {
            StateOutputHelper.outputState(State.PAUSED);
        } else {
            StateOutputHelper.outputState(State.OPEN_SCREEN);
        }
    }
}
