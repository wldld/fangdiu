package com.gigaiot.nlostserver.repository;


import com.gigaiot.nlostserver.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zz on 2017/5/25.
 */

public interface UserRepo extends CrudRepository<User, Integer> {


    User findByNameAndPassword(String nm, String psw);

    User findByEmailAndPassword(String email, String psw);

    User findById(int userId);

    User findByName(String name);

    User findByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "update t_user u set u.flags = ?1 where u.id = ?2")
    int setFlagsFor(int flags, int userId);


    @Transactional
    @Modifying
    @Query(value = "update t_user u set u.password = ?1 where u.id = ?2")
    int setPasswordFor(String password, int userId);

    @Transactional
    @Modifying
    @Query(value = "update t_user u set u.name = ?1 where u.id = ?2")
    int updateUserName(String userName, int userId);

    @Transactional
    @Modifying
    @Query(value = "update t_user u set u.phone = ?1 where u.id = ?2")
    int updatePhone(String phone, int userId);

    @Transactional
    @Modifying
    @Query(value = "update t_user u set u.email = ?1 where u.id = ?2")
    int updateEmail(String email, int userId);

    //    @Transactional
//    @Modifying
//    @Query(value = "update t_user u set u.email = ?1 where u.id = ?2")
//    int updateUser();
    User findByAccountTypeAndTpId(String accountType, String tpId);

}
