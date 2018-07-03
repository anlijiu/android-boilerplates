package com.anlijiu.example.data.misc;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.net.InetSocketAddress;
import java.net.Proxy;

import com.f2prateek.rx.preferences2.Preference;


import static java.net.Proxy.Type.HTTP;

public class InetSocketAddressPreferenceAdapter implements Preference.Converter<InetSocketAddress> {
    public static final InetSocketAddressPreferenceAdapter INSTANCE =
            new InetSocketAddressPreferenceAdapter();

    InetSocketAddressPreferenceAdapter() {
    }

    public static @Nullable
    InetSocketAddress parse(@Nullable String value) {
        if (TextUtils.isEmpty(value)) {
            return null;
        }
        String[] parts = value.split(":", 2);
        if (parts.length == 0) {
            return null;
        }
        String host = parts[0];
        int port = parts.length > 1 ? Integer.parseInt(parts[1]) : 80;
        return InetSocketAddress.createUnresolved(host, port);
    }

    public static @Nullable
    Proxy createProxy(@Nullable InetSocketAddress address) {
        if (address == null) {
            return null;
        }
        return new Proxy(HTTP, address);
    }

    @NonNull
    @Override
    public InetSocketAddress deserialize(@NonNull String serialized) {
        return parse(serialized);
    }

    @NonNull
    @Override
    public String serialize(@NonNull InetSocketAddress value) {
        String host = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            host = value.getHostString();
        } else {
            host = value.getHostName();
        }
        int port = value.getPort();
        return host + ":" + port;
    }
}
