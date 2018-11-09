package com.tm.leasewechat.dao;

import com.tm.leasewechat.domain.ApplyInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by pengchao on 2018/1/8.
 */
public interface ApplyInfoRepository extends JpaRepository<ApplyInfo, String> {
    List<ApplyInfo> findByOpenId(String openId);
    ApplyInfo findByUniqueMark(String uniqueMark);
    ApplyInfo findByApplyNum(String applyNum);
    ApplyInfo findByApplyNumAndSignStatus(String applyNum, String signStatus);
    ApplyInfo findTop1ByOpenIdAndSignStatusOrderByCreateTimeDesc(String openId,String signStatus);

    //查询在线助力融拒绝状态列表
    @Query(nativeQuery = true, value = "select * from wz_apply_info t where t.unique_mark in ?1 AND (status = '1' or status = '2')")
    List<ApplyInfo> getApplyInfo(List<String> uniqueMarkList);

    /**
     * 根据身份证 ，状态 ，创建时间查找微众预审批申请
     * @param idCard        身份证
     * @param status        状态
     * @param createTime    创建时间
     * @return
     */
    ApplyInfo findTop1ByIdCardAndStatusAndCreateTimeAfterOrderByCreateTimeDesc(String idCard,String status,Date createTime);
}
