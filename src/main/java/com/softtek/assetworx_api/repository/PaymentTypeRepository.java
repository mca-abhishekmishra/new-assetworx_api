package com.softtek.assetworx_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softtek.assetworx_api.entity.PaymentType;

@Repository
public interface PaymentTypeRepository extends JpaRepository<PaymentType, String> {

	Optional<PaymentType> findByName(String name);

	Optional<PaymentType> findByIdAndName(String id, String name);

	PaymentType findFirstByNameAndIdNotLike(String name, String id);
}
