package me.hp20.giz5;

import me.hp20.giz5.options.Giz5Options;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.DoubleOption;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Option;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

public class Giz5OptionsScreen extends GameOptionsScreen {

    private static final Option[] options;

    private static Giz5Options options() {
        return Giz5Mod.getOptions();
    }

    static {
        options = new Option[]{
                CyclingOption.create("giz5.options.fullBright", p -> options().fullBright, (g, o, t) -> options().fullBright = t),
                CyclingOption.create("giz5.options.fastSneak", p -> options().fastSneak, (g, o, t) -> options().fastSneak = t),
                CyclingOption.create("giz5.options.toggleSprint", p -> options().toggleSprint, (g, o, t) -> options().toggleSprint = t),
                CyclingOption.create("giz5.options.noHurtBobbing", p -> options().noHurtBobbing, (g, o, t) -> options().noHurtBobbing = t),
                CyclingOption.create("giz5.options.shiftFix", p -> options().shiftFix, (g, o, t) -> options().shiftFix = t),
                CyclingOption.create("giz5.options.ignoreCooldown", p -> options().ignoreCooldown, (g, o, t) -> options().ignoreCooldown = t),
                CyclingOption.create("giz5.options.softSneak", p -> options().softSneak, (g, o, t) -> options().softSneak = t),
                CyclingOption.create("giz5.options.forceIcon", p -> options().forceIcon, (g, o, t) -> options().forceIcon = t),
                CyclingOption.create("giz5.options.antiSwim", p -> options().antiSwim, (g, o, t) -> options().antiSwim = t),
                new DoubleOption("giz5.options.lowFire", 0, 100, 1, p -> options().lowFire, (s, b) -> options().lowFire = b,
                        (s, t) -> new TranslatableText("giz5.options.lowFire", String.format("%.0f", options().lowFire)))
        };
    }

    public Giz5OptionsScreen(Screen parent, GameOptions gameOptions) {
        super(parent, gameOptions, new TranslatableText("giz5.options.title"));
    }

    @Override
    protected void init() {
        int count = 0;
        int size = options.length;

        for (int i = 0; i < size; i++) {
            Option option = options[i];
            int x = this.width / 2 - 155 + i % 2 * 160;
            int y = this.height / 6 + 24 * (i / 2);
            addDrawableChild(option.createButton(this.client.options, x, y, 150));
            count++;
        }

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 6 + 24 * (count + 1) / 2, 200, 20, ScreenTexts.DONE, (buttonWidget) -> {
            onClose();
        }));
    }

    @Override
    public void onClose() {
        Giz5Mod.saveOptions();
        super.onClose();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 20, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
