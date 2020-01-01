package top.dsbbs2.whitelist.commands;

import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import top.dsbbs2.whitelist.config.struct.WhiteListConfig.WLPlayer;
import top.dsbbs2.whitelist.util.CommandUtil;
import top.dsbbs2.whitelist.util.PlayerUtil;
import top.dsbbs2.whitelist.util.VectorUtil;

public class Add implements IChildCommand {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if(arg3.length<=3 && arg3.length>=2)
		{
			long QQ=-1;
			if(arg3.length==3)
			{
				if(!CommandUtil.ArgumentUtil.isLong(arg3[2]))
				{
					arg0.sendMessage("QQ�ű�����һ������");
					return true;
				}else {
					QQ=Long.parseLong(arg3[2]);
					if(PlayerUtil.getWLPlayerByQQ(QQ)!=null)
					{
						arg0.sendMessage("��QQ�Ѱ�");
						return true;
					}
				}
			}
			OfflinePlayer op=Bukkit.getOfflinePlayer(arg3[1]);
			if(PlayerUtil.isInWhiteList(op))
			{
				arg0.sendMessage("������Ѵ����ڰ�����");
				return true;
			}
			try {
			PlayerUtil.addToWhiteListAndSave(WLPlayer.fromOfflinePlayer(op,QQ));
			arg0.sendMessage("�����ɹ�");
			}catch(Throwable e) {e.printStackTrace();arg0.sendMessage("�����쳣����ջ��¼�Ѵ�ӡ������̨");}
		return true;
		}
		return false;
	}
	
	@NotNull
	@Override
	public String getUsage() {
		return "/wl add <player_name> [QQ]";
	}

	@NotNull
	@Override
	public Vector<Class<?>> getArgumentsTypes()
	{
		return VectorUtil.toVector(String.class,long.class);
	}
	
	@NotNull
	@Override
	public Vector<String> getArgumentsDescriptions()
	{
		return VectorUtil.toVector("unwhitelisted_player","QQNum");
	}
	
	@NotNull
	@Override
	public String getPermission()
	{
		return "whitelist.add";
	}
}
