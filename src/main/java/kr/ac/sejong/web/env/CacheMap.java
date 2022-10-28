package kr.ac.sejong.web.env;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class CacheMap<K,V> extends ConcurrentHashMap<K,V>{

    private final Function<K,V> producer;
    
    public CacheMap(Function<K,V> producer){        
        this.producer = producer;
    }
    @SuppressWarnings({"unused","unchecked"})
    @Override
    public V get(Object key) {        
        V value = super.get(key);
        if(value != null)
            return value;
        synchronized(this){
            if(value != null)
                return value;
            try {            
                K k = (K)key;
                value = this.producer.apply(k);
                if(value == null) return null;
                this.put(k, value);
                return value;
            } catch (ClassCastException e) {
                return null;
            }
        }
    }
}
