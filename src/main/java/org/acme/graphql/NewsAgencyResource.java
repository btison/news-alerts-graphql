package org.acme.graphql;

import io.smallrye.graphql.api.Subscription;
import io.smallrye.mutiny.Multi;
import org.eclipse.microprofile.graphql.*;

import javax.inject.Inject;
import java.util.List;

@GraphQLApi
public class NewsAgencyResource {

    @Inject
    NewsAgencyService newsAgencyService;

    @Query("users")
    @Description("Get all users")
    @NonNull
    public List<User> getAllUsers() {
        return newsAgencyService.getAllUsers();
    }

    @Query("publishers")
    @Description("Get all publishers")
    @NonNull
    public List<Publisher> getAllPublishers() {
        return newsAgencyService.getAllPublishers();
    }

    @Mutation
    public User registerUser(@NonNull NewUser newUser) {
        return newsAgencyService.addUser(newUser);
    }

    @Mutation
    public Publisher registerPublisher(@NonNull NewPublisher newPublisher) {
        return newsAgencyService.addPublisher(newPublisher);
    }

    @Mutation("publish")
    public News publish(@NonNull NewsUpdate update) {
        return newsAgencyService.publishNewsUpdate(update);
    }

    @Subscription("news")
    public Multi<News> subscribe(String userId, Agency agency) {
        return newsAgencyService.subscribe(userId, agency);
    }

}
