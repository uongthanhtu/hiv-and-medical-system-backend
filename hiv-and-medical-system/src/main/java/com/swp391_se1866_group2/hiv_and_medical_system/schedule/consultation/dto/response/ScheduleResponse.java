package com.swp391_se1866_group2.hiv_and_medical_system.schedule.consultation.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScheduleResponse {
    LocalDate workDate;
    Set<ScheduleSlotResponse> scheduleSlots;
}
