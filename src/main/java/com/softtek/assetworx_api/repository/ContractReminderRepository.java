package com.softtek.assetworx_api.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.softtek.assetworx_api.entity.Contract;
import com.softtek.assetworx_api.entity.ContractReminder;



@Repository
public interface ContractReminderRepository extends JpaRepository<ContractReminder, String> {

	List<ContractReminder> findAllByContract(Contract contract);
	
	@Query("from ContractReminder c where Date(c.reminderDate) <= Date(:today) and c.isReminded = :isReminded and c.contract.id=  :contractId")
	List<ContractReminder> getcontractReminders(@Param("today") Date today, @Param("isReminded") boolean isReminded,
			@Param("contractId") String contractId);

}