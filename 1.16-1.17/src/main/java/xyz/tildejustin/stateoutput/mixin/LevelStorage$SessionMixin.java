package xyz.tildejustin.stateoutput.mixin;

import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelStorage.Session.class)
public abstract class LevelStorage$SessionMixin {
    @Redirect(
            method = "getIconFile",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/storage/LevelStorage$Session;checkValid()V"
            )
    )
    private void ignoreCheckValid(LevelStorage.Session instance) {
    }
}
