package bukkit.tommytony.war.command;

import org.bukkit.command.CommandSender;

import com.tommytony.war.Warzone;
import com.tommytony.war.WarHub;

import com.tommytony.war.mappers.VolumeMapper;
import com.tommytony.war.mappers.WarMapper;

import bukkit.tommytony.war.War;
import bukkit.tommytony.war.WarCommandHandler;

/**
 * Deletes the warhub.
 *
 * @author Tim Düsterhus
 */
public class DeleteWarhubCommand extends AbstractWarAdminCommand {
	public DeleteWarhubCommand(WarCommandHandler handler, CommandSender sender, String[] args) throws NotWarAdminException {
		super(handler, sender, args);
	}

	@Override
	public boolean handle() {
		if (this.args.length != 0) {
			return false;
		}

		if (War.war.getWarHub() != null) {
			// reset existing hub
			War.war.getWarHub().getVolume().resetBlocks();
			VolumeMapper.delete(War.war.getWarHub().getVolume());
			War.war.setWarHub((WarHub) null);
			for (Warzone zone : War.war.getWarzones()) {
				if (zone.getLobby() != null) {
					zone.getLobby().getVolume().resetBlocks();
					zone.getLobby().initialize();
				}
			}

			this.msg("War hub removed.");
		} else {
			this.badMsg("No War hub to delete.");
		}
		WarMapper.save();

		return true;
	}
}
