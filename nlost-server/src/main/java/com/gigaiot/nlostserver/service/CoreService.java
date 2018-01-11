package com.gigaiot.nlostserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gigaiot.nlostserver.NlostClasses;
import com.gigaiot.nlostserver.NlostError;
import com.gigaiot.nlostserver.NlostServer;
import com.gigaiot.nlostserver.dto.corerequestdto.LoginRequestDto;
import com.gigaiot.nlostserver.dto.corerequestdto.LoginSucceedDto;
import com.gigaiot.nlostserver.dto.corerequestdto.NewRegisterReqDto;
import com.gigaiot.nlostserver.dto.emailDto.GetVerifyCodeReqDto;
import com.gigaiot.nlostserver.dto.metadto.UserDto;
import com.gigaiot.nlostserver.email.EmailService;
import com.gigaiot.nlostserver.entity.ItemProperty;
import com.gigaiot.nlostserver.entity.ItemRight;
import com.gigaiot.nlostserver.entity.TpLoginReqDto;
import com.gigaiot.nlostserver.entity.User;
import com.gigaiot.nlostserver.exception.InvalidUserNameException;
import com.gigaiot.nlostserver.repository.ItemPropertyRepo;
import com.gigaiot.nlostserver.repository.ItemRepo;
import com.gigaiot.nlostserver.repository.ItemRightRepo;
import com.gigaiot.nlostserver.repository.UserRepo;
import com.gigaiot.nlostserver.session.Session;
import com.gigaiot.nlostserver.session.SessionService;
import com.gigaiot.nlostserver.utils.HttpClientUtil;
import com.gigaiot.nlostserver.utils.ValidateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import java.util.UUID;

import static com.gigaiot.nlostserver.utils.HttpClientUtil.createSSLClient;

/**
 * Created by zz on 2017/6/5.
 */

@Slf4j
@Service
public class CoreService {

    @Autowired
    ObjectMapper om;
    @Autowired
    UserRepo userRepo;
    @Autowired
    SessionService sessionService;
    @Autowired
    EmailService emailService;
    @Autowired
    ItemRepo itemRepo;
    @Autowired
    ItemRightRepo itemRightRepo;
    @Autowired
    ItemPropertyRepo itemPropertyRepo;
    @Value("${defaultNoDisturbingTimeList}")
    private String defaultNoDisturbingTimeList;
    @Value("${defaultNoDisturbingTimeSelect}")
    private String defaultNoDisturbingTimeSelect;

    @Value("${appIpAddress}")
    private String appIpAddress;
    @Value("${server.port}")
    private String appPort;


    public String login(String host, LoginRequestDto reqDto) throws Exception {

        long t1 = System.currentTimeMillis();
        User user = userRepo.findByEmailAndPassword(reqDto.getEmail(), reqDto.getPassword());
        if (user != null) {
            //
            user.setLastLoginHost(host);
            user.setLastLoginTime(System.currentTimeMillis());
            userRepo.save(user);

            Session session = sessionService.create(user);

            UserDto userDto = new UserDto();
            userDto.setName(user.getName());
            userDto.setId(user.getId());
            userDto.setEmail(user.getEmail());
            userDto.setPhone(user.getPhone());
            userDto.setPhotoPath(user.getProfilePhoto());
//            userDto.setCls(1);

//            userDto.setFl(user.getFlags());
//            String[] creators = user.getCreators().split(";");
//            userDto.setCrt(creators[creators.length - 1].replaceAll("\\[|\\]", ""));

//            userDto.setBact(String.valueOf(user.getAccountId()));

//            Set<ItemRight> itemRights = user.getItemRights();
//            itemRights.forEach(itemRight -> {
//                if (itemRight.getUnitId() == user.getUnitId()) {
//                    userDto.setUacl(itemRight.getValue());
//                }
//            });


//            HashMap<String, String> prp = new HashMap<>();
//            user.getProperties().forEach(itemProperty -> {
//                prp.put(itemProperty.getK(), itemProperty.getV());
//            });
//
//            userDto.setPrp(prp);

            LoginSucceedDto loginSucceedDto = new LoginSucceedDto();
//            loginSucceedDto.setHost(host);
            loginSucceedDto.setSid(session.getSid());
            loginSucceedDto.setUser(userDto);

            String json = om.writeValueAsString(loginSucceedDto);

            log.info("登录耗时 " + (System.currentTimeMillis() - t1));
//            System.out.println("登录耗时 " + (System.currentTimeMillis() - t1));

            return json;

        } else {
            throw new InvalidUserNameException("");
        }
    }

