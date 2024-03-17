package me.michelemanna.parkour.gui;

import me.michelemanna.parkour.ParkourPlugin;
import me.michelemanna.parkour.data.Parkour;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class ParkourItem extends AbstractItem {
    private final Parkour parkour;

    public ParkourItem(Parkour parkour) {
        this.parkour = parkour;
    }

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.LADDER)
                .setDisplayName(parkour.getName());
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        if (clickType.equals(ClickType.DROP)) {
            ParkourPlugin.getInstance().getDatabase().deleteParkour(parkour);

            player.sendMessage("§aParkour deleted.");
            player.closeInventory();
            return;
        }
    }
}
