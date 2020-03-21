package cc.baka9.catseedlogin.command;

import cc.baka9.catseedlogin.CatSeedLogin;
import cc.baka9.catseedlogin.Config;
import cc.baka9.catseedlogin.database.Cache;
import cc.baka9.catseedlogin.event.CatSeedPlayerLoginEvent;
import cc.baka9.catseedlogin.object.LoginPlayer;
import cc.baka9.catseedlogin.object.LoginPlayerHelper;
import cc.baka9.catseedlogin.util.Crypt;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static cc.baka9.catseedlogin.Config.Settings.debug;
import static cc.baka9.catseedlogin.Languages.*;
import java.util.Objects;

public class CommandLogin implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String lable, String[] args){
        if (args.length == 0 || !(sender instanceof Player)) return false;
        Player player = (Player) sender;
        String name = player.getName();
        if (LoginPlayerHelper.isLogin(name)) {
            sender.sendMessage(alreadyLog);
            return true;
        }
        LoginPlayer lp = Cache.getIgnoreCase(name);
        if (lp == null) {
            sender.sendMessage(notReg);
            return true;
        }
        if (Objects.equals(Crypt.encrypt(name, args[0]), lp.getPassword().trim())) {
            LoginPlayerHelper.add(lp);
            CatSeedPlayerLoginEvent loginEvent = new CatSeedPlayerLoginEvent(player, lp.getEmail(), CatSeedPlayerLoginEvent.Result.SUCCESS);
            Bukkit.getServer().getPluginManager().callEvent(loginEvent);
            sender.sendMessage(logSuccess);
            if(debug){
                CatSeedLogin.instance.getLogger().info(sender.getName()+" successfully logged in");}
            if (Config.Settings.AfterLoginBack) {
                player.teleport(Config.getOfflineLocation(player));
            }
        } else {
            sender.sendMessage(wrongPasswd);
            if(debug){
                CatSeedLogin.instance.getLogger().warning(sender.getName()+" used wrong password");}
            CatSeedPlayerLoginEvent loginEvent = new CatSeedPlayerLoginEvent(player, lp.getEmail(), CatSeedPlayerLoginEvent.Result.FAIL);
            Bukkit.getServer().getPluginManager().callEvent(loginEvent);
            if (Config.EmailVerify.Enable) {
                sender.sendMessage(wrongPasswdWithEmail);
            }
        }
        return true;
    }
}
