package com.transformuk.hee.tis.auth.repository;

import com.transformuk.hee.tis.auth.model.TraineeProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * Spring data Repository for {@link com.transformuk.hee.tis.auth.model.TraineeProfile} domain entity.
 */
@Repository
public interface TraineeIdRepository extends RevisionRepository<TraineeProfile, Long, Integer>, JpaRepository<TraineeProfile, Long> {

    List<TraineeProfile> findByGmcNumberIn(Collection<String> gmcNumber);

    Page<TraineeProfile> findByDesignatedBodyCode(String designatedBodyCode, Pageable pageable);

    List<TraineeProfile> findByDesignatedBodyCode(String dbc);
}
