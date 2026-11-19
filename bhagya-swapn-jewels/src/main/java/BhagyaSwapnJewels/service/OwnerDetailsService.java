package BhagyaSwapnJewels.service;

import BhagyaSwapnJewels.entity.Owner;
import BhagyaSwapnJewels.repository.OwnerRepository;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class OwnerDetailsService implements UserDetailsService {

    private final OwnerRepository ownerRepository;

    public OwnerDetailsService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Owner owner = ownerRepository.findByUsername(username)
                .orElseThrow(()
                        -> new UsernameNotFoundException("Owner not found"));

        return User.withUsername(owner.getUsername())
                .password(owner.getPassword())
                .roles(owner.getRole())
                .build();
    }
}
