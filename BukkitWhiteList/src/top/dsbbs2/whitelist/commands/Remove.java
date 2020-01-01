package top.dsbbs2.whitelist.commands;

import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import top.dsbbs2.whitelist.util.CommandUtil;
import top.dsbbs2.whitelist.util.PlayerUtil;
import top.dsbbs2.whitelist.util.VectorUtil;

public class Remove implements IChildCommand {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if(arg3.length==2)
		{
			OfflinePlayer op=Bukkit.getOfflinePlayer(arg3[1]);
			if(!PlayerUtil.isInWhiteList(op))
			{
				arg0.sendMessage("�ڰ��������Ҳ��������");
				return true;
			}
			try {
			PlayerUtil.removeFromWhiteListAndSave(op.getUniqueId());
			arg0.sendMessage("�����ɹ�");
			}catch(Throwable e) {e.printStackTrace();arg0.sendMessage("�����쳣����ջ��¼�Ѵ�ӡ������̨");}
		return true;
		}
		return false;
	}
	
	@NotNull
	@Override
	public String getUsage() {
		return "/wl remove <player_name>";
	}
	
	@NotNull
	@Override
	public Vector<Class<?>> getArgumentsTypes()
	{
		return VectorUtil.toVector(String.class);
	}
	
	@NotNull
	@Override
	public Vector<String> getArgumentsDescriptions()
	{
		return VectorUtil.toVector("whitelisted_player");
	}
	
	@NotNull
	@Override
	public String getPermission()
	{
		return "whitelist.remove";
	}
}
