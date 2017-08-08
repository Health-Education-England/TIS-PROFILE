package com.transformuk.hee.tis.profile.dto;

import com.google.common.collect.ImmutableMap;
import org.springframework.boot.actuate.audit.AuditEvent;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Holds the possible audit events for the revalidation service
 */
public enum AuditEventType {

  login("response"),
  logout("token");

  private static final String prefix = "auth_";
  private String dataField;

  AuditEventType(String dataField) {
    this.dataField = dataField;
  }

  /**
   * Creates an audit event
   *
   * @param userId    the user ID for the user which caused the event not null
   * @param eventType the event type not null
   * @param data      the data to store with the event
   * @return the created audit event
   */
  public static AuditEvent createEvent(String userId, AuditEventType eventType, Object data) {
    checkNotNull(userId);
    checkNotNull(eventType);
    return new AuditEvent(userId, prefix.concat(eventType.name()), ImmutableMap.of(eventType.field(), data));
  }

  /**
   * This is the label for the data to be stored with the audit event
   *
   * @return the data field label
   */
  public String field() {
    return dataField;
  }
}