package com.sys.vas.management.dto.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "api_history", schema = "vas_sms", catalog = "")
@Getter
@Setter
public class ApiHistoryEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID", nullable = false)
    private Long id;

    @Basic
    @Column(name = "commit_id", nullable = false, length = 50)
    private String commitId;

    @Basic
    @Column(name = "commit_message", nullable = false, length = 50)
    private String commitMessage;

    @Basic
    @Column(name = "version", nullable = true, length = 50)
    private String version;

    @Basic
    @Column(name = "commited_date_time", nullable = false)
    private LocalDateTime commitedDateTime;

    @Basic
    @Column(name = "xml", nullable = false, length = -1, columnDefinition = "LONG")
    private String xml;

    @Basic
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "api_id", referencedColumnName = "ID", nullable = false)
    private ApiEntity api;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiHistoryEntity that = (ApiHistoryEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(commitId, that.commitId) && Objects.equals(commitMessage, that.commitMessage) && Objects.equals(version, that.version) && Objects.equals(xml, that.xml) && Objects.equals(isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, commitId, commitMessage, version, xml, isActive);
    }
}
