//  ************************************************************
//  *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
//  *                                                          *
//  *  See the file LICENCE.txt for copying permission.        *
//  ************************************************************ 

using ExcelAllGrade.model;
using Newtonsoft.Json;
using System.Collections.Generic;
using System.Linq;

namespace ExcelAllGrade.web.json
{
    /// <summary>
    /// This class is able to serialize and deserialize everything required for the communication with WebAllGrade.
    /// </summary>
    public static class JsonBuilder
    {
        /// <summary>
        /// Deserializes a <see cref="List{T}"/> of <see cref="ExcelAllGrade.model.JsonLesson"/>s.
        /// </summary>
        /// <param name="response">The <see cref="System.String"/> to deserialize.</param>
        /// <returns>A deserialized <see cref="List{T}"/> of <see cref="ExcelAllGrade.model.JsonLesson"/>s.</returns>
        public static List<JsonLesson> deserializeJsonLessonList(string response)
        {
            return JsonConvert.DeserializeObject<List<JsonLesson>>(response);
        }

        /// <summary>
        /// Deserializes a <see cref="List{T}"/> of <see cref="ExcelAllGrade.model.Class"/>es.
        /// </summary>
        /// <param name="response">The <see cref="System.String"/> to deserialize.</param>
        /// <returns>A deserialized <see cref="List{T}"/> of <see cref="ExcelAllGrade.model.Class"/>es.</returns>
        public static List<Class> deserializeClassList(string response)
        {
            return JsonConvert.DeserializeObject<List<Class>>(response);
        }

        /// <summary>
        /// Deserializes a <see cref="List{T}"/> of <see cref="ExcelAllGrade.model.Gradelog"/>s.
        /// </summary>
        /// <param name="response">The <see cref="System.String"/> to deserialize.</param>
        /// <returns>A deserialized <see cref="List{T}"/> of <see cref="ExcelAllGrade.model.Gradelog"/>s.</returns>
        public static List<Gradelog> deserializeGradelogList(string response)
        {
            return JsonConvert.DeserializeObject<List<Gradelog>>(response);
        }

        /// <summary>
        /// Deserializes a <see cref="List{T}"/> of <see cref="ExcelAllGrade.model.Subject"/>s. 
        /// </summary>
        /// <param name="response">The <see cref="System.String"/> to deserialize.</param>
        /// <returns>A deserialized <see cref="List{T}"/> of <see cref="ExcelAllGrade.model.Subject"/>s.</returns>
        public static List<Subject> deserializeSubjectList(string response)
        {
            return JsonConvert.DeserializeObject<List<Subject>>(response);
        }

        /// <summary>
        /// Deserializes a <see cref="List{T}"/> of <see cref="ExcelAllGrade.model.Student"/>s.
        /// </summary>
        /// <param name="response">The <see cref="System.String"/> to deserialize.</param>
        /// <returns>A deserialized <see cref="List{T}"/> of <see cref="ExcelAllGrade.model.Student"/>s.</returns>
        public static List<Student> deserializeStudentList(string response)
        {
            return JsonConvert.DeserializeObject<List<Student>>(response);
        }

        /// <summary>
        /// Serializes and merges two a <see cref="List{T}"/>s of <see cref="ExcelAllGrade.model.Class"/>es.
        /// </summary>
        /// <param name="oldLessonList">The <see cref="List{T}"/> of old <see cref="ExcelAllGrade.model.Class"/>es.</param>
        /// <param name="newLessonList">The <see cref="List{T}"/> of new <see cref="ExcelAllGrade.model.Class"/>es.</param>
        /// <returns>A serialized <see cref="System.String"/>.</returns>
        public static string serializeChangedLessons(List<Class> oldLessonList, List<Class> newLessonList)
        {
            List<JsonLesson> oldJsonLessons = new List<JsonLesson>();
            List<JsonLesson> newJsonLessons = new List<JsonLesson>();

            for (int i = 0; i < oldLessonList.Count; i++)
            {
                for (int k = 0; k < oldLessonList.ElementAt(i).Lessons.Count; k++)
                {
                    JsonLesson oldJsonLesson = new JsonLesson(oldLessonList.ElementAt(i).Name, oldLessonList.ElementAt(i).Lessons.ElementAt(k).Subject.Token);

                    for (int l = 0; l < oldLessonList.ElementAt(i).Lessons.ElementAt(k).Teachers.Count; l++)
                    {
                        oldJsonLesson.addTeacher(new Teacher(oldLessonList.ElementAt(i).Lessons.ElementAt(k).Teachers.ElementAt(l).TeacherToken));
                    }

                    oldJsonLessons.Add(oldJsonLesson);
                }
            }

            for (int i = 0; i < newLessonList.Count; i++)
            {
                for (int k = 0; k < newLessonList.ElementAt(i).Lessons.Count; k++)
                {
                    JsonLesson newJsonLesson = new JsonLesson(newLessonList.ElementAt(i).Name, newLessonList.ElementAt(i).Lessons.ElementAt(k).Subject.Token);

                    for (int l = 0; l < newLessonList.ElementAt(i).Lessons.ElementAt(k).Teachers.Count; l++)
                    {
                        newJsonLesson.addTeacher(new Teacher(newLessonList.ElementAt(i).Lessons.ElementAt(k).Teachers.ElementAt(l).TeacherToken));
                    }

                    newJsonLessons.Add(newJsonLesson);
                }
            }

            string oldLessons = JsonConvert.SerializeObject(oldJsonLessons);
            string newLessons = JsonConvert.SerializeObject(newJsonLessons);

            return "{\"OldLessons\":" + oldLessons + ",\"NewLessons\":" + newLessons + "}";
        }
    }
}
