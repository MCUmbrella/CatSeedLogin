package cc.baka9.catseedlogin.command;

import cc.baka9.catseedlogin.CatSeedLogin;
import cc.baka9.catseedlogin.Config;
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

public class CommandAdminSetPassword implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String lable, String[] args){
        if (args.length != 2) return false;
        String name = args[0], pwd = args[1];
        if (!Util.passwordIsDifficulty(pwd)) {
            if(forceStrongPasswdEnabled){
                sender.sendMessage(forceStrongPasswd);
                return true;}
        }
        Bukkit.getScheduler().runTaskAsynchronously(CatSeedLogin.getInstance(), () -> {
            LoginPlayer lp = Cache.getIgnoreCase(name);
            if (lp == null) {
                lp = new LoginPlayer(name, pwd);
                lp.crypt();
                try {
                    CatSeedLogin.sql.add(lp);
                    sender.sendMessage(created);
                } catch (Exception e) {
                    sender.sendMessage(db_error);
                    e.printStackTrace();
                }
            } else {
                lp.setPassword(pwd);
                lp.crypt();
                try {
                    CatSeedLogin.sql.edit(lp);
                    sender.sendMessage(String.join(" ", passwdSet_front, lp.getName(), passwdSet_end));
                    LoginPlayer finalLp = lp;
                    Bukkit.getScheduler().runTask(CatSeedLogin.getInstance(), () -> {
                        Player p = Bukkit.getPlayer(finalLp.getName());
                        if (p != null && p.isOnline()) {
                            p.sendMessage(reLog_admin);
                            p.teleport(Bukkit.getWorld(Config.Settings.spawnWorld).getSpawnLocation());
                            LoginPlayerHelper.remove(finalLp);
                        }

                    });
                } catch (Exception e) {
                    sender.sendMessage(db_error);
                    e.printStackTrace();
                }
            }


        });

        return true;
    }
}
