package com.taskflow.activity.repository;

import com.taskflow.activity.entity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Page<Activity> findByUserIdOrderByTimestampDesc(Long userId, Pageable pageable);

    boolean existsByEventId(String eventId);
}
