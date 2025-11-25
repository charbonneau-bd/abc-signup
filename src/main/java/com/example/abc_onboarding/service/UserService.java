package com.example.abc_onboarding.service;

import com.example.abc_onboarding.dto.UpdateUserDTO;
import com.example.abc_onboarding.model.User;
import com.example.abc_onboarding.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class UserService {

    private final UserRepository userRepository;
    @Autowired
    private JavaMailSender mailSender;
    private static final SecureRandom random = new SecureRandom();
    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Generate a 12 digit alphanumeric ID to assign to the user, checking for existence to confirm it's unique.
    private String generateUniqueAccountId() {
        String id;
        do {
            id = IntStream.range(0, 12)
                    .mapToObj(i -> String.valueOf(ALPHANUMERIC.charAt(
                            random.nextInt(ALPHANUMERIC.length()))))
                    .collect(Collectors.joining());
        } while (userRepository.existsByAccountId(id));
        return id;
    }

    // Ensure users with that SSN are not already registered before generating their ID and saving them.
    public User createUser(User user, byte[] identificationFile) {
        if (userRepository.existsBySsn(user.getSsn())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with this SSN already exists.");
        }
        user.setOnboarding(Boolean.TRUE);
        user.setIdentification(identificationFile);
        return userRepository.save(user);
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }


    public void sendOnboardingCompletedEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Welcome to ABC Bank!");
        message.setText("Dear " + user.getFirstName() + ",\n\n" +
                "Your account has been verified. You now have full access to our services."+ ",\n\n" +
                "Thank you for registering with us, and we look forward to your business.");
        mailSender.send(message);
    }

    /* Implement a transactional handler for updating onboarding status.
    The reason it is not a hardcoded acceptance message is that I figured this
    implementation would allow for future scalability with something like
    failed onboarding or specific different onboarding messages for different
    users or registrations.
     */
    @Transactional
    public User updateOnboardingStatus(Long userId, boolean onboarding) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        boolean oldStatus = user.isOnboarding();
        if (user.getAccountId() == null) {
            user.setAccountId(generateUniqueAccountId());
        }
        user.setOnboarding(onboarding);
        userRepository.save(user);

        if (oldStatus && !onboarding) {
            sendOnboardingCompletedEmail(user);
        }

        return user;
    }

    @Transactional
    public User updateUser(UpdateUserDTO dto) {
        Optional<User> optionalUser = userRepository.findById(dto.getId());

        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        User user = optionalUser.get();

        // Do not include SSN, Identification, and onboarding.
        // If necessary, create an alternate updateClass that will not
        // be as exposed or broadly used.
        if (dto.getFirstName() != null) user.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) user.setLastName(dto.getLastName());
        if (dto.getGender() != null) user.setGender(dto.getGender());
        if (dto.getDateOfBirth() != null) user.setDateOfBirth(dto.getDateOfBirth());
        if (dto.getPhoneNumber() != null) user.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getNationality() != null) user.setNationality(dto.getNationality());
        if (dto.getAddress() != null) user.setAddress(dto.getAddress());

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userRepository.deleteById(id);
    }
}
