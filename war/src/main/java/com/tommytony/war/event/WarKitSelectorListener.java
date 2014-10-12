/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tommytony.war.event;

/*import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;*/
import com.tommytony.war.Team;
import com.tommytony.war.War;
import com.tommytony.war.Warzone;
import com.tommytony.war.utility.Loadout;
import com.tommytony.war.utility.LoadoutSelection;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Allows for selecting kits via an inventory selection menu
 *
 * @since 1.0.0
 * @author 1Rogue
 * @version 1.0.0
 */
public class WarKitSelectorListener implements Listener {

    protected static final String DATA_FILE = "kit_selection.yml";
    protected static final String KIT_ICON_PATH = "selector";
    protected static final String KITS_PATH = "kits";
    protected final ItemStack kitSelector;
    protected final Set<KitIcon> kits = new HashSet<KitIcon>();
    protected final String name;

    public WarKitSelectorListener() {
        FileConfiguration yaml = this.prepData();
        this.kitSelector = yaml.getItemStack(KIT_ICON_PATH);
        if (this.kitSelector == null) {
            throw new IllegalStateException("Error reading config value for kit selector!");
        }
        this.name = ChatColor.translateAlternateColorCodes('&', yaml.getString("name"));
        Map<String, Object> map = this.getConfigSectionValue(yaml.get(KITS_PATH));
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, Object> ent : map.entrySet()) {
                this.kits.add(new KitIcon((ItemStack) ent.getValue(), ent.getKey()));
            }
        }
    }

    public void test(PlayerToggleSneakEvent event) {
        if (War.war.isLoaded() && event.isSneaking()) {
            Warzone playerWarzone = Warzone.getZoneByLocation(event.getPlayer());
            Team playerTeam = Team.getTeamByPlayerName(event.getPlayer().getName());
            if (playerWarzone != null && playerTeam != null && playerTeam.getInventories().resolveLoadouts().keySet().size() > 1 && playerTeam.isSpawnLocation(event.getPlayer().getLocation())) {
                if (playerWarzone.getLoadoutSelections().keySet().contains(event.getPlayer().getName())
                        && playerWarzone.getLoadoutSelections().get(event.getPlayer().getName()).isStillInSpawn()) {
                    LoadoutSelection selection = playerWarzone.getLoadoutSelections().get(event.getPlayer().getName());
                    List<Loadout> loadouts = new ArrayList<Loadout>(playerTeam.getInventories().resolveNewLoadouts());
                    for (Iterator<Loadout> it = loadouts.iterator(); it.hasNext();) {
                        Loadout ldt = it.next();
                        if (ldt.getName().equals("first")
                                || (ldt.requiresPermission() && !event.getPlayer().hasPermission(ldt.getPermission()))) {
                            it.remove();
                        }
                    }
                    int currentIndex = (selection.getSelectedIndex() + 1) % loadouts.size();
                    selection.setSelectedIndex(currentIndex);

                    playerWarzone.equipPlayerLoadoutSelection(event.getPlayer(), playerTeam, false, true);
                } else {
                    War.war.badMsg(event.getPlayer(), "zone.loadout.reenter");
                }
            }
        }
    }

    protected final FileConfiguration prepData() {
        War.war.getDataFolder().mkdirs();
        File f = new File(War.war.getDataFolder(), DATA_FILE);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException ex) {
                War.war.getLogger().log(Level.SEVERE, "Error loading kit selection file!", ex);
            }
        }
        FileConfiguration yaml = YamlConfiguration.loadConfiguration(f);
        if (!yaml.isSet("name")) {
            yaml.set("name", "Select Kit");
        }
        if (!yaml.isSet(KIT_ICON_PATH)) {
            yaml.set(KIT_ICON_PATH, new ItemStack(Material.BLAZE_ROD, 1));
        }
        if (!yaml.isSet(KITS_PATH)) {
            yaml.set(KITS_PATH, new ArrayList<ItemStack>());
        }
        return yaml;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!(event.getAction() == Action.RIGHT_CLICK_AIR
                || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        if (!this.kitSelector.equals(event.getPlayer().getInventory().getItemInHand())) {
            return;
        }
        
        if (War.war.isLoaded()) {
            Warzone playerWarzone = Warzone.getZoneByLocation(event.getPlayer());
            Team playerTeam = Team.getTeamByPlayerName(event.getPlayer().getName());
            if (playerWarzone != null && playerTeam != null //if on team and in warzone
                    && playerTeam.getInventories().resolveLoadouts().keySet().size() > 1 //if there's multiple unique loadouts
                    && playerTeam.isSpawnLocation(event.getPlayer().getLocation())) { //if player is in spawn
                if (playerWarzone.getLoadoutSelections().keySet().contains(event.getPlayer().getName()) // ??
                        && playerWarzone.getLoadoutSelections().get(event.getPlayer().getName()).isStillInSpawn()) { // ...still in spawn?
                    LoadoutSelection selection = playerWarzone.getLoadoutSelections().get(event.getPlayer().getName());
                    List<Loadout> loadouts = new ArrayList<Loadout>(playerTeam.getInventories().resolveNewLoadouts());
                    for (Iterator<Loadout> it = loadouts.iterator(); it.hasNext();) {
                        Loadout ldt = it.next();
                        if (ldt.getName().equals("first")
                                || (ldt.requiresPermission() && !event.getPlayer().hasPermission(ldt.getPermission()))) {
                            it.remove();
                        }
                    }
                    int currentIndex = (selection.getSelectedIndex() + 1) % loadouts.size();
                    selection.setSelectedIndex(currentIndex);

                    playerWarzone.equipPlayerLoadoutSelection(event.getPlayer(), playerTeam, false, true);
                } else {
                    War.war.badMsg(event.getPlayer(), "zone.loadout.reenter");
                }
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!event.getView().getTopInventory().getName().equals(this.name)
                || !(event.getWhoClicked() instanceof Player)) {
            return;
        }
        if (event.getRawSlot() == event.getSlot()) {
            event.setCancelled(true);
        }
        Player p = (Player) event.getWhoClicked();
        switch (event.getAction()) {
            case CLONE_STACK:
            case DROP_ALL_CURSOR:
            case DROP_ALL_SLOT:
            case DROP_ONE_CURSOR:
            case DROP_ONE_SLOT:
            case HOTBAR_MOVE_AND_READD:
            case HOTBAR_SWAP:
            case MOVE_TO_OTHER_INVENTORY:
            case NOTHING:
            case PLACE_ALL:
            case PLACE_ONE:
            case PLACE_SOME:
            case SWAP_WITH_CURSOR:
            case UNKNOWN:
            case COLLECT_TO_CURSOR:
                break;
            default:
                Set<KitIcon> clicked = new HashSet<KitIcon>();
                for (KitIcon icon : this.kits) {
                    if (icon.getItem().equals(event.getCurrentItem())) {
                        clicked.add(icon);
                    }
                }
                if (!clicked.isEmpty()) {
                    KitIcon click = clicked.iterator().next();
                    //handle click
                }
                break;
        }
    }

    protected Inventory getInventory(Player p) {
        Warzone warzone = Warzone.getZoneByLocation(p);
        for (LoadoutSelection l : warzone.getLoadoutSelections().values()) {
            ();
        }
        return Adapter.getInventory(War.war, this.name, this.getItemStacks(this.kits));
    }


    /**
     * Returns a {@link Map} representative of the passed Object that represents
     * a section of a YAML file. This method neglects the implementation of the
     * section (whether it be {@link MemorySection} or just a {@link Map}), and
     * returns the appropriate value.
     *
     * @since 1.0.0
     * @version 1.0.0
     *
     * @param o The object to interpret
     * @return A {@link Map} representing the section
     */
    private Map<String, Object> getConfigSectionValue(Object o) {
        if (o == null) {
            return null;
        }
        Map<String, Object> map;
        if (o instanceof MemorySection) {
            map = ((MemorySection) o).getValues(false);
        } else if (o instanceof Map) {
            map = (Map<String, Object>) o;
        } else {
            return null;
        }
        return map;
    }

    private ItemStack[] getItemStacks(Collection<? extends KitIcon> items) {
        List<ItemStack> its = new ArrayList<ItemStack>();
        for (KitIcon i : items) {
            its.add(i.getItem());
        }
        return its.toArray(new ItemStack[its.size()]);
    }

    public static class Adapter {

        public static Inventory getInventory(War plugin, String name, ItemStack... icons) {
            int rows = icons.length / 9;
            if (icons.length % 9 != 0 || rows == 0) {
                rows++;
            }
            Inventory back = plugin.getServer().createInventory(null, rows * 9, name);

            List<ItemStack> items = new ArrayList<ItemStack>(Arrays.asList(icons));

            /*int[] slots = getSlots(items.size(), back.getSize());

            if (slots.length != items.size()) {
                plugin.getLogger().log(Level.SEVERE, "Could not determine size for inventory, quitting!");
                plugin.getKit().getPluginManager().disablePlugin(plugin);
            }*/

            for (int i = 0; /*i < slots.length && */i < items.size(); i++) {
                back.setItem(/*slots[i]*/i, items.get(i));
            }

            return back;
        }

        private static int[] getSlots(int numItems, int numSlots) {
            double nI = (double) numItems;
            double nS = (double) numSlots;
            double buff = Math.floor((nS - nI) / 2D);
            int[] back = new int[numItems];
            for (int i = 0; i < back.length; i++) {
                back[i] = i + (int) buff;
            }
            return back;
        }

        public static ItemStack removeAttributes(ItemStack item) {
            /*if (!MinecraftReflection.isCraftItemStack(item)) {
                item = MinecraftReflection.getBukkitItemStack(item);
            }
            NbtCompound compound = (NbtCompound) NbtFactory.fromItemTag(item);
            compound.put(NbtFactory.ofList("AttributeModifiers"));*/
            return item;
        }
    }

    protected static class KitIcon {

        private final ItemStack item;
        private final String kit;

        public KitIcon(ItemStack item, String kit) {
            this.item = Adapter.removeAttributes(item);
            this.kit = kit;
        }

        public ItemStack getItem() {
            return this.item.clone();
        }

        public String getKit() {
            return this.kit;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof KitIcon)) {
                return false;
            }
            final KitIcon other = (KitIcon) obj;
            return (this.item == null ? other.item == null : this.item.equals(other.item))
                && (this.kit == null ? other.kit == null : this.kit.equalsIgnoreCase(other.kit));
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 67 * hash + (this.item != null ? this.item.hashCode() : 0);
            hash = 67 * hash + (this.kit != null ? this.kit.hashCode() : 0);
            return hash;
        }

    }

}
