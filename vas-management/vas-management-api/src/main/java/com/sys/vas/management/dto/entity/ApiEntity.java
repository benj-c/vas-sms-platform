package com.sys.vas.management.dto.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "api")
@Getter
@Setter
public class ApiEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column(name = "xml", nullable = true, length = -1, columnDefinition = "LONG")
    private String xml;

    @Basic
    @Column(name = "version", nullable = true)
    private String version;

    @OneToMany(mappedBy = "api")
    private Set<ApiHistoryEntity> apis;

    @JsonIgnore
    @OneToMany(mappedBy = "api")
    private Set<ActionEntity> actions;

    @JsonIgnore
    @OneToMany(mappedBy = "api")
    private Set<CxResponseEntity> cxResponses;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiEntity apiEntity = (ApiEntity) o;
        return Objects.equals(id, apiEntity.id) && Objects.equals(description, apiEntity.description) && Objects.equals(name, apiEntity.name) && Objects.equals(xml, apiEntity.xml);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, name, xml);
    }

}
