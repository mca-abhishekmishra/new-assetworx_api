package com.softtek.assetworx_api.serviceImpl;

import java.util.Arrays;
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
import com.softtek.assetworx_api.entity.AssetAssignment;
import com.softtek.assetworx_api.entity.Document;
import com.softtek.assetworx_api.entity.Employee;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.exception.InvalidEntityException;
import com.softtek.assetworx_api.model.Mail;
import com.softtek.assetworx_api.repository.AssetAssignmentRepository;
import com.softtek.assetworx_api.service.AreaService;
import com.softtek.assetworx_api.service.AssetAssignmentService;
import com.softtek.assetworx_api.service.AssetAssignmentTypeService;
import com.softtek.assetworx_api.service.AssetService;
import com.softtek.assetworx_api.service.CostCenterService;
import com.softtek.assetworx_api.service.DepartmentService;
import com.softtek.assetworx_api.service.DocumentService;
import com.softtek.assetworx_api.service.EmployeeService;
import com.softtek.assetworx_api.service.ProjectService;
import com.softtek.assetworx_api.service.ProjectWbsIdService;
import com.softtek.assetworx_api.service.StatusService;
import com.softtek.assetworx_api.util.EmailSenderService;

@Service
public class AssetAssignmentServiceImpl implements AssetAssignmentService {

	@Autowired
	AssetAssignmentRepository assetAssignmentRepository;

	@Autowired
	Validator validator;

	@Autowired
	AssetService assetService;

	@Autowired
	CostCenterService costCenterService;

	@Autowired
	AssetAssignmentTypeService assetAssignmentTypeService;

	@Autowired
	EmployeeService employeeService;

	@Autowired
	DepartmentService departmentService;

	@Autowired
	AreaService areaService;

	@Autowired
	ProjectService projectService;

	@Autowired
	StatusService statusService;

	@Autowired
	HttpServletRequest httpServletRequest;

	@Autowired
	TaskExecutor executor;

	@Autowired
	EmailSenderService emailSenderService;

	@Autowired
	DocumentService documentService;
	
	@Autowired
	ProjectWbsIdService projectWbsIdService;

	@Override
	public AssetAssignment assign(AssetAssignment a) {
		a.setId("");
		Asset asset = assetService.findById((a.getAsset() != null ? a.getAsset().getId() : ""));
		a.setAsset(asset);
		a.setCostCenter(costCenterService.findById((a.getCostCenter() != null ? a.getCostCenter().getId() : "")));
		a.setAssetAssignmentType(assetAssignmentTypeService
				.findById(a.getAssetAssignmentType() != null ? a.getAssetAssignmentType().getId() : ""));
		a.setEmployee(employeeService.findById(a.getEmployee() != null ? a.getEmployee().getId() : ""));
		a.setDepartment(departmentService.findById(a.getDepartment() != null ? a.getDepartment().getId() : ""));
		a.setProjectWbsId(projectWbsIdService.findById(a.getProjectWbsId() != null ? a.getProjectWbsId().getId() : ""));
		a.setArea(areaService.findById(a.getArea() != null ? a.getArea().getId() : ""));
		a.setProject(projectService.findById(a.getProject() != null ? a.getProject().getId() : ""));
		a.setAssetAssignedBy(httpServletRequest.getHeader("username"));
		a.setUnassignmentDate(null);
		// a.setAssignedDate(new Date());
		if (validate(a)) {
			AssetAssignment a1 = assetAssignmentRepository.save(a);
			if (a1 != null) {
				assetService.saveAssignment(asset, a1);
				executor.execute(() -> {
					// sendAssignmentEmail(a1);
				});
				return a1;
			} else {
				return null;
			}

		}
		return null;
	}

	private void sendAssignmentEmail(AssetAssignment a1) {
		String mailTo = "";
		if (a1.getAssetAssignmentType().getName().equalsIgnoreCase("EMPLOYEE")) {
			Employee e = a1.getEmployee();
			mailTo = e.getEmail();
		}
			Document document = documentService.createLiabilityLetter(a1);
			String mailCc = a1.getAsset().getCategory().getMail();
			if(a1.getInformTo()!=null && !a1.getInformTo().trim().isEmpty()) {
				mailCc+=";"+a1.getInformTo();
			}
			String subject = "LIABILITY AGREEMENT OF EQUIPMENT ASSIGNMENT";
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("assetAssignment", a1);
			String type = a1.getAsset().getCategory().getName().equalsIgnoreCase("IT EQUIPMENT") ? "it asset" : "facility asset";
			Mail mail = new Mail(mailTo, mailCc, subject, document, variables, "liability_letter_html_template","/asset/details/"+a1.getAsset().getId(), type);
			emailSenderService.sendEmail(mail);
	}

