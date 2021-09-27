package com.cs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cs.model.Bill;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long>{
	
	@Query("SELECT SUM(billAmount) FROM Bill  WHERE TO_CHAR(billingDate,'YYYYMMDD') = TO_CHAR(SYSDATE,'YYYYMMDD')")
	Double calculateDailyRevenue();

}

