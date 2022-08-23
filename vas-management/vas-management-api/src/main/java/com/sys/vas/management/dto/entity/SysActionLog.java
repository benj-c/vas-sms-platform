package com.sys.vas.management.dto.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "sys_action_log")
@Getter
@Setter
public class SysActionLog {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "log", nullable = true, length = 1000)
    private String log;

    @Basic
    @Column(name = "timestamp", nullable = true)
    private LocalDateTime timestamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysActionLog that = (SysActionLog) o;
        return Objects.equals(id, that.id) && Objects.equals(log, that.log) && timestamp.isEqual(that.getTimestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, log, timestamp);
    }
}
