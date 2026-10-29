package rendersnap.star.end.mixin.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rendersnap.star.end.client.Compat;
import rendersnap.star.end.client.McCompat;
import rendersnap.star.end.client.menu.SnapHubPg;
import rendersnap.star.end.client.menu.VideoPg;

import java.util.function.Supplier;

@Mixin(OptionsScreen.class)
public abstract class OB extends Screen {
    @Shadow @Final
    private static Component VIDEO;

    @Shadow @Final
    private Options options;

    private OB(Component title) {
        super(title);
    }

    /**
     * When Sodium / Iris own Video Settings, keep their screen and expose Rendersnap's
     * settings through a separate button instead of replacing the Video button.
     */
    @Inject(method = "init", at = @At("TAIL"), require = 0)
    private void rendersnap$snapButton(CallbackInfo ci) {
        if (!Compat.hasCustomVideoUi()) return;

        this.addRenderableWidget(Button.builder(
                        Component.translatable("rendersnap.screen.rendersnap"),
                        btn -> {
                            Minecraft mc = this.minecraft;
                            if (mc != null) {
                                McCompat.setScreen(mc, new SnapHubPg((Screen)(Object)this, this.options));
                            }
                        })
                .bounds(this.width - 106, 6, 100, 20)
                .build());
    }

    @Inject(method = "openScreenButton", at = @At("HEAD"), cancellable = true)
    private void videoButton(Component label, Supplier<Screen> vanilla, CallbackInfoReturnable<Button> cir) {
        if (!VIDEO.equals(label)) return;
        // Sodium / Iris build their own Video Settings screen; don't replace it with VideoPg.
        if (Compat.hasCustomVideoUi()) return;

        cir.setReturnValue(Button.builder(label, btn -> {
            Minecraft mc = this.minecraft;
            if (mc != null) {
                McCompat.setScreen(mc, new VideoPg((Screen)(Object)this, mc, this.options));
            }
        }).build());
    }
}
