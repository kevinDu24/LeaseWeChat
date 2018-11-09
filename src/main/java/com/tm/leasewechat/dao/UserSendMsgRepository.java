package com.tm.leasewechat.dao;

import com.tm.leasewechat.domain.UserSendMsg;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by pengchao on 2018/6/26.
 */
public interface UserSendMsgRepository  extends JpaRepository<UserSendMsg, String> {

}
