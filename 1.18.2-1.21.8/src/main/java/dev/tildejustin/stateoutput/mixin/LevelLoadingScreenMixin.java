package dev.tildejustin.stateoutput.mixin;

import dev.tildejustin.stateoutput.*;
import net.minecraft.client.gui.screen.world.LevelLoadingScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelLoadingScreen.class)
public abstract class LevelLoadingScreenMixin {
    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void outputStartGen(CallbackInfo ci) {
        StateOutputHelper.outputState(State.GENERATING.withProgress(0));
    }
}