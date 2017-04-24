package com.transformuk.hee.tis.profile.repository;

import com.transformuk.hee.tis.profile.domain.ManageRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the ManageRecord entity.
 */
@SuppressWarnings("unused")
public interface ManageRecordRepository extends JpaRepository<ManageRecord, Long> {

}
