package me.michelemanna.parkour.gui;

import me.michelemanna.parkour.ParkourPlugin;
import me.michelemanna.parkour.data.Parkour;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.window.Window;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class ParkourMenus {
    public static void openParkours(Player player) {
        Map<String, Parkour> parkours = ParkourPlugin.getInstance().getParkourManager().getParkours();

        if (parkours.isEmpty()) {
            player.sendMessage(ParkourPlugin.getInstance().getMessage("gui.no-parkours"));
            return;
        }

        PagedGui gui = PagedGui.items()
                .setStructure(
                        "# # # # # # # # #",
                        "# . . . . . . . #",
                        "# . . . . . . . #",
                        "# # # # # # # # #"
                )
                .setContent(parkours.values().stream()
                        .map(ParkourItem::new)
                        .collect(Collectors.toList()))
                .addIngredient('#', new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(""))
                .addIngredient('.', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .build();

        Window window = Window.single()
                .setViewer(player)
                .setTitle("Parkours")
                .setGui(gui)
                .build();

        window.open();
    }
}
