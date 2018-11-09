package com.tm.leasewechat.dao;

import com.tm.leasewechat.domain.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by LEO on 16/9/27.
 */
public interface BankRepository extends JpaRepository<Bank, Long>{
    Bank findByBin(String bankNum);
    @Query(nativeQuery = true, value = "SELECT distinct(name), bank_num FROM bank")
    List<Object> getBankList();
}
