package org.acme.graphql;

import org.eclipse.microprofile.graphql.Input;
import org.eclipse.microprofile.graphql.Type;

@Type("NewPublisher")
@Input("NewPublisher")
public class NewPublisher {

    public String name;

    public Agency agency;

}
