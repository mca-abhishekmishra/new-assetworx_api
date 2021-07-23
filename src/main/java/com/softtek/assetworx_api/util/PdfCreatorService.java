package com.softtek.assetworx_api.util;

import java.io.FileOutputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Service
public class PdfCreatorService {

	@Autowired
	private SpringTemplateEngine templateEngine;

	public String createPdf(String templateName, Map<String, Object> variables, String filePathAndName) {
		Context context = new Context();
		context.setVariables(variables);
		String pdfTemplate = templateEngine.process(templateName, context);
		try (FileOutputStream outputStream = new FileOutputStream(filePathAndName)) {
			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocumentFromString(pdfTemplate);
			renderer.layout();
			renderer.createPDF(outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filePathAndName;
	}

}
