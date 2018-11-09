package com.tm.leasewechat.dao;

import com.tm.leasewechat.domain.RepaycardChangeRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by wangbiao on 2016/12/10 0010.
 */
public interface RepaycardchangerecordRepository extends JpaRepository<RepaycardChangeRecord, Long> {

    RepaycardChangeRecord findByContractNum(String contractNum);

}
