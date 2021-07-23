package com.softtek.assetworx_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softtek.assetworx_api.entity.Contract;

@Repository
public interface ContractRepository extends JpaRepository<Contract, String> {

	Optional<Contract> findByContractNo(String contractNo);

	Contract findFirstByContractNoAndIdNotLike(String contractNo, String id);

}
