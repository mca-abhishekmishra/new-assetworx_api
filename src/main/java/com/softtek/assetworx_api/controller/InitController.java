package com.softtek.assetworx_api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class InitController {
	
	@RequestMapping("/")
	public String init() {
		return "html_template";
	}

}
