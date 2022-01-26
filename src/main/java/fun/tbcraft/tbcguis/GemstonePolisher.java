package fun.tbcraft.tbcguis;

import com.google.common.base.Strings;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

public class GemstonePolisher implements Listener {

    private Main plugin;
    private Inventory inv;

    public GemstonePolisher(Main instance) {
        plugin = instance;
    }

    private void createInv() {
        inv = Bukkit.createInventory(null, 45, "Polish your Gemstones");
        initializeItems();
    }

    private void initializeItems() {
        ItemStack item = new ItemStack(Material.BROWN_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer tag = meta.getPersistentDataContainer();

        tag.set(new NamespacedKey(plugin, "moveable"), PersistentDataType.STRING, "false");

        meta.setDisplayName(plugin.format("&7↓ Input ↓"));
        item.setItemMeta(meta);
        inv.setItem(10, item);
        meta.setDisplayName(plugin.format("&7↑ Input ↑"));
        item.setItemMeta(meta);
        inv.setItem(28, item);

        item.setType(Material.PURPLE_STAINED_GLASS_PANE);
        meta.setDisplayName(plugin.format("&7↓ Polish ↓"));
        item.setItemMeta(meta);
        inv.setItem(12, item);
        meta.setDisplayName(plugin.format("&7↑ Polish ↑"));
        item.setItemMeta(meta);
        inv.setItem(30, item);

        item.setType(Material.WHITE_STAINED_GLASS_PANE);
        meta.setDisplayName(plugin.format("&7↓ Output ↓"));
        item.setItemMeta(meta);
        inv.setItem(16, item);
        meta.setDisplayName(plugin.format("&7↑ Output ↑"));
        item.setItemMeta(meta);
        inv.setItem(34, item);

        item.setType(Material.YELLOW_STAINED_GLASS_PANE);
        meta.setDisplayName(plugin.format("&e&lClick to Polish"));
        item.setItemMeta(meta);
        inv.setItem(23, item);

        item.setType(Material.BLACK_STAINED_GLASS_PANE);
        meta.setDisplayName(null);
        item.setItemMeta(meta);

        for (int i = 0; i < 45; i++) {
            ItemStack slot = inv.getItem(i);

            if (i == 19 || i == 21 || i == 25) continue;

            if (slot == null) {
                inv.setItem(i, item);
            }
        }
    }

    public void open(Player player) {
        createInv();
        player.openInventory(inv);
    }

    public void startPolish() {

        ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(plugin.format("&7|||||||||||||||||||||||||"));
        item.setItemMeta(meta);
        inv.setItem(23, item);

        new BukkitRunnable() {
            int i = 10 * 20;
            @Override
            public void run() {
                if (i == 0) {
                    cancel();
                    return;
                }

                meta.setDisplayName(plugin.format(getLoadingBar(i, 200)));

            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    private String getLoadingBar(int current, int max) {
        float percent = (float) current / max;
        int bars = (int) (25 * percent);

        return Strings.repeat(plugin.format("&a|"), bars)
                + Strings.repeat(plugin.format("&7|"), 25 - bars);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        PersistentDataContainer container = meta.getPersistentDataContainer();
        String moveable = container.get(new NamespacedKey(plugin, "moveable"), PersistentDataType.STRING);

        if (moveable == null) return;

        if (moveable.equals("false")) {
            event.setCancelled(true);
            if (meta.getDisplayName().contains("Click to Polish")) {
                startPolish();
            }
        }
    }

}
