package com.teammachine.staffrostering.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import com.teammachine.staffrostering.domain.enumeration.ContractLineType;

/**
 * A ContractLine.
 */
@Entity
@Table(name = "contract_line")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ContractLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "contract_line_type")
    private ContractLineType contractLineType;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "weight")
    private Integer weight;

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

    @ManyToOne
    private Contract contract;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContractLineType getContractLineType() {
        return contractLineType;
    }

    public void setContractLineType(ContractLineType contractLineType) {
        this.contractLineType = contractLineType;
    }

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

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContractLine contractLine = (ContractLine) o;
        if(contractLine.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, contractLine.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ContractLine{" +
            "id=" + id +
            ", contractLineType='" + contractLineType + "'" +
            ", enabled='" + enabled + "'" +
            ", weight='" + weight + "'" +
            ", minimumEnabled='" + minimumEnabled + "'" +
            ", minimumValue='" + minimumValue + "'" +
            ", minimumWeight='" + minimumWeight + "'" +
            ", maximumEnabled='" + maximumEnabled + "'" +
            ", maximumValue='" + maximumValue + "'" +
            ", maximumWeight='" + maximumWeight + "'" +
            '}';
    }
}
