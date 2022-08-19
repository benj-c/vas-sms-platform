package com.sys.vas.datamodel.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "sms_history")
@Getter
@Setter
public class SmsHistoryEntity {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "received_time", nullable = false)
    private LocalDateTime receivedTime;

    @Basic
    @Column(name = "sent_time", nullable = false)
    private LocalDateTime sentTime;

    @Basic
    @Column(name = "msisdn", nullable = false, length = 10)
    private String msisdn;

    @Basic
    @Column(name = "incoming_sms", nullable = false, length = 255)
    private String incomingSms;

    @Basic
    @Column(name = "response_sms", nullable = false, length = 255)
    private String responseSms;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SmsHistoryEntity that = (SmsHistoryEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(receivedTime, that.receivedTime) && Objects.equals(sentTime, that.sentTime) && Objects.equals(msisdn, that.msisdn) && Objects.equals(incomingSms, that.incomingSms) && Objects.equals(responseSms, that.responseSms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, receivedTime, sentTime, msisdn, incomingSms, responseSms);
    }
}
