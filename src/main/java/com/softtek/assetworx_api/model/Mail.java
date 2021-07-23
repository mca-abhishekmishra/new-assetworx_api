package com.softtek.assetworx_api.model;

import java.util.Map;

import com.softtek.assetworx_api.entity.Document;

public class Mail {

	private String from;
	private String mailTo;
	private String mailCc;
	private String subject;
	private Document document;
	private Map<String, Object> props;
	private String templateName;
	private String url;
	private String type;

	public Mail(String mailTo, String mailCc, String subject, Document document,
			Map<String, Object> props, String templateName, String url, String type) {
		super();
		this.mailTo = mailTo;
		this.mailCc = mailCc;
		this.subject = subject;
		this.document = document;
		this.props = props;
		this.templateName = templateName;
		this.url = url;
		this.type = type;
	}

	public Mail() {
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getMailTo() {
		return mailTo;
	}

	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public Map<String, Object> getProps() {
		return props;
	}

	public void setProps(Map<String, Object> props) {
		this.props = props;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getMailCc() {
		return mailCc;
	}

	public void setMailCc(String mailCc) {
		this.mailCc = mailCc;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	
	

}
