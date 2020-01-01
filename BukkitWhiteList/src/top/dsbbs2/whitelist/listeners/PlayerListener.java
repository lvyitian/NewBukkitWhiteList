package top.dsbbs2.whitelist.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import top.dsbbs2.whitelist.util.EventUtil;
import top.dsbbs2.whitelist.util.PlayerUtil;

@SuppressWarnings("deprecation")
public class PlayerListener implements Listener {
	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=false)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent e) throws Throwable
	{
		EventUtil.checkAndCancel(e,e.getPlayer());
	}
	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=false)
	public void onPlayerConsumeItem(PlayerItemConsumeEvent e) throws Throwable
	{
		EventUtil.checkAndCancel(e,e.getPlayer());
	}
	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=false)
	public void onPlayerPickupItem(PlayerPickupItemEvent e) throws Throwable
	{
		EventUtil.checkAndCancel(e,e.getPlayer());
	}
	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=false)
	public void onPlayerDropItem(PlayerDropItemEvent e) throws Throwable
	{
		EventUtil.checkAndCancel(e,e.getPlayer());
	}
	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=false)
	public void onPlayerInteract(PlayerInteractEvent e) throws Throwable
	{
		EventUtil.checkAndCancel(e,e.getPlayer());
	}
	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=false)
	public void onPlayerChat(PlayerChatEvent e) throws Throwable
	{
		EventUtil.checkAndCancel(e,e.getPlayer());
	}
	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=false)
	public void onPlayerDamageEntity(EntityDamageByEntityEvent e) throws Throwable
	{
			if(e.getDamager() instanceof Player)
			{
				Player p=(Player)e.getDamager();
				EventUtil.checkAndCancel(e,p);
			}
	}
	@EventHandler(priority=EventPriority.MONITOR,ignoreCancelled=false)
	public void onGameModeChange(PlayerGameModeChangeEvent e) throws Throwable
	{
		if(!PlayerUtil.isInWhiteList(e.getPlayer().getUniqueId()))
			PlayerUtil.setInv(e.getPlayer(), true);
	}
	@EventHandler(priority=EventPriority.MONITOR,ignoreCancelled=false)
	public void onPlayerJoin(PlayerJoinEvent e) throws Throwable
	{
		if(!PlayerUtil.isInWhiteList(e.getPlayer().getUniqueId()))
		{
			PlayerUtil.setInv(e.getPlayer(), true);
			PlayerUtil.massivelyInformPlayer(e.getPlayer(), 200);
		}	
	}
	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=false)
	public void onCommandProcess(PlayerCommandPreprocessEvent e) throws Throwable
	{
		EventUtil.checkAndCancel(e,e.getPlayer());
	}
}
