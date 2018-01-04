/*
 *  Copyright 2018 David Sargent
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *  the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *  an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations under the License.
 */

package me.davidsargent.stubjars.components;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public class JarConstructor<T> extends JarModifers {
    private final Constructor<T> constructor;

    JarConstructor(@NotNull Constructor<T> constructor) {
        this.constructor = constructor;
    }

    @Override
    protected int getModifiers() {
        return constructor.getModifiers();
    }

    boolean shouldIncludeCotr() {
        for (Class<?> paramType : constructor.getParameterTypes()) {
            if (!JarClass.hasSafeName(paramType)) return false;
        }

        return true;
    }

    @NotNull
    public String[] parameters() {
        Parameter[] parameters = constructor.getParameters();
        return Arrays.stream(parameters)
                .map(parameter -> JarType.toString(parameter.getParameterizedType()) + " " + parameter.getName())
                .toArray(String[]::new);
    }

    @NotNull
    public String name() {
        return constructor.getDeclaringClass().getSimpleName();
    }

    public static boolean hasDefaultConstructor(@NotNull JarClass<?> klazz) {
        return hasDefaultConstructor(klazz.getKlazz());
    }

    public boolean isDefaultConstructor() {
        return isDefaultConstructor(constructor);
    }

    private static boolean isDefaultConstructor(@NotNull Constructor<?> constructor) {
        Class<?> klazz = constructor.getDeclaringClass();
        if (klazz.getDeclaringClass() == null || Modifier.isStatic(klazz.getModifiers()))
            return constructor.getParameterCount() == 0;
        else
            return constructor.getParameterCount() == 1 && constructor.getGenericParameterTypes()[0].equals(klazz.getDeclaringClass());
    }

    public static boolean hasDefaultConstructor(@NotNull Class<?> klazz) {
        try {
            Class<?> declaringClass = klazz.getDeclaringClass();
            Constructor<?> declaredConstructor;
            if (declaringClass == null || Modifier.isStatic(klazz.getModifiers()))
                declaredConstructor = klazz.getDeclaredConstructor();
            else {
                declaredConstructor = klazz.getDeclaredConstructor(declaringClass);
            }

            return !Modifier.isPrivate(declaredConstructor.getModifiers());
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    public Constructor<T> getConstructor() {
        return constructor;
    }
}
