package com.softtek.assetworx_api.serviceImpl;

import static com.softtek.assetworx_api.util.Constants.*;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.softtek.assetworx_api.entity.PaymentType;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.exception.InvalidEntityException;
import com.softtek.assetworx_api.repository.PaymentTypeRepository;
import com.softtek.assetworx_api.service.PaymentTypeService;

@Service
public class PayementTypeServiceImpl implements PaymentTypeService {

	@Autowired
	PaymentTypeRepository paymentTypeRepository;

	@Autowired
	Validator validator;

	@Override
	public PaymentType findById(String id) {
		return paymentTypeRepository.findById(id).orElse(null);
	}

	@Override
	public PaymentType findByName(String name) {
		return paymentTypeRepository.findByName(name).orElse(null);
	}

	public boolean validate(PaymentType paymentType) {
		List<String> messages = validator.validate(paymentType).stream()
				.map(e -> e.getPropertyPath() + ":" + e.getMessage()).collect(Collectors.toList());
		PaymentType f = paymentTypeRepository.findFirstByNameAndIdNotLike(paymentType.getName(),
				paymentType.getId());
		if (f != null) {
			messages.add(PAYMENT_TYPE_EXISTS + paymentType.getName());
		}
		if (!messages.isEmpty()) {
			throw new InvalidEntityException(PAYMENT_TYPE_NOTSAVED, messages);
		}
		return true;

	}

	@Override
	public PaymentType save(PaymentType paymentType) {
		paymentType.setId("");
		if (validate(paymentType)) {
			return paymentTypeRepository.save(paymentType);
		}
		return null;
	}

	@Override
	public PaymentType update(PaymentType paymentType) {
		PaymentType f = findById(paymentType.getId());
		if (f == null) {
			throw new GenericRestException(PAYMENT_TYPE_NOTFOUND_ID + paymentType.getId(), HttpStatus.NOT_FOUND);
		} else {
			f.setName(paymentType.getName());
			f.setDescription(paymentType.getDescription());
			if (validate(f)) {
				return paymentTypeRepository.save(f);
			}
			return null;
		}
	}

	public boolean isDeletable(PaymentType paymentType) {
		/*
		 * if (paymentType.getAssets().size()>0) { throw new
		 * ResourceNotDeletableException("paymentType cannot be deleted since assets with this paymentType exists."
		 * ); }
		 */
		return true;
	}

	@Override
	public boolean delete(String id) {
		PaymentType paymentType = findById(id);
		if (paymentType == null) {
			throw new GenericRestException(PAYMENT_TYPE_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);
		}
		if (isDeletable(paymentType)) {
			paymentType.setActive(false);
			paymentType.setName(paymentType.getName() + "~" + System.nanoTime());
			paymentTypeRepository.save(paymentType);
			return true;
		} else {
			return false;
		}
	}

}
