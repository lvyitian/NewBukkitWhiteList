package top.dsbbs2.whitelist.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import top.dsbbs2.whitelist.WhiteListPlugin;
import top.dsbbs2.whitelist.util.PlayerUtil;
import top.dsbbs2.whitelist.util.VectorUtil;

import java.util.Vector;

public class Import implements IChildCommand {
    @Override
    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
        try {

            arg0.sendMessage("导入成功");
        }catch(Throwable e){throw new RuntimeException(e);}
        return true;
    }

    @NotNull
    @Override
    public String getUsage() {
        return "/wl import";
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
        return VectorUtil.toVector("Mojang/Old");
    }

    @NotNull
    @Override
    public String getPermission()
    {
        return "whitelist.add";
    }
}
