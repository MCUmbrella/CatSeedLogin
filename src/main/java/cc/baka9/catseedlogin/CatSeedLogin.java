package cc.baka9.catseedlogin;

import cc.baka9.catseedlogin.command.*;
import cc.baka9.catseedlogin.database.Cache;
import cc.baka9.catseedlogin.database.MySQL;
import cc.baka9.catseedlogin.database.SQL;
import cc.baka9.catseedlogin.database.SQLite;
import cc.baka9.catseedlogin.object.LoginPlayerHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import static cc.baka9.catseedlogin.Languages.*;

public class CatSeedLogin extends JavaPlugin {

    public static CatSeedLogin instance;
    public static SQL sql;

    @Override
    public void onEnable(){
        instance = this;
        //Config
        Config.load();
        sql = Config.MySQL.Enable ? new MySQL(this) : new SQLite(this);
        try {

            sql.init();

            Cache.refreshAll();
        } catch (Exception e) {
            getLogger().warning(db_error);
            e.printStackTrace();
        }
        //Listeners
        getServer().getPluginManager().registerEvents(new Listeners(), this);
        //Commands
        getServer().getPluginCommand("login").setExecutor(new CommandLogin());
        getServer().getPluginCommand("login").setTabCompleter((commandSender, command, s, args)
                -> args.length == 1 ? Collections.singletonList(login_usage) : new ArrayList<>(0));

        getServer().getPluginCommand("register").setExecutor(new CommandRegister());
        getServer().getPluginCommand("register").setTabCompleter((commandSender, command, s, args)
                -> args.length == 1 ? Collections.singletonList(reg_usage) : new ArrayList<>(0));

        getServer().getPluginCommand("changepassword").setExecutor(new CommandChangePassword());
        getServer().getPluginCommand("changepassword").setTabCompleter((commandSender, command, s, args)
                -> args.length == 1 ? Collections.singletonList(changepass_usage) : new ArrayList<>(0));

        getServer().getPluginCommand("adminsetpassword").setExecutor(new CommandAdminSetPassword());

        PluginCommand bindemail = getServer().getPluginCommand("bindemail");
        bindemail.setExecutor(new CommandBindEmail());
        bindemail.setTabCompleter((commandSender, command, s, args) -> {
            if (args.length == 1) {
                return Arrays.asList(bindmail_set, bindmail_verify);
            }
            if (args.length == 2) {
                if (args[0].equals("set")) {
                    return Collections.singletonList(bindmail_set_usage);
                }
                if (args[0].equals("verify")) {
                    return Collections.singletonList(bindmail_verify_usage);
                }
            }
            return Collections.emptyList();
        });
        PluginCommand resetpassword = getServer().getPluginCommand("resetpassword");
        resetpassword.setExecutor(new CommandResetPassword());
        resetpassword.setTabCompleter((commandSender, command, s, args) -> {
            if (args.length == 1) {
                return Arrays.asList("forget", resetpass_re);
            }
            if (args[0].equals("re")) {
                if (args.length == 2) {
                    return Collections.singletonList(resetpass_re_usage);
                }
                if (args.length == 3) {
                    return Collections.singletonList(resetpass_re_newpass);
                }
            }
            return Collections.emptyList();
        });
        PluginCommand catseedlogin = getServer().getPluginCommand("catseedlogin");
        catseedlogin.setExecutor(new CommandCatSeedLogin());
        catseedlogin.setTabCompleter((commandSender, command, s, args) -> {
            if (args.length == 1) {
                return Collections.singletonList(reload);
            }
            return Collections.emptyList();
        });


        //Task
        getServer().getScheduler().runTaskTimer(this, () -> {
            if (!Cache.isLoaded) return;
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!LoginPlayerHelper.isLogin(player.getName())) {
                    if (!LoginPlayerHelper.isRegister(player.getName())) {
                        player.sendMessage(plsreg);
                        continue;
                    }
                    player.sendMessage(plslog);
                }
            }
        }, 0, 20 * 5);


    }

    @Override
    public void onDisable(){
        Bukkit.getOnlinePlayers().forEach(p -> {
            if (!LoginPlayerHelper.isLogin(p.getName())) return;
            Config.setOfflineLocation(p);

        });
        try {
            sql.getConnection().close();
        } catch (Exception e) {
            getLogger().warning(db_error);
            e.printStackTrace();
        }
        super.onDisable();
    }

    public static CatSeedLogin getInstance(){
        return instance;
    }

}
