package com.devops.usermanager.common;

import java.util.Map;

/** Format unique des erreurs renvoyées par l'API. */
public record ApiError(int status, String message, Map<String, String> details) {
}
