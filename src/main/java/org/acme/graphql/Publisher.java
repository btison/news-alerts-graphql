package org.acme.graphql;

import org.eclipse.microprofile.graphql.NonNull;

public class Publisher {

    @NonNull
    public String id;

    @NonNull
    public String name;

    @NonNull
    public Agency agency;

}
