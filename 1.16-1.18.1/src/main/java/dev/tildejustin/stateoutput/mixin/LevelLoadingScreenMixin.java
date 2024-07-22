package dev.tildejustin.stateoutput.mixin;

import dev.tildejustin.stateoutput.*;
import net.minecraft.client.gui.screen.LevelLoadingScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelLoadingScreen.class)
public abstract class LevelLoadingScreenMixin {
    @SuppressWarnings("ConstantValue")
    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void outputStartGen(CallbackInfo ci) {
        if (this.getClass() != LevelLoadingScreenMixin.class) {
            // SeedQueue extends this class
            return;
        }
        StateOutputHelper.outputState(State.GENERATING.withProgress(0));
    }
}