    public void logout(String userId, String sid) {
        sessionService.detachUser(userId, sid);
    }

    public String getVerifyCode(String params) throws Exception {
        GetVerifyCodeReqDto requestDto = om.readValue(params, GetVerifyCodeReqDto.class);
        String email = requestDto.getEmail();
        String userName = requestDto.getUserName();

        User user = userRepo.findByEmail(email);
        if (user != null) {
            return new NlostError(NlostError.EMAIL_HAS_BEEN_REGISTERED).toString();
        }

        String verifyCode = produceVerifyCode(6);
        sessionService.sendEmailVerifyCode(email + "," + verifyCode);

        String subject = "N.Lost APP - Email verification";
        String context = new StringBuffer("Dear ").append(userName).append(":<br><br>")
                .append("You are registering.<br>")
                .append("Your code is ").append("<b style='color:red'>").append(verifyCode).append("</b>. ")
                .append("Please fill it in the code input box.<br>")
                .append("If you have any questions, please contact us(support@gigaiot.com).<br>")
                .append("Thank you for using our products.<br><br>")
                .append("N.Lost APP R&D team").toString();
        boolean flag =  emailService.sendMail(subject, context, email);
        if (flag) {
            return NlostServer.SUCCESS;
        }else{
            return new NlostError(NlostError.SEND_EMAIL_FAILED).toString();
        }
    }

//    public String register(String params) throws Exception{
//        RegisterReqDto reqDto = om.readValue(params, RegisterReqDto.class);
//        String userName = reqDto.getUserName();
//        String password = reqDto.getPassword();
//        String email = reqDto.getEmail();
//        int verifyCode = reqDto.getVerifyCode();
//
//        User user = userRepo.findByName(reqDto.getUserName());
//        if (user != null) {   //用户名已存在
//            throw new InvalidUserNameException("");
//        }
//
//        boolean flag = sessionService.checkEmailVerifyCode(email + "," + verifyCode);
//        if (!flag) {
//            return new NlostError(NlostError.INVALID_VERIFYCODE).toString();
//        }
//
//        User newUser = new User();
//        newUser.setName(userName);
//        newUser.setPassword(password);
//        newUser.setFlags(255);
//        newUser.setCls(NlostClasses.USER);
//        newUser.setCreateTime(System.currentTimeMillis());
//        newUser.setEmail(email);
//        itemRepo.save(newUser);
//
////        ItemProperty emailProperty = new ItemProperty();
////        emailProperty.setItem(newUser);
////        emailProperty.setK("email");
////        emailProperty.setV(reqDto.getEmail());
////        itemPropertyRepo.save(emailProperty);
//
////        ItemProperty noDisturbingTimeList = new ItemProperty();
////        noDisturbingTimeList.setItem(newUser);
////        noDisturbingTimeList.setK("noDisturbingTimeList");
////        noDisturbingTimeList.setV(defaultNoDisturbingTimeList);
////        itemPropertyRepo.save(noDisturbingTimeList);
////
////        ItemProperty noDisturbingTimeSelect = new ItemProperty();
////        noDisturbingTimeSelect.setItem(newUser);
////        noDisturbingTimeSelect.setK("noDisturbingTimeSelect");
////        noDisturbingTimeSelect.setV(defaultNoDisturbingTimeSelect);
////        itemPropertyRepo.save(noDisturbingTimeSelect);
//
//        ItemRight selfItemRight = new ItemRight();
//        selfItemRight.setUserId(newUser.getId());
//        selfItemRight.setItem(newUser);
//        selfItemRight.setValue(2146947L);
//        itemRightRepo.save(selfItemRight);
//
//        return NlostServer.SUCCESS;
//    }

