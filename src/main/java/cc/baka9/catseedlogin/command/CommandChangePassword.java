package cc.baka9.catseedlogin.command;

import cc.baka9.catseedlogin.CatSeedLogin;
import cc.baka9.catseedlogin.Config;
import cc.baka9.catseedlogin.util.Crypt;
import cc.baka9.catseedlogin.util.Util;
import cc.baka9.catseedlogin.database.Cache;
import cc.baka9.catseedlogin.object.LoginPlayer;
import cc.baka9.catseedlogin.object.LoginPlayerHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static cc.baka9.catseedlogin.Config.Settings.debug;
import static cc.baka9.catseedlogin.Config.Settings.forceStrongPasswdEnabled;
import static cc.baka9.catseedlogin.Languages.*;
import java.util.Objects;

public class CommandChangePassword implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String lable, String[] args){
        if (args.length != 3 || !(sender instanceof Player)) {
            return false;
        }
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
        if (!Objects.equals(Crypt.encrypt(name, args[0]), lp.getPassword().trim())) {
            sender.sendMessage(wrongOldPasswd);
            return true;

        }
        if (!args[1].equals(args[2])) {
            sender.sendMessage(notSamePasswd);
            return true;
        }
        if (!Util.passwordIsDifficulty(args[1])) {
            if(forceStrongPasswdEnabled){
                sender.sendMessage(forceStrongPasswd);
                return true;}else {if(debug){CatSeedLogin.instance.getLogger().warning(sender+" used weak password");}}
        }
        if (!Cache.isLoaded) {
            return true;
        }
        Bukkit.getScheduler().runTaskAsynchronously(CatSeedLogin.getInstance(), () -> {
            try {
                lp.setPassword(args[1]);
                lp.crypt();
                CatSeedLogin.sql.edit(lp);
                LoginPlayerHelper.remove(lp);

                Bukkit.getScheduler().runTask(CatSeedLogin.getInstance(), () -> {
                    Player player = Bukkit.getPlayer(((Player) sender).getUniqueId());
                    if (player != null && player.isOnline()) {
                        player.sendMessage(reLog);
                        Config.setOfflineLocation(player);
                        player.teleport(Bukkit.getWorld(Config.Settings.spawnWorld).getSpawnLocation());

                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
                sender.sendMessage(internalError);
            }
        });
        return true;
    }
}
