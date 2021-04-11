package xyz.invisraidinq.redis;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

public class RedisManager {

    private final JedisPool jedisPool;
    private final Jedis jedis;

    private final String redisChannel;

    public RedisManager(RedisPlugin plugin) {
        this.jedisPool = new JedisPool(plugin.getConfig().getString("REDIS.ADDRESS"), plugin.getConfig().getInt("REDIS.PORT"));

        this.jedis = this.jedisPool.getResource();
        if (plugin.getConfig().getBoolean("REDIS.AUTH.ENABLED")) {
            this.jedis.auth(plugin.getConfig().getString("REDIS.AUTH.PASSWORD"));
        }

        this.redisChannel = plugin.getConfig().getString("REDIS.CHANNEL");

        System.out.println("Connected: " + this.jedis.isConnected());

        new Thread(() -> this.jedis.subscribe(this.startPubSub(), this.redisChannel)).start();
    }

    private JedisPubSub startPubSub() {
        return new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                if (channel.equalsIgnoreCase(redisChannel)) {
                    String[] data = message.split("///");
                    JsonObject object = JsonParser.parseString(data[1]).getAsJsonObject();
                    switch (data[0]) {
                        case "UPDATE":
                            Bukkit.getConsoleSender().sendMessage("Received " + data[0] + " for server name " + object.get("serverName").getAsString());
                            System.out.println("Is jibell bad? " + object.get("isJibellBad").getAsString());
                            System.out.println("is invis welsh? " + object.get("isInvisWelsh").getAsString());
                            break;
                        default:
                            break;
                    }
                }
            }
        };
    }

    public JedisPool getJedisPool() {
        return this.jedisPool;
    }

}
