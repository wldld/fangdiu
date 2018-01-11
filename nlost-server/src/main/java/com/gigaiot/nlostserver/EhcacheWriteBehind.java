package com.gigaiot.nlostserver;

import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.CacheEntry;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.writer.CacheWriter;
import net.sf.ehcache.writer.writebehind.operations.SingleOperationType;

import java.util.Collection;

/**
 * Created by zz on 2017/5/26.
 */
@Slf4j
public class EhcacheWriteBehind implements CacheWriter {


    @Override
    public CacheWriter clone(Ehcache ehcache) throws CloneNotSupportedException {
        return null;
    }

    @Override
    public void init() {

    }

    @Override
    public void dispose() throws CacheException {

        log.info("dispose");
    }

    @Override
    public void write(Element element) throws CacheException {

        log.info("write");
    }

    @Override
    public void writeAll(Collection<Element> collection) throws CacheException {

        log.info("writeAll");
    }

    @Override
    public void delete(CacheEntry cacheEntry) throws CacheException {

        log.info("delete");
    }

    @Override
    public void deleteAll(Collection<CacheEntry> collection) throws CacheException {

        log.info("deleteAll");
    }

    @Override
    public void throwAway(Element element, SingleOperationType singleOperationType, RuntimeException e) {

        log.info("throwAway");
    }
}
