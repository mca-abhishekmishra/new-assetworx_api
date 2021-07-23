package com.softtek.assetworx_api.config;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class CustomInitBinder {


	@InitBinder
	public void bindMyCustomValidator(WebDataBinder dataBinder){
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor (String.class,stringTrimmerEditor);
	}

}
