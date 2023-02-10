package org.acme.graphql;

import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.graphql.NonNull;

public class News {

    @NonNull
    public String id;

    @NonNull
    public String headline;

    @NonNull
    public String brief;

    @NonNull
    public String content;

    @NonNull
    public Publisher publisher;

    public JsonObject toJson() {
        return JsonObject.mapFrom(this);
    }

}
