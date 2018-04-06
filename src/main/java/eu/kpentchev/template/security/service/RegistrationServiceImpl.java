package eu.kpentchev.template.security.service;

import eu.kpentchev.template.security.domain.Account;
import eu.kpentchev.template.security.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    @Autowired
    private AccountRepository accounts;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${template.security.default.roles:USER}")
    private List<String> defaultRoles;

    @Override
    @Transactional
    public Account register(String email, String password) {
        return accounts.save(Account.of(email, passwordEncoder.encode(password), defaultRoles));
    }
}
