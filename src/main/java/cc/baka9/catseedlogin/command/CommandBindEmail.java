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
import static cc.baka9.catseedlogin.Languages.*;
import java.util.Optional;


public class CommandBindEmail implements CommandExecutor {


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
        if (!LoginPlayerHelper.isLogin(name)) {
            sender.sendMessage(notLogin);
            return true;
        }
        if (!Config.EmailVerify.Enable) {
            sender.sendMessage(mailDisabled);
            return true;
        }

        // command set email
        if (args[0].equalsIgnoreCase("set") && args.length > 1) {
            if (lp.getEmail() != null && Util.checkMail(lp.getEmail())) {
                sender.sendMessage(alreadyBind);
            } else {
                String mail = args[1];
                Optional<EmailCode> bindEmailOptional = EmailCode.getByName(name, EmailCode.Type.Bind);
                if (bindEmailOptional.isPresent() && bindEmailOptional.get().getEmail().equals(mail)) {
                    sender.sendMessage(mailSent_front + mail + mailSent_end);

                } else if (Util.checkMail(mail)) {
                    //创建有效期为20分钟的验证码
                    EmailCode bindEmail = EmailCode.create(name, mail, 1000 * 60 * 20, EmailCode.Type.Bind);
                    sender.sendMessage(mailSending);
                    Bukkit.getScheduler().runTaskAsynchronously(CatSeedLogin.getInstance(), () -> {
                        try {
                            Mail.sendMail(mail, bind_subject,
                                    bind_code_front + bindEmail.getCode() + bind_code_end +
                                            bind_name_front + name + bind_name_middle + bindEmail.getCode() + bind_name_end + bind_timeout_front + (bindEmail.getDurability() / (1000 * 60)) + bind_timeout_end);
                            Bukkit.getScheduler().runTask(CatSeedLogin.getInstance(), () -> {
                                sender.sendMessage(bindCodeSent_front + mail + bindCodeSent_end);
                            });
                        } catch (Exception e) {
                            Bukkit.getScheduler().runTask(CatSeedLogin.getInstance(), () -> sender.sendMessage(mailError));
                            e.printStackTrace();
                        }
                    });


                } else {
                    sender.sendMessage(mailFormatInvalid);
                }
            }
            return true;
        }

        // command verify code
        if (args[0].equalsIgnoreCase("verify") && args.length > 1) {
            if (lp.getEmail() != null && Util.checkMail(lp.getEmail())) {
                sender.sendMessage(alreadyBind);
            } else {
                Optional<EmailCode> emailOptional = EmailCode.getByName(name, EmailCode.Type.Bind);
                if (emailOptional.isPresent()) {
                    EmailCode bindEmail = emailOptional.get();
                    String code = args[1];
                    if (bindEmail.getCode().equals(code)) {
                        sender.sendMessage(binding);
                        Bukkit.getScheduler().runTaskAsynchronously(CatSeedLogin.getInstance(), () -> {
                            try {
                                lp.setEmail(bindEmail.getEmail());
                                CatSeedLogin.sql.edit(lp);
                                Bukkit.getScheduler().runTask(CatSeedLogin.getInstance(), () -> {
                                    Player syncPlayer = Bukkit.getPlayer(((Player) sender).getUniqueId());
                                    if (syncPlayer != null && syncPlayer.isOnline()) {
                                        syncPlayer.sendMessage(binded_front + bindEmail.getEmail() + binded_end);
                                        EmailCode.removeByName(name, EmailCode.Type.Bind);
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                                sender.sendMessage(internalError);
                            }
                        });

                    } else {
                        sender.sendMessage(wrongVerifyCode);
                    }

                } else {
                    sender.sendMessage(bind_timeoutOrNotSet);
                }


            }
            return true;
        }

        return true;
    }
}
