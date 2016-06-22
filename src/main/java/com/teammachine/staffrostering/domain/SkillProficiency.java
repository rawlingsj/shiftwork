package com.teammachine.staffrostering.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A SkillProficiency.
 */
@Entity
@Table(name = "skill_proficiency")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SkillProficiency implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private Employee employee;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "skill_proficiency_skill_list",
               joinColumns = @JoinColumn(name="skill_proficiencies_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="skill_lists_id", referencedColumnName="ID"))
    private Set<Skill> skillLists = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Set<Skill> getSkillLists() {
        return skillLists;
    }

    public void setSkillLists(Set<Skill> skills) {
        this.skillLists = skills;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SkillProficiency skillProficiency = (SkillProficiency) o;
        if(skillProficiency.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, skillProficiency.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SkillProficiency{" +
            "id=" + id +
            '}';
    }
}
