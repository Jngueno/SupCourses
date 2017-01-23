/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jngue
 */
@Entity
@Table(name = "response_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ResponseType.findAll", query = "SELECT r FROM ResponseType r"),
    @NamedQuery(name = "ResponseType.findByResponseTypeId", query = "SELECT r FROM ResponseType r WHERE r.responseTypeId = :responseTypeId"),
    @NamedQuery(name = "ResponseType.findByResponseTypeContent", query = "SELECT r FROM ResponseType r WHERE r.responseTypeContent = :responseTypeContent")})
public class ResponseType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 36)
    @Column(name = "response_type_id")
    private String responseTypeId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "response_type_content")
    private String responseTypeContent;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "responseTypeId")
    private Collection<Response> responseCollection;

    public ResponseType() {
    }

    public ResponseType(String responseTypeId) {
        this.responseTypeId = responseTypeId;
    }

    public ResponseType(String responseTypeId, String responseTypeContent) {
        this.responseTypeId = responseTypeId;
        this.responseTypeContent = responseTypeContent;
    }

    public String getResponseTypeId() {
        return responseTypeId;
    }

    public void setResponseTypeId(String responseTypeId) {
        this.responseTypeId = responseTypeId;
    }

    public String getResponseTypeContent() {
        return responseTypeContent;
    }

    public void setResponseTypeContent(String responseTypeContent) {
        this.responseTypeContent = responseTypeContent;
    }

    @XmlTransient
    public Collection<Response> getResponseCollection() {
        return responseCollection;
    }

    public void setResponseCollection(Collection<Response> responseCollection) {
        this.responseCollection = responseCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (responseTypeId != null ? responseTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ResponseType)) {
            return false;
        }
        ResponseType other = (ResponseType) object;
        if ((this.responseTypeId == null && other.responseTypeId != null) || (this.responseTypeId != null && !this.responseTypeId.equals(other.responseTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.supcourses.entity.ResponseType[ responseTypeId=" + responseTypeId + " ]";
    }
    
}
