package eu.kpentchev.template.security.service;

import eu.kpentchev.template.security.domain.Account;

public interface RegistrationService {

    Account register(String email, String password);

}
