package top.dsbbs2.whitelist.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import top.dsbbs2.whitelist.config.struct.WhiteListConfig;
import top.dsbbs2.whitelist.util.PlayerUtil;

class DedicatedMethods {
    protected static OfflinePlayer tryGetOfflinePlayerByQQ(long QQ, CommandSender s)
    {
        if(QQ==-1)
        {
            s.sendMessage("��֧�ֵĲ���");
            return null;
        }
        WhiteListConfig.WLPlayer wl= PlayerUtil.getWLPlayerByQQ(QQ);
        if(wl==null)
        {
            s.sendMessage("�Ҳ�����QQ������Ӧ�����");
            return null;
        }
        return wl.toOfflinePlayer();
    }
}
