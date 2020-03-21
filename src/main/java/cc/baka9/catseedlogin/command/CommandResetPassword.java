package cc.baka9.catseedlogin.command;

import cc.baka9.catseedlogin.CatSeedLogin;
import cc.baka9.catseedlogin.Config;
import cc.baka9.catseedlogin.database.Cache;
import cc.baka9.catseedlogin.object.EmailCode;
import cc.baka9.catseedlogin.object.LoginPlayer;
import cc.baka9.catseedlogin.object.LoginPlayerHelper;
import cc.baka9.catseedlogin.util.Mail;
import cc.baka9.catseedlogin.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static cc.baka9.catseedlogin.Config.Settings.debug;
import static cc.baka9.catseedlogin.Config.Settings.forceStrongPasswdEnabled;
import static cc.baka9.catseedlogin.Languages.*;
import java.util.Optional;

public class CommandResetPassword implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args){
        if (args.length == 0 || !(sender instanceof Player)) return false;
        Player player = (Player) sender;
        String name = player.getName();
        LoginPlayer lp = Cache.getIgnoreCase(name);

        if (lp == null) {
            sender.sendMessage(notReg);
            return true;
        }
        if (!Config.EmailVerify.Enable) {
            sender.sendMessage(mailDisabled);
            return true;
        }
        //command forget
        if (args[0].equalsIgnoreCase("forget")) {
            if (lp.getEmail() == null) {
                sender.sendMessage(mailNotSet);
            } else {
                Optional<EmailCode> optionalEmailCode = EmailCode.getByName(name, EmailCode.Type.ResetPassword);
                if (optionalEmailCode.isPresent()) {
                    sender.sendMessage(mailSent_front + optionalEmailCode.get().getEmail() + mailSent_end);
                } else {
                    //20分钟有效期的验证码
                    EmailCode emailCode = EmailCode.create(name, lp.getEmail(), 1000 * 60 * 20, EmailCode.Type.ResetPassword);
                    sender.sendMessage(mailSending);
                    Bukkit.getScheduler().runTaskAsynchronously(CatSeedLogin.getInstance(), () -> {
                        try {
                            Mail.sendMail(emailCode.getEmail(), reset_subject,
                                    reset_code_front + emailCode.getCode() + reset_code_end +
                                            reset_name_front + name + reset_name_middle + emailCode.getCode() + reset_name_end +
                                            reset_timeout_front + (emailCode.getDurability() / (1000 * 60)) + reset_timeout_end);
                            Bukkit.getScheduler().runTask(CatSeedLogin.getInstance(), () -> {
                                sender.sendMessage(resetCodeSent_front + emailCode.getEmail() + resetCodeSent_end);
                                if(debug){CatSeedLogin.instance.getLogger().info(sender.getName()+" sent a password reset Email request");}
                            });
                        } catch (Exception e) {
                            Bukkit.getScheduler().runTask(CatSeedLogin.getInstance(), () -> sender.sendMessage(mailError));
                            e.printStackTrace();
                        }
                    });
                }
            }
            return true;
        }
        //command re
        if (args[0].equalsIgnoreCase("re") && args.length > 2) {
            if (lp.getEmail() == null) {
                sender.sendMessage(mailNotSet);
            } else {
                Optional<EmailCode> optionalEmailCode = EmailCode.getByName(name, EmailCode.Type.ResetPassword);
                if (optionalEmailCode.isPresent()) {
                    EmailCode emailCode = optionalEmailCode.get();
                    String code = args[1], pwd = args[2];

                    if (emailCode.getCode().equals(code)) {
                        if (forceStrongPasswdEnabled && !Util.passwordIsDifficulty(pwd)) {
                            sender.sendMessage(forceStrongPasswd);
                            return true;
                        }
                        Bukkit.getScheduler().runTaskAsynchronously(CatSeedLogin.getInstance(), () -> {
                            lp.setPassword(pwd);
                            lp.crypt();
                            try {
                                CatSeedLogin.sql.edit(lp);
                                EmailCode.removeByName(name, EmailCode.Type.ResetPassword);
                                Bukkit.getScheduler().runTask(CatSeedLogin.getInstance(), () -> {
                                    Player p = Bukkit.getPlayer(lp.getName());
                                    if (p != null && p.isOnline()) {
                                        if (LoginPlayerHelper.isLogin(name)) {
                                            p.sendMessage(reLog);
                                            p.teleport(Bukkit.getWorld(Config.Settings.spawnWorld).getSpawnLocation());
                                            LoginPlayerHelper.remove(lp);

                                        } else {
                                            p.sendMessage(resetSuccess);
                                            if(debug){CatSeedLogin.instance.getLogger().info(sender.getName()+" successfully reset password");}
                                        }
                                    }

                                });
                            } catch (Exception e) {
                                Bukkit.getScheduler().runTask(CatSeedLogin.getInstance(), () -> sender.sendMessage(db_error));
                                e.printStackTrace();
                            }


                        });
                    } else {
                        sender.sendMessage(wrongVerifyCode);
                    }

                } else {
                    sender.sendMessage(reset_timeoutOrNotSet);
                }
            }
            return true;
        }
        return true;
    }
}
