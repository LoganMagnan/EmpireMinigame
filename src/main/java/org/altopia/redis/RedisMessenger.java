package org.altopia.redis;

import org.altopia.empiregame.Main;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RedisMessenger {

    private Main main = Main.getInstance();
    private final Thread thread;
    public static RedisMessenger redisMessenger;
    private static JedisPool jedisPool;
    private static final ExecutorService executor = Executors.newFixedThreadPool(4);

    public JedisPoolConfig getConfig(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(10000);
        jedisPoolConfig.setMaxIdle(32);
        jedisPoolConfig.setMinIdle(8);
        jedisPoolConfig.setTestOnBorrow(true);
        return jedisPoolConfig;
    }

    public RedisMessenger(){
        redisMessenger = this;
        this.thread = new Thread(()->{

            String host = this.main.getSettingsConfig().getConfig().getString("redis.host");
            int port = this.main.getSettingsConfig().getConfig().getInt("redis.port");
            String password = this.main.getSettingsConfig().getConfig().getString("redis.password");

            if (password.isEmpty() || password.equals(" ")){
                jedisPool = new JedisPool(getConfig(),host,6379,0);
            } else {
                jedisPool = new JedisPool(getConfig(),host,port,0,password);
            }

            Jedis jedis = new Jedis(host,port);
            if (!password.isEmpty()){
                jedis.auth(password);
            }
            jedis.subscribe(new RedisChannel()
            ,RedisChannels.WORLD_SYNC.name(),
                    RedisChannels.DATA_SWITCH.name(),
                    RedisChannels.TAB_ADD.name(),
                    RedisChannels.TAB_REMOVE.name(),RedisChannels.CHAT.name());
        });
        this.thread.setDaemon(true); // low priority thread
        this.thread.start();
    }

    public static void publish(String channel,String message){
        executor.execute(()->{
            // trial to see if channel actually exists before pushing
            try {
                RedisChannels redisChannels = RedisChannels.valueOf(channel);
            } catch (Exception ignored){

            }
            try (Jedis jedis = jedisPool.getResource()){
                jedis.publish(channel,message);
            }
        });
    }

}