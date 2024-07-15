package org.senju.eshopeule.listener;

import org.senju.eshopeule.model.AbstractAuditDocument;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

@Component
public class MongoAuditingEntityListener extends AbstractMongoEventListener<Object> {

    private final AuditingHandler auditingHandler;

    public MongoAuditingEntityListener(@Qualifier("mongoAuditingHandler") AuditingHandler handler) {
        this.auditingHandler = handler;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Object> event) {
        Object source = event.getSource();
        if (source instanceof AbstractAuditDocument document) {
            if (document.getCreatedBy() == null) {
                auditingHandler.markCreated(document);
            }
            if (document.getLastModifiedBy() == null) {
                document.setLastModifiedBy(document.getCreatedBy());
            }
        }
        super.onBeforeConvert(event);
    }

    @Override
    public void onBeforeSave(BeforeSaveEvent<Object> event) {
        Object source = event.getSource();
        if (source instanceof AbstractAuditDocument document) {
            if (document.getLastModifiedBy() == null) {
                auditingHandler.markCreated(document);
            }
        }
        super.onBeforeSave(event);
    }
}
