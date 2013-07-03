/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/ 
package com.satanko.weballgrade.data.serializer;

import com.google.gson.*;
import com.satanko.weballgrade.data.model.Teacher;
import java.lang.reflect.Type;

/**
 * This class provides an implemenation of a {@link JsonSerializer} for a {@link Teacher} that
 * can be used by Google's {@link Gson}.
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * 
 * @version 1.0
 */
public class TeacherAdapter implements JsonSerializer<Teacher> {

    @Override
    public JsonElement serialize(Teacher src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("TeacherToken", src.getToken());
        return jsonObject;
    }
}