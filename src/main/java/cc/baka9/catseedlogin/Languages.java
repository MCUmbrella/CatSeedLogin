package cc.baka9.catseedlogin;

import org.bukkit.configuration.file.FileConfiguration;

import static cc.baka9.catseedlogin.Config.Settings.debug;
import static cc.baka9.catseedlogin.Config.Settings.language;
import static cc.baka9.catseedlogin.Config.getConfig;

public class Languages {
    //LANG START
    public static String db_error="";
    public static String login_usage="";
    public static String reg_usage="";
    public static String changepass_usage="";
    public static String bindmail_set="";
    public static String bindmail_verify="";
    public static String bindmail_set_usage="";
    public static String bindmail_verify_usage="";
    public static String resetpass_re="";
    public static String resetpass_re_usage="";
    public static String resetpass_re_newpass="";
    public static String reload="";
    public static String plsreg="";
    public static String plslog="";
    public static String db_cache_loaded_front="";
    public static String db_cache_loaded_end="";
    public static String db_cache_error="";
    public static String internalError="";
    public static String reloaded="";
    public static String playerOnly="";
    //LANG BEFORELOGIN
    public static String stillStart="";
    public static String caseSensitiveFront="";
    public static String caseSensitiveEnd="";
    public static String playerOnline_front="";
    public static String playerOnline_end="";
    public static String maxLoginIP="";
    public static String IDSpecialSymbolLimit="";
    public static String minIDLengthLimit_front="";
    public static String minIDLengthLimit_end="";
    public static String maxIDLengthLimit_front="";
    public static String maxIDLengthLimit_end="";
    //LANG REG
    public static String alreadyReg="";
    public static String nameTaken="";
    public static String notSamePasswd="";
    public static String forceStrongPasswd="";
    public static String regSuccess="";
    //LANG LOGIN
    public static String alreadyLog="";
    public static String notReg="";
    public static String logSuccess="";
    public static String wrongPasswd="";
    public static String wrongPasswdWithEmail="";
    //LANG ADMINSETPASSWD
    public static String created="";
    public static String passwdSet_front="";
    public static String passwdSet_end="";
    public static String reLog_admin="";
    //LANG CHANGEPASS
    public static String notLogin="";
    public static String wrongOldPasswd="";
    public static String reLog="";
    //EMAIL
    public static String mailDisabled="";
    public static String alreadyBind="";
    public static String mailSent_front="";
    public static String mailSent_end="";
    public static String mailSending="";
    public static String bindCodeSent_front="";
    public static String bindCodeSent_end="";
    public static String mailError="";
    public static String mailFormatInvalid="";
    public static String binding="";
    public static String binded_front="";
    public static String binded_end="";
    public static String wrongVerifyCode="";
    public static String bind_timeoutOrNotSet="";
    //EMAIL FILE
    public static String bind_subject="";
    public static String bind_code_front ="";
    public static String bind_code_end="";
    public static String bind_name_front="";
    public static String bind_name_middle="";
    public static String bind_name_end="";
    public static String bind_timeout_front="";
    public static String bind_timeout_end="";
    public static String reset_subject="";
    public static String reset_code_front ="";
    public static String reset_code_end="";
    public static String reset_name_front="";
    public static String reset_name_middle="";
    public static String reset_name_end="";
    public static String reset_timeout_front="";
    public static String reset_timeout_end="";
    //LANG RESETPASS
    public static String mailNotSet="";
    public static String resetSuccess="";
    public static String resetCodeSent_front="";
    public static String resetCodeSent_end="";
    public static String reset_timeoutOrNotSet="";
    //UNREG
    public static String unregSuccess="";
    public static String unregUsage="";
    //LANG END
    public static void load()
    {
        FileConfiguration lang = getConfig(language);
        if(lang.getInt("version")!=1){CatSeedLogin.instance.getLogger().warning("\n[!]WARNING=================================================\n" +
                "  You are using an invalid version of language file!\n" +
                "  Some translations may not be displayed correctly!\n" +
                "  You are using version ["+lang.getInt("version")+"]\n" +
                "  but this version of CatSeedLogin requires version ["+1+"].\n" +
                "===========================================================");}
        db_error = lang.getString("db-error");
        login_usage = lang.getString("login-usage");
        reg_usage = lang.getString("reg-usage");
        changepass_usage = lang.getString("changepass-usage");
        bindmail_set = lang.getString("bindmail-set");
        bindmail_verify = lang.getString("bindmail-verify");
        bindmail_set_usage = lang.getString("bindmail-set-usage");
        bindmail_verify_usage = lang.getString("bindmail-verifu-usage");
        resetpass_re = lang.getString("resetpass-re");
        resetpass_re_usage = lang.getString("resetpass-re-usage");
        resetpass_re_newpass = lang.getString("resetpass-re-newpass");
        reload = lang.getString("reload");
        plsreg = lang.getString("plsreg");
        plslog = lang.getString("plslog");
        db_cache_loaded_front = lang.getString("db-cache-loaded-front");
        db_cache_loaded_end = lang.getString("db-cache-loaded-end");
        db_cache_error = lang.getString("db-cache-error");
        internalError = lang.getString("internalError");
        reload = lang.getString("reloaded");
        playerOnly = lang.getString("playerOnly");
        stillStart = lang.getString("stillStart");
        caseSensitiveFront = lang.getString("caseSensitive-front");
        caseSensitiveEnd= lang.getString("caseSensitive-end");
        playerOnline_front = lang.getString("playeronline-front");
        playerOnline_end = lang.getString("playeronline-end");
        maxLoginIP = lang.getString("maxLoginIP");
        IDSpecialSymbolLimit = lang.getString("IDSpecialSymbolLimit");
        minIDLengthLimit_front = lang.getString("minIDLengthLimit-front");
        minIDLengthLimit_end = lang.getString("minIDLengthLimit-end");
        maxIDLengthLimit_front = lang.getString("maxIDLengthLimit-front");
        maxIDLengthLimit_end = lang.getString("maxIDLengthLimit-end");
        alreadyReg = lang.getString("alreadyRegistered");
        nameTaken = lang.getString("nameTaken");
        notSamePasswd = lang.getString("notSamePasswd");
        forceStrongPasswd = lang.getString("forceStrongPasswd");
        regSuccess = lang.getString("regSuccess");
        alreadyLog = lang.getString("alreadyLogged");
        notReg = lang.getString("notRegistered");
        logSuccess = lang.getString("loginSuccess");
        wrongPasswd = lang.getString("wrongPasswd");
        wrongPasswdWithEmail = lang.getString("wrongPasswdWithEmail");
        created = lang.getString("created");
        passwdSet_front=lang.getString("passwdSet-front");
        passwdSet_end=lang.getString("passwdSet-end");
        reLog_admin = lang.getString("reLog-admin");
        notLogin = lang.getString("notLogin");
        wrongOldPasswd = lang.getString("wrongOldPasswd");
        reLog = lang.getString("reLog");
        mailDisabled = lang.getString("mailDisabled");
        alreadyBind = lang.getString("alreadyBind");
        mailSent_front = lang.getString("mailSent-front");
        mailSent_end = lang.getString("mailSent-end");
        mailSending = lang.getString("mailSending");
        bindCodeSent_front = lang.getString("bindCodeSent-front");
        bindCodeSent_end = lang.getString("bindCodeSent-end");
        mailError = lang.getString("mailError");
        mailFormatInvalid = lang.getString("mailFormatInvalid");
        binding = lang.getString("binding");
        binded_front = lang.getString("binded-front");
        binded_end = lang.getString("binded-end");
        wrongVerifyCode = lang.getString("wrongVerifyCode");
        bind_timeoutOrNotSet = lang.getString("bind-timeoutOrNotSet");
        bind_subject = lang.getString("bind-subject");
        bind_code_front = lang.getString("bind-code-front");
        bind_code_end = lang.getString("bind-code-end");
        bind_name_front = lang.getString("bind-name-front");
        bind_name_middle = lang.getString("bind-name-middle");
        bind_name_end = lang.getString("bind-name-end");
        bind_timeout_front = lang.getString("bind-timeout-front");
        bind_timeout_end = lang.getString("bind-timeout-end");
        reset_subject = lang.getString("reset-subject");
        reset_code_front = lang.getString("reset-code-front");
        reset_code_end = lang.getString("reset-code-end");
        reset_name_front = lang.getString("reset-name-front");
        reset_name_middle = lang.getString("reset-name-middle");
        reset_name_end = lang.getString("reset-name-end");
        reset_timeout_front = lang.getString("reset-timeout-front");
        reset_timeout_end = lang.getString("reset-timeout-end");
        mailNotSet = lang.getString("mailNotSet");
        resetSuccess = lang.getString("resetSuccess");
        resetCodeSent_front = lang.getString("resetCodeSent-front");
        resetCodeSent_end = lang.getString("resetCodeSent-end");
        reset_timeoutOrNotSet = lang.getString("reset-timeoutOrNotSet");
        unregSuccess = lang.getString("unregSuccess");
        unregUsage = lang.getString("unregUsage");

        if(debug){CatSeedLogin.instance.getLogger().info("Language files loaded.");}
    }
}
