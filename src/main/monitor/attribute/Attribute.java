package main.monitor.attribute;

import main.monitor.attribute.exception.AttrWrongDefinitionException;

import java.io.Serializable;

/**
 * Abstract class for catalog attribute.
 *
 * @author Evgenii Morenkov
 */
public abstract class Attribute implements Serializable {
    // javaFX SimpleStringProperty and SimpleLongProperty are not serializable.
    protected String attrName;
    protected String attrType;
    protected  String attrConfig;
    // pol period in seconds.
    protected Long pollPeriod;

    /**
     * Gets pollPeriod.
     *
     * @return Value of pollPeriod.
     */
    public Long getPollPeriod() {
        return pollPeriod;
    }

    /**
     * Sets new attrNameColumn.
     *
     * @param attrName New value of attrNameColumn.
     */
    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    /**
     * Gets attrNameColumn.
     *
     * @return Value of attrNameColumn.
     */
    public String getAttrName() {
        return attrName;
    }

    /**
     * Sets new attrConfig.
     *
     * @param attrConfig New value of attrConfig.
     */
    public void setAttrConfig(String attrConfig) {
        this.attrConfig = attrConfig;
        setConfValue(attrConfig);
    }

    /**
     * Setter for configuration value.
     *
     * @param value- string representation of configuration.
     */
    protected abstract void setConfValue(String value);

    /**
     * Sets new pollPeriod.
     *
     * @param pollPeriod New value of pollPeriod.
     */
    public void setPollPeriod(Long pollPeriod) {
        this.pollPeriod = pollPeriod;
    }

    /**
     * Gets attrTypeColumn.
     *
     * @return Value of attrTypeColumn.
     */
    public String getAttrType() {
        return attrType;
    }

    /**
     * Sets new attrTypeColumn.
     *
     * @param attrType New value of attrTypeColumn.
     */
    public void setAttrType(String attrType) {
        this.attrType = attrType;
    }

    /**
     * Gets attrConfig.
     *
     * @return Value of attrConfig.
     */
    public String getAttrConfig() {
        return attrConfig;
    }

    public abstract Attribute getAttrFromString(String value) throws AttrWrongDefinitionException;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Attribute attribute = (Attribute) o;

        if (attrName != null ? !attrName.equals(attribute.attrName) : attribute.attrName != null) return false;
        if (attrType != null ? !attrType.equals(attribute.attrType) : attribute.attrType != null) return false;
        if (!pollPeriod.equals(attribute.pollPeriod)) return false;
        if (getAttrConfig() != null ? !getAttrConfig().equals(attribute.getAttrConfig()) : attribute.getAttrConfig() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = attrName != null ? attrName.hashCode() : 0;
        result = 31 * result + (attrType != null ? attrType.hashCode() : 0);
        result = 31 * result + pollPeriod.intValue();
        result = 31 * result + (getAttrConfig() != null ? getAttrConfig().hashCode() : 0);
        return result;
    }

    /**
     * Marshal attribute into string.
     *
     * @return string
     */
    public String getStringFromAttr() {
        StringBuilder sb = new StringBuilder(getAttrType());
        sb.append(" ").append(getAttrName()).append(" ").append(getAttrConfig()).append(" ").append(getPollPeriod());
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "attrName='" + attrName + '\'' +
                ", attrType='" + attrType + '\'' +
                ", attrConf=" + getAttrConfig() +
                ", pollPeriod=" + pollPeriod +
                '}';
    }
}
