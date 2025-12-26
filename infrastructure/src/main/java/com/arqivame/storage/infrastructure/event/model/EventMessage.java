package com.arqivame.storage.infrastructure.event.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

public class EventMessage<D extends Serializable> implements Serializable {

    private String domain;
    private String entity;
    private String action;
    private String service;
    private String version;
    private Instant occurredAt;
    private Set<EventEntity> relatedEntities;
    private D data;

    public EventMessage(
            String domain,
            String entity,
            String action,
            String service,
            String version,
            Instant occurredAt,
            Set<EventEntity> relatedEntities,
            D data) {
        this.domain = domain;
        this.entity = entity;
        this.action = action;
        this.service = service;
        this.version = version;
        this.occurredAt = occurredAt;
        this.relatedEntities = relatedEntities;
        this.data = data;
    }

    public record EventEntity(String type, String id) {

    }

    public EventMessage() {
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(Instant occurredAt) {
        this.occurredAt = occurredAt;
    }

    public Set<EventEntity> getRelatedEntities() {
        return relatedEntities;
    }

    public void setRelatedEntities(Set<EventEntity> relatedEntities) {
        this.relatedEntities = relatedEntities;
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }

}
