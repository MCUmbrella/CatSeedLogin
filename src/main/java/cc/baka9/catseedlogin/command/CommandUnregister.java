package cc.baka9.catseedlogin.command;

import cc.baka9.catseedlogin.CatSeedLogin;
import cc.baka9.catseedlogin.Config;
import cc.baka9.catseedlogin.database.Cache;
import cc.baka9.catseedlogin.event.CatSeedPlayerRegisterEvent;
import cc.baka9.catseedlogin.object.LoginPlayer;
import cc.baka9.catseedlogin.object.LoginPlayerHelper;
import cc.baka9.catseedlogin.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import static cc.baka9.catseedlogin.Config.Settings.debug;
import static cc.baka9.catseedlogin.Languages.*;

public class CommandUnregister implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) return false;
        if(sender instanceof ConsoleCommandSender){CatSeedLogin.instance.getLogger().warning(playerOnly);return false;}
        String name = sender.getName();
        LoginPlayer lp = Cache.getIgnoreCase(name);
        if (lp == null) {
            sender.sendMessage(notReg);
            return true;
        }
        if (!LoginPlayerHelper.isLogin(name)) {
            sender.sendMessage(notLogin);
            return true;
        }
        if (!args[0].equals(args[1])) {
            sender.sendMessage(notSamePasswd);
            return true;
        }

        Bukkit.getScheduler().runTaskAsynchronously(CatSeedLogin.getInstance(), () -> {
            try {
                CatSeedLogin.sql.del(lp.getName());
                LoginPlayerHelper.remove(lp);
                Bukkit.getScheduler().runTask(CatSeedLogin.getInstance(), () -> {
                    Player player = Bukkit.getPlayer(((Player) sender).getUniqueId());
                    if (player != null && player.isOnline()) {
                        if(debug){CatSeedLogin.instance.getLogger().info(sender.getName()+" unregistered");}
                        player.teleport(Bukkit.getWorld(Config.Settings.spawnWorld).getSpawnLocation());
                    }
                });
                Bukkit.getServer().getPlayer(sender.getName()).kickPlayer(unregSuccess);
            } catch (Exception e) {
                e.printStackTrace();
                sender.sendMessage(internalError);
            }
        });

        return false;
    }
}
