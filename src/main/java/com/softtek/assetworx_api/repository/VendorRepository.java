package com.softtek.assetworx_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softtek.assetworx_api.entity.Vendor;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, String> {

	Optional<Vendor> findByVendorId(String vendorId);

	Vendor findFirstByVendorIdAndIdNotLike(String vendorId, String id);

}
