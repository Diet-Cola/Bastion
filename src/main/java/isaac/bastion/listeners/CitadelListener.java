package isaac.bastion.listeners;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import isaac.bastion.Bastion;
import isaac.bastion.BastionBlock;
import isaac.bastion.BastionType;
import isaac.bastion.manager.BastionBlockManager;
import vg.civcraft.mc.citadel.events.ReinforcementCreationEvent;

public class CitadelListener implements Listener {
	
	private BastionBlockManager blockManager;
	
	public CitadelListener() {
		this.blockManager = Bastion.getBastionManager();
	}

	@EventHandler(ignoreCancelled = true)
	public void onReinforcementCreation(ReinforcementCreationEvent event) {
		Set<BastionBlock> preblocking = blockManager.getBlockingBastionsWithoutPermission(event.getReinforcement().getLocation(), 
				event.getPlayer().getUniqueId(), Bastion.getInstance().getPermissionManager().getPlaceInBastion());
		for(BastionBlock bastion : preblocking) {
			BastionType type = bastion.getType();
			if(type.isBlockReinforcements()) {
				event.setCancelled(true);
				if (!Bastion.getSettingManager().getIgnorePlacementMessages(event.getPlayer().getUniqueId())) {
					event.getPlayer().sendMessage(ChatColor.RED + "Bastion prevented reinforcement");
				}
				blockManager.erodeFromPlace(event.getPlayer(), preblocking);
				return;
			}
		}
	}
}
