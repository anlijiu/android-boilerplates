/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anlijiu.example.data.entity.mapper;

import android.content.ComponentName;

import com.anlijiu.example.data.entity.ComponentNameEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Mapper class used to transform {@link ComponentName} (in the android system) to {@link ComponentNameEntity} in the
 * data layer.
 */
@Singleton
public class ComponentNameEntitySysMapper {

    @Inject
    ComponentNameEntitySysMapper() {
    }

    /**
     * Transform a {@link ComponentName} into an {@link ComponentNameEntity}.
     *
     * @param componentName Object to be transformed.
     * @return {@link ComponentNameEntity} if valid {@link ComponentName} otherwise null.
     */
    public ComponentNameEntity transform(ComponentName componentName) {
        if (componentName != null) {
            return new ComponentNameEntity(componentName.getPackageName(), componentName.getClassName());
        } else {
            return null;
        }
    }

    /**
     * Transform a List of {@link ComponentName} into a Collection of {@link ComponentNameEntity}.
     *
     * @param componentNameCollection Object Collection to be transformed.
     * @return {@link ComponentNameEntity} if valid {@link ComponentName} otherwise null.
     */
    public List<ComponentNameEntity> transform(Collection<ComponentName> componentNameCollection) {
        final List<ComponentNameEntity> componentNameEntityList = new ArrayList<>(20);
        for (ComponentName componentName : componentNameCollection) {
            final ComponentNameEntity componentNameEntity = transform(componentName);
            if (componentNameEntity != null) {
                componentNameEntityList.add(componentNameEntity);
            }
        }
        return componentNameEntityList;
    }

}
