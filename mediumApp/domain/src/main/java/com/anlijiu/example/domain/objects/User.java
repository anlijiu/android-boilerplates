package com.anlijiu.example.domain.objects;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class User {
    public abstract int id();

    public abstract String login();

    @SerializedName("avatar_url")
    public abstract String avatarUrl();

    public static TypeAdapter<User> typeAdapter(Gson gson) {
        return new AutoValue_User.GsonTypeAdapter(gson);
    }
}

