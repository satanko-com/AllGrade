//  ************************************************************
//  *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
//  *                                                          *
//  *  See the file LICENCE.txt for copying permission.        *
//  ************************************************************ 

using Newtonsoft.Json;
using System.Collections.Generic;

namespace ExcelAllGrade.model
{
    /// <summary>
    /// This class represents a <c>JsonLesson</c>.
    /// </summary>
    public class JsonLesson
    {
        /// <summary>
        /// Gets or sets the <c>SubjectToken</c> of the <see cref="JsonLesson"/>.
        /// </summary>
        public string SubjectToken { get; set; }
        /// <summary>
        /// Gets or sets the <c>ClassToken</c> of the <see cref="JsonLesson"/>.
        /// </summary>
        public string ClassToken { get; set; }
        /// <summary>
        /// Gets or sets the <c>Teachers</c> of the <see cref="JsonLesson"/>.
        /// </summary>
        public List<Teacher> Teachers { get; set; }

        /// <summary>
        /// Initializes a new instance of <see cref="JsonLesson"/>.
        /// </summary>
        /// <param name="LessonClassToken">The <see cref="ClassToken"/> of the <see cref="JsonLesson"/>.</param>
        /// <param name="LessonSubjectToken">The <see cref="SubjectToken"/> of the <see cref="JsonLesson"/>.</param>
        /// <param name="LessonTeacherList">The <see cref="Teachers"/> of the <see cref="JsonLesson"/>.</param>
        [JsonConstructor]
        public JsonLesson(string LessonClassToken, string LessonSubjectToken, List<Teacher> LessonTeacherList)
        {
            this.SubjectToken = LessonSubjectToken;
            this.ClassToken = LessonClassToken;
            this.Teachers = LessonTeacherList;
        }

        /// <summary>
        /// Initializes a new instance of <see cref="JsonLesson"/>. <see cref="Teachers"/> will be initialized as an empty <see cref="List{T}"/>.
        /// </summary>
        /// <param name="LessonClassToken">The <see cref="ClassToken"/> of the <see cref="JsonLesson"/></param>
        /// <param name="LessonSubjectToken">The <see cref="SubjectToken"/> of the <see cref="JsonLesson"/></param>
        public JsonLesson(string LessonClassToken, string LessonSubjectToken)
        {
            this.SubjectToken = LessonSubjectToken;
            this.ClassToken = LessonClassToken;
            this.Teachers = new List<Teacher>();
        }

        /// <summary>
        /// Adds a new <see cref="ExcelAllGrade.model.Teacher"/> to <see cref="Teachers"/>.
        /// </summary>
        /// <param name="teacher">The <see cref="ExcelAllGrade.model.Teacher"/> to add</param>
        public void addTeacher(Teacher teacher)
        {
            this.Teachers.Add(teacher);
        }
    }
}
