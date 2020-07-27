package com.lxn.urlshortener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class UrlShortcut {
    @Id
    private String alias;
    
    @Column
    private String url;

    public UrlShortcut() {
    }

    public UrlShortcut(String alias, String url) {
        this.alias = alias;
        this.url = url;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "UrlShortcut{" + "alias=" + alias + ", url=" + url + '}';
    }
}
