package org.acme.graphql;

import org.eclipse.microprofile.graphql.NonNull;

public class User {

    @NonNull
    public String id;

    @NonNull
    public String name;

    @NonNull
    public int age;

}
