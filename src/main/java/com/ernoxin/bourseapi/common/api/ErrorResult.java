package com.ernoxin.bourseapi.common.api;

import java.util.List;

public record ErrorResult(
        List<String> details
) {
}
