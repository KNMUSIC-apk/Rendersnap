package rendersnap.star.end.client.menu;

import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;
import rendersnap.star.end.client.McCompat;

/**
 * Entry point for Rendersnap's own settings when another mod (Sodium, Iris) owns the
 * Video Settings screen. Lists the same Performance / Visuals pages that VideoPg links to.
 */
public final class SnapHubPg extends OptionsSubScreen {
    private static final Component TITLE = Component.translatable("rendersnap.group.snap");

    public SnapHubPg(Screen last, Options opts) {
        super(last, opts, TITLE);
    }

    @Override
    protected void addOptions() {
        this.list.addSmall(
                pageButton("rendersnap.screen.optimization", SnapPg.Tab.OPTIMIZATION),
                pageButton("rendersnap.screen.effects", SnapPg.Tab.EFFECTS)
        );
    }

    private Button pageButton(String key, SnapPg.Tab tab) {
        return Button.builder(
                        Component.translatable(key),
                        button -> McCompat.setScreen(this.minecraft, new SnapPg(this, this.minecraft, this.options, tab))
                )
                .bounds(0, 0, 150, 20)
                .build();
    }
}
