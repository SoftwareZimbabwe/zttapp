package org.zt.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LostItem.
 */
@Entity
@Table(name = "lost_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LostItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date_lost")
    private LocalDate dateLost;

    @Column(name = "description")
    private String description;

    @Column(name = "province")
    private String province;

    @Column(name = "item_number")
    private String itemNumber;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LostItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateLost() {
        return this.dateLost;
    }

    public LostItem dateLost(LocalDate dateLost) {
        this.setDateLost(dateLost);
        return this;
    }

    public void setDateLost(LocalDate dateLost) {
        this.dateLost = dateLost;
    }

    public String getDescription() {
        return this.description;
    }

    public LostItem description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProvince() {
        return this.province;
    }

    public LostItem province(String province) {
        this.setProvince(province);
        return this;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getItemNumber() {
        return this.itemNumber;
    }

    public LostItem itemNumber(String itemNumber) {
        this.setItemNumber(itemNumber);
        return this;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LostItem)) {
            return false;
        }
        return id != null && id.equals(((LostItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LostItem{" +
            "id=" + getId() +
            ", dateLost='" + getDateLost() + "'" +
            ", description='" + getDescription() + "'" +
            ", province='" + getProvince() + "'" +
            ", itemNumber='" + getItemNumber() + "'" +
            "}";
    }
}
