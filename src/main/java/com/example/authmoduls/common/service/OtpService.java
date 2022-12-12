package com.example.authmoduls.common.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {

    private static final Integer EXPIRE_MIN = 1;

    private LoadingCache<String, Integer> otpCache;

    public void otpService() {
        otpCache = CacheBuilder.newBuilder().
                expireAfterWrite(EXPIRE_MIN, TimeUnit.MINUTES).build(new CacheLoader<String, Integer>() {
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }

    public int generateOtp(String key){
        Random random = new Random();
        int otp = 10000 + random.nextInt(900000);
        otpCache.put(key,otp);
        return otp;

    }

    public int getOtp(String key){
        try{
            return otpCache.get(key);
        } catch (ExecutionException e) {
            return 0;
        }
    }

    public void clearOtp(String key){

        otpCache.invalidate(key);

    }

}
