package xyz.tildejustin.stateoutput.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.tildejustin.stateoutput.StateOutputHelper;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @Shadow
    @Nullable
    public Screen currentScreen;

    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V", at = @At("TAIL"))
    private void stateoutput$outputWaitingState(Screen screen, CallbackInfo info) {
        // We do this inject after this.player is set to null in the disconnect method.
        // This is because the inworld state output depends on the player being non-null,
        // so it makes more sense to set the state for exiting after the player becomes null.

        // While disconnect is intended for leaving a world, it may also occur before the first world creation,
        // hence the output "waiting" as opposed to "exiting"
        StateOutputHelper.outputState("waiting");
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void stateoutput$outputInWorldState(CallbackInfo info) {
        // If there is no player, there is no world to be in
        if (this.player == null) return;
        if (this.currentScreen == null) {
            StateOutputHelper.outputState("inworld,unpaused");
        } else if (this.currentScreen.isPauseScreen()) {
            StateOutputHelper.outputState("inworld,paused");
        } else {
            StateOutputHelper.outputState("inworld,gamescreenopen");
        }
    }
}
