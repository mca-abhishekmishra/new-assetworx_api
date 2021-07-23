package com.softtek.assetworx_api.controller;

import static com.softtek.assetworx_api.util.Constants.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softtek.assetworx_api.entity.PaymentType;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.service.PaymentTypeService;

@RestController
@RequestMapping("/paymentType")
public class PaymentTypeController {

	@Autowired
	PaymentTypeService paymentTypeService;

	@GetMapping("/{id}")
	private ResponseEntity<?> findById(@PathVariable("id") String id) {
		PaymentType paymentType = paymentTypeService.findById(id);
		if (paymentType != null) {
			return new ResponseEntity<PaymentType>(paymentType, HttpStatus.OK);
		}
		throw new GenericRestException(PAYMENT_TYPE_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);
	}

	@GetMapping("/findByName/{name}")
	private ResponseEntity<?> findByName(@PathVariable("name") String name) {
		PaymentType paymentType = paymentTypeService.findByName(name);
		if (paymentType != null) {
			return new ResponseEntity<PaymentType>(paymentType, HttpStatus.OK);
		}
		throw new GenericRestException(PAYMENT_TYPE_NOTFOUND_NAME + name, HttpStatus.NOT_FOUND);
	}

	@PostMapping("/")
	private ResponseEntity<?> save(@RequestBody PaymentType paymentType) {
		System.out.println(paymentType);
		PaymentType createdPaymentType = paymentTypeService.save(paymentType);
		if (createdPaymentType != null) {
			return new ResponseEntity<PaymentType>(createdPaymentType, HttpStatus.CREATED);
		}
		throw new GenericRestException(PAYMENT_TYPE_NOTSAVED, HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/")
	private ResponseEntity<?> update(@RequestBody PaymentType paymentType) {
		PaymentType updatedPaymentType = paymentTypeService.update(paymentType);
		if (updatedPaymentType != null) {
			return new ResponseEntity<PaymentType>(updatedPaymentType, HttpStatus.OK);
		}
		throw new GenericRestException(PAYMENT_TYPE_NOTUPDATED, HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping("/{id}")
	private ResponseEntity<?> delete(@PathVariable("id") String id) {
		if (paymentTypeService.delete(id)) {
			return new ResponseEntity<String>(PAYMENT_TYPE_DELETED, HttpStatus.OK);
		}
		throw new GenericRestException(PAYMENT_TYPE_NOTDELETED, HttpStatus.BAD_REQUEST);
	}

}
