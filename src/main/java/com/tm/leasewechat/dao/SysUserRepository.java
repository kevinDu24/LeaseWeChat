package com.tm.leasewechat.dao;

import com.tm.leasewechat.domain.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by LEO on 16/9/26.
 */
public interface SysUserRepository extends JpaRepository<SysUser, Long> {
    SysUser findByOpenId(String openId);
    SysUser findByCardId(String cardId);
}
