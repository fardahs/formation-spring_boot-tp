package com.training.springcore.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "authorities")
public class Authority {
    @EmbeddedId
    private AuthorityId authorityId;
// Vous devez générer getters et setters, hashcode, equals


    public AuthorityId getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(AuthorityId authorityId) {
        this.authorityId = authorityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Authority authority = (Authority) o;
        return authorityId.equals(authority.authorityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorityId);
    }

    @Override
    public String toString() {
        return "Authority{" +
                "authorityId=" + authorityId +
                '}';
    }
}
