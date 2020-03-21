package cc.baka9.catseedlogin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Config {
    private static CatSeedLogin plugin = CatSeedLogin.getInstance();
    private static Map<String, String> offlineLocations = new HashMap<>();

    public static class MySQL {
        public static boolean Enable;
        public static String Host;
        public static String Port;
        public static String Database;
        public static String User;
        public static String Password;

        public static void load(){


            FileConfiguration config = getConfig("sql.yml");
            MySQL.Enable = config.getBoolean("MySQL.Enable");
            MySQL.Host = config.getString("MySQL.Host");
            MySQL.Port = config.getString("MySQL.Port");
            MySQL.Database = config.getString("MySQL.Database");
            MySQL.User = config.getString("MySQL.User");
            MySQL.Password = config.getString("MySQL.Password");
        }
    }

    public static class Settings {
        //SETTINGS START
        public static int IpCountLimit;
        public static String spawnWorld;
        public static boolean LimitChineseID;
        public static int MaxLengthID;
        public static int MinLengthID;
        public static boolean BeforeLoginNoDamage;
        public static long ReenterInterval;
        public static boolean AfterLoginBack;
        public static boolean debug=false;
        public static String language="";
        public static boolean forceStrongPasswdEnabled=true;
        //SETTINGS END

        public static void load(){
            Properties properties = new Properties();
            String lvname="";
            boolean lvnameinited = false;
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader("server.properties"));
                properties.load(bufferedReader);
                lvname=properties.getProperty("level-name");
                lvnameinited=true;
            } catch (Exception ex) {
                CatSeedLogin.instance.getLogger().warning("Exception caught in reading server.properties: "+ex.toString());
                ex.printStackTrace();
            }
            FileConfiguration config = getConfig("settings.yml");
            IpCountLimit = config.getInt("maxLogPerIP", 1);
            if(lvnameinited){spawnWorld = config.getString("spawnWorld", lvname);}else {spawnWorld = config.getString("spawnWorld", "world");}
            LimitChineseID = config.getBoolean("specialSymbolsIDLimit", true);
            MinLengthID = config.getInt("IDMinLength", 3);
            MaxLengthID = config.getInt("IDMaxLength", 16);
            BeforeLoginNoDamage = config.getBoolean("noDamageBeforeLogin", true);
            ReenterInterval = config.getLong("loginTimeout", 60);
            AfterLoginBack = config.getBoolean("backAfterLogin", true);
            language=config.getString("languageFile", "lang_zhCN.yml");
            debug=config.getBoolean("debug", false);
            forceStrongPasswdEnabled=config.getBoolean("forceStrongPasswd");
            Languages.load();
            if(debug){CatSeedLogin.instance.getLogger().info("Debug mode enabled.\nSettings load success.\nUsing language file: "+language+"\nLogin world: "+lvname);}
        }
    }

    public static class EmailVerify {

        public static boolean Enable;
        public static String EmailAccount;
        public static String EmailPassword;
        public static String EmailSmtpHost;
        public static String EmailSmtpPort;
        public static boolean SSLAuthVerify;
        public static String FromPersonal;


        public static void load(){
            FileConfiguration config = getConfig("emailVerify.yml");
            Enable = config.getBoolean("Enable");
            EmailAccount = config.getString("EmailAccount");
            EmailPassword = config.getString("EmailPassword");
            EmailSmtpHost = config.getString("EmailSmtpHost");
            EmailSmtpPort = config.getString("EmailSmtpPort");
            SSLAuthVerify = config.getBoolean("SSLAuthVerify");
            FromPersonal = config.getString("FromPersonal");

        }

    }

    public static FileConfiguration getConfig(String yamlFileName){
        File file = new File(plugin.getDataFolder(), yamlFileName);
        if (!file.exists()) {
            plugin.saveResource(yamlFileName, false);
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    public static void load(){
        plugin.saveDefaultConfig();
        FileConfiguration config = plugin.getConfig();
        if (config.contains("offlineLocations")) {
            config.getConfigurationSection("offlineLocations").getKeys(false).forEach(key ->
                    offlineLocations.put(key, config.getString("offlineLocations." + key))
            );
        }
        MySQL.load();
        Settings.load();
        EmailVerify.load();
    }

    public static void reload(){
        plugin.reloadConfig();
        load();

    }

    public static Location getOfflineLocation(Player player){
        String data = offlineLocations.get(player.getName());
        return str2Location(data);
    }

    public static void setOfflineLocation(Player player){
        String name = player.getName();
        String data = loc2String(player.getLocation());
        offlineLocations.put(name, data);
        plugin.getConfig().set("offlineLocations." + name, data);
        plugin.saveConfig();
    }

    private static Location str2Location(String str){
        Location loc;
        String[] locStrs = str.split(":");
        try {

            World world = Bukkit.getWorld(locStrs[0]);
            double x = Double.valueOf(locStrs[1]);
            double y = Double.valueOf(locStrs[2]);
            double z = Double.valueOf(locStrs[3]);
            float yaw = Float.valueOf(locStrs[4]);
            float pitch = Float.valueOf(locStrs[5]);
            loc = new Location(world, x, y, z, yaw, pitch);
        } catch (Exception ignored) {
            loc = Bukkit.getWorld(Config.Settings.spawnWorld).getSpawnLocation();
        }
        return loc;

    }

    private static String loc2String(Location loc){
        return loc.getWorld().getName() + ":" + loc.getX() + ":" + loc.getY() + ":" + loc.getZ() + ":" + loc.getYaw() + ":" + loc.getPitch();

    }


}