	@Override
	public AssetAssignment unassign(AssetAssignment assetAssignment) {
		AssetAssignment a = assetAssignmentRepository.findByIdAndIsActive(assetAssignment.getId(), true);
		if (a == null || !a.getAsset().getAssetStatus().getName().equalsIgnoreCase("ASSIGNED")) {
			throw new GenericRestException("Asset could not be unassigned.", HttpStatus.NOT_FOUND);
		}
		a.setAssetUnassignedBy(httpServletRequest.getHeader("username"));
		System.out.println(a.getAssetUnassignedBy());
		Date unassignmentDate = assetAssignment.getUnassignmentDate() == null ? new Date()
				: assetAssignment.getUnassignmentDate();
		a.setUnassignmentDate(unassignmentDate);
		AssetAssignment a1 = assetAssignmentRepository.save(a);
		if (a1 != null) {
			assetService.saveUnAssignment(a1);
			executor.execute(() -> {
				// sendUnAssignmentEmail(a1);
			});
			return a1;
		} else {
			return null;
		}
	}

	private void sendUnAssignmentEmail(AssetAssignment a1) {
		String mailTo = "";
		if (a1.getAssetAssignmentType().getName().equalsIgnoreCase("EMPLOYEE")) {
			Employee e = a1.getEmployee();
			mailTo = e.getEmail();
		}
			String mailCc = a1.getAsset().getCategory().getMail();
			if(a1.getInformTo()!=null && !a1.getInformTo().trim().isEmpty()) {
				mailCc+=";"+a1.getInformTo();
			}
			String subject = "Asset Unassigned";
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("assetAssignment", a1);
			String type = a1.getAsset().getCategory().getName().equalsIgnoreCase("IT EQUIPMENT") ? "it asset" : "facility asset";
			Mail mail = new Mail(mailTo, mailCc, subject, null, variables, "liability_letter_html_template2","/asset/details/"+a1.getAsset().getId(), type);
			emailSenderService.sendEmail(mail);
	}

	@Override
	public AssetAssignment reassign(AssetAssignment a) {
		AssetAssignment a1 = unassign(a);
		if (a1 != null) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return assign(a);
		}
		return null;
	}

	public boolean validate(AssetAssignment a) {
		List<String> messages = validator.validate(a).stream().map(e -> e.getPropertyPath() + ":" + e.getMessage())
				.collect(Collectors.toList());
		if (a.getAssetAssignmentType() != null) {
			String type = a.getAssetAssignmentType().getName().toUpperCase();
			if (type.equals("EMPLOYEE") && a.getEmployee() == null)
				messages.add("employee:Employee is required for assignment type EMPLOYEE.");
			if (type.equals("LOCATION") && (a.getLocation() == null || a.getLocation().isEmpty()))
				messages.add("location:Location is required for assignment type LOCATION.");
			if (type.equals("AREA") && a.getArea() == null)
				messages.add("area:Area is required for assignment type AREA.");
			if (type.equals("PROJECT") && a.getProject() == null)
				messages.add("project:Project is required for assignment type PROJECT.");
			if (type.equals("DEPARTMENT") && a.getDepartment() == null)
				messages.add("department:Department is required for assignment type DEPARTMENT.");
			if (a.getAsset().getTagId().contains("TAG-ID-")) {
				messages.add("tagId: Please change the tag id before assigning the asset.");
			}
			String[] statusList = { "UNASSIGNED", "APPROVED" };
			if (!Arrays.asList(statusList).contains(a.getAsset().getAssetStatus().getName().toUpperCase())) {
				messages.add("asset.id: Asset is not eligible to be assigned.");
			}
		}
		if (!messages.isEmpty()) {
			throw new InvalidEntityException("Asset could not be assigned.", messages);
		}
		return true;
	}

	@Override
	public AssetAssignment update(AssetAssignment assetAssignment) {
		AssetAssignment a = assetAssignmentRepository.findById(assetAssignment.getId()).orElse(null);
		if (a == null) {
			throw new GenericRestException("Asset assignment could not be found.", HttpStatus.NOT_FOUND);
		} else {
			a.setAdditionalDetails(assetAssignment.getAdditionalDetails());
			a.setComment(assetAssignment.getComment());
			a.setCountry(assetAssignment.getCountry());
			a.setInformTo(assetAssignment.getInformTo());
			return assetAssignmentRepository.save(a);
		}
	}

	@Override
	public List<AssetAssignment> findAllByEmployeeAndUnassignmentDateIsNotNull(Employee employee) {
		return assetAssignmentRepository.findAllByEmployeeAndUnassignmentDateIsNotNull(employee);
	}
	
	@Override
	public List<AssetAssignment> findAllByEmployeeAndUnassignmentDateIsNull(Employee employee) {
		return assetAssignmentRepository.findAllByEmployeeAndUnassignmentDateIsNull(employee);
	}
}
