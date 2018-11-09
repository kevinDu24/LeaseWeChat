package com.tm.leasewechat.dao;

import com.tm.leasewechat.domain.MessageLogs;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 短信日志 dao 层
 * Created by zcHu on 2017/5/15.
 */
public interface MessageLogsRepository extends JpaRepository<MessageLogs,String> {
}
