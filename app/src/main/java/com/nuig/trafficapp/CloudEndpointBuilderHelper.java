/*
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nuig.trafficapp;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.nuig.trafficapp.activities.LoginActivity;
import com.nuig.trafficappbackend.trafficApp.TrafficApp;
import java.io.IOException;

/**
 * Allows configuring Cloud Endpoint builders to support authenticated calls, as
 * well as calls to
 * CLoud Endpoints exposed from an App Engine backend that run locally during
 * development.
 */
public final class CloudEndpointBuilderHelper {

    /**
     * Default constructor, never called.
     */
    private CloudEndpointBuilderHelper() {
    }

    /**
     * *
     *
     * @return TrafficApp endpoints to the GAE backend.
     */
    public static TrafficApp getEndpoints() {

        // Create API handler
        TrafficApp.Builder builder = new TrafficApp.Builder(
                AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), getRequestInitializer())
                .setRootUrl(Constants.ROOT_URL)
                .setApplicationName("Roadviser")
                .setGoogleClientRequestInitializer(
                        new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(
                                    final AbstractGoogleClientRequest<?>
                                            abstractGoogleClientRequest)
                                    throws IOException {
                                abstractGoogleClientRequest
                                        .setDisableGZipContent(true);
                            }
                        }
                );

        return builder.build();
    }

    /**
     * Returns appropriate HttpRequestInitializer depending whether the
     * application is configured to require users to be signed in or not.
     * @return an appropriate HttpRequestInitializer.
     */
    static HttpRequestInitializer getRequestInitializer() {
        if (Constants.SIGN_IN_REQUIRED) {
            return LoginActivity.getCredential();
            //return LoginActivity.getToken();
        } else {
            return new HttpRequestInitializer() {
                @Override
                public void initialize(final HttpRequest arg0) {
                }
            };
        }
    }
}
