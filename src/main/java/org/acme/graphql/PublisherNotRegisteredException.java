package org.acme.graphql;

public class PublisherNotRegisteredException extends RuntimeException {

    public PublisherNotRegisteredException(String message) {
        super(message);
    }
}
