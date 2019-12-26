package eti.query.demonstration.location.entities;

import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LOCATIONS", schema = "HR", catalog = "")
public class LocationEntity {

    private long locationId;
    private String streetAddress;
    private String postalCode;
    private String city;
    private String stateProvince;
    private String countryId;

    @Id
    @Column(name = "LOCATION_ID")
    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    @Basic
    @Column(name = "STREET_ADDRESS")
    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    @Basic
    @Column(name = "POSTAL_CODE")
    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Basic
    @Column(name = "CITY")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Basic
    @Column(name = "STATE_PROVINCE")
    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    @Basic
    @Column(name = "COUNTRY_ID")
    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationId, streetAddress, postalCode, city, stateProvince, countryId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LocationEntity that = (LocationEntity) o;
        return locationId == that.locationId && Objects.equals(streetAddress, that.streetAddress) &&
                Objects.equals(postalCode, that.postalCode) && Objects.equals(city, that.city) &&
                Objects.equals(stateProvince, that.stateProvince) && Objects.equals(countryId, that.countryId);
    }
}
