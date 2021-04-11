package xyz.invisraidinq.redis;

import com.google.gson.JsonObject;
import org.bukkit.plugin.java.JavaPlugin;

public class RedisPlugin extends JavaPlugin {

    private RedisManager redisManager;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        this.redisManager = new RedisManager(this);
        JsonObject object = new JsonObject();

        object.addProperty("serverName", this.getConfig().getString("SERVER-NAME"));
        object.addProperty("isJibellBad", "Yes");
        object.addProperty("isInvisWelsh", "SheepShagger");

        new RedisPublisher(this, redisManager).publishData("UPDATE///" + object.toString());

    }

    @Override
    public void onDisable() {

    }
}
