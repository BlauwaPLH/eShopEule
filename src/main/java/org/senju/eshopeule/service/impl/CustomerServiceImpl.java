package org.senju.eshopeule.service.impl;

import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.ProfileDTO;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.mappers.CustomerMapper;
import org.senju.eshopeule.model.user.Customer;
import org.senju.eshopeule.repository.jpa.CustomerRepository;
import org.senju.eshopeule.service.CustomerService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static org.senju.eshopeule.constant.exceptionMessage.CustomerExceptionMsg.CUSTOMER_NOT_FOUND_WITH_USERNAME_MSG;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerMapper mapper;
    private final CustomerRepository customerRepository;

    @Override
    public ProfileDTO getProfileOfCurrentUser() {
        return mapper.convertToDTO(this.getCustomerMetaOfCurrentUser());
    }

    @Override
    public ProfileDTO updateProfile(ProfileDTO dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer loadedCustomer = customerRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException(String.format(CUSTOMER_NOT_FOUND_WITH_USERNAME_MSG, username))
        );

        Customer updatedProfile = mapper.convertToEntity(dto);
        updatedProfile.setId(loadedCustomer.getId());
        updatedProfile.setUser(loadedCustomer.getUser());

        return mapper.convertToDTO(customerRepository.save(updatedProfile));
    }

    private Customer getCustomerMetaOfCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return customerRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException(String.format(CUSTOMER_NOT_FOUND_WITH_USERNAME_MSG, username))
        );
    }
}
