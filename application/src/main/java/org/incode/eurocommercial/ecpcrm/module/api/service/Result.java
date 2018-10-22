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
package org.incode.eurocommercial.ecpcrm.module.api.service;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Result {

    public static final int STATUS_OK = 200;
    public static final int STATUS_INVALID_DEVICE = 301;


    public static final int STATUS_INVALID_PARAMETER = 302;

    public static final int STATUS_INVALID_CARD = 303;
    public static final int STATUS_INVALID_USER = 304;
    public static final int STATUS_EMAIL_ALREADY_EXISTS = 305;
    public static final int STATUS_EMAIL_ALREADY_EXISTS_INVALID_CHECK = 306;
    public static final int STATUS_DUPLICATE_REQUEST_FOR_REPLACEMENT_LOST_CARD = 307;
    public static final int STATUS_INVALID_CARD_NUMBER = 312;
    public static final int STATUS_UNABLE_TO_BIND_USER = 314;
    public static final int STATUS_CARD_ALREADY_PLAYED = 315;
    public static final int STATUS_CARD_CENTER_NOT_EQUAL_TO_DEVICE_CENTER = 317;
    public static final int STATUS_EMAIL_ALREADY_EXISTS_VALID_CHECK_ASK_IF_LOST = 318;
    public static final int STATUS_OUTDATED_CARD = 319;
    public static final int STATUS_INCORRECT_CHECK_CODE = 402;

    @Getter
    private final int status;

    @Getter
    private final String message;

    @Getter
    private final Object response;

    public static Result error(final int status, final String message) {
        return new Result(status, message, null);
    }
    public static Result ok(String  message, Object viewModel) {
        return new Result(STATUS_OK, message, viewModel);
    }
    public static Result ok(Object viewModel) {
        return ok(null, viewModel);
    }
    public static Result ok() {
        return ok(null);
    }

    public Response asResponse() {
        return Response
                .ok()
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(new Gson().toJson(this))
                .build();
    }
}
