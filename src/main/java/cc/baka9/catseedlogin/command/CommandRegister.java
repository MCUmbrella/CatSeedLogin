package cc.baka9.catseedlogin.command;

import cc.baka9.catseedlogin.CatSeedLogin;
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
import static cc.baka9.catseedlogin.Config.Settings.forceStrongPasswdEnabled;

public class CommandRegister implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String lable, String[] args){
        if (args.length != 2) return false;
        if(sender instanceof ConsoleCommandSender){CatSeedLogin.instance.getLogger().warning(playerOnly);return false;}

        String name = sender.getName();
        if (LoginPlayerHelper.isLogin(name)) {
            sender.sendMessage(alreadyReg);
            return true;
        }
        if (Cache.getIgnoreCase(name) != null) {
            sender.sendMessage(nameTaken);
            return true;
        }
        if (!args[0].equals(args[1])) {
            sender.sendMessage(notSamePasswd);
            return true;
        }
        if (forceStrongPasswdEnabled && !Util.passwordIsDifficulty(args[0])) {
            sender.sendMessage(forceStrongPasswd);
            return true;
        }
        if (!Cache.isLoaded) {
            return true;
        }
        Bukkit.getScheduler().runTaskAsynchronously(CatSeedLogin.getInstance(), () -> {
            try {
                LoginPlayer lp = new LoginPlayer(name, args[0]);
                lp.crypt();
                CatSeedLogin.sql.add(lp);
                LoginPlayerHelper.add(lp);
                Bukkit.getScheduler().runTask(CatSeedLogin.getInstance(), () -> {
                    CatSeedPlayerRegisterEvent event = new CatSeedPlayerRegisterEvent(Bukkit.getPlayer(sender.getName()));
                    Bukkit.getServer().getPluginManager().callEvent(event);
                });
                sender.sendMessage(regSuccess);
                if(debug){CatSeedLogin.instance.getLogger().info(sender.getName()+" successfully registered");}

            } catch (Exception e) {
                e.printStackTrace();
                sender.sendMessage(internalError);
            }
        });
        return true;

    }
}
