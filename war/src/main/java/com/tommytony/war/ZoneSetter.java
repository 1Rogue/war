package com.tommytony.war;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import com.tommytony.war.mappers.WarMapper;
import com.tommytony.war.mappers.WarzoneMapper;
import com.tommytony.war.volumes.NotNorthwestException;
import com.tommytony.war.volumes.NotSoutheastException;
import com.tommytony.war.volumes.TooBigException;
import com.tommytony.war.volumes.TooSmallException;

import bukkit.tommytony.war.War;
import bukkit.tommytony.war.command.SetZoneCommand;

public class ZoneSetter {

	private final Player player;
	private final String zoneName;

	public ZoneSetter(Player player, String zoneName) {
		this.player = player;
		this.zoneName = zoneName;
	}

	public void placeNorthwest() {
		Warzone warzone = War.war.findWarzone(this.zoneName);
		Block northwestBlock = this.player.getLocation().getWorld().getBlockAt(this.player.getLocation());
		StringBuilder msgString = new StringBuilder();
		try {
			if (warzone == null) {
				// create the warzone
				warzone = new Warzone(this.player.getLocation().getWorld(), this.zoneName);
				warzone.addAuthor(player.getName());
				War.war.getIncompleteZones().add(warzone);
				warzone.getVolume().setNorthwest(northwestBlock);
				War.war.msg(this.player, "Warzone " + warzone.getName() + " created. Northwesternmost point set to x:" + warzone.getVolume().getNorthwestX() + " z:" + warzone.getVolume().getNorthwestZ() + ". ");
			} else if (!this.isPlayerAuthorOfZone(warzone)) {
				return;
			} else {
				// change existing warzone
				this.resetWarzone(warzone, msgString);
				warzone.getVolume().setNorthwest(northwestBlock);
				msgString.append("Warzone " + warzone.getName() + " modified. Northwesternmost point set to x:" + warzone.getVolume().getNorthwestX() + " z:" + warzone.getVolume().getNorthwestZ() + ". ");
			}
			this.saveIfReady(warzone, msgString);
		} catch (NotNorthwestException e) {
			War.war.badMsg(this.player, "The block you selected is not to the northwest of the existing southeasternmost block.");
			if (warzone.getVolume().isSaved()) {
				warzone.initializeZone(); // was reset before changing
			}
		} catch (TooSmallException e) {
			this.handleTooSmall();
			if (warzone.getVolume().isSaved()) {
				warzone.initializeZone();
			}
		} catch (TooBigException e) {
			this.handleTooBig();
			if (warzone.getVolume().isSaved()) {
				warzone.initializeZone();
			}
		}
	}

	

	public void placeSoutheast() {
		Warzone warzone = War.war.findWarzone(this.zoneName);
		Block southeastBlock = this.player.getLocation().getWorld().getBlockAt(this.player.getLocation());
		StringBuilder msgString = new StringBuilder();
		try {
			if (warzone == null) {
				// create the warzone
				warzone = new Warzone(this.player.getLocation().getWorld(), this.zoneName);
				warzone.addAuthor(player.getName());
				War.war.getIncompleteZones().add(warzone);
				warzone.getVolume().setSoutheast(southeastBlock);
				War.war.msg(this.player, "Warzone " + warzone.getName() + " created. Southeasternmost point set to x:" + warzone.getVolume().getSoutheastX() + " z:" + warzone.getVolume().getSoutheastZ() + ". ");
			} else if (!this.isPlayerAuthorOfZone(warzone)) {
				return;
			} else {
				// change existing warzone
				this.resetWarzone(warzone, msgString);
				warzone.getVolume().setSoutheast(southeastBlock);
				msgString.append("Warzone " + warzone.getName() + " modified. Southeasternmost point set to x:" + warzone.getVolume().getSoutheastX() + " z:" + warzone.getVolume().getSoutheastZ() + ". ");
			}
			this.saveIfReady(warzone, msgString);
		} catch (NotSoutheastException e) {
			War.war.badMsg(this.player, "The block you selected is not to the southeast of the existing northwestnmost block.");
			if (warzone.getVolume().isSaved()) {
				warzone.initializeZone(); // was reset before changing
			}
		} catch (TooSmallException e) {
			this.handleTooSmall();
			if (warzone.getVolume().isSaved()) {
				warzone.initializeZone();
			}
		} catch (TooBigException e) {
			this.handleTooBig();
			if (warzone.getVolume().isSaved()) {
				warzone.initializeZone();
			}
		}
	}

	public void placeCorner1() {
		Block corner1Block = this.player.getLocation().getWorld().getBlockAt(this.player.getLocation());
		this.placeCorner1(corner1Block);
	}

