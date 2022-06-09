package com.sys.vas.datamodel.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "keyword")
@Getter
@Setter
public class KeywordEntity {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "first_key", nullable = true, length = 255)
    private String firstKey;

    @Basic
    @Column(name = "reg_ex", nullable = true, length = 255)
    private String regEx;

    @ManyToOne
    @JoinColumn(name = "action_id", referencedColumnName = "id")
    private ActionEntity action;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeywordEntity that = (KeywordEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(firstKey, that.firstKey) && Objects.equals(regEx, that.regEx);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstKey, regEx);
    }

}
