package com.teammachine.staffrostering.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * A MinMaxContractLine.
 */
@Entity
@Table(name = "contract_line")
@Inheritance
@DiscriminatorValue(value = "M")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MinMaxContractLine extends ContractLine {

    private static final long serialVersionUID = 1L;

    @Column(name = "minimum_enabled")
    private Boolean minimumEnabled;

    @Column(name = "minimum_value")
    private Integer minimumValue;

    @Column(name = "minimum_weight")
    private Integer minimumWeight;

    @Column(name = "maximum_enabled")
    private Boolean maximumEnabled;

    @Column(name = "maximum_value")
    private Integer maximumValue;

    @Column(name = "maximum_weight")
    private Integer maximumWeight;

    public Boolean isMinimumEnabled() {
        return minimumEnabled;
    }

    public void setMinimumEnabled(Boolean minimumEnabled) {
        this.minimumEnabled = minimumEnabled;
    }

    public Integer getMinimumValue() {
        return minimumValue;
    }

    public void setMinimumValue(Integer minimumValue) {
        this.minimumValue = minimumValue;
    }

    public Integer getMinimumWeight() {
        return minimumWeight;
    }

    public void setMinimumWeight(Integer minimumWeight) {
        this.minimumWeight = minimumWeight;
    }

    public Boolean isMaximumEnabled() {
        return maximumEnabled;
    }

    public void setMaximumEnabled(Boolean maximumEnabled) {
        this.maximumEnabled = maximumEnabled;
    }

    public Integer getMaximumValue() {
        return maximumValue;
    }

    public void setMaximumValue(Integer maximumValue) {
        this.maximumValue = maximumValue;
    }

    public Integer getMaximumWeight() {
        return maximumWeight;
    }

    public void setMaximumWeight(Integer maximumWeight) {
        this.maximumWeight = maximumWeight;
    }

    @Override
    public String toString() {
        return "MinMaxContractLine{" +
            "id=" + getId() +
            ", contractLineType='" + getContractLineType() + "'" +
            ", minimumEnabled='" + minimumEnabled + "'" +
            ", minimumValue='" + minimumValue + "'" +
            ", minimumWeight='" + minimumWeight + "'" +
            ", maximumEnabled='" + maximumEnabled + "'" +
            ", maximumValue='" + maximumValue + "'" +
            ", maximumWeight='" + maximumWeight + "'" +
            '}';
    }

    @Override
    Boolean isEnabled() {
        return minimumEnabled || maximumEnabled;
    }
}
