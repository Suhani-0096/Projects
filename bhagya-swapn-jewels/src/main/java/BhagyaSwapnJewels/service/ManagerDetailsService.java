package BhagyaSwapnJewels.service;

import BhagyaSwapnJewels.entity.Manager;
import BhagyaSwapnJewels.repository.ManagerRepository;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ManagerDetailsService implements UserDetailsService {

    private final ManagerRepository managerRepository;

    public ManagerDetailsService(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Manager manager = managerRepository.findByUsername(username)
                .orElseThrow(()
                        -> new UsernameNotFoundException("Manager not found"));

        return User.withUsername(manager.getUsername())
                .password(manager.getPassword())
                .roles(manager.getRole())
                .build();
    }
}
