package mister.autofish;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AutoFishConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance()
            .getConfigDir().resolve("autofish-lostera.json");

    public boolean precisionMode = false;
    public int reactionMinMs = 300;
    public int reactionMaxMs = 850;
    public int recastMinMs = 1250;
    public int recastMaxMs = 2450;
    public int biteTimeoutSeconds = 60;
    public String triggerMessage = "pour pêcher ce poisson";

    public boolean hoeAbility = false;
    public int hoeCooldownSeconds = 60;
    public boolean pickaxeAbility = false;
    public int pickaxeCooldownSeconds = 60;
    public boolean rodAbility = false;
    public int rodCooldownSeconds = 60;

    public static AutoFishConfig load() {
        try {
            if (Files.exists(FILE)) {
                AutoFishConfig loaded = GSON.fromJson(Files.readString(FILE), AutoFishConfig.class);
                if (loaded != null) return loaded;
            }
        } catch (Exception e) {
            AutofishLostera.LOGGER.error("Config illisible, valeurs par défaut", e);
        }
        AutoFishConfig config = new AutoFishConfig();
        config.save();
        return config;
    }

    public void save() {
        try {
            Files.writeString(FILE, GSON.toJson(this));
        } catch (IOException e) {
            AutofishLostera.LOGGER.error("Impossible de sauvegarder la config", e);
        }
    }
}