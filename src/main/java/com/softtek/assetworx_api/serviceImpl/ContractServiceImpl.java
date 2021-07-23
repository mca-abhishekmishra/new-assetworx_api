package com.softtek.assetworx_api.serviceImpl;

import static com.softtek.assetworx_api.util.Constants.CONTRACT_EXISTS;
import static com.softtek.assetworx_api.util.Constants.CONTRACT_NOTFOUND_ID;
import static com.softtek.assetworx_api.util.Constants.CONTRACT_NOTSAVED;
import static com.softtek.assetworx_api.util.Constants.NEW;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.softtek.assetworx_api.entity.Asset;
import com.softtek.assetworx_api.entity.AssetContract;
import com.softtek.assetworx_api.entity.Contract;
import com.softtek.assetworx_api.entity.ContractReminder;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.exception.InvalidEntityException;
import com.softtek.assetworx_api.model.Mail;
import com.softtek.assetworx_api.repository.AssetContractRepository;
import com.softtek.assetworx_api.repository.ContractReminderRepository;
import com.softtek.assetworx_api.repository.ContractRepository;
import com.softtek.assetworx_api.service.AssetService;
import com.softtek.assetworx_api.service.CategoryService;
import com.softtek.assetworx_api.service.ContractService;
import com.softtek.assetworx_api.service.ContractTypeService;
import com.softtek.assetworx_api.service.CostCenterService;
import com.softtek.assetworx_api.service.PaymentTypeService;
import com.softtek.assetworx_api.service.StatusService;
import com.softtek.assetworx_api.service.SubCategoryService;
import com.softtek.assetworx_api.service.VendorService;
import com.softtek.assetworx_api.util.AssetworxUtil;
import com.softtek.assetworx_api.util.EmailSenderService;

@Service
public class ContractServiceImpl implements ContractService {

	@Autowired
	ContractRepository contractRepository;

	@Autowired
	AssetContractRepository assetContractRepository;

	@Autowired
	Validator validator;

	@Autowired
	VendorService vendorService;

	@Autowired
	CostCenterService costCenterService;

	@Autowired
	PaymentTypeService paymentTypeService;

	@Autowired
	StatusService statusService;

	@Autowired
	ContractTypeService contractTypeService;

	@Autowired
	CategoryService categoryService;

	@Autowired
	SubCategoryService subCategoryService;

	@Autowired
	AssetService assetService;

	@Autowired
	EmailSenderService emailSenderService;

	@Autowired
	TaskExecutor executor;

	@Autowired
	HttpServletRequest httpServletRequest;
	
	@Autowired
	ContractReminderRepository reminderRepo;

	@Override
	public Contract findById(String id) {
		return contractRepository.findById(id).orElse(null);
	}

	@Override
	public Contract findByContractNo(String contractNo) {
		return contractRepository.findByContractNo(contractNo).orElse(null);
	}

	public boolean validate(Contract contract) {
		List<String> messages = validator.validate(contract).stream()
				.map(e -> e.getPropertyPath() + ":" + e.getMessage()).collect(Collectors.toList());
		Contract f = contractRepository.findFirstByContractNoAndIdNotLike(contract.getContractNo(), contract.getId());
		if (f != null) {
			messages.add(CONTRACT_EXISTS + contract.getContractNo());
		}
		if (contract.getAmcStartDate() == null || contract.getAmcEndDate() == null
				|| (contract.getAmcStartDate().after(contract.getAmcEndDate()))) {
			messages.add("amcStartDate: The AMC start date should be before AMC end date.");
		}
		if (!messages.isEmpty()) {
			throw new InvalidEntityException(CONTRACT_NOTSAVED, messages);
		}
		return true;

	}

