package com.gigaiot.nlostserver;

import com.gigaiot.nlostserver.entity.ItemProperty;
import com.gigaiot.nlostserver.entity.ItemRight;
import com.gigaiot.nlostserver.entity.User;
import com.gigaiot.nlostserver.repository.ItemRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Set;

@Slf4j
@SpringBootApplication
public class NlostServer {

    public static final String SUCCESS = "{\"success\":true}";

    public static void initUser(ApplicationContext ctx) {

        User rootUser = new User();
        rootUser.setName("root");
        rootUser.setPassword("123456");
        rootUser.setCreators("");
        rootUser.setCreatorName("");
        rootUser.setLastLoginHost("");
        rootUser.setLastLoginTime(System.currentTimeMillis());
        rootUser.setCls(1);
        rootUser.setAccountId(0);
        rootUser.setAccountName("");
        rootUser.setFlags(255);

        ItemRepo itemRepo = ctx.getBean(ItemRepo.class);
        itemRepo.save(rootUser);

        Set<ItemRight> itemRights = rootUser.getItemRights();


        ItemRight itemRight = new ItemRight();
        itemRight.setUserId(rootUser.getId());
        itemRight.setValue(2146947L); //read only
        //itemRight.setItemId(rootUser.getUnitId());
        itemRight.setItem(rootUser);
        itemRights.add(itemRight);


		/*ItemRight itemRight2 = new ItemRight();
		itemRight2.setUserId(4);
		itemRight2.setValue(1); //read only
		itemRight2.setItem(rootUser);
		//itemRight2.setItemId(rootUser.getUnitId());
		itemRights.add(itemRight2);*/


//        ItemProperty itemProperty = new ItemProperty();
//        itemProperty.setK("age");
//        itemProperty.setV("19");
//
//        ItemProperty itemProperty2 = new ItemProperty();
//        itemProperty2.setK("sh");
//        itemProperty2.setV("190");
//
//        rootUser.getProperties().add(itemProperty);
//        rootUser.getProperties().add(itemProperty2);

        itemRepo.save(rootUser);
    }

    public static void clearDB(ApplicationContext ctx) {

    }

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(NlostServer.class);
		//app.setBannerMode(Banner.Mode.OFF);
		ApplicationContext ctx = app.run(args);

        //initUser(ctx);

        log.info("启动完成");

	}
}
