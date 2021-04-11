package xyz.invisraidinq.redis;

import redis.clients.jedis.Jedis;

public class RedisPublisher {

    private Jedis jedis;

    private final RedisPlugin plugin;
    private final RedisManager redisManager;

    public RedisPublisher(RedisPlugin plugin, RedisManager redisManager) {
        this.plugin = plugin;
        this.redisManager = redisManager;
    }

    public void publishData(String data) {
        try {
            this.jedis = this.redisManager.getJedisPool().getResource();

            if (this.plugin.getConfig().getBoolean("REDIS.AUTH.ENABLED")) {
                this.jedis.auth(this.plugin.getConfig().getString("REDIS.AUTH.PASSWORD"));
            }

            this.jedis.publish(this.plugin.getConfig().getString("REDIS.CHANNEL"), data);
        } finally {
            this.jedis.close();
        }
    }
}
