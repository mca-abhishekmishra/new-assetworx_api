package com.softtek.assetworx_api.serviceImpl;

import static com.softtek.assetworx_api.util.Constants.EMPLOYEE_EXISTS_EMAIL;
import static com.softtek.assetworx_api.util.Constants.EMPLOYEE_EXISTS_ID;
import static com.softtek.assetworx_api.util.Constants.EMPLOYEE_EXISTS_ISID;
import static com.softtek.assetworx_api.util.Constants.EMPLOYEE_NOTFOUND_ID;
import static com.softtek.assetworx_api.util.Constants.EMPLOYEE_NOTSAVED;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.softtek.assetworx_api.entity.AssetworxUser;
import com.softtek.assetworx_api.entity.Employee;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.exception.InvalidEntityException;
import com.softtek.assetworx_api.repository.EmployeeRepository;
import com.softtek.assetworx_api.service.AssetAssignmentService;
import com.softtek.assetworx_api.service.DepartmentService;
import com.softtek.assetworx_api.service.EmployeeService;
import com.softtek.assetworx_api.service.ProjectService;
import com.softtek.assetworx_api.service.UserService;
import com.softtek.assetworx_api.service.WorkstationAllocationService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	UserService userService;

	@Autowired
	Validator validator;
	
	@Autowired
	DepartmentService departmentService;
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	WorkstationAllocationService workstationAllocationService;
	
	@Autowired
	AssetAssignmentService assetAssignmentService;
	
	@Autowired
	EmployeeService employeeService;

	@Override
	public Employee findById(String id) {
		return employeeRepository.findById(id).orElse(null);
	}

	@Override
	public Employee findByName(String name) {
		return employeeRepository.findByName(name).orElse(null);
	}
	
	@Override
	public Employee findByEmail(String email) {
		return employeeRepository.findByEmail(email).orElse(null);
	}

	public boolean validate(Employee employee) {
		List<String> messages = validator.validate(employee).stream()
				.map(e -> e.getPropertyPath() + ":" + e.getMessage()).collect(Collectors.toList());
		Employee f = employeeRepository.findFirstByEmailAndIdNotLike(employee.getEmail(), employee.getId());
		if (f != null)
			messages.add(EMPLOYEE_EXISTS_EMAIL + employee.getEmail());
		f = employeeRepository.findFirstByIsidAndIdNotLike(employee.getIsid(), employee.getId());
		if (f != null)
			messages.add(EMPLOYEE_EXISTS_ISID + employee.getIsid());
		f = employeeRepository.findFirstByEmployeeIdAndIdNotLike(employee.getEmployeeId(), employee.getId());
		if (f != null)
			messages.add(EMPLOYEE_EXISTS_ID + employee.getEmployeeId());
		if (!messages.isEmpty()) {
			throw new InvalidEntityException(EMPLOYEE_NOTSAVED, messages);
		}
		return true;

	}

	@Override
	public Employee save(Employee employee) {
		employee.setId("");
		employee.setDepartment(departmentService.findById(employee.getDepartment() != null ? employee.getDepartment().getId() : ""));
		employee.setProject(projectService.findById(employee.getProject()!=null ? employee.getProject().getId() :  "" ));
		employee.setReportingTo(findById(employee.getReportingTo()!=null ? employee.getReportingTo().getId() : ""));
		if (validate(employee)) {
			return employeeRepository.save(employee);
		}
		return null;
	}

	@Override
	public Employee update(Employee employee) {
		Employee f = findById(employee.getId());
		if (f == null) {
			throw new GenericRestException(EMPLOYEE_NOTFOUND_ID + employee.getId(), HttpStatus.NOT_FOUND);
		} else {
			f.setName(employee.getName());
			f.setIsid(employee.getIsid());
			f.setEmail(employee.getEmail());
			f.setEmployeeId(employee.getEmployeeId());
			f.setEmploymentType(employee.getEmploymentType());
			f.setMobileNo(employee.getMobileNo());
			f.setVoip(employee.getVoip());
			f.setDepartment(departmentService.findById(employee.getDepartment() != null ? employee.getDepartment().getId() : ""));
			f.setProject(projectService.findById(employee.getProject()!=null ? employee.getProject().getId() :  "" ));
			f.setReportingTo(findById(employee.getReportingTo()!=null ? employee.getReportingTo().getId() : ""));
			if (validate(f)) {
				AssetworxUser user = userService.findByUserName(employee.getEmployeeId());
				if (user != null) {
					user.setFullName(employee.getName());
					user.setEmail(employee.getEmail());
					userService.save(user);
				}
				return employeeRepository.save(f);
			}
			return null;
		}
	}

	public boolean isDeletable(Employee employee) {
		/*
		 * if (employee.getAssets().size()>0) { throw new
		 * ResourceNotDeletableException("employee cannot be deleted since assets with this employee exists."
		 * ); }
		 */
		return true;
	}

	@Override
	public boolean delete(String id) {
		Employee employee = findById(id);
		if (employee == null) {
			throw new GenericRestException(EMPLOYEE_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);
		}
		if (isDeletable(employee)) {
			employee.setActive(false);
			employee.setName(employee.getName() + "~" + System.nanoTime());
			// for employee role part
			try {
				userService.delete(employee.getIsid());
			} catch (Exception e) {
				e.printStackTrace();
			}
			employeeRepository.save(employee);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Employee findByIsid(String isid) {
		return employeeRepository.findByIsid(isid).orElse(null);
	}

	@Override
	public Employee removeEmployee(Employee emp) {
		Employee employee = findById(emp.getId());
		if (employee == null) {
			return null;
		} else {
			if(workstationAllocationService.findAllByEmployeeAndIsAllocated(employee, true).size() > 0 ) {
				throw new GenericRestException("Please deallocate the employee  from the workstation before removing the employee.", HttpStatus.BAD_REQUEST);
			}
			if(assetAssignmentService.findAllByEmployeeAndUnassignmentDateIsNull(employee).size() > 0 ) {
				throw new GenericRestException("Please unassign all the assets assigend to  the employee before removing the employee.", HttpStatus.BAD_REQUEST);
			}
			employee.setLastWorkingDate(emp.getLastWorkingDate() == null ? new Date() : emp.getLastWorkingDate());
			employee.setEmploymentStatus(false);
			employee.setActive(false);
			employee.setEmployeeId(employee.getEmployeeId()+"~"+System.nanoTime());
			employee.setIsid(employee.getIsid()+"~"+System.nanoTime());
			employee.setEmail(employee.getEmail()+"~"+System.nanoTime());
			employee.setVoip(null);
			AssetworxUser user = userService.findByUserName(employee.getEmployeeId());
			if (user != null) {
				user.setActive(false);
				 userService.save(user);
			}
			return employeeRepository.save(employee);
		}
	}

}