    public String register(String params) throws Exception{
//        RegisterReqDto reqDto = om.readValue(params, RegisterReqDto.class);
        NewRegisterReqDto reqDto = om.readValue(params, NewRegisterReqDto.class);
        String email = reqDto.getEmail();
        String password = reqDto.getPassword();

        User user = userRepo.findByEmail(email);
        if (user != null) {
            return new NlostError(NlostError.EMAIL_HAS_BEEN_REGISTERED).toString();
        }

        boolean flag = sessionService.checkEmailVerifyCode(email + ":true");
        if (!flag) {
            return new NlostError(NlostError.INVALID_VERIFYCODE).toString();
        }

        User newUser = new User();
        newUser.setName("user");
        newUser.setPassword(password);
        newUser.setFlags(255);
        newUser.setCls(NlostClasses.USER);
        newUser.setCreateTime(System.currentTimeMillis());
        newUser.setEmail(email);
        itemRepo.save(newUser);


//        ItemProperty noDisturbingTimeList = new ItemProperty();
//        noDisturbingTimeList.setItem(newUser);
//        noDisturbingTimeList.setK("noDisturbingTimeList");
//        noDisturbingTimeList.setV(defaultNoDisturbingTimeList);
//        itemPropertyRepo.save(noDisturbingTimeList);
//
        ItemProperty noDisturbingTimeSelect = new ItemProperty();
        noDisturbingTimeSelect.setItem(newUser);
        noDisturbingTimeSelect.setK("noDisturbingTimeSelect");
        noDisturbingTimeSelect.setV(defaultNoDisturbingTimeSelect);
        itemPropertyRepo.save(noDisturbingTimeSelect);

        ItemRight selfItemRight = new ItemRight();
        selfItemRight.setUserId(newUser.getId());
        selfItemRight.setItem(newUser);
        selfItemRight.setValue(2146947L);
        itemRightRepo.save(selfItemRight);

        return NlostServer.SUCCESS;
    }

    /**
     * 生成随机码
     *
     * @param pwdLen
     *            生成的随机码的总长度
     * @return 随机码的字符串
     */
    public static String produceVerifyCode(int pwdLen) {
        // 9是因为数组是从0开始的
        final int maxNum = 10;
        int i; // 生成的随机数
        int count = 0; // 生成的随机码的长度
        char[] str = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
        StringBuffer pwd = new StringBuffer("");
        Random r = new Random();
        while (count < pwdLen) {
            // 生成随机数，取绝对值，防止生成负数，
            i = Math.abs(r.nextInt(maxNum)); // 生成的数最大为10-1
            if (i >= 0 && i < str.length) {
                pwd.append(str[i]);
                count++;
            }
        }
        return pwd.toString();
    }

    public boolean checkUserName(String userName) {
        User user = userRepo.findByName(userName);
        if (user == null) {
            return true;
        }else{
            return false;
        }
    }

    public String sendVerifyUrl(String email) throws Exception{
        User user = userRepo.findByEmail(email);
        if (user != null) {
            String str = new NlostError(NlostError.EMAIL_HAS_BEEN_REGISTERED).toString();
            return str;
        }
        String verifyCode = produceVerifyCode(6);
        sessionService.sendEmailVerifyCode(email + "," + verifyCode);


        String keyValue = email + "," + verifyCode;
        String base64KeyValue = ValidateUtils.getBase64(keyValue);
        String hrefUrl="http://" + appIpAddress + ":" + appPort +"/nlost/ajax.html/verifyEmail?key=" + base64KeyValue;

        String subject = "N.Lost APP - Email verification";
        String context = new StringBuffer("Hello, You are registering.<br>")
                .append("please click the link below to finish the verification.<br>")
                .append("<a href=\"").append(hrefUrl).append("\">").append("Verify your email address</a><br>")
                .append("If you have any questions, please contact us(support@gigaiot.com).<br>")
                .append("Thank you for using our products.<br><br>")
                .append("N.Lost APP R&D team").toString();
        boolean flag =  emailService.sendMail(subject, context, email);
        if (flag) {
            return NlostServer.SUCCESS;
        }else{
            return new NlostError(NlostError.SEND_EMAIL_FAILED).toString();
        }
    }

    public String verifyEmail(String email, String verifyCode){
        boolean flag = sessionService.checkEmailVerifyCode(email + "," + verifyCode);
        if (!flag) {
            return "<script>alert(\"verify error\")</script>";
        }
        sessionService.sendEmailVerifyCode(email + ":true");
//        StringBuffer response = new StringBuffer("<html> ")
//                .append("<head> </head> ")
//                .append("<body> ")
//                .append("<p>success</p>")
//                .append("</body>")
//                .append("</html>");
        StringBuffer response = new StringBuffer("<script>alert(\"verify success\")</script>");
        return response.toString();
    }

