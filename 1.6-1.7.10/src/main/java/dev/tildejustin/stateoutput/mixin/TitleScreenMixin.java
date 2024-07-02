package dev.tildejustin.stateoutput.mixin;

import dev.tildejustin.stateoutput.*;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This mixin is to state output when the title screen loads.
 */
@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin {
    // We inject into the first tick rather than the tail of init because the tail of init will still load even if Atum is resetting.
    @Dynamic
    @Inject(method = {"tick", "Lnet/minecraft/class_388;method_21936()V"}, at = @At("TAIL"), require = 1)
    private void outputTitleState(CallbackInfo ci) {
        StateOutputHelper.outputState(State.TITLE);
    }
}
