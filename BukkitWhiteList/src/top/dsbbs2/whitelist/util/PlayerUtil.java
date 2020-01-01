package top.dsbbs2.whitelist.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import top.dsbbs2.whitelist.WhiteListPlugin;
import top.dsbbs2.whitelist.config.struct.WhiteListConfig;
import top.dsbbs2.whitelist.config.struct.WhiteListConfig.WLPlayer;
import top.dsbbs2.whitelist.gui.ItemBuilder;

public class PlayerUtil {
	public static volatile String informMess=WhiteListPlugin.instance.whitelist.con.mess;
	public static void setInv(Player p,boolean inv) throws Throwable
	{
		Method getHandle=p.getClass().getDeclaredMethod("getHandle", new Class<?>[0]);
		getHandle.setAccessible(true);
		Object NMSPlayer=getHandle.invoke(p, new Object[0]);
		Field abilities=NMSPlayer.getClass().getSuperclass().getDeclaredField("abilities");
		abilities.setAccessible(true);
		Object abiObj=abilities.get(NMSPlayer);
		Class<?> abiClass=abiObj.getClass();
		Field isInvulnerable=abiClass.getDeclaredField("isInvulnerable");
		isInvulnerable.setAccessible(true);
		isInvulnerable.set(abiObj, inv);
		abilities.set(NMSPlayer, abiObj);
		Method upd=NMSPlayer.getClass().getDeclaredMethod("updateAbilities", new Class<?>[0]);
		upd.setAccessible(true);
		upd.invoke(NMSPlayer, new Object[0]);
	}
	public static boolean isInWhiteList(UUID uuid)
	{
		for(WhiteListConfig.WLPlayer i : WhiteListPlugin.instance.whitelist.con.players)
		{
			if(i.uuid!=null && i.uuid.equals(uuid))
				return true;
		}
		return false;
	}
	public static boolean isInWhiteList(Player p)
	{
		if(p==null)
			return false;
		return isInWhiteList(p.getUniqueId());
	}
	public static boolean isInWhiteList(OfflinePlayer p)
	{
		if(p==null)
			return false;
		return isInWhiteList(p.getUniqueId());
	}
	public static void addToWhiteListAndSave(WLPlayer p) throws IOException
	{
		try {
		Player p2=Bukkit.getPlayer(p.uuid);
		if(p2!=null && p2.getGameMode() != GameMode.CREATIVE && p2.getGameMode()!=GameMode.SPECTATOR)
		  PlayerUtil.setInv(p2,false);
		}catch(Throwable e) {throw new RuntimeException(e);}
		WhiteListPlugin.instance.whitelist.con.players.add(p);
		WhiteListPlugin.instance.whitelist.saveConfig();
	}
	public static void removeFromWhiteListAndSave(UUID uuid) throws IOException
	{
		try {
		Player p2=Bukkit.getPlayer(uuid);
		if(p2!=null)
		  PlayerUtil.setInv(p2,true);
		}catch(Throwable e) {throw new RuntimeException(e);}
		WhiteListPlugin.instance.whitelist.con.players.removeIf(i->((WLPlayer)i).uuid.equals(uuid));
		WhiteListPlugin.instance.whitelist.saveConfig();
	}
	public static ArrayList<String> playerListToNameList(Vector<Player> v)
	{
		ArrayList<String> ret=new ArrayList<>();
		for(Player i : v)
			ret.add(i.getName());
		return ret;
	}
	public static ArrayList<String> whiteListPlayerListToNameList(Vector<WLPlayer> v)
	{
		ArrayList<String> ret=new ArrayList<>();
		for(WLPlayer i : v)
		{
			OfflinePlayer t=i.toOfflinePlayer();
			if(t!=null)
			{
				String tn=t.getName();
				if(tn!=null)
				  ret.add(tn);
			}
		}
		return ret;
	}
	public static ArrayList<String> offlinePlayerListToNameList(Vector<OfflinePlayer> v)
	{
		ArrayList<String> ret=new ArrayList<>();
		for(OfflinePlayer i : v)
			ret.add(i.getName());
		return ret;
	}
	public static boolean informPlayer(Player p)
	{
		if(p==null || !p.isOnline())
			return false;
		p.sendMessage(informMess);
		return true;
	}
	public static boolean informPlayer(OfflinePlayer p)
	{
		if(p==null || !p.isOnline())
			return false;
		return informPlayer(p.getPlayer());
	}
	public static void massivelyInformPlayer(Player p,long count)
	{
		for(long i=0;i<count;i++)
			informPlayer(p);
	}
	public static ArrayList<String> getOfflinePlayersNameList()
	{
		 return PlayerUtil.offlinePlayerListToNameList(VectorUtil.toVector(Bukkit.getOfflinePlayers()));
	}
	public static ArrayList<String> getUnwhitelistedOfflinePlayersNameList()
	{
		Vector<OfflinePlayer> temp=VectorUtil.toVector(Bukkit.getOfflinePlayers());
		temp.removeIf(i->isInWhiteList(i));
		return offlinePlayerListToNameList(temp);
	}
	public static WLPlayer getWLPlayerByQQ(long qq)
	{
		for(WLPlayer i : WhiteListPlugin.instance.whitelist.con.players)
		{
			if(i.QQ==qq)
				return i;
		}
		return null;
	}
	public static Vector<Long> getQQList()
	{
		Vector<Long> ret=new Vector<>();
		WhiteListPlugin.instance.whitelist.con.players.forEach(i->{
			if(i.QQ!=-1)
				ret.add(i.QQ);
		});
		return ret;
	}
	public static Vector<String> getNoNameWhiteListPlayerUUIDString()
	{
		Vector<String> ret=new Vector<>();
		WhiteListPlugin.instance.whitelist.con.players.forEach(i->{
			if(i.toOfflinePlayer().getName()==null)
				ret.add(i.uuid.toString());
		});
		return ret;
	}
	public static Vector<Inventory> getPages()
	{
		return getPages(null);
	}
	public static Vector<Inventory> getPages(InventoryHolder h)
	{
		Vector<Inventory> pages=new Vector<>();
		Vector<WhiteListConfig.WLPlayer> temp=WhiteListPlugin.instance.whitelist.con.players;
		for(int i=0;i<temp.size();i+=36)
		{
			Inventory t=Bukkit.createInventory(h,45,"白名单列表 "+"第"+(i/36+1)+"页");
			for(int i2=i;i2<i+36&&i2<temp.size();i2++)
			{
				OfflinePlayer top=temp.get(i2).toOfflinePlayer();
				String n=top.getName();
				if(n==null)
					n=top.getUniqueId().toString();
				t.addItem(new ItemBuilder().setType(Material.SKULL_ITEM).setAmount(1).setDamage((short)3).setSkullOwner(top).setDisplayName(n).setLore("QQ号:"+(temp.get(i2).QQ==-1?"未知":temp.get(i2).QQ)).create());
			}
			t.setItem(36,new ItemBuilder().setType(Material.BOOK).setAmount(1).setDisplayName("上一页").create());
			t.setItem(44,new ItemBuilder().setType(Material.BOOK).setAmount(1).setDisplayName("下一页").create());
			pages.add(t);
		}
		return pages;
	}
}
