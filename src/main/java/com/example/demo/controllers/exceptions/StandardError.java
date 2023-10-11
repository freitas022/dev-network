package com.example.demo.controllers.exceptions;

import java.time.Instant;

public record StandardError(Instant timestamp, Integer status, String error, String path) {
}
