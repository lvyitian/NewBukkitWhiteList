package top.dsbbs2.whitelist.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import top.dsbbs2.whitelist.config.struct.WhiteListConfig;
import top.dsbbs2.whitelist.util.CommandUtil;
import top.dsbbs2.whitelist.util.PlayerUtil;
import top.dsbbs2.whitelist.util.VectorUtil;

import java.util.Vector;

public class QRemove implements IChildCommand {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if(arg3.length==2)
		{
			if(!CommandUtil.ArgumentUtil.isLong(arg3[1]))
			{
				arg0.sendMessage("QQ号必须是一个整数");
				return true;
			}else {
				OfflinePlayer op=DedicatedMethods.tryGetOfflinePlayerByQQ(Long.parseLong(arg3[1]),arg0);
				if(op==null)
					return true;
				try {
					PlayerUtil.removeFromWhiteListAndSave(op.getUniqueId());
					arg0.sendMessage("操作成功");
				}catch(Throwable e) {e.printStackTrace();arg0.sendMessage("出现异常，堆栈记录已打印至控制台");}
			}
			return true;
		}
		return false;
	}
	
	@NotNull
	@Override
	public String getUsage() {
		return "/wl qremove <qq>";
	}
	
	@NotNull
	@Override
	public Vector<Class<?>> getArgumentsTypes()
	{
		return VectorUtil.toVector(long.class);
	}
	
	@NotNull
	@Override
	public Vector<String> getArgumentsDescriptions()
	{
		return VectorUtil.toVector("qq");
	}
	
	@NotNull
	@Override
	public String getPermission()
	{
		return "whitelist.remove";
	}
}
