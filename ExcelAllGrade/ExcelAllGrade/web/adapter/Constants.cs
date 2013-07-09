//  ************************************************************
//  *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
//  *                                                          *
//  *  See the file LICENCE.txt for copying permission.        *
//  ************************************************************ 

namespace ExcelAllGrade.web.adapter
{
    /// <summary>
    /// This class stores all constants required for the communication with WebAllGrade.
    /// </summary>
    public static class Constants
    {
        /// <summary>
        /// Gets the location of WebAllGrade.
        /// </summary>
        public const string SERVER_HOST = "http://localhost";
        /// <summary>
        /// Gets the port of WebAllGrade.
        /// </summary>
        public const string PORT = "8080";

        /// <summary>
        /// Gets the path of the subjects.
        /// </summary>
        public const string PATH_GET_SUBJECTS = "/weballgrade/database/subjects.json";
        /// <summary>
        /// Gets the path of the lessons.
        /// </summary>
        public const string PATH_GET_LESSONS = "/weballgrade/database/lessons.json";
        /// <summary>
        /// Gets the path of the classes.
        /// </summary>
        public const string PATH_GET_CLASSES = "/weballgrade/database/classes.json";
        /// <summary>
        /// Gets the path of the students.
        /// </summary>
        public const string PATH_GET_STUDENTS = "/weballgrade/database/students.json";
        /// <summary>
        /// Gets the path of the gradelogs.
        /// </summary>
        public const string PATH_GET_GRADELOGS = "/weballgrade/database/gradelogs.json";

        /// <summary>
        /// The parameter for classes.
        /// </summary>
        public const string PARAM_CLASS = "class";
        /// <summary>
        /// The parameter for teachers.
        /// </summary>
        public const string PARAM_TEACHER = "teacher";
        /// <summary>
        /// The parameter for subjects.
        /// </summary>
        public const string PARAM_SUBJECT = "subject";
    }
}
