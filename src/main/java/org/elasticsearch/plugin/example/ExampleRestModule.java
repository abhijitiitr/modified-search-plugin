package org.elasticsearch.plugin.example;

import org.elasticsearch.common.inject.AbstractModule;

public class ExampleRestModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(HelloRestHandler.class).asEagerSingleton();
        // bind(ModifiedInternalSearchHit.class).asEagerSingleton();
        // bind(ModifiedInternalSearchHits.class).asEagerSingleton();
        // bind(ModifiedInternalSearchResponse.class).asEagerSingleton();
        bind(ModifiedSearchResponse.class).asEagerSingleton();
        bind(RestModifiedSearchAction.class).asEagerSingleton();
    }
}