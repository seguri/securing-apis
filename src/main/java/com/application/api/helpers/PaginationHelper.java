package com.application.api.helpers;

import spark.Request;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public class PaginationHelper {

    private static Integer defaultPageSize = 10;
    private static Integer MaxPageSize = 100;

    public static long getOffset(Request req) {
        String offset = req.queryParamOrDefault("offset", "0");
        return Long.parseLong(offset);
    }

    public static Integer getPageLimit(Request req) {
        var limitParam = req.queryMap().get("limit").integerValue();

        Integer limit = Optional.ofNullable(limitParam).orElse(defaultPageSize);
        if (limit > MaxPageSize) {
            return MaxPageSize;
        }
        return limit;
    }

    public static String buildNextPageLink(Request req, String lastID) throws MalformedURLException {
        if (lastID.isBlank()) {
            return ""; // no more pages
        }

        var reqURL = new URL(req.url());
        var nextReq = new StringBuilder();
        nextReq.append(reqURL);

        String reqLimit = req.queryMap("limit").value();
        if (reqLimit == null || reqLimit.isBlank()) {
            nextReq.append(String.format("?limit=%d", defaultPageSize));
        } else {
            nextReq.append(String.format("?limit=%s", reqLimit));
        }


        nextReq.append(String.format("&offset=%s", lastID));
        return nextReq.toString();
    }
}
