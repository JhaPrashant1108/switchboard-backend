package com.jhaprashant1108.SwitchBoard.repositories.redis;


import com.jhaprashant1108.SwitchBoard.config.AppConstants;
import com.jhaprashant1108.SwitchBoard.models.SwitchModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import static com.jhaprashant1108.SwitchBoard.utils.CommonUtil.getRedisKey;

@Repository
public class SwitchRedisRepo {

    @Autowired
    private RedisTemplate redisTemplate;

    // Constant moved to AppConstants class for better configuration management
    public static final String SWITCH = AppConstants.Redis.SWITCH_HASH_KEY;

    public void createSwitch(SwitchModel switchModel){
        redisTemplate.opsForHash().put(SWITCH ,  getRedisKey(switchModel) , switchModel);
    }

    public SwitchModel readSwitch(String redisKey){
        return (SwitchModel) redisTemplate.opsForHash().get(SWITCH ,  redisKey);
    }

    public SwitchModel updateSwitch(SwitchModel switchModel){
        redisTemplate.opsForHash().put(SWITCH ,  getRedisKey(switchModel) , switchModel);
        return switchModel;
    }

    public void deleteSwitch(String redisKey){
        redisTemplate.opsForHash().delete(SWITCH ,  redisKey);
    }

}
