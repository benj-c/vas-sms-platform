package com.sys.vas.datamodel.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "service")
@Getter
@Setter
public class ServiceEntity {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "description", nullable = true, length = 255)
    private String description;

    @Basic
    @Column(name = "name", nullable = true, length = 255)
    private String name;

    @Basic
    @Column(name = "disable", nullable = false)
    private Boolean disable;

    @Basic
    @Column(name = "disable_sms", nullable = true, length = 255)
    private String disableSms;

    @Basic
    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;

    @OneToMany(mappedBy = "service")
    private Set<ActionEntity> actions;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceEntity that = (ServiceEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(description, that.description) && Objects.equals(name, that.name) && Objects.equals(disable, that.disable) && Objects.equals(disableSms, that.disableSms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, name, disable, disableSms);
    }
}
