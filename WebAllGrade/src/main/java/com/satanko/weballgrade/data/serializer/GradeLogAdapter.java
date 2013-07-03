/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.weballgrade.data.serializer;

import com.google.gson.*;
import com.satanko.weballgrade.data.model.GradeLog;
import com.satanko.weballgrade.data.model.Student;
import java.lang.reflect.Type;

/**
 * This class provides an implemenation of a {@link JsonSerializer} for a {@link GradeLog} that
 * can be used by Google's {@link Gson}.
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * 
 * @version 1.0
 */
public class GradeLogAdapter implements JsonSerializer<GradeLog>{

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LessonAdapter.class, new LessonAdapter())
            .registerTypeAdapter(Student.class, new StudentAdapter())
            .create();
    
    @Override
    public JsonElement serialize(GradeLog src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("GradelogTimestamp", src.getTimestamp().toString());
        jsonObject.addProperty("GradelogComment", src.getComment());
        jsonObject.addProperty("GradelogGrading", src.getGrading());
        jsonObject.addProperty("GradelogType", src.getType());
        jsonObject.addProperty("GradelogStudent", src.getStudent().getToken());
        return jsonObject;
    }
    
    
}
