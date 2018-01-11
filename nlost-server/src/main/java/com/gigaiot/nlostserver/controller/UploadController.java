package com.gigaiot.nlostserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gigaiot.nlostserver.entity.Unit;
import com.gigaiot.nlostserver.entity.User;
import com.gigaiot.nlostserver.repository.UnitRepo;
import com.gigaiot.nlostserver.repository.UserRepo;
import com.gigaiot.nlostserver.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * Created by cxm on 2017/9/29.
 */
@RestController
@RequestMapping("/nlost/ajax.html")
public class UploadController {
    @Autowired
    UserRepo userRepo;
    @Autowired
    UnitRepo unitRepo;
    @Autowired
    ObjectMapper om;


    @RequestMapping(method = RequestMethod.POST, params = "svc=upload/uploadUserPhoto")
    public String uploadUserPhoto(HttpServletRequest req, String sid) throws Exception{
        // 获得项目的路径
        ServletContext sc = req.getSession().getServletContext();
        // 上传位置
//        String path = sc.getRealPath("/img") + "/"; // 设定文件保存的目录
        String path = System.getProperty("user.dir") + "/img/";
        File f= new File(path);
        if (!f.exists()) {
            f.mkdir();
        }

        Session session = (Session)req.getAttribute("session");
        String userId = session.getUserId();
        User user = userRepo.findById(Integer.valueOf(userId));
        if ((user.getProfilePhoto() != null) && !"".equals(user.getProfilePhoto())) {
            File oldPhoto = new File(path + user.getProfilePhoto());
            if (oldPhoto.exists()) {
                oldPhoto.delete();
            }
        }

        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(req.getSession().getServletContext());
        if (multipartResolver.isMultipart(req)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) req;
            Iterator iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                MultipartFile file = multiRequest.getFile(iter.next().toString());
                if (file != null) {
                    String fileName = UUID.randomUUID() + "," +  file.getOriginalFilename();
                    user.setProfilePhoto(fileName);
                    userRepo.save(user);
                    String filePath = path + fileName;
                    File localFile = new File(filePath);
                    file.transferTo(localFile);
                }
            }
        }

        Map<String, String> response = new HashMap<>();
        response.put("profielPhoto", user.getProfilePhoto());
        return om.writeValueAsString(response);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=upload/uploadUnitPhoto")
    public String uploadUnitPhoto(HttpServletRequest req, String sid, int unitId) throws Exception{
        // 获得项目的路径
        ServletContext sc = req.getSession().getServletContext();
        // 上传位置
//        String path = sc.getRealPath("/img") + "/"; // 设定文件保存的目录
        String path = System.getProperty("user.dir") + "/img/";
        File f= new File(path);
        if (!f.exists()) {
            f.mkdir();
        }

        Unit unit = unitRepo.findById(unitId);

        if ((unit.getPhotoPath() != null) && !"".equals(unit.getPhotoPath())) {
            File oldPhoto = new File(path + unit.getPhotoPath());
            if (oldPhoto.exists()) {
                oldPhoto.delete();
            }
        }

        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(req.getSession().getServletContext());
        if (multipartResolver.isMultipart(req)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) req;
            Iterator iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                MultipartFile file = multiRequest.getFile(iter.next().toString());
                if (file != null) {
                    String fileName = UUID.randomUUID() + "," +  file.getOriginalFilename();
                    unit.setPhotoPath(fileName);
                    unitRepo.save(unit);
                    String filePath = path + fileName;
                    File localFile = new File(filePath);
                    file.transferTo(localFile);
                }
            }
        }

        Map<String, String> response = new HashMap<>();
        response.put("unitPhoto", unit.getPhotoPath());
        return om.writeValueAsString(response);
    }

}