	public void placeCorner1(Block corner1Block) {
		Warzone warzone = War.war.findWarzone(this.zoneName);
		StringBuilder msgString = new StringBuilder();
		try {
			if (warzone == null) {
				// create the warzone
				warzone = new Warzone(this.player.getLocation().getWorld(), this.zoneName);
				warzone.addAuthor(player.getName());
				War.war.getIncompleteZones().add(warzone);
				warzone.getVolume().setZoneCornerOne(corner1Block);
				War.war.msg(this.player, "Warzone " + warzone.getName() + " created. Corner 1 set to x:" + corner1Block.getX() + " y:" + corner1Block.getY() + " z:" + corner1Block.getZ() + ". ");
			} else if (!this.isPlayerAuthorOfZone(warzone)) {
				return;
			} else {
				// change existing warzone
				this.resetWarzone(warzone, msgString);
				warzone.getVolume().setZoneCornerOne(corner1Block);
				msgString.append("Warzone " + warzone.getName() + " modified. Corner 1 set to x:" + corner1Block.getX() + " y:" + corner1Block.getY() + " z:" + corner1Block.getZ() + ". ");
			}
			this.saveIfReady(warzone, msgString);
		} catch (TooSmallException e) {
			this.handleTooSmall();
			if (warzone.getVolume().isSaved()) {
				warzone.initializeZone();
			}
		} catch (TooBigException e) {
			this.handleTooBig();
			if (warzone.getVolume().isSaved()) {
				warzone.initializeZone();
			}
		}
	}

	public void placeCorner2() {
		Block corner2Block = this.player.getLocation().getWorld().getBlockAt(this.player.getLocation());
		this.placeCorner2(corner2Block);
	}

	public void placeCorner2(Block corner2Block) {
		Warzone warzone = War.war.findWarzone(this.zoneName);
		StringBuilder msgString = new StringBuilder();
		try {
			if (warzone == null) {
				// create the warzone
				warzone = new Warzone(this.player.getLocation().getWorld(), this.zoneName);
				warzone.addAuthor(player.getName());
				War.war.getIncompleteZones().add(warzone);
				warzone.getVolume().setZoneCornerTwo(corner2Block);
				War.war.msg(this.player, "Warzone " + warzone.getName() + " created. Corner 2 set to x:" + corner2Block.getX() + " y:" + corner2Block.getY() + " z:" + corner2Block.getZ() + ". ");
			} else if (!this.isPlayerAuthorOfZone(warzone)) {
				return;
			} else {
				// change existing warzone
				this.resetWarzone(warzone, msgString);
				warzone.getVolume().setZoneCornerTwo(corner2Block);
				msgString.append("Warzone " + warzone.getName() + " modified. Corner 2 set to x:" + corner2Block.getX() + " y:" + corner2Block.getY() + " z:" + corner2Block.getZ() + ". ");
			}
			this.saveIfReady(warzone, msgString);
		} catch (TooSmallException e) {
			this.handleTooSmall();
			if (warzone.getVolume().isSaved()) {
				warzone.initializeZone();
			}
		} catch (TooBigException e) {
			this.handleTooBig();
			if (warzone.getVolume().isSaved()) {
				warzone.initializeZone();
			}
		}
	}

	private boolean isPlayerAuthorOfZone(Warzone warzone) {
		boolean isAuthor = warzone.isAuthor(player);
		if (!isAuthor) {
			War.war.badMsg(player, "You can't do this because you are not an author of the " + warzone.getName() + " warzone." );
		}
		return isAuthor;
	}
	
	private void resetWarzone(Warzone warzone, StringBuilder msgString) {
		if (warzone.getVolume().isSaved()) {
			War.war.msg(this.player, "Resetting " + warzone.getName() + " blocks.");
			if (warzone.getLobby() != null && warzone.getLobby().getVolume() != null) {
				warzone.getLobby().getVolume().resetBlocks();
			}
			int reset = warzone.getVolume().resetBlocks();
			msgString.append(reset + " blocks reset. ");
		}
	}

	private void handleTooSmall() {
		War.war.badMsg(this.player, "That would make the " + this.zoneName + " warzone too small. Sides must be at least 10 blocks and all existing structures (spawns, flags, etc) must fit inside.");
	}

	private void handleTooBig() {
		War.war.badMsg(this.player, "That would make the " + this.zoneName + " warzone too big. Sides must be less than 750 blocks.");
	}

	private void saveIfReady(Warzone warzone, StringBuilder msgString) {
		if (warzone.ready()) {
			if (!War.war.getWarzones().contains(warzone)) {
				War.war.addWarzone(warzone);
			}
			if (War.war.getIncompleteZones().contains(warzone)) {
				War.war.getIncompleteZones().remove(warzone);
			}
			WarMapper.save();
			msgString.append("Saving new warzone blocks...");
			War.war.msg(this.player, msgString.toString());
			warzone.saveState(false); // we just changed the volume, cant reset walls
			
			if (warzone.getLobby() == null) {
				// Set default lobby on south side
				ZoneLobby lobby = new ZoneLobby(warzone, BlockFace.SOUTH);
				warzone.setLobby(lobby);
				if (War.war.getWarHub() != null) { // warhub has to change
					War.war.getWarHub().getVolume().resetBlocks();
					War.war.getWarHub().initialize();
				}
				War.war.msg(this.player, "Default lobby created on south side of zone. Use /setzonelobby <n/s/e/w> to change its position.");
			}
			
			warzone.initializeZone();
			WarzoneMapper.save(warzone, true);
			War.war.msg(this.player, "Warzone saved.");
		} else {
			if (warzone.getVolume().getCornerOne() == null) {
				msgString.append("Still missing corner 1.");
			} else if (warzone.getVolume().getCornerTwo() == null) {
				msgString.append("Still missing corner 2.");
			}
			War.war.msg(this.player, msgString.toString());
		}
	}

}
