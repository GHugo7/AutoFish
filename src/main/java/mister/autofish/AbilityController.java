package mister.autofish;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Hand;

public class AbilityController {

    private final AutoFishConfig config;
    private int hoeTimer = 0;
    private int pickaxeTimer = 0;
    private int rodTimer = 0;

    public AbilityController(AutoFishConfig config) {
        this.config = config;
    }

    public void tick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;

        if (hoeTimer > 0) hoeTimer--;
        if (pickaxeTimer > 0) pickaxeTimer--;
        if (rodTimer > 0) rodTimer--;

        ItemStack held = client.player.getMainHandStack();

        if (config.hoeAbility && hoeTimer == 0 && held.isIn(ItemTags.HOES)) {
            activate(client);
            hoeTimer = config.hoeCooldownSeconds * 20;
        } else if (config.pickaxeAbility && pickaxeTimer == 0 && held.isIn(ItemTags.PICKAXES)) {
            activate(client);
            pickaxeTimer = config.pickaxeCooldownSeconds * 20;
        } else if (config.rodAbility && rodTimer == 0 && held.isOf(Items.FISHING_ROD)) {
            activate(client);
            rodTimer = config.rodCooldownSeconds * 20;
        }
    }

    private void activate(MinecraftClient client) {
        client.player.dropSelectedItem(false);
        client.player.swingHand(Hand.MAIN_HAND);
    }
}