package com.transformuk.hee.tis.auth.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Repository to log audit events in the logs.
 */
public class CustomAuditRepository implements AuditEventRepository {

    private static final Logger LOG = getLogger(CustomAuditRepository.class);

    public static final String ANONYMOUS_USER = "anonymousUser";

    private ObjectMapper mapper = new ObjectMapper();
   
    @Override
    public void add(AuditEvent event) {
        String user = ofNullable(event.getPrincipal()).orElse(ANONYMOUS_USER);
        String data = null;
        try {
            data = mapper.writeValueAsString(event.getData());
        } catch (JsonProcessingException e) {
           throw new RuntimeException(e.getOriginalMessage());
        }
        LOG.info("AuditEvent: type={} user={} data={}", event.getType(), user, data);
    }

    @Override
    public List<AuditEvent> find(Date after) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<AuditEvent> find(String principal, Date after) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<AuditEvent> find(String principal, Date after, String type) {
        throw new UnsupportedOperationException();
    }
}
