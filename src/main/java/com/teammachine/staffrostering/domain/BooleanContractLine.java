package com.teammachine.staffrostering.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import com.teammachine.staffrostering.domain.enumeration.ContractLineType;

/**
 * A BooleanContractLine.
 */
@Entity
@Table(name = "contract_line")
@Inheritance
@DiscriminatorValue(value = "B")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BooleanContractLine extends ContractLine {

    private static final long serialVersionUID = 1L;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "weight")
    private Integer weight;

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "BooleanContractLine{" +
            "id=" + getId() +
            ", contractLineType='" + getContractLineType() + "'" +
            ", enabled='" + enabled + "'" +
            ", weight='" + weight + "'" +
            '}';
    }
}