    public String tpLogin(String host, String params) throws Exception{
        long t1 = System.currentTimeMillis();
        TpLoginReqDto reqDto = om.readValue(params, TpLoginReqDto.class);
        log.debug("origin Icon:" + reqDto.getIcon());
        reqDto.setType(reqDto.getType().toLowerCase());//统一小写
        User user = userRepo.findByAccountTypeAndTpId(reqDto.getType(), reqDto.getId());
        if (user == null) {
            user = new User();
            user.setAccountType(reqDto.getType());
            user.setTpId(reqDto.getId());
            user.setEmail(UUID.randomUUID().toString().replaceAll("-", ""));
            user.setPassword(UUID.randomUUID().toString().replaceAll("-", ""));
            user.setName(reqDto.getUsername());
            String photo = null;
            try {
                photo = saveTpLoginIcon(reqDto.getIcon(), user.getProfilePhoto(), reqDto.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (photo != null) {
                user.setProfilePhoto(photo);
            }

            user.setFlags(255);
            user.setCls(NlostClasses.USER);
            user.setCreateTime(System.currentTimeMillis());

            user.setLastLoginHost(host);
            user.setLastLoginTime(System.currentTimeMillis());
            userRepo.save(user);

            ItemProperty noDisturbingTimeSelect = new ItemProperty();
            noDisturbingTimeSelect.setItem(user);
            noDisturbingTimeSelect.setK("noDisturbingTimeSelect");
            noDisturbingTimeSelect.setV(defaultNoDisturbingTimeSelect);
            itemPropertyRepo.save(noDisturbingTimeSelect);

            ItemRight selfItemRight = new ItemRight();
            selfItemRight.setUserId(user.getId());
            selfItemRight.setItem(user);
            selfItemRight.setValue(2146947L);
            itemRightRepo.save(selfItemRight);
        }else {
            user.setLastLoginHost(host);
            user.setLastLoginTime(System.currentTimeMillis());
            userRepo.save(user);
        }


        Session session = sessionService.create(user);

        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhone());
        userDto.setPhotoPath(user.getProfilePhoto());
        userDto.setPwd(user.getPassword());

        LoginSucceedDto loginSucceedDto = new LoginSucceedDto();
        loginSucceedDto.setSid(session.getSid());
        loginSucceedDto.setUser(userDto);

        String json = om.writeValueAsString(loginSucceedDto);

        log.info("第三方登录耗时 " + (System.currentTimeMillis() - t1));

        return json;
    }

    public static String saveTpLoginIcon(String iconUrl, String oldPhotoPath, String type) {
        CloseableHttpClient httpClient = null;
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        CloseableHttpClient httpsClient = HttpClientUtil.createSSLClient();
        HttpGet httpGet = new HttpGet(iconUrl);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();//设置请求和传输超时时间
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        InputStream in = null;
        OutputStream out= null;

        String path = System.getProperty("user.dir") + "/img/";
        String fileName = UUID.randomUUID().toString().replace("-", "");
        log.debug("type:" + type);
        if ("googleplus".equals(type)) {
            fileName = fileName  + ".jpg";
        }else {
            fileName = fileName + ".png";
        }

        log.debug("fileName:" + fileName);

       if ((oldPhotoPath != null) && !"".equals(oldPhotoPath)) {
            File oldPhoto = new File(oldPhotoPath);
            if (oldPhoto.exists()) {
                oldPhoto.delete();
            }
        }

        try {
           if("googleplus".equals(type)){
               httpClient = HttpClientUtil.createSSLClient();
           }else {
               httpClient = HttpClients.createDefault();
           }
            response = httpClient.execute(httpGet);
            log.debug("有response了");
            in = response.getEntity().getContent();
            log.debug("有inputstream了");
            out = new FileOutputStream(path + fileName);

            byte[] buffer = new byte[8192];
            int len = 0;

            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
            in.close();
            out.close();
            log.debug("走完try了");
        } catch (Exception e) {
            log.error("请求图片出错了", e);
            return null;
        }finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return fileName;
    }

//    public static void main(String[] args) {
//        saveTpLoginIcon("http://avadtar.csdn.net/3/B/8/1_qq_32079585.jpg", "", "");
//    }


}
