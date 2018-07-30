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

package davidsar.gent.stubjars.components;

import davidsar.gent.stubjars.components.writer.Constants;

public enum SecurityModifier {
    PRIVATE("private"), PROTECTED("protected"), PACKAGE(Constants.EMPTY_STRING), PUBLIC("public");
    private final String modifier;

    SecurityModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getModifier() {
        return modifier;
    }

    public Expression expression() {
        return Expression.of(Expression.of(getModifier()), Expression.when(this != SecurityModifier.PACKAGE, Expression.StringExpression.SPACE));
    }
}