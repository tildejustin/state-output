package dev.tildejustin.stateoutput.mixin.accessor;

import net.minecraft.client.network.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientPlayNetworkHandler.class)
public interface ClientPlayNetworkHandlerAccessor {
    @Accessor
    WorldLoadingState getWorldLoadingState();
}
