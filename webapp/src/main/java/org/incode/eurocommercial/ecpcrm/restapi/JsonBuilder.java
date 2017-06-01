/**
 * Copyright 2015-2016 Eurocommercial Properties NV
 * <p>
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.incode.eurocommercial.ecpcrm.restapi;

import com.google.gson.JsonObject;

public class JsonBuilder {
    private final JsonObject json = new JsonObject();

    public String toJsonString() {
        return json.toString();
    }

    public JsonObject toJsonObject() {
        return json;
    }

    public JsonBuilder add(String key, String value) {
        json.addProperty(key, value);
        return this;
    }

    public JsonBuilder add(String key, Number value) {
        json.addProperty(key, value);
        return this;
    }

    public JsonBuilder add(String key, Boolean value) {
        json.addProperty(key, value);
        return this;
    }

    public JsonBuilder add(String key, JsonBuilder value) {
        json.add(key, value.toJsonObject());
        return this;
    }

    public JsonBuilder add(String key, JsonObject value) {
        json.add(key, value);
        return this;
    }
}