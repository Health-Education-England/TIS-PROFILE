package com.transformuk.hee.tis.auth.service;

import com.transformuk.hee.tis.auth.model.User;
import com.transformuk.hee.tis.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class provides methods to consolidate/refresh data around different sources into single view repository .
 */
@Service
public class TestDataService {
    private static String[] users =
            new String[]{"jamesH James Hale 1000000 1-85KJU0 7788996655","hubertM Hubert Matthews 1000001 1-85KJU0 7898496655",
                    "saraH Sarah Harrison 1000002 1-85KJU0 7258426105"};

    @Autowired
    private UserRepository userRepository;

    public void setupTestData() {
        for(String userStr : users) {
            String[] userInfo = userStr.split(" ");
            User user = new User();
            user.setUserName(userInfo[0]);
            user.setFirstName(userInfo[1]);
            user.setLastName(userInfo[2]);
            user.setGmcId(userInfo[3]);
            user.setDesignatedBodyCode(userInfo[4]);
            user.setPhoneNumber(userInfo[5]);

            userRepository.save(user);
        }
    }
}
