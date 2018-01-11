package com.gigaiot.nlostserver;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.writer.CacheWriter;
import net.sf.ehcache.writer.CacheWriterFactory;

import java.util.Properties;

/**
 * Created by zz on 2017/5/26.
 */
public class WriteBehindFactory extends CacheWriterFactory {

    @Override
    public CacheWriter createCacheWriter(Ehcache ehcache, Properties properties) {

        return new EhcacheWriteBehind();

    }
}
