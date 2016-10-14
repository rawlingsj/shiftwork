package com.teammachine.staffrostering.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Style {

    @Column(name = "background_color")
    private String backgroundColor;
    @Column(name = "text_color")
    private String textColor;
    @Column(name = "icon")
    private String icon;

    // for hibernate
    public Style() {
    }

    public Style(String backgroundColor, String textColor, String icon) {
        this();
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.icon = icon;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
