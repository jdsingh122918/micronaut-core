/*
 * Copyright 2018 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.particleframework.docs.server.filters;

// tag::imports[]
import org.particleframework.http.*;
import org.particleframework.http.annotation.Filter;
import org.particleframework.http.filter.HttpServerFilter;
import org.particleframework.http.filter.ServerFilterChain;
import org.reactivestreams.Publisher;
// end::imports[]

/**
 * @author Graeme Rocher
 * @since 1.0
 */
// tag::class[]
@Filter("/hello/**") // <1>
public class TraceFilter implements HttpServerFilter { // <2>
    private final TraceService traceService;

    public TraceFilter(TraceService traceService) { // <3>
        this.traceService = traceService;
    }
// end::class[]

    // tag::doFilter[]
    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
        return traceService.trace(request) // <1>
                           .switchMap(aBoolean -> chain.proceed(request)) // <2>
                           .doAfterNext(res -> // <3>
                                res.getHeaders().add("X-Trace-Enabled", "true")
                           ) ;
    }
    // end::doFilter[]
}
