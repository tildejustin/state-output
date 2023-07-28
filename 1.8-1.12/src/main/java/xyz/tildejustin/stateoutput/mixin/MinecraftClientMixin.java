package xyz.tildejustin.stateoutput.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.ClientPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.tildejustin.stateoutput.State;
import xyz.tildejustin.stateoutput.StateOutputHelper;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow
    @Nullable
    public Screen currentScreen;

    @Shadow
    public ClientPlayerEntity player;

    @Shadow
    private volatile boolean paused;

    @Inject(method = "connect(Lnet/minecraft/client/world/ClientWorld;Ljava/lang/String;)V", slice = @Slice(from = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/level/storage/LevelStorageAccess;clearAll()V")), at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;player:Lnet/minecraft/entity/player/ClientPlayerEntity;"))
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
        if (this.player == null) return;
        if (this.currentScreen == null && !this.paused) {
            StateOutputHelper.outputState(State.INGAME);
        } else if (this.paused || this.currentScreen instanceof GameMenuScreen) {
            StateOutputHelper.outputState(State.PAUSED);
        } else {
            StateOutputHelper.outputState(State.OPEN_SCREEN);
        }
    }

    @Inject(method = "startIntegratedServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/LoadingScreenRenderer;setTitle(Ljava/lang/String;)V", shift = At.Shift.AFTER))
    public void outputStartGen(CallbackInfo ci) {
        StateOutputHelper.loadingProgress = 0;
        StateOutputHelper.outputState(State.GENERATING);
    }
}
