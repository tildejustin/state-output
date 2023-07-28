package xyz.tildejustin.stateoutput.mixin;

import net.minecraft.server.WorldGenerationProgressLogger;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.tildejustin.stateoutput.StateOutputHelper;

@Mixin(WorldGenerationProgressLogger.class)
public abstract class WorldGenerationProgressLoggerMixin {
    @Shadow
    public abstract int getProgressPercentage();

    @Inject(method = "setChunkStatus", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;info(Ljava/lang/String;)V", shift = At.Shift.BEFORE))
    private void outputGenerationState(CallbackInfo ci) {
        // Using the getProgressPercentage to recalculate is slightly unoptimized but prevents needing to do locals capture, making it easier to port this mixin.
        StateOutputHelper.loadingProgress = MathHelper.clamp(getProgressPercentage(), 0, 100);
        StateOutputHelper.outputLastState();
    }
}
