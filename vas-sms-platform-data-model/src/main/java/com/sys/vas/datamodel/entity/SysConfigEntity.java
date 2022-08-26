package com.sys.vas.datamodel.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "sys_config", schema = "vas_sms", catalog = "")
@Getter
@Setter
public class SysConfigEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "sms_in_count", nullable = false)
    private Integer smsInCount;

    @Basic
    @Column(name = "sms_out_count", nullable = false)
    private Integer smsOutCount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysConfigEntity that = (SysConfigEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(smsInCount, that.smsInCount) && Objects.equals(smsOutCount, that.smsOutCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, smsInCount, smsOutCount);
    }
}
