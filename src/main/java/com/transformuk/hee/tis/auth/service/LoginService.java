package com.transformuk.hee.tis.auth.service;

import com.transformuk.hee.tis.auth.exception.UserNotFoundException;
import com.transformuk.hee.tis.auth.model.User;
import com.transformuk.hee.tis.auth.model.UserListResponse;
import com.transformuk.hee.tis.auth.repository.UserRepository;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.transformuk.hee.tis.auth.filters.UserSpecification.isEqualTo;
import static java.lang.String.format;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * Service for user login/logout process and getting user's data
 */
@Service
public class LoginService {
    private UserRepository userRepository;
    private AuditEventRepository auditEventRepository;

    public LoginService(UserRepository userRepository, AuditEventRepository auditEventRepository) {
        this.userRepository = userRepository;
        this.auditEventRepository = auditEventRepository;
    }

    /**
     *
     * @param userName User's unique identifier
     * @return {@link User} User associated with given unique user name
     */
    public User getUserByUserName(String userName) throws UserNotFoundException {
        User user = userRepository.findOne(userName);
        if(user == null) {
            throw new UserNotFoundException(format("User with username %s not found", userName));
        }
        auditEventRepository.add(new AuditEvent(userName, "GetUserEvent"));
        return user;
    }

    /**
     * Logs given audit event
     * @param {@link AuditEvent} Audit event to be logged
     */
    public void logEvent(AuditEvent event) {
        auditEventRepository.add(event);
    }

    /**
     * Returns all users by search criteria
     * @return {@link List<User>} list of users
     * @param offset
     * @param limit
     * @param filter
     */
    public UserListResponse getUsers(int offset, int limit, String filter) {
        int pageNumber = offset / limit;
        Pageable page = new PageRequest(pageNumber, limit, new Sort(ASC, "firstName"));
        Page<User> users;
        if(!isEmpty(filter)) {
            String[] nameValue = filter.split("=");
            users = userRepository.findAll(isEqualTo(nameValue[0], nameValue[1]), page);
        } else {
            users = userRepository.findAll(page);
        }
        auditEventRepository.add(new AuditEvent("Anonymous", "ListUsersEvent"));
        return new UserListResponse(users.getTotalElements(), users.getContent());
    }
}
