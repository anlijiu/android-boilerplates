/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anlijiu.example.data.entity.mapper;

import com.anlijiu.example.data.entity.CardEntity;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import javax.inject.Inject;

/**
 * Class used to transform from Strings representing json to valid objects.
 */
public class CardEntityJsonMapper {

  private final Gson gson;

  @Inject
  public CardEntityJsonMapper() {
    this.gson = new Gson();
  }

  /**
   * Transform from valid json string to {@link CardEntity}.
   *
   * @param userJsonResponse A json representing a user profile.
   * @return {@link CardEntity}.
   * @throws com.google.gson.JsonSyntaxException if the json string is not a valid json structure.
   */
  public CardEntity transformCardEntity(String userJsonResponse) throws JsonSyntaxException {
    final Type userEntityType = new TypeToken<CardEntity>() {}.getType();
    return this.gson.fromJson(userJsonResponse, userEntityType);
  }

  /**
   * Transform from valid json string to List of {@link CardEntity}.
   *
   * @param userListJsonResponse A json representing a collection of users.
   * @return List of {@link CardEntity}.
   * @throws com.google.gson.JsonSyntaxException if the json string is not a valid json structure.
   */
  public List<CardEntity> transformCardEntityCollection(String userListJsonResponse)
      throws JsonSyntaxException {
    final Type listOfCardEntityType = new TypeToken<List<CardEntity>>() {}.getType();
    return this.gson.fromJson(userListJsonResponse, listOfCardEntityType);
  }
}
