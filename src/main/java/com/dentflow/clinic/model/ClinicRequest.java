package com.dentflow.clinic.model;

import com.dentflow.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClinicRequest {
    private long clinicId;
    private String name;
    private User owner;
    private Long clinicId;

    public static Clinic toEntity(ClinicRequest request,User user) {
        return Clinic.builder()
                .name(request.getName())
                .owner(user)
                .build();
    }
}
