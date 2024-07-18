package dev.tildejustin.stateoutput.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import dev.tildejustin.stateoutput.StateOutputHelper;
import net.minecraft.server.WorldGenerationProgressLogger;
import net.minecraft.util.math.MathHelper;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WorldGenerationProgressLogger.class)
public abstract class WorldGenerationProgressLoggerMixin {
    @Shadow
    public abstract int getProgressPercentage();

    @WrapOperation(method = "setChunkStatus", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;)V"))
    private void outputGenerationState(Logger logger, String s, Operation<Void> original) {
        // Using the getProgressPercentage to recalculate is slightly unoptimized but prevents needing to do locals capture, making it easier to port this mixin.#
        // WrapOperation is used for compatibility with SeedQueue muting background loggers
        StateOutputHelper.updateLastState(state -> state.withProgress(MathHelper.clamp(getProgressPercentage(), 0, 100)));
        original.call(logger, s);
    }
}
