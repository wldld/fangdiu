package com.gigaiot.nlostserver.repository;

import com.gigaiot.nlostserver.entity.Feedback;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by cxm on 2017/10/18.
 */
public interface FeedbackRepo extends CrudRepository<Feedback, Integer> {

}
