package com.transformuk.hee.tis.auth.service;

import com.transformuk.hee.tis.auth.model.User;
import com.transformuk.hee.tis.auth.model.UserListResponse;
import com.transformuk.hee.tis.auth.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class LoginServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuditEventRepository auditEventRepository;

    @Mock
    private Page<User> users;

    @InjectMocks
    private LoginService service;

    @Test
    public void shouldHandleFilterCorrectlyWhenFetchingUsers() {
        // given
        given(users.getTotalElements()).willReturn(1L);
        given(users.getContent()).willReturn(newArrayList(new User()));
        given(userRepository.findAll(any(Specification.class), any(Pageable.class))).willReturn(users);

        // when
        UserListResponse response = service.getUsers(0, 1, "(firstName=James)");

        // then
        assertEquals(1L, response.getTotal());
    }
}
