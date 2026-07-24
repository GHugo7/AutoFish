package mister.autofish;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.MinecraftClient;
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

        // Toggle mode précision
        timers.addEntry(e.startBooleanToggle(Text.literal("Mode précision"),
                        config.precisionMode)
                .setDefaultValue(false)
                .setTooltip(Text.literal("Utilise des champs de saisie exacte plutôt que des sliders"))
                .setSaveConsumer(v -> {
                    config.precisionMode = v;
                    config.save();
                    MinecraftClient.getInstance().setScreen(create(parent));
                })
                .build());

        // Réaction min
        if (config.precisionMode) {
            timers.addEntry(e.startIntField(Text.literal("Réaction min (ms)"),
                            config.reactionMinMs)
                    .setDefaultValue(300).setMin(0).setMax(10000)
                    .setTooltip(Text.literal("Délai mini entre la touche et le coup de canne"))
                    .setSaveConsumer(v -> config.reactionMinMs = v)
                    .build());
        } else {
            timers.addEntry(e.startIntSlider(Text.literal("Réaction min (ms)"),
                            config.reactionMinMs, 0, 1900)
                    .setDefaultValue(300)
                    .setTextGetter(v -> Text.literal(v + " ms"))
                    .setTooltip(Text.literal("Délai mini entre la touche et le coup de canne"))
                    .setSaveConsumer(v -> config.reactionMinMs = v)
                    .build());
        }

        // Réaction max
        if (config.precisionMode) {
            timers.addEntry(e.startIntField(Text.literal("Réaction max (ms)"),
                            config.reactionMaxMs)
                    .setDefaultValue(850).setMin(0).setMax(10000)
                    .setTooltip(Text.literal("Délai maxi entre la touche et le coup de canne"))
                    .setSaveConsumer(v -> config.reactionMaxMs = v)
                    .build());
        } else {
            timers.addEntry(e.startIntSlider(Text.literal("Réaction max (ms)"),
                            config.reactionMaxMs, 0, 1900)
                    .setDefaultValue(850)
                    .setTextGetter(v -> Text.literal(v + " ms"))
                    .setTooltip(Text.literal("Délai maxi entre la touche et le coup de canne"))
                    .setSaveConsumer(v -> config.reactionMaxMs = v)
                    .build());
        }

        // Recast min
        if (config.precisionMode) {
            timers.addEntry(e.startIntField(Text.literal("Recast min (ms)"),
                            config.recastMinMs)
                    .setDefaultValue(1250).setMin(0).setMax(60000)
                    .setTooltip(Text.literal("Pause mini entre ramener et relancer"))
                    .setSaveConsumer(v -> config.recastMinMs = v)
                    .build());
        } else {
            timers.addEntry(e.startIntSlider(Text.literal("Recast min (ms)"),
                            config.recastMinMs, 0, 10000)
                    .setDefaultValue(1250)
                    .setTextGetter(v -> Text.literal(v + " ms"))
                    .setTooltip(Text.literal("Pause mini entre ramener et relancer"))
                    .setSaveConsumer(v -> config.recastMinMs = v)
                    .build());
        }

        // Recast max
        if (config.precisionMode) {
            timers.addEntry(e.startIntField(Text.literal("Recast max (ms)"),
                            config.recastMaxMs)
                    .setDefaultValue(2450).setMin(0).setMax(60000)
                    .setTooltip(Text.literal("Pause maxi entre ramener et relancer"))
                    .setSaveConsumer(v -> config.recastMaxMs = v)
                    .build());
        } else {
            timers.addEntry(e.startIntSlider(Text.literal("Recast max (ms)"),
                            config.recastMaxMs, 0, 10000)
                    .setDefaultValue(2450)
                    .setTextGetter(v -> Text.literal(v + " ms"))
                    .setTooltip(Text.literal("Pause maxi entre ramener et relancer"))
                    .setSaveConsumer(v -> config.recastMaxMs = v)
                    .build());
        }

        timers.addEntry(e.startIntField(Text.literal("Timeout sans touche (s)"),
                        config.biteTimeoutSeconds)
                .setDefaultValue(60)
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

        ConfigCategory abilities = builder.getOrCreateCategory(Text.literal("Capacités"));

        abilities.addEntry(e.startBooleanToggle(Text.literal("Capacité houe"), config.hoeAbility)
                .setDefaultValue(false)
                .setTooltip(Text.literal("Active la capacité (drop) quand la houe est en main"))
                .setSaveConsumer(v -> config.hoeAbility = v)
                .build());

        abilities.addEntry(e.startIntField(Text.literal("Cooldown houe (s)"), config.hoeCooldownSeconds)
                .setDefaultValue(60).setMin(1).setMax(3600)
                .setSaveConsumer(v -> config.hoeCooldownSeconds = v)
                .build());

        abilities.addEntry(e.startBooleanToggle(Text.literal("Capacité pioche"), config.pickaxeAbility)
                .setDefaultValue(false)
                .setTooltip(Text.literal("Active la capacité (drop) quand la pioche est en main"))
                .setSaveConsumer(v -> config.pickaxeAbility = v)
                .build());

        abilities.addEntry(e.startIntField(Text.literal("Cooldown pioche (s)"), config.pickaxeCooldownSeconds)
                .setDefaultValue(60).setMin(1).setMax(3600)
                .setSaveConsumer(v -> config.pickaxeCooldownSeconds = v)
                .build());

        abilities.addEntry(e.startBooleanToggle(Text.literal("Capacité canne à pêche"), config.rodAbility)
                .setDefaultValue(false)
                .setTooltip(Text.literal("Active la capacité (drop) quand la canne est en main"))
                .setSaveConsumer(v -> config.rodAbility = v)
                .build());

        abilities.addEntry(e.startIntField(Text.literal("Cooldown canne (s)"), config.rodCooldownSeconds)
                .setDefaultValue(60).setMin(1).setMax(3600)
                .setSaveConsumer(v -> config.rodCooldownSeconds = v)
                .build());

        return builder.build();
    }
}