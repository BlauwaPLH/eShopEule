package org.senju.eshopeule.service;

import org.senju.eshopeule.dto.ProfileDTO;

public interface CustomerService {

    ProfileDTO getProfileOfCurrentUser();

    ProfileDTO updateProfile(ProfileDTO dto);
}
