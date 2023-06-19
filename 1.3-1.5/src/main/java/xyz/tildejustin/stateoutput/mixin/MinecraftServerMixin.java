package xyz.tildejustin.stateoutput.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import xyz.tildejustin.stateoutput.StateOutputHelper;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @ModifyArg(method = "prepareWorlds",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;logProgress(Ljava/lang/String;I)V"),
            index = 1
    )
    private int stateoutput$outputGenerationState(int worldProgress) {
        StateOutputHelper.outputState("generating," + worldProgress);
        return worldProgress;
    }
}
