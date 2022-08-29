package com.sys.vas.dto;

import javax.xml.bind.annotation.XmlElement;

public class Result {

    @XmlElement(name = "resCode")
    private String resCode;
    @XmlElement(name = "resDesc")
    private String resDesc;

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResDesc() {
        return resDesc;
    }

    public void setResDesc(String resDesc) {
        this.resDesc = resDesc;
    }

}
