/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.weballgrade.data.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.satanko.weballgrade.data.model.Subject;
import java.lang.reflect.Type;

/**
 * This class provides an implemenation of a {@link JsonSerializer} for the {@link Subject} that
 * can be used by Google's {@link Gson}.
 * 
 * @author Julian Tropper (julian.tropper@gmail.com)
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * 
 * @version 1.0
 */
public class SubjectAdapter implements JsonSerializer<Subject> {
    
    @Override
    public JsonElement serialize(Subject src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("SubjectToken", src.getToken());
        jsonObject.addProperty("SubjectName", src.getName());
        return jsonObject;
    }
    
}
