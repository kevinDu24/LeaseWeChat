package com.tm.leasewechat.dao;

import com.tm.leasewechat.domain.ContractInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by pengchao on 2018/1/15.
 */
public interface ContractInfoRepository extends JpaRepository<ContractInfo, String> {
    @Query(nativeQuery = true, value = "select * from wz_contract_info t where t.apply_num in (SELECT t1.apply_num FROM wz_apply_info t1 where t1.sign_status = ?2 and t1.status <> '6' and t1.open_id = ?1) ORDER BY t.create_time DESC limit 1")
    ContractInfo findTop1ByOpenIdAndSignStatusOrderByCreateTimeDesc(String openId, String signStatus);

    ContractInfo findTop1ByApplyNumOrderByCreateTimeDesc(String applyNum);

}
