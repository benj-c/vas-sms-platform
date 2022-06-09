package com.sys.vas.management.dto.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "action")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class ActionEntity {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "description", nullable = true, length = 255)
    private String description;

    @ManyToOne
    @JoinColumn(name = "api_id", referencedColumnName = "id")
    private ApiEntity api;

    @ManyToOne
    @JoinColumn(name = "service_id", referencedColumnName = "id")
    private ServiceEntity service;

    @OneToMany(mappedBy = "action")
    private Set<KeywordEntity> keywords;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActionEntity that = (ActionEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description);
    }

}
