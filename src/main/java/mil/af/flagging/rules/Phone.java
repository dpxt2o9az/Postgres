/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.rules;

/**
 *
 * @author Brad
 */
public class Phone {
    private String model;
    private OSType osType;

    Phone(OSType osType) {
        this.osType = osType;
    }
    
    public enum OSType {
        ANDROID, IOS, WINDOWS;
    }

    /**
     * @return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * @return the osType
     */
    public OSType getOsType() {
        return osType;
    }

    /**
     * @param osType the osType to set
     */
    public void setOsType(OSType osType) {
        this.osType = osType;
    }

    @Override
    public String toString() {
        return "Phone{" + "model=" + model + ", osType=" + osType + '}';
    }
    
    
}
