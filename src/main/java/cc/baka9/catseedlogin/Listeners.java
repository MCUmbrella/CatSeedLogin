package cc.baka9.catseedlogin;

import cc.baka9.catseedlogin.database.Cache;
import cc.baka9.catseedlogin.object.LoginPlayer;
import cc.baka9.catseedlogin.object.LoginPlayerHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import static cc.baka9.catseedlogin.Languages.*;
import java.util.regex.Pattern;

public class Listeners implements Listener {
    Pattern[] commandWhitelists = new Pattern[]{
            Pattern.compile("/(?i)l(ogin)?(\\z| .*)")
            , Pattern.compile("/(?i)reg(ister)?(\\z| .*)")
            , Pattern.compile("/(?i)resetpassword?(\\z| .*)")
            , Pattern.compile("/(?i)repw?(\\z| .*)")
    };

    private boolean playerIsCitizensNPC(Player p){
        return p.getClass().getName().matches("^net\\.citizensnpcs.*?EntityHumanNPC.*");
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event){
        if (playerIsCitizensNPC(event.getPlayer())) return;
        if (LoginPlayerHelper.isLogin(event.getPlayer().getName())) return;
        String input = event.getMessage().toLowerCase();
        for (Pattern regex : commandWhitelists) {
            if (regex.matcher(input).find()) return;
        }
        event.setCancelled(true);

    }

    @EventHandler
    public void onPlayerLogin(AsyncPlayerPreLoginEvent event){
        if (!Cache.isLoaded) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, stillStart);
            return;
        }
        String name = event.getName();
        LoginPlayer lp = Cache.getIgnoreCase(name);
        if (lp == null) return;
        if (!lp.getName().equals(name)) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, caseSensitiveFront + lp.getName() + caseSensitiveEnd);
            return;
        }
        if (LoginPlayerHelper.isLogin(name)) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, playerOnline_front + lp.getName() + playerOnline_end);
        }
        int count = 0;
        String hostAddress = event.getAddress().getHostAddress();
        for (Player p : Bukkit.getOnlinePlayers()) {
            String ip = p.getAddress().getAddress().getHostAddress();
            if (ip.equals(hostAddress)) {
                count++;
            }
            if (count >= Config.Settings.IpCountLimit) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, maxLoginIP);
                return;
            }
        }


    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event){
        if (playerIsCitizensNPC(event.getPlayer())) return;
        if (LoginPlayerHelper.isLogin(event.getPlayer().getName())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        if (playerIsCitizensNPC(event.getPlayer())) return;
        if (LoginPlayerHelper.isLogin(event.getPlayer().getName())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event){
        if (LoginPlayerHelper.isLogin(event.getPlayer().getName())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if (!(event.getWhoClicked() instanceof Player) || LoginPlayerHelper.isLogin(event.getWhoClicked().getName()))
            return;
        event.setCancelled(true);
    }

    //登陆之前不能攻击
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
        if (!(event.getDamager() instanceof Player)) return;
        if (playerIsCitizensNPC((Player) event.getDamager())) return;
        if (LoginPlayerHelper.isLogin(event.getDamager().getName())) return;
        event.setCancelled(true);
    }

    //登陆之前不会受到伤害
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event){
        if (Config.Settings.BeforeLoginNoDamage) {

            Entity entity = event.getEntity();
            if (entity instanceof Player && !playerIsCitizensNPC((Player) entity)) {
                if (!LoginPlayerHelper.isLogin(entity.getName())) {
                    event.setCancelled(true);
                }

            }

        }

    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event){
        if (event.getTo().equals(Bukkit.getWorld(Config.Settings.spawnWorld).getSpawnLocation())) return;
        if (playerIsCitizensNPC(event.getPlayer())) return;
        if (LoginPlayerHelper.isLogin(event.getPlayer().getName())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event){
        if (playerIsCitizensNPC(event.getPlayer())) return;
        if (LoginPlayerHelper.isLogin(event.getPlayer().getName())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event){
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (playerIsCitizensNPC(player)) return;
        if (LoginPlayerHelper.isLogin(player.getName())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        if (playerIsCitizensNPC(event.getPlayer())) return;
        if (LoginPlayerHelper.isLogin(event.getPlayer().getName())) return;
        if ((Math.abs(event.getFrom().getZ()) - Math.abs(event.getTo().getZ())) == 0
                && (Math.abs(event.getFrom().getX()) - Math.abs(event.getTo().getX())) == 0) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if (!LoginPlayerHelper.isLogin(player.getName())) return;
        Config.setOfflineLocation(player);
        Bukkit.getScheduler().runTaskLater(CatSeedLogin.getInstance(), () -> LoginPlayerHelper.remove(player.getName()), Config.Settings.ReenterInterval);

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player p = event.getPlayer();
        Cache.refresh(p.getName());
        p.teleport(Bukkit.getWorld(Config.Settings.spawnWorld).getSpawnLocation());
    }

    //id只能下划线字母数字
    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event){
        String name = event.getName();
        if (Config.Settings.LimitChineseID) {
            if (!name.matches("^[0-9a-zA-Z_]+$")) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                        IDSpecialSymbolLimit);
            }
        }
        if (name.length() < Config.Settings.MinLengthID) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                    minIDLengthLimit_front + Config.Settings.MinLengthID + minIDLengthLimit_front);
        }
        if (name.length() > Config.Settings.MaxLengthID) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                    maxIDLengthLimit_front + Config.Settings.MaxLengthID + maxIDLengthLimit_end);
        }

    }

}
