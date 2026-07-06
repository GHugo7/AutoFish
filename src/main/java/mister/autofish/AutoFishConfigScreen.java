package mister.autofish;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class AutoFishConfigScreen {

    public static Screen create(Screen parent) {
        AutoFishConfig config = AutofishLostera.CONFIG;

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("AutoFish — Lostera"))
                .setSavingRunnable(config::save);

        ConfigEntryBuilder e = builder.entryBuilder();

        ConfigCategory timers = builder.getOrCreateCategory(Text.literal("Timers"));

        timers.addEntry(e.startIntSlider(Text.literal("Réaction min (ms)"),
                        config.reactionMinMs, 50, 1900)
                .setDefaultValue(300)
                .setTooltip(Text.literal("Délai mini entre la touche et le coup de canne"))
                .setSaveConsumer(v -> config.reactionMinMs = v)
                .build());

        timers.addEntry(e.startIntSlider(Text.literal("Réaction max (ms)"),
                        config.reactionMaxMs, 50, 1900)
                .setDefaultValue(850)
                .setTooltip(Text.literal("Délai maxi entre la touche et le coup de canne"))
                .setSaveConsumer(v -> config.reactionMaxMs = v)
                .build());

        timers.addEntry(e.startIntSlider(Text.literal("Recast min (ms)"),
                        config.recastMinMs, 250, 10000)
                .setDefaultValue(1250)
                .setTooltip(Text.literal("Pause mini entre ramener et relancer"))
                .setSaveConsumer(v -> config.recastMinMs = v)
                .build());

        timers.addEntry(e.startIntSlider(Text.literal("Recast max (ms)"),
                        config.recastMaxMs, 250, 10000)
                .setDefaultValue(2450)
                .setTooltip(Text.literal("Pause maxi entre ramener et relancer"))
                .setSaveConsumer(v -> config.recastMaxMs = v)
                .build());

        timers.addEntry(e.startIntField(Text.literal("Timeout sans touche (s)"),
                        config.biteTimeoutSeconds)
                .setDefaultValue(60)
                .setMin(5).setMax(600)
                .setTooltip(Text.literal("Ramène et relance si rien ne mord après ce délai"))
                .setSaveConsumer(v -> config.biteTimeoutSeconds = v)
                .build());

        ConfigCategory detection = builder.getOrCreateCategory(Text.literal("Détection"));

        detection.addEntry(e.startStrField(Text.literal("Message déclencheur"),
                        config.triggerMessage)
                .setDefaultValue("pour pêcher ce poisson")
                .setTooltip(Text.literal("Sous-chaîne du message actionbar envoyé par le serveur"))
                .setSaveConsumer(v -> config.triggerMessage = v)
                .build());

        return builder.build();
    }
}