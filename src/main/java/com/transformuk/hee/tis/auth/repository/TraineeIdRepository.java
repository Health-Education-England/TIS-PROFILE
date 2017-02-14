package com.transformuk.hee.tis.auth.repository;

import com.transformuk.hee.tis.auth.model.TraineeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring data Repository for {@link com.transformuk.hee.tis.auth.model.TraineeId} domain entity.
 */
@Repository
public interface TraineeIdRepository extends JpaRepository<TraineeId, Long> {

    List<TraineeId> findByGmcNumberIn(List<String> gmcNumber);
}
