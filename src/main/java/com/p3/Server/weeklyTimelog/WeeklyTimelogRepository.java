package com.p3.Server.weeklyTimelog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeeklyTimelogRepository extends JpaRepository<WeeklyTimelog, Integer> {


}