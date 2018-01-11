package com.gigaiot.nlostserver.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gigaiot.nlostserver.dto.eventdto.EventListDto;
import com.gigaiot.nlostserver.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by zz on 2017/6/10.
 */
@Slf4j
@Service
public class SessionService {

    @Autowired
    private RedissonClient redisson;

    @Autowired
    private ObjectMapper om;

    @PostConstruct
    public void init() {

        RPatternTopic<String> tt = redisson.getPatternTopic("__keyevent@0__:expired", StringCodec.INSTANCE);
        tt.addListener(( pattern,  channel,  msg) -> {

            String sid = msg.substring(msg.indexOf(":") + 1);
            log.info("expired value:" + sid);
            RBucket<Session> bucketSession = redisson.getBucket("session:" + sid);
            Session session = bucketSession.get();
            if (session != null ) {

                String userId = session.getUserId();
                try {
                    //DBMessageLog.insertMessageLog(new ObjectId(userId), session.getUserName(), "logout", session.getHost(), "userName");//记录用户退出系统日志
                    detachUser(userId, session.getSid());
                } catch (Exception e) {
                    log.error("Session clear error", e);
                    e.printStackTrace();
                }
                bucketSession.delete();
                log.info( pattern +"  " +  channel  + "  " + sid+ "过期");
            }


        });
    }


    public Session create(User user) {

        Session session = new Session(user);
        String sid = session.getSid();
        attachUser(String.valueOf(user.getId()), sid);

        RBucket<Session> bucketSession = redisson.getBucket("session:" + sid);
        bucketSession.set(session);
        RBucket bucketShadow = redisson.getBucket("shadow:" + sid, StringCodec.INSTANCE);
        bucketShadow.set("", 3, TimeUnit.DAYS);

        return session;
    }

    public void sendEvents(List<String> events, String sid) {
        RDeque<String> queue = redisson.getDeque("queue:" + sid);
        queue.addAll(events);
    }

    public void sendEvent(String event, String sid) {
        RDeque<String> queue = redisson.getDeque("queue:" + sid);
        queue.add(event);
    }

    public void sendItemEvent(String itemId, String event) {


    }

    public void sendUserEvent(String userId, String event) {
        RSetMultimap<String, String> mm = redisson.getSetMultimap("user:session");
        Set<String> sidList = mm.get(userId);
        if (sidList != null) {
            for (String sid: sidList) {
                sendEvent(event, sid);
            }
        }
    }

    public String getEvents(String sid) throws Exception {

        EventListDto evtDto =new EventListDto();
        evtDto.setTm(System.currentTimeMillis());
        RDeque<String> queue = redisson.getDeque("queue:" + sid);
        //System.out.println("qqq" + queue.size());
        List<String> evts = queue.readAll();
        queue.removeAll(evts);
        evtDto.setEvents(evts);

        //System.out.println("获取消息耗时=" + (t2 -t1) +"---" +evt.size());
        return om.writeValueAsString(evtDto);
    }
    
    public Session findBySid(String sid) {
        RBucket<Session> bucketSession = redisson.getBucket("session:" + sid);
        if (bucketSession.isExists()) {
            RBucket bucketShadow = redisson.getBucket("shadow:" + sid, StringCodec.INSTANCE);
//            bucketShadow.expire(2, TimeUnit.HOURS);
            bucketShadow.expire(3, TimeUnit.DAYS);
            return bucketSession.get();
        }else {
            return null;
        }
    }

    public void attachUser(String userId, String sid) {
        RSetMultimap<String, String> mm = redisson.getSetMultimap("user:session");
        mm.put(userId, sid);
    }

    public void detachUser(String userId, String sid) {
        RSetMultimap<String, String> mm = redisson.getSetMultimap("user:session");
        mm.remove(userId, sid);
    }

    public boolean isUserOnline(String userId) {
        RSetMultimap<String, String> mm = redisson.getSetMultimap("user:session");
        if (mm.containsKey(userId)) {
            return true;
        }else{
            return false;
        }
    }

    public void sendEmailVerifyCode(String emailVerifyCode) {
        RDeque<String> queue = redisson.getDeque("emailVerifyCode:" + emailVerifyCode);
        queue.add(emailVerifyCode);
        queue.expire(30, TimeUnit.MINUTES);
    }

    public boolean checkEmailVerifyCode(String emailVerifyCode) {
        RDeque<String> queue = redisson.getDeque("emailVerifyCode:" + emailVerifyCode);
        if (queue.isExists()) {
            queue.delete();
            return true;
        }else{
            return false;
        }
    }

}
