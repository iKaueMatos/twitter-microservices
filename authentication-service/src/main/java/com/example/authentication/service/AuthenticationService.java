package com.example.authentication.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.authentication.client.ProfileServiceClient;
import com.example.authentication.client.request.CreateProfileRequest;
import com.example.authentication.dto.request.AuthenticationRequest;
import com.example.authentication.dto.request.RegisterRequest;
import com.example.authentication.dto.response.ActivationCodeResponse;
import com.example.authentication.dto.response.AuthenticationResponse;
import com.example.authentication.entity.Account;
import com.example.authentication.entity.ActivationCode;
import com.example.authentication.exception.AccountNotActivatedException;
import com.example.authentication.exception.InvalidCredentialsException;
import com.example.authentication.repository.AccountRepository;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtService jwtService;
    private final ActivationCodeService activationCodeService;
    private final AccountService accountService;
    private final TokenService tokenService;
    private final MessageSourceService messageService;
    private final ProfileServiceClient profileServiceClient;
    private final AuthenticationManager authenticationManager;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public ActivationCodeResponse register(RegisterRequest request) {
        if (accountService.isAccountExists(request.email())) {
            throw new EntityExistsException(
                    messageService.generateMessage("error.account.already_exists", request.email())
            );
        }

        Account newAccount = accountService.createAccount(request.email(), request.password(), false);
        log.info("Account {} has been created", newAccount.getId());

        CreateProfileRequest createProfileRequest = new CreateProfileRequest(request.username(), request.email(), LocalDate.now());
        String profileId = profileServiceClient.createProfile(createProfileRequest);
        log.info("Profile {} has been created", profileId);

        activationCodeService.sendNewActivationCode(newAccount);
        log.info("Activation code has been sent to {}", newAccount.getEmail());

        return ActivationCodeResponse.builder()
                .message(messageService.generateMessage("activation.send.success"))
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Optional<Account> optionalAccount = accountRepository.findByEmail(request.email());
        if (optionalAccount.isEmpty()) {
            log.error("Authentication failed: account with email {} not found", request.email());
            throw new InvalidCredentialsException(
                    messageService.generateMessage("error.account.credentials_invalid")
            );
        }
    
        Account account = optionalAccount.get();
        if (!account.isEnabled()) {
            log.error("Authentication failed: account with email {} is not activated", account.getEmail());
            throw new AccountNotActivatedException(
                    messageService.generateMessage("error.account.not_activated", account.getEmail())
            );
        }
    
        if (!passwordEncoder.matches(request.password(), account.getPassword())) {
            log.error("Authentication failed: invalid password for account {}", request.email());
            throw new InvalidCredentialsException(
                    messageService.generateMessage("error.account.credentials_invalid")
            );
        }
    
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
        } catch (BadCredentialsException e) {
            log.error("Authentication failed: invalid credentials for account {}", request.email());
            throw new InvalidCredentialsException(
                    messageService.generateMessage("error.account.credentials_invalid")
            );
        } catch (DisabledException e) {
            log.error("Authentication failed: account with email {} is disabled", request.email());
            throw new AccountNotActivatedException(
                    messageService.generateMessage("error.account.not_activated", request.email())
            );
        }
    
        String jwt = jwtService.generateJwt(account);
        log.info("JWT successfully generated for account {}", account.getEmail());
        tokenService.deleteTokenByAccount(account);
        tokenService.createToken(account, jwt);
        
        return AuthenticationResponse.builder()
                .jwt(jwt)
                .build();
    }
    
    public ActivationCodeResponse activate(String key) {
        ActivationCode activationCode = activationCodeService.findActivationCodeByKey(key);
        activationCodeService.checkActivationCodeExpiration(activationCode);
        accountService.enableAccount(activationCode.getAccount());
        activationCodeService.deleteActivationCodeById(activationCode.getId());

        log.info("Account with email {} has been successfully activated", activationCode.getAccount().getEmail());
        return ActivationCodeResponse.builder()
                .message(messageService.generateMessage("account.activation.success"))
                .build();
    }
}
