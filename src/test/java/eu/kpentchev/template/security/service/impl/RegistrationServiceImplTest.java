package eu.kpentchev.template.security.service.impl;

import eu.kpentchev.template.security.domain.Account;
import eu.kpentchev.template.security.repository.AccountRepository;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationServiceImplTest {

    private static final String EMAIL = "test@email.com";

    private static final String PLAIN_PWD = "123456";

    private static final String ENCODED_PWD = "qweasd";

    private static final List<String> DEFAULT_ROLES = Arrays.asList("USER");

    @InjectMocks
    private RegistrationServiceImpl tested;

    @Mock
    private AccountRepository accounts;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(tested, "defaultRoles", DEFAULT_ROLES);
        Mockito.when(passwordEncoder.encode(PLAIN_PWD)).thenReturn(ENCODED_PWD);
    }

    @Test
    public void testRegister() {
        Mockito.when(accounts.save(Mockito.any(Account.class))).thenAnswer((args) -> {
            Account toBeSaved = args.getArgument(0);
            toBeSaved.setId(1L);
            toBeSaved.setCreated(LocalDateTime.now());
            toBeSaved.setUpdated(LocalDateTime.now());
            return toBeSaved;
        });
        Account saved = tested.register(EMAIL, PLAIN_PWD);
        Assert.assertThat(saved, Matchers.notNullValue());
    }

}
