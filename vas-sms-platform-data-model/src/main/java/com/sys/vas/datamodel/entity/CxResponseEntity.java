package com.sys.vas.datamodel.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "cx_response")
@Getter
@Setter
public class CxResponseEntity {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "res_code", nullable = false)
    private Integer resCode;

    @Basic
    @Column(name = "res_desc", nullable = true, length = 255)
    private String resDesc;

    @Basic
    @Column(name = "send_to_originated_no", nullable = false)
    private Integer sendToOriginatedNo;

    @Basic
    @Column(name = "sms", nullable = true, length = 512)
    private String sms;

    @ManyToOne
    @JoinColumn(name = "api_id", referencedColumnName = "id")
    private ApiEntity api;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CxResponseEntity that = (CxResponseEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(resCode, that.resCode) && Objects.equals(resDesc, that.resDesc) && Objects.equals(sendToOriginatedNo, that.sendToOriginatedNo) && Objects.equals(sms, that.sms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, resCode, resDesc, sendToOriginatedNo, sms);
    }
}
