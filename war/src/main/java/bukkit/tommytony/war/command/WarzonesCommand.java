package bukkit.tommytony.war.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import bukkit.tommytony.war.War;
import bukkit.tommytony.war.WarCommandHandler;

import com.tommytony.war.Team;
import com.tommytony.war.Warzone;

/**
 * Lists all warzones
 *
 * @author Tim Düsterhus
 */
public class WarzonesCommand extends AbstractWarCommand {
	public WarzonesCommand(WarCommandHandler handler, CommandSender sender, String[] args) {
		super(handler, sender, args);
	}

	public boolean handle() {
		String warzonesMessage = "Warzones: ";
		if (War.war.getWarzones().isEmpty()) {
			warzonesMessage += "none.";
		}
		else {
			for (Warzone warzone : War.war.getWarzones()) {
				warzonesMessage += warzone.getName() + " (" + warzone.getTeams().size() + " teams, ";
				int playerTotal = 0;
				for (Team team : warzone.getTeams()) {
					playerTotal += team.getPlayers().size();
				}
				warzonesMessage += playerTotal + " players)";
			}
		}

		this.msg(warzonesMessage + ((this.sender instanceof Player) ? " Use /zone <zone-name> to teleport to a warzone." : ""));

		return true;
	}
}
