package xyz.tildejustin.stateoutput.mixin;

import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.tildejustin.stateoutput.State;
import xyz.tildejustin.stateoutput.StateOutputHelper;

/**
 * This mixin is to state output when the title screen loads.
 */
@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin {
    // We inject into the first tick rather than the tail of init because the tail of init will still load even if Atum is resetting.
    @Inject(method = "tick", at = @At("HEAD"))
    private void outputTitleState(CallbackInfo ci) {
        StateOutputHelper.outputState(State.TITLE);
    }
}
