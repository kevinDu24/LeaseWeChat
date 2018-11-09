package com.tm.leasewechat.security;

import com.google.common.collect.Sets;
import com.tm.leasewechat.dao.SysUserRepository;
import com.tm.leasewechat.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by LEO on 2015/9/12.
 */
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private SysUserRepository sysUserRepository;

    @Override
    public UserDetails loadUserByUsername(String userName){
        SysUser sysUser = sysUserRepository.findByOpenId(userName);
        if(sysUser == null){
            throw new UsernameNotFoundException("系统中不存在该用户。");
        }
        return new User(sysUser.getCardId(), sysUser.getCode(), Sets.newHashSet());
    }
}
