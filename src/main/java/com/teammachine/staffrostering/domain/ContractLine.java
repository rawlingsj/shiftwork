package com.teammachine.staffrostering.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.teammachine.staffrostering.domain.enumeration.ContractLineType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ContractLine.
 */
@Entity
@Table(name = "contract_line")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@DiscriminatorColumn(name="boolean_or_minmax")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = BooleanContractLine.class, name = "boolean"),
        @JsonSubTypes.Type(value = MinMaxContractLine.class, name = "minmax")
})
public abstract class ContractLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "contract_line_type")
    private ContractLineType contractLineType;

    @ManyToOne
    @JsonIgnore
    private Contract contract;

    abstract Boolean isEnabled();

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

    @JsonProperty("contract")
    public EntityRefInfo getContract() {
        return contract != null ? new EntityRefInfo(contract.getId(), contract.getCode()) : null;
    }

    @JsonProperty("contract")
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

}
