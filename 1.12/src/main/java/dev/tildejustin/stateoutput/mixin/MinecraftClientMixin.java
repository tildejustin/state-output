package dev.tildejustin.stateoutput.mixin;

import dev.tildejustin.stateoutput.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow
    @Nullable
    public Screen currentScreen;

    @Shadow
    public ClientPlayerEntity player;

    @Shadow
    private volatile boolean paused;

    @Inject(method = "method_0_2253", slice = @Slice(from = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/level/storage/LevelStorage;method_245()V")), at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;player:Lnet/minecraft/client/network/ClientPlayerEntity;"))
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
        if (this.player == null || this.currentScreen instanceof TitleScreen || (this.currentScreen instanceof DownloadingTerrainScreen && !StateOutputHelper.inWorld())) return;
        if (this.currentScreen == null && !this.paused) {
            StateOutputHelper.outputState(State.INGAME);
        } else if (this.paused || this.currentScreen instanceof GameMenuScreen) {
            StateOutputHelper.outputState(State.PAUSED);
        } else {
            StateOutputHelper.outputState(State.OPEN_SCREEN);
        }
    }

    @Inject(method = "startIntegratedServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_0_675;method_15412(Ljava/lang/String;)V", shift = At.Shift.AFTER))
    public void outputStartGen(CallbackInfo ci) {
        StateOutputHelper.outputState(State.GENERATING.withProgress(0));
    }
}
