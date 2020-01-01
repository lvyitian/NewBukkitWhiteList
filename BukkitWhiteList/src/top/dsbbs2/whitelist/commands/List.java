package top.dsbbs2.whitelist.commands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import top.dsbbs2.whitelist.gui.WhiteListGUI;
import top.dsbbs2.whitelist.util.CommandUtil;
import top.dsbbs2.whitelist.util.VectorUtil;

import java.util.Vector;

public class List implements IChildCommand {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if(arg3.length>=1 && arg3.length<=2)
		{
			int page=0;
			if(arg3.length==2)
			  if(!CommandUtil.ArgumentUtil.isInteger(arg3[1])){
			  	arg0.sendMessage("ҳ������Ϊ����.");
			  	return true;
			  }else page=Integer.valueOf(arg3[1])-1;
			  if(page<0)
			  	arg0.sendMessage("��Ч��ҳ��");
			  if(!(arg0 instanceof Player))
			  {
			  	arg0.sendMessage("������ֻ���������ִ��");
			  	return true;
			  }
			  new WhiteListGUI((Player)arg0,page).open();
			  return true;
		}
		return false;
	}
	
	@NotNull
	@Override
	public String getUsage() {
		return "/wl list [num]";
	}
	
	@NotNull
	@Override
	public Vector<Class<?>> getArgumentsTypes()
	{
		return VectorUtil.toVector(int.class);
	}
	
	@NotNull
	@Override
	public Vector<String> getArgumentsDescriptions()
	{
		return VectorUtil.toVector();
	}
	
	@NotNull
	@Override
	public String getPermission()
	{
		return "whitelist.list";
	}
}
