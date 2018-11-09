package com.tm.leasewechat.dao;

import com.tm.leasewechat.domain.AppUserBasicInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by pengchao on 2018/5/12.
 */
public interface AppUserBasicInfoRepository  extends JpaRepository<AppUserBasicInfo, String> {

    AppUserBasicInfo findByUniqueMark(String uniqueMark);
}
