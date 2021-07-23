package com.softtek.assetworx_api.serviceImpl;

import static com.softtek.assetworx_api.util.Constants.VENDOR_EXISTS_ID;
import static com.softtek.assetworx_api.util.Constants.VENDOR_NOTFOUND_ID;
import static com.softtek.assetworx_api.util.Constants.VENDOR_NOTSAVED;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.softtek.assetworx_api.entity.Vendor;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.exception.InvalidEntityException;
import com.softtek.assetworx_api.repository.VendorRepository;
import com.softtek.assetworx_api.service.VendorService;

@Service
public class VendorServiceImpl implements VendorService {

	@Autowired
	VendorRepository vendorRepository;

	@Autowired
	Validator validator;


	@Override
	public Vendor findById(String id) {
		return vendorRepository.findById(id).orElse(null);
	}

	@Override
	public Vendor findByVendorId(String vendorId) {
		return vendorRepository.findByVendorId(vendorId).orElse(null);
	}

	public boolean validate(Vendor vendor) {
		List<String> messages = validator.validate(vendor).stream().map(e->
		e.getPropertyPath()+":"+e.getMessage()).collect(Collectors.toList());
		Vendor f = vendorRepository.findFirstByVendorIdAndIdNotLike(vendor.getVendorId(),vendor.getId());
		if(f!=null) messages.add(VENDOR_EXISTS_ID + vendor.getVendorId());
		if(!messages.isEmpty()) {
			throw new InvalidEntityException(VENDOR_NOTSAVED, messages);
		}
		return true;

	}

	@Override
	public Vendor save(Vendor vendor) {
		vendor.setId("");
		vendor.setVendorId(this.getVendorId(vendor.getVendorId()));
		if(validate(vendor)) {
			return vendorRepository.save(vendor);
		}		
		return null; 
	}
	
	private String getVendorId(String vendorId) {
		if(vendorId!=null && !vendorId.isEmpty()) {
			return vendorId;
		}
		return "VEN-"+System.currentTimeMillis();
	}

	@Override
	public Vendor update(Vendor vendor) {
		Vendor f = findById(vendor.getId());
		if(f==null) {
			throw new GenericRestException(VENDOR_NOTFOUND_ID + vendor.getId(), HttpStatus.NOT_FOUND);
		}
		else {
			f.setName(vendor.getName());
			f.setEmail(vendor.getEmail());
			// f.setVendorId(vendor.getVendorId());
			f.setVendorId(this.getVendorId(vendor.getVendorId()));
			f.setPrimaryContactEmail(vendor.getPrimaryContactEmail());
			f.setPrimaryContactMobileNo(vendor.getPrimaryContactMobileNo());
			f.setPrimaryContactName(vendor.getPrimaryContactName());
			f.setSecondaryContactEmail(vendor.getSecondaryContactEmail());
			f.setSecondaryContactMobileNo(vendor.getSecondaryContactMobileNo());
			f.setSecondaryContactName(vendor.getSecondaryContactName());
			f.setVendorAddress(vendor.getVendorAddress());
			f.setMobileNo(vendor.getMobileNo());
			f.setDistrict(vendor.getDistrict());
			f.setState(vendor.getState());
			f.setCountry(vendor.getCountry());
			f.setPincode(vendor.getPincode());
			if(validate(f)) {
				return vendorRepository.save(f);
			}
			return null;
		}
	}

	public boolean isDeletable(Vendor vendor) {
		/*if (vendor.getAssets().size()>0) {
			throw new ResourceNotDeletableException("vendor cannot be deleted since assets with this vendor exists.");
		}*/
		return true;
	}

	@Override
	public boolean delete(String id) {
		Vendor vendor = findById(id);
		if(vendor == null) {
			throw new GenericRestException(VENDOR_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);
		}
		if(isDeletable(vendor)) {
			vendor.setActive(false);
			vendor.setName(vendor.getName()+"~"+System.nanoTime());
			vendorRepository.save(vendor);	
			return true;
		}
		else{
			return false;
		}
	}

}
