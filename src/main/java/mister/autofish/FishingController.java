package mister.autofish;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

import java.util.Random;

public class FishingController {
    private enum State { IDLE, WAITING_BITE, REELING, RECASTING }

    private static final Random RANDOM = new Random();

    private final AutoFishConfig config;
    private boolean enabled = false;
    private State state = State.IDLE;
    private int timer = 0;

    public FishingController(AutoFishConfig config) {
        this.config = config;
    }

    public boolean isEnabled() { return enabled; }

    public void toggle(MinecraftClient client) {
        enabled = !enabled;
        state = enabled ? State.RECASTING : State.IDLE;
        timer = enabled ? 10 : 0;
        if (client.player != null) {
            client.player.sendMessage(
                    Text.literal(enabled ? "§a[AutoFish] ON" : "§c[AutoFish] OFF"), true);
        }
    }

    public void onBite() {
        if (state == State.WAITING_BITE) {
            state = State.REELING;
            timer = ticksFromMs(randomBetween(config.reactionMinMs, config.reactionMaxMs));
        }
    }

    public void tick(MinecraftClient client) {
        if (!enabled || client.player == null || client.world == null) return;
        if (timer > 0) { timer--; return; }

        switch (state) {
            case REELING -> {
                useRod(client);
                state = State.RECASTING;
                timer = ticksFromMs(randomBetween(config.recastMinMs, config.recastMaxMs));
            }
            case RECASTING -> {
                if (useRod(client)) {
                    state = State.WAITING_BITE;
                    timer = config.biteTimeoutSeconds * 20;
                } else {
                    timer = 20;
                }
            }
            case WAITING_BITE -> {
                state = State.REELING;
                timer = 0;
            }
            default -> {}
        }
    }

    private static int randomBetween(int min, int max) {
        if (max <= min) return min;
        return min + RANDOM.nextInt(max - min + 1);
    }

    private static int ticksFromMs(int ms) {
        return Math.max(1, Math.round(ms / 50f));
    }

    private boolean useRod(MinecraftClient client) {
        if (!client.player.getMainHandStack().isOf(Items.FISHING_ROD)) return false;
        client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);
        client.player.swingHand(Hand.MAIN_HAND);
        return true;
    }
}