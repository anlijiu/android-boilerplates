package com.anlijiu.example.ui;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * A "view server" adaptation which automatically hooks itself up to all activities.
 */
public interface ActivityLifecyclesServer extends Application.ActivityLifecycleCallbacks {
    class Proxy implements ActivityLifecyclesServer {
        private List<ActivityLifecyclesServer> servers = new ArrayList<>();

        public void addServer(ActivityLifecyclesServer server) {
            servers.add(server);
        }

        public void removeServer(ActivityLifecyclesServer server) {
            servers.remove(server);
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            for (ActivityLifecyclesServer server : servers) {
                server.onActivityCreated(activity, savedInstanceState);
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {
            for (ActivityLifecyclesServer server : servers) {
                server.onActivityStarted(activity);
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {
            for (ActivityLifecyclesServer server : servers) {
                server.onActivityResumed(activity);
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
            for (ActivityLifecyclesServer server : servers) {
                server.onActivityPaused(activity);
            }
        }

        @Override
        public void onActivityStopped(Activity activity) {
            for (ActivityLifecyclesServer server : servers) {
                server.onActivityStopped(activity);
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            for (ActivityLifecyclesServer server : servers) {
                server.onActivitySaveInstanceState(activity, outState);
            }
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            for (ActivityLifecyclesServer server : servers) {
                server.onActivityDestroyed(activity);
            }
        }
    }


    class Empty implements ActivityLifecyclesServer {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }
}
