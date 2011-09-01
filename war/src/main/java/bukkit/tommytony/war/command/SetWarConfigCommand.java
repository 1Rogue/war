package bukkit.tommytony.war.command;

import org.bukkit.command.CommandSender;

import bukkit.tommytony.war.War;
import bukkit.tommytony.war.WarCommandHandler;

import com.tommytony.war.mappers.WarMapper;

public class SetWarConfigCommand extends AbstractZoneMakerCommand {

	public SetWarConfigCommand(WarCommandHandler handler, CommandSender sender, String[] args) throws NotZoneMakerException {
		super(handler, sender, args);
	}

	@Override
	public boolean handle() {
		boolean wantsToPrint = false;
		if (this.args.length == 0) {
			return false;
		} else if (this.args.length == 1 && (this.args[0].equals("-p") || this.args[0].equals("print"))) {
			String config = War.war.printConfig();
			this.msg(config);
			return true;
		} else if (this.args.length > 1 && (this.args[0].equals("-p") || this.args[0].equals("print"))) {
			wantsToPrint = true;
		}

		if (War.war.updateFromNamedParams(this.getSender(), this.args)) {
			WarMapper.save();
			if (wantsToPrint) {
				String config = War.war.printConfig();
				this.msg("War config saved. " + config);
			} else {
				this.msg("War config saved.");
			}
		} else {
			this.msg("Failed to read named parameters.");
		}

		return true;
	}

}