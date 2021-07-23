package com.softtek.assetworx_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softtek.assetworx_api.entity.ContractType;

@Repository
public interface ContractTypeRepository extends JpaRepository<ContractType, String> {

	Optional<ContractType> findByName(String name);

	Optional<ContractType> findByIdAndName(String id, String name);

	ContractType findFirstByNameAndIdNotLike(String name, String id);
}
