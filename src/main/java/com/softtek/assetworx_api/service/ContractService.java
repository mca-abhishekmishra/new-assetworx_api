package com.softtek.assetworx_api.service;

import java.util.List;
import java.util.Map;

import com.softtek.assetworx_api.entity.AssetContract;
import com.softtek.assetworx_api.entity.Contract;

public interface ContractService {

	Contract findById(String id);

	Contract findByContractNo(String contractNo);

	Contract save(Contract contract);

	Contract update(Contract contract);

	boolean delete(String id);

	Map<String, String> addAssets(List<String> assetIdList, String contractId);

	List<AssetContract> inActivateAssetContracts(List<String> assetIds);

	Contract rework(Contract contract, String comment);

	Contract approval(Contract contract);

	Contract extending(Contract contract, String comment);

	Contract approve(Contract contract);

	Contract close(Contract contract, String comment);

	int getActiveAssetCountForContract(Contract contract);


}
