package com.transformuk.hee.tis.auth.service;

import com.transformuk.hee.tis.auth.model.User;
import com.transformuk.hee.tis.auth.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.EntityNotFoundException;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class LoginServiceTest {

    public static final String USER_NAME = "jamesh";
    public static final String DESIGNATED_BODY_CODE = "1-DGBODY";
    
    @Mock
    private UserRepository userRepository;

    @Mock
    private AuditEventRepository auditEventRepository;

    @InjectMocks
    private LoginService service;

    @Test
    public void shouldFetchUsersWithPermissions() {
        // given
        String permissions = "revalidation:submit:to:gmc,revalidation:submit:on:behalf:of:ro";
        Page<User> page = new PageImpl<>(newArrayList(new User()));
        given(userRepository.findAll(any(Specification.class), any(Pageable.class))).willReturn(page);

        // when
        Page<User> actualPage =  service.getUsers(0, 1, DESIGNATED_BODY_CODE, permissions);

        // then
        assertSame(page, actualPage);
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
