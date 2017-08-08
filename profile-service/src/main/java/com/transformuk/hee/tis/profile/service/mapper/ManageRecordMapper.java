package com.transformuk.hee.tis.profile.service.mapper;

import com.transformuk.hee.tis.profile.domain.ManageRecord;
import com.transformuk.hee.tis.profile.service.dto.ManageRecordDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity ManageRecord and its DTO ManageRecordDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ManageRecordMapper {

  ManageRecordDTO manageRecordToManageRecordDTO(ManageRecord manageRecord);

  List<ManageRecordDTO> manageRecordsToManageRecordDTOs(List<ManageRecord> manageRecords);

  ManageRecord manageRecordDTOToManageRecord(ManageRecordDTO manageRecordDTO);

  List<ManageRecord> manageRecordDTOsToManageRecords(List<ManageRecordDTO> manageRecordDTOs);
}
