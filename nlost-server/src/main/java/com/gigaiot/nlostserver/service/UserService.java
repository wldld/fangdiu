package com.gigaiot.nlostserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gigaiot.nlostserver.NlostError;
import com.gigaiot.nlostserver.NlostServer;
import com.gigaiot.nlostserver.dto.corerequestdto.SingleParamRequestDto;
import com.gigaiot.nlostserver.dto.settingdto.GetSettingsResDto;
import com.gigaiot.nlostserver.dto.userdto.GetUserResDto;
import com.gigaiot.nlostserver.dto.userdto.*;
import com.gigaiot.nlostserver.email.EmailService;
import com.gigaiot.nlostserver.entity.ItemProperty;
import com.gigaiot.nlostserver.entity.User;
import com.gigaiot.nlostserver.repository.ItemPropertyRepo;
import com.gigaiot.nlostserver.repository.UserRepo;
import com.gigaiot.nlostserver.session.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by cxm on 2017/9/25.
 */
@Service
public class UserService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    private ObjectMapper om;
    @Autowired
    EmailService emailService;
    @Autowired
    SessionService sessionService;
    @Autowired
    ItemPropertyRepo itemPropertyRepo;



    public String resetPassword(String params) throws Exception{
        ResetPasswordRequestDto requestDto = om.readValue(params, ResetPasswordRequestDto.class);
//        String userName = requestDto.getUserName();
        String email = requestDto.getEmail();

//        User user = userRepo.findByName(userName);
        User user = userRepo.findByEmail(email);

        if (user == null) {
            return new NlostError(NlostError.NO_SUCH_EMAIL).toString();
        }

//        String email2 = user.getEmail();
//        if (email2 == null || "".equals(email) || !email2.equals(email)) {
//            return new NlostError(NlostError.USERNAME_EMAIL_MISMATCH).toString();
//        }

        String randomPassWord = genRandomNum(6);
        user.setPassword(randomPassWord);
        userRepo.save(user);

        String subject = "N.Lost APP - Reset password";
        String content = new StringBuffer("Dear ").append(user.getName()).append(":<br>")
                .append("You requested to reset the password for N.Lost account.<br> Your new password is ")
                .append("<b style='color:red'>").append(randomPassWord).append("</b>. ").append("After login APP, you must change the password in setting page.<br>")
                .append("If you have any questions, please contact us(support@gigaiot.com).<br>")
                .append("Thank you for using our products.<br>")
                .append("N.Lost APP R&D team").toString();
        boolean flag = emailService.sendMail(subject, content, email);
        if (flag) {
            return NlostServer.SUCCESS;
        }else {
            return new NlostError(NlostError.SEND_EMAIL_FAILED).toString();
        }
    }

    public static String genRandomNum(int pwdLen) {
        // 35是因为数组是从0开始的，26个字母+10个数字
        final int maxNum = 36;
        int i; // 生成的随机数
        int count = 0; // 生成的密码的长度
        char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
                't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
        StringBuffer pwd = new StringBuffer("");
        Random r = new Random();
        while (count < pwdLen) {
            // 生成随机数，取绝对值，防止生成负数，
            i = Math.abs(r.nextInt(maxNum)); // 生成的数最大为36-1
            if (i >= 0 && i < str.length) {
                pwd.append(str[i]);
                count++;
            }
        }
        return pwd.toString();
    }

    public String editUserName(String userId, String params) throws Exception{
        SingleParamRequestDto requestDto = om.readValue(params, SingleParamRequestDto.class);
        String newUserName = requestDto.getData();
        User user = userRepo.findByName(newUserName);
        if (user != null) {
            return new NlostError(NlostError.INVALID_USER_OR_PSD).toString();
        }
        userRepo.updateUserName(newUserName, Integer.valueOf(userId));
        return NlostServer.SUCCESS;
    }

    public String editPhone(String userId, String params) throws Exception {
        SingleParamRequestDto requestDto = om.readValue(params, SingleParamRequestDto.class);
        String phone = requestDto.getData();

        userRepo.updatePhone(phone, Integer.valueOf(userId));

        return NlostServer.SUCCESS;
    }

    public String getUser(String userId) throws Exception{
        User user = userRepo.findById(Integer.valueOf(userId));
        GetUserResDto resDto = new GetUserResDto();
        resDto.setName(user.getName());
        resDto.setPhone(user.getPhone());
        resDto.setEmail(user.getEmail());
        resDto.setPhotoPath(user.getProfilePhoto());
        return om.writeValueAsString(resDto);
    }

    public String updateEmail(String userId, String params) throws Exception{
        UpdateEmailReqDto reqDto = om.readValue(params, UpdateEmailReqDto.class);
        String email = reqDto.getEmail();
        String verifyCode = reqDto.getVerifyCode();
        boolean flag = sessionService.checkEmailVerifyCode(email + "," + verifyCode);
        if (!flag) {
            return new NlostError(NlostError.INVALID_VERIFYCODE).toString();
        }
        userRepo.updateEmail(email, Integer.valueOf(userId));
        Map<String, String> response = new HashMap<>();
        response.put("email", email);
        return om.writeValueAsString(response);
    }

    public String updatePassword(String userId, String params) throws Exception{
        UpdatePasswordReqDto reqDto = om.readValue(params, UpdatePasswordReqDto.class);
        String newPassword = reqDto.getNewPassword();
        String oldPassword = reqDto.getOldPassword();
        User user = userRepo.findById(Integer.valueOf(userId));
        if (user.getPassword().equals(oldPassword)) {
            user.setPassword(newPassword);
            userRepo.save(user);
        }else {
            return new NlostError(NlostError.INVALID_USER_OR_PSD).toString();
        }
        return NlostServer.SUCCESS;
    }

    public String setRepeatAlert(String userId, String onOrOff) throws Exception{
//        SingleParamRequestDto requestDto = om.readValue(params, SingleParamRequestDto.class);
//        String onOrOff = requestDto.getData();
        if ("on".equals(onOrOff) || "off".equals(onOrOff)) {
            ItemProperty itemProperty = itemPropertyRepo.findByKAndItem_Id("repeatAlert", Integer.valueOf(userId));
            if (itemProperty != null) {
                itemProperty.setV(onOrOff);
            }else {
                itemProperty = new ItemProperty();
                User user = userRepo.findById(Integer.valueOf(userId));
                itemProperty.setItem(user);
                itemProperty.setK("repeatAlert");
                itemProperty.setV(onOrOff);
            }
            itemPropertyRepo.save(itemProperty);
        }else {
            return new NlostError(NlostError.INVALID_INPUT).toString();
        }
        return NlostServer.SUCCESS;
    }

    public String setReconnectInform(String userId, String onOrOff) throws Exception{
//        SingleParamRequestDto requestDto = om.readValue(params, SingleParamRequestDto.class);
//        String onOrOff = requestDto.getData();
        if ("on".equals(onOrOff) || "off".equals(onOrOff)) {
            ItemProperty itemProperty = itemPropertyRepo.findByKAndItem_Id("reconnectInform", Integer.valueOf(userId));
            if (itemProperty != null) {
                itemProperty.setV(onOrOff);
            }else {
                itemProperty = new ItemProperty();
                User user = userRepo.findById(Integer.valueOf(userId));
                itemProperty.setItem(user);
                itemProperty.setK("reconnectInform");
                itemProperty.setV(onOrOff);
            }
            itemPropertyRepo.save(itemProperty);
        }else {
            return new NlostError(NlostError.INVALID_INPUT).toString();
        }
        return NlostServer.SUCCESS;
    }

    public String getSettings(String userId) throws Exception{
        User user = userRepo.findById(Integer.valueOf(userId));
        ItemProperty noDisturbingTimeProperty = itemPropertyRepo.findByKAndItem_Id("noDisturbingTimeSelect", Integer.valueOf(userId));
        String noDisturbingTime = null;
        if (noDisturbingTimeProperty != null) {
            noDisturbingTime = noDisturbingTimeProperty.getV();
        }
        if (noDisturbingTime == null || noDisturbingTime.equals("")) {
            noDisturbingTime = "60";
        }
        ItemProperty repeatAlertProperty = itemPropertyRepo.findByKAndItem_Id("repeatAlert", Integer.valueOf(userId));
        String repeatAlert = null;
        if (repeatAlertProperty != null) {
            repeatAlert = repeatAlertProperty.getV();
        }else{
            repeatAlert = "off";
        }
        ItemProperty reconnectInformProperty = itemPropertyRepo.findByKAndItem_Id("reconnectInform", Integer.valueOf(userId));
        String reconnectInform = null;

        if (reconnectInformProperty != null) {
            reconnectInform = reconnectInformProperty.getV();
        }else{
            reconnectInform = "off";
        }

        GetSettingsResDto resDto = new GetSettingsResDto();
        resDto.setUserName(user.getName());
        resDto.setEmail(user.getEmail());
        resDto.setPhotoPath(user.getProfilePhoto());
        resDto.setNoDisturbingTime(Integer.valueOf(noDisturbingTime));
        resDto.setRepeatAlert(repeatAlert);
        resDto.setReconnectInform(reconnectInform);

        return om.writeValueAsString(resDto);
    }

    public String updateUser(int userId, String params) throws Exception {
        UpdateUserReqDto reqDto = om.readValue(params, UpdateUserReqDto.class);
        User user = userRepo.findById(userId);
        user.setName(reqDto.getName());
//        user.setEmail(reqDto.getEmail());
        user.setPhone(reqDto.getPhone());
//        user.setProfilePhoto(reqDto.getPhoto());
        userRepo.save(user);
        return NlostServer.SUCCESS;
    }

}
