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

import javax.persistence.EntityNotFoundException;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class LoginServiceTest {

    public static final String USER_NAME = "jamesh";
    
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

    @Test
    public void shouldFetchUsersWithOutFilter() {
        // given
        given(users.getTotalElements()).willReturn(1L);
        given(users.getContent()).willReturn(newArrayList(new User()));
        given(userRepository.findAll(any(Pageable.class))).willReturn(users);

        // when
        UserListResponse response = service.getUsers(0, 1, null);

        // then
        assertEquals(1L, response.getTotal());
    }

    @Test
    public void shouldGetUserByUserName() throws Exception {
        // given
        User aUser = new User();
        aUser.setName(USER_NAME);
        
        given(userRepository.findOne(USER_NAME)).willReturn(aUser);

        // when
        User user = service.getUserByUserName(USER_NAME);

        // then
        assertThat(user).isSameAs(aUser);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowExceptionWhenUserNameNotFound() {
        // given
        given(userRepository.findOne(USER_NAME)).willReturn(null);

        // when
        service.getUserByUserName(USER_NAME);
    }
}
