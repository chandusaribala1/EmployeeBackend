package org.example.angularbackend.repository;

import java.time.LocalDate;
import java.util.List;
import org.example.angularbackend.entity.PublicHoliday;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicHolidayRepository extends JpaRepository<PublicHoliday, Long> {
    List<PublicHoliday> findAllBySourceOrderByHolidayDateAsc(String source);
    List<PublicHoliday> findAllByHolidayDateBetweenAndSourceOrderByHolidayDateAsc(LocalDate startDate, LocalDate endDate, String source);
    boolean existsByHolidayDate(LocalDate holidayDate);
    java.util.Optional<PublicHoliday> findByHolidayDateAndName(LocalDate holidayDate, String name);
}
