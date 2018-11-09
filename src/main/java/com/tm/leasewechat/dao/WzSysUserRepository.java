package com.tm.leasewechat.dao;

import com.tm.leasewechat.domain.WzSysUser;
import org.springframework.data.jpa.repository.JpaRepository;


public interface WzSysUserRepository extends JpaRepository<WzSysUser, Long> {
    WzSysUser findByOpenId(String openId);
    WzSysUser findByPhoneNum(String phoneNum);
}
