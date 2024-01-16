package dev.tildejustin.stateoutput.mixin;

import dev.tildejustin.stateoutput.StateOutputHelper;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @ModifyArg(method = "prepareWorlds", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;logProgress(Ljava/lang/String;I)V"), index = 1)
    private int outputGenerationState(int worldProgress) {
        StateOutputHelper.updateLastState(state -> state.withProgress(worldProgress));
        return worldProgress;
    }
}
