/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.weballgrade.data.serializer;

import com.google.gson.*;
import com.satanko.weballgrade.data.model.Lesson;
import com.satanko.weballgrade.data.model.Teacher;
import java.lang.reflect.Type;
import java.util.List;

/**
 * This class provides an implemenation of a {@link JsonSerializer} for a {@link Lesson} that
 * can be used by Google's {@link Gson}.
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * 
 * @version 1.0
 */
public class LessonAdapter implements JsonSerializer<Lesson>{

    Gson gson = new GsonBuilder().registerTypeAdapter(Teacher.class, new TeacherAdapter()).create();
    
    @Override
    public JsonElement serialize(Lesson src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("LessonClassToken", src.getClazz().getToken());
        jsonObject.addProperty("LessonSubjectToken", src.getSubject().getToken());
        JsonArray jsonTeacherArray = new JsonArray();
        List<Teacher> teachers = src.getTeachers();
        for (int i = 0; i < teachers.size(); i++) {
            jsonTeacherArray.add(gson.toJsonTree(teachers.get(i)));
        }
        jsonObject.add("LessonTeacherList", jsonTeacherArray);
        return jsonObject;
    }
    
}
