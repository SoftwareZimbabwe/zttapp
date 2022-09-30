package org.zt.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link org.zt.domain.LostItem} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LostItemDTO implements Serializable {

    private Long id;

    private LocalDate dateLost;

    private String description;

    private String province;

    private String itemNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateLost() {
        return dateLost;
    }

    public void setDateLost(LocalDate dateLost) {
        this.dateLost = dateLost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LostItemDTO)) {
            return false;
        }

        LostItemDTO lostItemDTO = (LostItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, lostItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LostItemDTO{" +
            "id=" + getId() +
            ", dateLost='" + getDateLost() + "'" +
            ", description='" + getDescription() + "'" +
            ", province='" + getProvince() + "'" +
            ", itemNumber='" + getItemNumber() + "'" +
            "}";
    }
}
