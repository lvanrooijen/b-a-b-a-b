package com.lvr.babab.babab.entities.users.dto;

import com.lvr.babab.babab.entities.users.BusinessUser;

public record BusinessPatchResponse(Long id, String email, String companyName, String kvkNumber) {
    public static BusinessPatchResponse to(BusinessUser user) {
        return new BusinessPatchResponse(
                user.getId(),
                user.getEmail(),
                user.getCompanyName(),
                user.getKvkNumber());
    }
}
