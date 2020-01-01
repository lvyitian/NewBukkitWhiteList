package top.dsbbs2.whitelist;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import io.netty.channel.Channel;
import top.dsbbs2.whitelist.com.comphenix.tinyprotocol.TinyProtocol;
import top.dsbbs2.whitelist.commands.*;
import top.dsbbs2.whitelist.config.SimpleConfig;
import top.dsbbs2.whitelist.config.struct.WhiteListConfig;
import top.dsbbs2.whitelist.listeners.PlayerListener;
import top.dsbbs2.whitelist.util.*;

public class WhiteListPlugin extends JavaPlugin {
	public static volatile WhiteListPlugin instance=null;
	public volatile SimpleConfig<WhiteListConfig> whitelist=new SimpleConfig<>(this.getDataFolder()+"/whitelist.json","UTF8",WhiteListConfig.class);
	public volatile Vector<IChildCommand> childCmds=new Vector<>();
	public volatile TinyProtocol protocol;
	public void registerListeners()
	{
		registerListener(new PlayerListener());
	}
	public void registerListener(Listener lis)
	{
		Bukkit.getPluginManager().registerEvents(lis, this);
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length>0)
		{
			IChildCommand c=CommandUtil.getCommand(this.childCmds,args[0]);
			if(c==null)
			{
				sender.sendMessage("命令 "+args[0]+" 不存在");
			}else {
				if(!c.getPermission().trim().equals("") && !sender.hasPermission(c.getPermission()))
				{
					sender.sendMessage("你没有权限这么做");
					return true;
				}
				if(!c.onCommand(sender, command, label, args))
				{
					String usage=c.getUsage();
					if(!usage.trim().equals(""))
						sender.sendMessage(usage);
				}
			}
			return true;
		}
		return super.onCommand(sender, command, label, args);
	}
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(args.length>0)
            return TabUtil.betterGetStartsWithList(realOnTabComplete(sender,command,alias,args),args[args.length-1]);
	    else
	    	return realOnTabComplete(sender,command,alias,args);
	}
	public List<String> realOnTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(args.length<=1)
			return VectorUtil.toArrayList(CommandUtil.commandListToCommandNameList(childCmds));
		if(args.length>1)
		{
			IChildCommand c=CommandUtil.getCommand(childCmds, args[0]);
			if(c!=null)
			{
				Vector<Class<?>> cats=c.getArgumentsTypes();
				if(cats.size()>args.length-2)
				{
					Class<?> argType=cats.get(args.length-2);
					Vector<String> des=c.getArgumentsDescriptions();
					String desc=null;
					if(des.size()>args.length-2)
						desc=des.get(args.length-2);
					if(desc==null)
					{
						return ListUtil.toList(argType.getSimpleName());
					}else if(desc.equals("player"))
					{
						return PlayerUtil.getOfflinePlayersNameList();
					}else if(desc.equals("unwhitelisted_player"))
					{
						return PlayerUtil.getUnwhitelistedOfflinePlayersNameList();
					}else if(desc.equals("whitelisted_player")){
						return PlayerUtil.whiteListPlayerListToNameList(this.whitelist.con.players);
					}else if(desc.equals("qq")){
						return VectorUtil.toArrayList(VectorUtil.toStringVector(PlayerUtil.getQQList()));
					}else if(desc.equals("noname_player")){
						return VectorUtil.toArrayList(PlayerUtil.getNoNameWhiteListPlayerUUIDString());
					}else if(desc.contains("/")){
						return ListUtil.toList(desc.split("/"));
					}else{
						return ListUtil.toList(desc);
					}
				}
			}
		}
		return new ArrayList<>();
	}
	public void initChildCommands()
	{
		addChildCmd(new Add());
		addChildCmd(new Remove());
		addChildCmd(new QRemove());
		addChildCmd(new QBan());
		addChildCmd(new top.dsbbs2.whitelist.commands.List());
	    addChildCmd(new NoNameRemove());
	    addChildCmd(new Reload());
	    addChildCmd(new Import());
	}
	public void addChildCmd(IChildCommand c)
	{
		this.childCmds.add(c);
	}
	@Override
	public void onLoad()
	{
		instance=this;
		try {
		whitelist.loadConfig();
		}catch(Throwable e) {throw new RuntimeException(e);}
		initChildCommands();
	}
	@Override
	public void onEnable()
	{
		registerListeners();
		protocol = new TinyProtocol(this) {
			@Override
			public Object onPacketInAsync(Player sender, Channel channel, Object packet) {
				if(WhiteListPlugin.instance!=null && WhiteListPlugin.instance.isEnabled() && packet.getClass().getSimpleName().equals("PacketPlayInTabComplete"))
				{
					try {
						if(!PlayerUtil.isInWhiteList(sender))
						{
							PlayerUtil.setInv(sender, true);
							return null;
						}
						}catch(Throwable e2)
						{
							e2.printStackTrace();
							return null;
						}
				}
				
				return super.onPacketInAsync(sender, channel, packet);
			}

			@Override
			public Object onPacketOutAsync(Player reciever, Channel channel, Object packet) {
				if(WhiteListPlugin.instance!=null && WhiteListPlugin.instance.isEnabled() && packet.getClass().getSimpleName().equals("PacketPlayOutTabComplete"))
				{
					try {
						if(!PlayerUtil.isInWhiteList(reciever))
						{
							PlayerUtil.setInv(reciever, true);
							return null;
						}
						}catch(Throwable e2)
						{
							e2.printStackTrace();
							return null;
						}
				}
				
				return super.onPacketOutAsync(reciever, channel, packet);
			}
		};
	}
	@Override
	public void onDisable()
	{
		instance=null;
		if(protocol!=null)
		  protocol.close();
	}
}
