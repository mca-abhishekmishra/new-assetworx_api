package com.softtek.assetworx_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softtek.assetworx_api.entity.AssetContract;
import com.softtek.assetworx_api.entity.Contract;

public interface AssetContractRepository extends JpaRepository<AssetContract, String> {

	List<AssetContract> findAllByContract(Contract contract);

	List<AssetContract> findAllByContractAndIsActive(Contract contract, boolean b);

}
