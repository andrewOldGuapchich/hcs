package com.andrew.authenticationserver.service.logic;

import com.andrew.authenticationserver.entity.auth.Role;
import com.andrew.authenticationserver.entity.auth.User;
import com.andrew.authenticationserver.entity.dtos.RegistrationUserDto;
import com.andrew.authenticationserver.repository.RoleRepository;
import com.andrew.authenticationserver.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }


    public void updateUserStatus(String email){
        User user = findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(
                String.format("Пользователь '%s' не найден!", email)
        ));

        user.setStatus("A");
        userRepository.save(user);
    }

    public void createUserWaitingStatus(RegistrationUserDto registrationUserDto, int verificationCode){
        User user = new User();
        user.setEmail(registrationUserDto.getEmail());
        user.setRoles(List.of(getUserRole()));
        user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        user.setStatus("W");

        user.setCode(verificationCode);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(
                String.format("Пользователь '%s' не найден!", email)
        ));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList())
        );
    }

    private Role getUserRole(){
        return roleRepository.findByRole("USER").get();
    }
}
