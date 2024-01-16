package dev.tildejustin.stateoutput.mixin;

import dev.tildejustin.stateoutput.StateOutputHelper;
import net.minecraft.server.WorldGenerationProgressLogger;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldGenerationProgressLogger.class)
public abstract class WorldGenerationProgressLoggerMixin {
    @Shadow
    public abstract int getProgressPercentage();

    @Inject(method = "setChunkStatus", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;)V", shift = At.Shift.BEFORE))
    private void outputGenerationState(CallbackInfo ci) {
        // Using the getProgressPercentage to recalculate is slightly unoptimized but prevents needing to do locals capture, making it easier to port this mixin.
        StateOutputHelper.updateLastState(state -> state.withProgress(MathHelper.clamp(getProgressPercentage(), 0, 100)));
    }
}
