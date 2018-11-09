package com.tm.leasewechat.dao;

import com.tm.leasewechat.domain.WzRepayCardChangeRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by wangbiao on 2016/12/10 0010.
 */
public interface WzRepayCardChangeRecordRepository extends JpaRepository<WzRepayCardChangeRecord, Long> {

    WzRepayCardChangeRecord findTop1ByContractNumOrderByUpdateTimeDesc(String contractNum);


    WzRepayCardChangeRecord findTop1ByApplyNumOrderByUpdateTimeDesc(String applyNum);

}
