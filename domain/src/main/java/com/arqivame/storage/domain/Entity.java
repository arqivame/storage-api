package com.arqivame.storage.domain;

import java.util.Objects;

public abstract class Entity<I extends Identifier<?>> implements Validatable {

    private final I id;

    protected Entity(final I id) {
        this.id = Objects.requireNonNull(id, "'id' should not be null");
    }

    public I getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Entity<?> other = (Entity<?>) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
