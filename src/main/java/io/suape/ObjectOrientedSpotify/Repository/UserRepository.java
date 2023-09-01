package io.suape.ObjectOrientedSpotify.Repository;

import io.suape.ObjectOrientedSpotify.Domain.User;

import java.util.Collection;

public interface UserRepository <T extends User>{
    /* Basic Crud Operations */
    T create (T data);
    Collection<T> list(int page, int pageSize);
    T get(String id);
    T update(T data);
    Boolean delete(String id);
    /* Basic Crud Operations */

    /* Additional Operations */
}
