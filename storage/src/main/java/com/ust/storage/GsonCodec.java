package com.ust.storage;

import com.google.gson.Gson;

public class GsonCodec{
    ThreadLocal<Gson> threadGson = new ThreadLocal<>();
    
    private Gson getGson()
    {
        Gson gson = threadGson.get();
        if(gson == null)
        {
            gson = new Gson();
            threadGson.set(gson);
        }
        return gson;
    }
    
    public String encode(Object obj)
    {
        return getGson().toJson(obj);
    }
    
    public <T> T decode(Class<T> clazz,String data)
    {
        return getGson().fromJson(data, clazz);
    }
}
