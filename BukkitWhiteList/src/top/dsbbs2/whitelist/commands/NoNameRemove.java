package top.dsbbs2.whitelist.commands;

import java.util.UUID;
import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import top.dsbbs2.whitelist.util.CommandUtil;
import top.dsbbs2.whitelist.util.PlayerUtil;
import top.dsbbs2.whitelist.util.VectorUtil;

public class NoNameRemove implements IChildCommand {

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
        if(arg3.length==2)
        {
            if(!CommandUtil.ArgumentUtil.isUUID(arg3[1]))
            {
                arg0.sendMessage("无效的UUID");
                return true;
            }
            OfflinePlayer op=Bukkit.getOfflinePlayer(UUID.fromString(arg3[1]));
            if(!PlayerUtil.isInWhiteList(op))
            {
                arg0.sendMessage("在白名单中找不到该玩家");
                return true;
            }
            try {
                PlayerUtil.removeFromWhiteListAndSave(op.getUniqueId());
                arg0.sendMessage("操作成功");
            }catch(Throwable e) {e.printStackTrace();arg0.sendMessage("出现异常，堆栈记录已打印至控制台");}
            return true;
        }
        return false;
    }

    @NotNull
    @Override
    public String getUsage() {
        return "/wl nonameremove <player_uuid>";
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
        return VectorUtil.toVector("noname_player");
    }

    @NotNull
    @Override
    public String getPermission()
    {
        return "whitelist.remove";
    }
}
