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

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Set;

public class JarMethod extends JarModifers {
    private final Method method;

    public JarMethod(Method method) {
        this.method = method;
    }

    @Override
    protected int getModifiers() {
        return method.getModifiers();
    }

    public String name() {
        return method.getName();
    }

    public Class<?> returnType() {
        return method.getReturnType();
    }

    public String[] parameters() {
        Parameter[] parameters = method.getParameters();
        return Arrays.stream(parameters)
                .map(parameter -> JarType.toString(parameter.getParameterizedType()) + " " + parameter.getName())
                .toArray(String[]::new);
    }

    public boolean isSynthetic() {
        return method.isSynthetic();
    }

    public boolean shouldIncludeStaticMethod() {
        if (!isStatic()) return shouldIncludeMethod();
        JarClass<?> klazz = JarClass.forClass(method.getDeclaringClass());
        if (klazz.isEnum()) {
            String name = method.getName();
            if (name.equals("values") || name.equals("valueOf")) {
                return false;
            }
        }

        Set<JarClass> jarClasses = klazz.allSuperClassesAndInterfaces();
        long count = jarClasses.stream()
                .filter(x -> x.hasMethod(method))
                .count();
        return count == 0 && shouldIncludeMethod();
    }

    private boolean shouldIncludeMethod() {
        for (Class<?> paramType : method.getParameterTypes()) {
            if (!JarClass.hasSafeName(paramType)) return false;
        }

        return JarClass.hasSafeName(method.getReturnType());
    }

    public Type genericReturnType() {
        return method.getGenericReturnType();
    }

    public TypeVariable<Method>[] typeParameters() {
        return method.getTypeParameters();
    }

    public Class<?>[] parameterTypes() {
        return method.getParameterTypes();
    }
}
