package mil.af.flagging.model;

import java.io.Serializable;
import java.util.*;
import javax.persistence.*;

@Entity
public class Intercept implements Serializable, Comparable<Intercept> {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long interceptId;
    @Column(unique = true)
    private String wranglerId;
    private String elnot;

    // timestamps
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeProcessed;
    @Temporal(TemporalType.TIMESTAMP)
    private Date intUpTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date intDownTime;
    
    // pulse fields
    private int burstCount;

    @ElementCollection
    @OrderColumn(name = "sequence")
    @Column(name="rf")
    private List<Double> rfs;
    
    private String modType;
    @ElementCollection
    @OrderColumn(name = "sequence")
    @Column(name="pri")
    private List<Double> pris;
    
    @ElementCollection
    @OrderColumn(name = "sequence")
    @Column(name="pd")
    private List<Double> pds;

    // scan fields
    private String scanType;
    private Double scanPeriod;

    // location fields
    @ManyToOne(targetEntity = Country.class,optional = false)
    @JoinColumn(name="COUNTRY_ID")
    private Country country;
    private Double latitude;
    private Double longitude;
    private Double semiMajor;
    private Double semiMinor;
    private Double orientation;
    private String readOutStation;

    private boolean processed;
    
    public Long getInterceptId() {
        return interceptId;
    }

    public void setInterceptId(Long interceptId) {
        this.interceptId = interceptId;
    }

    public String getWranglerId() {
        return wranglerId;
    }

    public void setWranglerId(String wranglerId) {
        this.wranglerId = wranglerId;
    }

    public String getElnot() {
        return elnot;
    }
    
    public void setElnot(String elnot) {
        this.elnot = elnot;
    }
    
    public Date getTimeProcessed() {
        return timeProcessed;
    }

    public void setTimeProcessed(Date timeProcessed) {
        this.timeProcessed = timeProcessed;
    }

    public Date getIntUpTime() {
        return intUpTime;
    }

    public void setIntUpTime(Date intUpTime) {
        this.intUpTime = intUpTime;
    }

    public Date getIntDownTime() {
        return intDownTime;
    }

    public void setIntDownTime(Date intDownTime) {
        this.intDownTime = intDownTime;
    }

    public String getModType() {
        return modType;
    }

    public void setModType(String modType) {
        this.modType = modType;
    }

    public List<Double> getRfs() {
        return rfs;
    }

    public void setRfs(List<Double> rfs) {
        this.rfs = rfs;
    }

    public List<Double> getPris() {
        return pris;
    }

    public void setPris(List<Double> pris) {
        this.pris = pris;
    }

    public List<Double> getPds() {
        return pds;
    }

    public void setPds(List<Double> pds) {
        this.pds = pds;
    }

    public String getScanType() {
        return scanType;
    }

    public void setScanType(String scanType) {
        this.scanType = scanType;
    }

    public Double getScanPeriod() {
        return scanPeriod;
    }

    public void setScanPeriod(Double scanPeriod) {
        this.scanPeriod = scanPeriod;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getSemiMajor() {
        return semiMajor;
    }

    public void setSemiMajor(Double semiMajor) {
        this.semiMajor = semiMajor;
    }

    public Double getSemiMinor() {
        return semiMinor;
    }

    public void setSemiMinor(Double semiMinor) {
        this.semiMinor = semiMinor;
    }

    public Double getOrientation() {
        return orientation;
    }

    public void setOrientation(Double orientation) {
        this.orientation = orientation;
    }

    public String getReadOutStation() {
        return readOutStation;
    }

    public void setReadOutStation(String readOutStation) {
        this.readOutStation = readOutStation;
    }

    public int getBurstCount() {
        return burstCount;
    }

    public void setBurstCount(int burstCount) {
        this.burstCount = burstCount;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.interceptId);
        hash = 71 * hash + Objects.hashCode(this.wranglerId);
        hash = 71 * hash + Objects.hashCode(this.elnot);
        hash = 71 * hash + Objects.hashCode(this.timeProcessed);
        hash = 71 * hash + Objects.hashCode(this.intUpTime);
        hash = 71 * hash + Objects.hashCode(this.intDownTime);
        hash = 71 * hash + Objects.hashCode(this.rfs);
        hash = 71 * hash + Objects.hashCode(this.modType);
        hash = 71 * hash + Objects.hashCode(this.pris);
        hash = 71 * hash + Objects.hashCode(this.pds);
        hash = 71 * hash + Objects.hashCode(this.scanType);
        hash = 71 * hash + Objects.hashCode(this.scanPeriod);
        hash = 71 * hash + Objects.hashCode(this.latitude);
        hash = 71 * hash + Objects.hashCode(this.longitude);
        hash = 71 * hash + Objects.hashCode(this.semiMajor);
        hash = 71 * hash + Objects.hashCode(this.semiMinor);
        hash = 71 * hash + Objects.hashCode(this.orientation);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Intercept other = (Intercept) obj;
        if (!Objects.equals(this.interceptId, other.interceptId)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int compareTo(Intercept o) {
        return Long.compare(this.interceptId, o.interceptId);
    }
    
    
}
