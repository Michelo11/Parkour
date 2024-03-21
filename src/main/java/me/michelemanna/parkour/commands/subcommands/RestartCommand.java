package me.michelemanna.parkour.commands.subcommands;

import me.michelemanna.parkour.ParkourPlugin;
import me.michelemanna.parkour.commands.SubCommand;
import me.michelemanna.parkour.data.ParkourSession;
import org.bukkit.entity.Player;

public class RestartCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        ParkourSession session = ParkourPlugin.getInstance().getParkourManager().getSession(player);

        if (session == null) {
            player.sendMessage(ParkourPlugin.getInstance().getMessage("commands.not-in-parkour"));
            return;
        }

        session.teleport(player);

        player.sendMessage(ParkourPlugin.getInstance().getMessage("commands.restart"));
    }
}