package com.example.demo.controllers.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;

public class URL {

	public static String decodeParam(String text) throws UnsupportedEncodingException {
		return URLDecoder.decode(text, StandardCharsets.UTF_8);
	}

	public static Instant convertDate(String textDate, Instant defaultValue) throws ParseException {
		if (textDate.isEmpty())
			return defaultValue;

		return Instant.parse(textDate);
	}
}
