package com.softtek.assetworx_api.serviceImpl;

import static com.softtek.assetworx_api.util.Constants.AREA_EXISTS;
import static com.softtek.assetworx_api.util.Constants.AREA_NOTFOUND_ID;
import static com.softtek.assetworx_api.util.Constants.AREA_NOTSAVED;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.softtek.assetworx_api.entity.Area;
import com.softtek.assetworx_api.entity.Workstation;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.exception.InvalidEntityException;
import com.softtek.assetworx_api.repository.AreaRepository;
import com.softtek.assetworx_api.service.AreaService;
import com.softtek.assetworx_api.service.AreaTypeService;
import com.softtek.assetworx_api.service.FloorService;
import com.softtek.assetworx_api.service.WorkStationService;

@Service
public class AreaServiceImpl implements AreaService {

	@Autowired
	AreaRepository areaRepository;

	@Autowired
	Validator validator;

	@Autowired
	FloorService floorService;

	@Autowired
	AreaTypeService areaTypeService;

	@Autowired
	WorkStationService workstationService;

	@Override
	public Area findById(String id) {
		System.out.println(workstationService.findEmployeeByWorkstationId(""));
		return areaRepository.findById(id).orElse(null);
	}

	@Override
	public Area findByName(String name) {
		return areaRepository.findByName(name).orElse(null);
	}

	public boolean validate(Area area) {
		List<String> messages = validator.validate(area).stream().map(e -> e.getPropertyPath() + ":" + e.getMessage())
				.collect(Collectors.toList());
		Area s = areaRepository.findFirstByNameAndIdNotLikeAndFloor(area.getName(), area.getId(), area.getFloor());
		if (s != null) {
			messages.add(AREA_EXISTS + area.getName());
		}
		if (!messages.isEmpty()) {
			throw new InvalidEntityException(AREA_NOTSAVED, messages);
		}
		return true;

	}

	@Override
	public Area save(Area area) {
		area.setId("");
		area.setParentArea(findById(area.getParentArea() != null ? area.getParentArea().getId() : ""));
		area.setFloor(floorService.findById(area.getFloor() != null ? area.getFloor().getId() : ""));
		area.setAreaType(areaTypeService.findById(area.getAreaType() != null ? area.getAreaType().getId() : ""));
		if (validate(area)) {
			Area savedArea = areaRepository.save(area);
			if (area.getAreaType().getName().equalsIgnoreCase("WORKSTATION")
					|| area.getAreaType().getName().equalsIgnoreCase("CABIN")) {
				// create workstation object here
				Workstation newStation = new Workstation();
				newStation.setArea(savedArea);
				workstationService.save(newStation);
			}
			return savedArea;
		}
		return null;
	}

	@Override
	public Area update(Area area) {
		Area s = findById(area.getId());
		if (s == null) {
			throw new GenericRestException(AREA_NOTFOUND_ID + area.getId(), HttpStatus.NOT_FOUND);
		} else {
			s.setParentArea(findById(area.getParentArea() != null ? area.getParentArea().getId() : ""));
			s.setName(area.getName());
			s.setDescription(area.getDescription());
			s.setFloor(floorService.findById(area.getFloor() != null ? area.getFloor().getId() : ""));
			s.setAreaType(areaTypeService.findById(area.getAreaType() != null ? area.getAreaType().getId() : ""));
			if (validate(s)) {
				Workstation ws = workstationService.findWorkStationByArea(s);
				if(ws != null ) {
					throw new GenericRestException("Area cannot be updated because the area is of type 'WORKSTATION' or 'CABIN'", HttpStatus.BAD_REQUEST);
				} else {
					if (area.getAreaType().getName().equalsIgnoreCase("WORKSTATION")
							|| area.getAreaType().getName().equalsIgnoreCase("CABIN")) {
						Workstation newStation = new Workstation();
						newStation.setArea(s);
						workstationService.save(newStation);
					}
				}
				return areaRepository.save(s);
			}
			return null;
		}
	}

	public boolean isDeletable(Area area) {
		/*
		 * if (area.getAssets().size()>0) { throw new
		 * ResourceNotDeletableException("Floor cannot be deleted since assets with this area exists."
		 * ); }
		 */
		return true;
	}

	@Override
	public boolean delete(String id) {
		Area area = findById(id);
		if (area == null) {
			throw new GenericRestException(AREA_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);
		}
		if (isDeletable(area)) {
			area.setActive(false);
			area.setName(area.getName() + "~" + System.nanoTime());
			areaRepository.save(area);
			return true;
		} else {
			return false;
		}
	}

}
