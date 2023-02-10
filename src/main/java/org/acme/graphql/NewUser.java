package org.acme.graphql;

import org.eclipse.microprofile.graphql.Input;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Type;

@Type("NewUser")
@Input("NewUser")
public class NewUser {

    @NonNull
    public String name;

    @NonNull
    public int age;

}
