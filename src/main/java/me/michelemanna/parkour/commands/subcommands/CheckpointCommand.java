package me.michelemanna.parkour.commands.subcommands;

import me.michelemanna.parkour.ParkourPlugin;
import me.michelemanna.parkour.commands.SubCommand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class CheckpointCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        if (args.length == 1) {
            player.sendMessage("§cUsage: /parkour checkpoint <parkour>");
            return;
        }

        ParkourPlugin
                .getInstance()
                .getParkourManager()
                .getParkours().get(args[1])
                .getCheckpoints()
                .add(player.getLocation().getBlock().getLocation());

        ArmorStand armor = (ArmorStand) player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);

        armor.setGravity(false);
        armor.setCanPickupItems(false);
        armor.setCustomName("§aCheckpoint");
        armor.setCustomNameVisible(true);
        armor.setVisible(false);

        player.sendMessage("§aCheckpoint created!");
    }
}