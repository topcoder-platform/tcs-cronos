package com.topcoder.web.common.model;

import java.io.Serializable;

/**
 * @author  dok
 * @version  $Revision: 1.2 $ $Date: 2006/03/03 22:03:00 $
 * Create Date: Jan 27, 2005
 */
public class ImageInfo implements Serializable {
    private int width = -1;
    private int height = -1;
    private String src = null;
    private String link = null;

    public ImageInfo() {

    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
