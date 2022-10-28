package kr.ac.sejong.web.env;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ChainMap<K, V> implements Map<K,V> {

    private final List<Map<K,V>> srcs;

    public ChainMap(Collection<Map<K,V>> maps){
        this.srcs = new ArrayList<>(maps);
    }
    @SafeVarargs
    @Deprecated
    public ChainMap(Map<K,V> ...maps){
        this.srcs = Arrays.asList(maps);
    }

    public void addFirst(Map<K,V> map){
        this.srcs.add(0, map);
    }

    public void addLast(Map<K,V> map){
        this.srcs.add(map);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.srcs.stream().anyMatch( src -> src.containsKey(key));
    }

    @Override
    public boolean containsValue(Object value) {
        return this.srcs.stream().anyMatch( src -> src.containsValue(value));
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return this.srcs
            .stream()
            .map(Map::keySet)
            .flatMap(Collection::stream)
            .collect(Collectors.toSet())
            .stream()
            .map(this::makeEntry)
            .collect(Collectors.toSet())
            ;
    }
    private Entry<K,V> makeEntry(K k){
        return new AbstractMap.SimpleEntry<>(k, this.get(k));
    }

    @Override
    public V get(Object key) {
        return this.srcs.stream()
            .map( src ->
                    src.get(key)
            )
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(null);
    }

    @Override
    public boolean isEmpty() {
        return this.srcs.stream().allMatch(Map::isEmpty);
    }

    @Override
    public Set<K> keySet() {
        return this.srcs.stream()
            .map(Map::keySet)
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());      
    }

    @Override
    public V put(K arg0, V arg1) {
        throw new UnsupportedOperationException();     
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> arg0) {
        throw new UnsupportedOperationException();        
    }

    @Override
    public V remove(Object arg0) {
        throw new UnsupportedOperationException();        
    }

    @Override
    public int size() {
        return this.keySet().size();
    }

    @Override
    public Collection<V> values() {
        return this.srcs.stream()
            .map(Map::values)
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());         
    }

    
}