	@Override
	public Contract save(Contract contract) {
		contract.setId("");
		contract.setVendor(vendorService.findById((contract.getVendor() != null ? contract.getVendor().getId() : "")));
		contract.setCostCenter(
				costCenterService.findById((contract.getCostCenter() != null ? contract.getCostCenter().getId() : "")));
		contract.setContractStatus(statusService.findByName(NEW));
		contract.setPaymentType(paymentTypeService
				.findById(contract.getPaymentType() != null ? contract.getPaymentType().getId() : ""));
		contract.setContractType(
				contractTypeService.findById(contract.getContractType() != null ? contract.getContractType().getId() : ""));
		contract.setCategory(
				categoryService.findById(contract.getCategory() != null ? contract.getCategory().getId() : ""));
		contract.setSubCategory(subCategoryService
				.findById(contract.getSubCategory() != null ? contract.getSubCategory().getId() : ""));
		contract.setContractNo("CTR-" + System.nanoTime());
		if (validate(contract)) {
			Contract c = contractRepository.save(contract);
			this.saveReminderDate(contract.getAmcStartDate(), contract.getAmcEndDate(), c);
			System.out.println("contract.getCategory().getMail()1:"+contract.getCategory().getMail());
			executor.execute(() -> {
				System.out.println("contract.getCategory().getMail()2:"+contract.getCategory().getMail());
				String mailTo = contract.getCategory().getMail();
				String mailCc = contract.getCategory().getMail();
				String subject = "NEW CONTRACT CREATED";
				Map<String, Object> variables = new HashMap<String, Object>();
				variables.put("name", "TEAM");
				variables.put("body",
						"This is to inform you that a new contract  " + contract.getContractName() + " for "+ contract.getSubCategory().getName() +" is created.");
				Mail mail = new Mail(mailTo, mailCc, subject, null, variables, "html_template","/contract/details/"+contract.getId(), "contract");
				emailSenderService.sendEmail(mail);
			});
			return c;
			//return contractRepository.save(contract);
		}
		return null;
	}

	@Override
	public Contract update(Contract contract) {
		Contract f = findById(contract.getId());
		if (f == null) {
			throw new GenericRestException(CONTRACT_NOTFOUND_ID + contract.getId(), HttpStatus.NOT_FOUND);
		}
		if (f.getContractStatus().getName().equalsIgnoreCase("APPROVED")
				|| f.getContractStatus().getName().equalsIgnoreCase("CLOSED")) {
			throw new GenericRestException(
					"Contract cannot be updated because it is in " + f.getContractStatus().getName() + " state.",
					HttpStatus.NOT_FOUND);
		} else {
			f.setPoNo(contract.getPoNo());
			f.setContractName(contract.getContractName());
			f.setAmcStartDate(contract.getAmcStartDate());
			f.setAmcEndDate(contract.getAmcEndDate());
			f.setVendor(vendorService.findById((contract.getVendor() != null ? contract.getVendor().getId() : "")));
			f.setCostCenter(costCenterService
					.findById((contract.getCostCenter() != null ? contract.getCostCenter().getId() : "")));
			f.setPaymentType(paymentTypeService
					.findById(contract.getPaymentType() != null ? contract.getPaymentType().getId() : ""));
			f.setContractType(
					contractTypeService.findById(contract.getContractType() != null ? contract.getContractType().getId() : ""));
			f.setCategory(
					categoryService.findById(contract.getCategory() != null ? contract.getCategory().getId() : ""));
			f.setSubCategory(subCategoryService
					.findById(contract.getSubCategory() != null ? contract.getSubCategory().getId() : ""));
			f.setAmcCost(contract.getAmcCost());
			f.setDescription(contract.getDescription());
			f.setComment(contract.getComment());
			if (validate(f)) {
				Contract savedContract = contractRepository.save(f);
				this.saveReminderDate(f.getAmcStartDate(), f.getAmcEndDate(), f);
				executor.execute(() -> {
					System.out.println("f.getCategory().getMail()1:"+f.getCategory().getMail());
					String mailTo = f.getCategory().getMail();
					String mailCc = f.getCategory().getMail();
					String subject = "CONTRACT UPDATED";
					Map<String, Object> variables = new HashMap<String, Object>();
					variables.put("name", "TEAM");
					variables.put("body",
							"This is to inform you that the contract  " + f.getContractName() + " for "+f.getSubCategory().getName()+" is updated.");
					Mail mail = new Mail(mailTo, mailCc, subject, null, variables, "html_template","/contract/details/"+f.getId(), "update");
					emailSenderService.sendEmail(mail);
				});
					
				return savedContract;
			}
			return null;
		}
	}

