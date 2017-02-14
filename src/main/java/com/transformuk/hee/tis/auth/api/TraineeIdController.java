package com.transformuk.hee.tis.auth.api;


import com.transformuk.hee.tis.auth.model.GmcNumberWrapper;
import com.transformuk.hee.tis.auth.model.PagedResponse;
import com.transformuk.hee.tis.auth.model.TraineeId;
import com.transformuk.hee.tis.auth.model.TraineeIdListResponse;
import com.transformuk.hee.tis.auth.service.TraineeIdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@Api(value = "/api/trainee-id", description = "API to get trainee id mappings")
@RequestMapping("/api/trainee-id")
@Validated
public class TraineeIdController {
	
	private static final Logger LOG = getLogger(TraineeIdController.class);

	private TraineeIdService traineeIdService;

	@Autowired
	public TraineeIdController(TraineeIdService traineeIdService) {
		this.traineeIdService = traineeIdService;
	}

	@ApiOperation(value = "traineeIds()", notes = "creates/gets mapping between traineeId and gmcIds", 
			response = TraineeIdListResponse.class, responseContainer = "traineeIdList")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Mapped trainee Ids", response = TraineeIdListResponse.class)
	})
	@CrossOrigin
	@RequestMapping(path = "/register", method = POST, produces = APPLICATION_JSON_VALUE, consumes = 
			APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('trainee-id:register:trainee')")
	public TraineeIdListResponse getOrCreateTraineeIds(@Valid @RequestBody List<GmcNumberWrapper> gmcNumbers) {
		List<String> gmcIds = gmcNumbers.stream().map(GmcNumberWrapper::getGmcNumber).collect(Collectors.toList());
		List<TraineeId> traineeIds = traineeIdService.findOrCreate(gmcIds);
		return new TraineeIdListResponse(traineeIds);
	}

	@ApiOperation(value = "getTraineeIds()", notes = "returns mapped trainee Ids", response = TraineeIdListResponse.class,
			responseContainer = "traineeIdList")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "returns mapped trainee Ids", response = TraineeIdListResponse.class)
	})
	@CrossOrigin
	@RequestMapping(path = "/mappings", method = GET, produces = APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('trainee-id:view:all:mappings')")
	public PagedResponse<TraineeId> getTraineeIds(Pageable pageable) {
		Page<TraineeId> page = traineeIdService.findAll(pageable);
		return new PagedResponse<>(page.getContent(), page.getTotalElements(), page.getTotalPages());
	}
}
