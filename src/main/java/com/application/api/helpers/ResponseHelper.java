package com.application.api.helpers;

import com.application.api.dto.ErrorResponse;
import spark.Response;

public class ResponseHelper {
    public static ErrorResponse internalServerError(Response res) {
        return error(res, 500, "internal_server_error");
    }

    public static ErrorResponse badRequestError(Response res, String msg) {
        return error(res, 400, msg);
    }

    public static ErrorResponse error(Response res, Integer code, String msg) {
        res.status(200);
        return new ErrorResponse(code, msg);
    }
}
