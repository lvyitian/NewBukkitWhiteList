package top.dsbbs2.whitelist.util;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class EventUtil {
    public static boolean checkAndCancel(Cancellable e, Player p, String mess) throws Throwable
    {
        try {
            if(!PlayerUtil.isInWhiteList(p.getUniqueId()))
            {
                e.setCancelled(true);
                PlayerUtil.setInv(p, true);
                if(mess!=null && !mess.trim().equals(""))
                    p.sendMessage(mess);
                return true;
            }
        }catch(Throwable e2)
        {
            e.setCancelled(true);
            throw e2;
        }
        return false;
    }
    public static boolean checkAndCancel(Cancellable e, Player p) throws Throwable{
        return checkAndCancel(e,p,PlayerUtil.informMess);
    }
}
