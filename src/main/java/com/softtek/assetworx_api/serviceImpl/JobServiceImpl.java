package com.softtek.assetworx_api.serviceImpl;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.softtek.assetworx_api.entity.Contract;
import com.softtek.assetworx_api.entity.ContractReminder;
import com.softtek.assetworx_api.model.Mail;
import com.softtek.assetworx_api.repository.ContractReminderRepository;
import com.softtek.assetworx_api.repository.ContractRepository;
import com.softtek.assetworx_api.service.JobServcie;
import com.softtek.assetworx_api.util.EmailSenderService;

@Service
public class JobServiceImpl implements JobServcie {

	private Logger logger = LogManager.getLogger(JobServiceImpl.class);
	@Autowired
	private ContractReminderRepository reminderRepo;

	@Autowired
	private ContractRepository contractRepo;

	@Autowired
	EmailSenderService emailSenderService;

	@Override
	@Transactional(rollbackOn = Exception.class)
	public void pickReminderDate() {
		List<ContractReminder> dateList = null;
		Date date = new Date();
		logger.info("getcontractReminders::::::::::: started");
		List<Contract> allContracts = contractRepo.findAll();

		for (Contract contract : allContracts) {
			dateList = reminderRepo.getcontractReminders(date, false, contract.getId());

			System.out.println(dateList);

			System.out.println(contract);

			logger.info("getcontractReminders::::::::::: ended");
			String mailTo = contract.getCategory().getMail();
			String mailCc = contract.getCategory().getMail();
			String subject = "CONTRACT EXPIRY REMINDER";
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("body",
					"This is to inform you that contract " + contract.getContractName() + " (" 
							+ contract.getPoNo() + ") with the " + contract.getVendor().getName() + "("
							+ contract.getVendor().getVendorId() + ") for "
							+ contract.getSubCategory().getName() +" will be expiring in" + " "
							+ ChronoUnit.DAYS.between(new Date().toInstant(), contract.getAmcEndDate().toInstant())
							+ " days, that is on" + " " + Instant.ofEpochMilli(contract.getAmcEndDate().getTime())
						      .atZone(ZoneId.systemDefault())
						      .toLocalDate());
			
			System.out.println(ChronoUnit.DAYS.between(new Date().toInstant(), contract.getAmcEndDate().toInstant()));
			if (dateList.size() > 0 && !dateList.isEmpty()) {
				logger.info("Sending mail notification:::::::::::started");
				Mail mail = new Mail(mailTo, mailCc, subject, null, variables, "html_template","/contract/details/"+contract.getId(), "reminder");
				emailSenderService.sendEmail(mail);

				for (ContractReminder reminder : dateList) {
					reminder.setReminded(true);
					reminderRepo.save(reminder);
				}
			}
		}

	}

}
