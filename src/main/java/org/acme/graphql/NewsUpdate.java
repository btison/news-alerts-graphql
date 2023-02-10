package org.acme.graphql;

import org.eclipse.microprofile.graphql.Input;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Type;

@Type("NewsUpdate")
@Input("NewsUpdate")
public class NewsUpdate {

    @NonNull
    public String headline;

    @NonNull
    public String brief;

    @NonNull
    public String content;

    @NonNull
    public String publisherId;

}
