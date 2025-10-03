package dev.tildejustin.stateoutput.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.tildejustin.stateoutput.StateOutputHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.DeltaChunkLoadProgress;
import net.minecraft.world.chunk.LoggingChunkLoadProgress;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LoggingChunkLoadProgress.class)
public abstract class LoggingChunkLoadProgressMixin {
    @Shadow
    @Final
    private DeltaChunkLoadProgress delegate;

    @WrapOperation(method = "progress", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;info(Ljava/lang/String;)V", remap = false))
    private void outputGenerationState(Logger logger, String s, Operation<Void> original) {
        // Using the getLoadProgress to recalculate is slightly unoptimized but prevents needing to do locals capture, making it easier to port this mixin.#
        // WrapOperation is used for compatibility with SeedQueue muting background loggers
        StateOutputHelper.updateLastState(state -> state.withProgress(MathHelper.clamp(MathHelper.floor(this.delegate.getLoadProgress() * 100.0F), 0, 100)));
        original.call(logger, s);
    }
}