	public boolean isDeletable(Contract contract) {
		/*
		 * if (contract.getAssets().size()>0) { throw new
		 * ResourceNotDeletableException("contract cannot be deleted since assets with this contract exists."
		 * ); }
		 */
		return true;
	}

	@Override
	public boolean delete(String id) {
		Contract contract = findById(id);
		if (contract == null) {
			throw new GenericRestException(CONTRACT_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);
		}
		if (isDeletable(contract)) {
			contract.setActive(false);
			contract.setContractNo(contract.getContractNo() + "~" + System.nanoTime());
			contractRepository.save(contract);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Map<String, String> addAssets(List<String> assetIdList, String contractId) {
		String[] statusList = { "NEW", "APPROVAL", "REWORK", "RETIRED", "LOST", "DAMAGED" };
		String[] contractStatusList = { "NEW", "REWORK", "APPROVAL", "EXTENDING" };
		List<Asset> assets = assetService.findAllByIdInAndStatusNotIn(assetIdList, statusList);
		Contract contract = contractRepository.findById(contractId).orElse(null);
		Map<String, String> returnData = new HashMap<String, String>();
		if (contract == null) {
			returnData.put("message", "Contract details not found");
		} else if (!Arrays.asList(contractStatusList).contains(contract.getContractStatus().getName().toUpperCase())) {
			returnData.put("message", "Assets could not be attached to the contract as the contract is in "
					+ contract.getContractStatus().getName() + " state.");
		} else if (assetIdList.size() != assets.size()) {
			returnData.put("message",
					"Some assets selected by you are not eligible to be attached to a contract, because they are not approved or may be retired.");
		} else {
			assets.forEach(asset -> {
				if (asset.getContract() == null) {
					AssetContract a = new AssetContract(contract, asset);
					assetContractRepository.save(a);
					assetService.attachContractToAsset(asset, contract);
				} else {
					returnData.put(asset.getId(), asset.getTagId());
				}
			});
			if (returnData.size() > 0) {
				returnData.put("message",
						"The following assets could not be attached to the contract, as they are already attached to a contract.");
			}
		}
		return returnData.size() == 0 ? null : returnData;
	}

	@Override
	public List<AssetContract> inActivateAssetContracts(List<String> assetContractIds) {
		List<AssetContract> assetContracts = assetContractRepository.findAllById(assetContractIds);
		String status = assetContracts.get(0).getContract().getContractStatus().getName().toUpperCase();
		String[] contractStatusList = { "NEW", "REWORK", "APPROVAL", "EXTENDING" };
		if (!Arrays.asList(contractStatusList).contains(status)) {
			return null;
		}
		List<AssetContract> inActivatedAssetContracts = new ArrayList<AssetContract>();
		assetContracts.forEach(ac -> {
			ac.setActive(false);
			assetService.detachContract(ac.getAsset());
			inActivatedAssetContracts.add(ac);
		});
		return inActivatedAssetContracts;
	}

	@Override
	public Contract rework(Contract contract, String comment) {
		contract.setHiddenComment(comment);
		contract.setContractStatus(statusService.findByName("Rework"));
		Contract savedContract = contractRepository.save(contract);
		executor.execute(() -> {
			String mailTo = contract.getCategory().getMail();
			String mailCc = contract.getCategory().getMail();
			String subject = "CONTRACT SUBMITTED TO REWORK";
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("name", "TEAM");
			variables.put("comment", comment);
			variables.put("body", "This is to inform you that the contract  " + contract.getContractName() + " for " + contract.getSubCategory().getName()
					+ " is submitted to rework.");
			Mail mail = new Mail(mailTo, mailCc, subject, null, variables, "html_template","/contract/details/"+contract.getId(), "rework");
			emailSenderService.sendEmail(mail);
		});
		return savedContract;
	}

	@Override
	public Contract approval(Contract contract) {
		contract.setContractStatus(statusService.findByName("Approval"));
		Contract savedContract = contractRepository.save(contract);
		executor.execute(() -> {
			String mailTo = contract.getCategory().getMail();
			String mailCc = contract.getCategory().getMail();
			String subject = "CONTRACT SUBMITTED FOR APPROVAL";
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("name", "TEAM");
			variables.put("body", "This is to inform you that the contract  " + contract.getContractName()  + " for " + contract.getSubCategory().getName()
					+ " is submitted for approval.");
			Mail mail = new Mail(mailTo, mailCc, subject, null, variables, "html_template","/contract/details/"+contract.getId(), "approval");
			emailSenderService.sendEmail(mail);
		});
		return savedContract;
	}

	@Override
	public Contract approve(Contract contract) {
		contract.setContractStatus(statusService.findByName("Approved"));
		contract.setApprovedBy(httpServletRequest.getHeader("username"));
		contract.setApprovedDate(new Date());
		Contract savedContract = contractRepository.save(contract);
		executor.execute(() -> {
			String mailTo = contract.getCategory().getMail();
			String mailCc = contract.getCategory().getMail();
			String subject = "CONTRACT APPROVED";
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("name", "TEAM");
			variables.put("body",
					"This is to inform you that the contract  " + contract.getContractName()  + " for " + contract.getSubCategory().getName()
					+ " is approved.");
			Mail mail = new Mail(mailTo, mailCc, subject, null, variables, "html_template","/contract/details/"+contract.getId(), "approved");
			emailSenderService.sendEmail(mail);
		});
		return savedContract;
	}

	@Override
	public Contract extending(Contract contract, String comment) {
		contract.setHiddenComment(comment);
		contract.setContractStatus(statusService.findByName("Extending"));
		Contract savedContract = contractRepository.save(contract);
		executor.execute(() -> {
			String mailTo = contract.getCategory().getMail();
			String mailCc = contract.getCategory().getMail();
			String subject = "CONTRACT SUBMITTED FOR EXTENSION";
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("name", "TEAM");
			variables.put("comment", comment);
			variables.put("body", "This is to inform you that the contract  " + contract.getContractName()  + " for " + contract.getSubCategory().getName()
					+ " is submitted for extension.");
			Mail mail = new Mail(mailTo, mailCc, subject, null, variables, "html_template","/contract/details/"+contract.getId(), "extending");
			emailSenderService.sendEmail(mail);
		});
		return savedContract;
	}

	@Override
	public Contract close(Contract contract, String comment) {
		contract.setHiddenComment(comment);
		contract.setContractStatus(statusService.findByName("Closed"));
		List<AssetContract> assetContracts = assetContractRepository.findAllByContractAndIsActive(contract, true);
		assetContracts.forEach(ac -> {
			assetService.detachContract(ac.getAsset());
		});
		Contract savedContract = contractRepository.save(contract);
		executor.execute(() -> {
			String mailTo = contract.getCategory().getMail();
			String mailCc = contract.getCategory().getMail();
			String subject = "CONTRACT CLOSED";
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("name", "TEAM");
			variables.put("comment", comment);
			variables.put("body",
					"This is to inform you that the contract  " + contract.getContractName()  + " for " + contract.getSubCategory().getName()
					+ " is closed.");
			Mail mail = new Mail(mailTo, mailCc, subject, null, variables, "html_template","/contract/details/"+contract.getId(), "closed");
			emailSenderService.sendEmail(mail);
		});
		return savedContract;
	}
	
	public void saveReminderDate(Date startDate, Date endDate, Contract contract)
	{
		List<ContractReminder> reminders = reminderRepo.findAllByContract(contract);

		if(reminders!= null && !reminders.isEmpty())
		{
			reminderRepo.deleteAll();

		}
		List<Date> date = AssetworxUtil.getReminderDates(startDate,endDate);//75,95,100
		Collections.sort(date);
		date.add(new Date(endDate.getTime() -  Duration.ofDays(30).toMillis()));//pick the 30 th date
		for(Date d : date) {
			if(d.before(new Date())) {
				continue;
			}
			ContractReminder c = new ContractReminder();
			c.setReminderDate(d);
			c.setContract(contract);
			reminderRepo.save(c);
		}
	}

	@Override
	public int getActiveAssetCountForContract(Contract contract) {
		return assetContractRepository.findAllByContractAndIsActive(contract, true).size();
	}

}
