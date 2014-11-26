package com.tommytony.war.utility;

import java.util.Collection;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class PotionEffectHelper {

    public static void restorePotionEffects(Player player,
            Collection<PotionEffect> potionEffects) {
        clearPotionEffects(player);
        potionEffects.forEach(e -> player.addPotionEffect(e, true));
    }

    public static void clearPotionEffects(Player player) {
        player.getActivePotionEffects().stream()
                .map(e -> e.getType())
                .forEach(player::removePotionEffect);
    }
}
