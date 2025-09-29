package dev.tildejustin.stateoutput.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.tildejustin.stateoutput.ScreenHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.LevelLoadingScreen;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientChunkLoadProgress;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.listener.TickablePacketListener;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin extends ClientCommonNetworkHandler implements ClientPlayPacketListener, TickablePacketListener {
    protected ClientPlayNetworkHandlerMixin(MinecraftClient client, ClientConnection connection, ClientConnectionState connectionState) {
        super(client, connection, connectionState);
    }

    @ModifyArg(
            method = "startWorldLoading",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreenAndRender(Lnet/minecraft/client/gui/screen/Screen;)V")
    )
    private @Nullable Screen captureScreenInstance(@Nullable Screen screen) {
        ScreenHolder.screen = screen;
        return screen;
    }

    @WrapOperation(method = "startWorldLoading", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/world/LevelLoadingScreen;init(Lnet/minecraft/client/world/ClientChunkLoadProgress;Lnet/minecraft/client/gui/screen/world/LevelLoadingScreen$WorldEntryReason;)V"))
    private void captureScreenInstance2(LevelLoadingScreen instance, ClientChunkLoadProgress chunkLoadProgress, LevelLoadingScreen.WorldEntryReason reason, Operation<Void> original) {
        ScreenHolder.screen = instance;
        original.call(instance, chunkLoadProgress, reason);
    }
}
