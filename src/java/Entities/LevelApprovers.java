/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author kabugi
 */
@Entity
public class LevelApprovers implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long levelID;
    private int approversCount;
    private String levelAlias;

    public String getLevelAlias() {
        return levelAlias;
    }

    public void setLevelAlias(String levelAlias) {
        this.levelAlias = levelAlias;
    }
    
    

    public Long getLevelID() {
        return levelID;
    }

    public void setLevelID(Long levelID) {
        this.levelID = levelID;
    }

    public int getApproversCount() {
        return approversCount;
    }

    public void setApproversCount(int approversCount) {
        this.approversCount = approversCount;
    }
    
    
    
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LevelApprovers)) {
            return false;
        }
        LevelApprovers other = (LevelApprovers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.LevelApprovers[ id=" + id + " ]";
    }
    
}
