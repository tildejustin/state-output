package xyz.tildejustin.stateoutput.mixin;

import net.minecraft.client.gui.screen.LevelLoadingScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.tildejustin.stateoutput.State;
import xyz.tildejustin.stateoutput.StateOutputHelper;

@Mixin(LevelLoadingScreen.class)
public abstract class LevelLoadingScreenMixin {
    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void outputStartGen(CallbackInfo ci) {
        StateOutputHelper.loadingProgress = 0;
        StateOutputHelper.outputState(State.GENERATING);
    }
}