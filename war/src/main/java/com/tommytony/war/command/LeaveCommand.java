package com.tommytony.war.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import com.tommytony.war.War;
import com.tommytony.war.Warzone;
import com.tommytony.war.config.WarzoneConfig;

/**
 * Leaves a game.
 *
 * @author Tim Düsterhus
 */
public class LeaveCommand extends AbstractWarCommand {
	public LeaveCommand(WarCommandHandler handler, CommandSender sender, String[] args) {
		super(handler, sender, args);
	}

	@Override
	public boolean handle() {
		if (!(this.getSender() instanceof Player)) {
			this.badMsg("command.console");
			return true;
		}

		if (this.args.length != 0) {
			return false;
		}

		Player player = (Player) this.getSender();
		Warzone zone = Warzone.getZoneByPlayerName(player.getName());
		if (zone == null) {
			return false;
		}

		zone.handlePlayerLeave(player, zone.getWarzoneConfig().getBoolean(WarzoneConfig.AUTOJOIN) ?
				War.war.getWarHub().getLocation() : zone.getTeleport(), true);
		return true;
	}
}
