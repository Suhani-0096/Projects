package BhagyaSwapnJewels.service;

import BhagyaSwapnJewels.entity.Owner;
import BhagyaSwapnJewels.repository.OwnerRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;

    public OwnerService(
            OwnerRepository ownerRepository,
            PasswordEncoder passwordEncoder) {
        this.ownerRepository = ownerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean verifyOwnerPassword(String username, String rawPassword) {
        return ownerRepository.findByUsername(username)
                .map(owner -> passwordEncoder.matches(
                        rawPassword, owner.getPassword()))
                .orElse(false);
    }
}
