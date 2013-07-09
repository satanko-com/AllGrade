//  ************************************************************
//  *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
//  *                                                          *
//  *  See the file LICENCE.txt for copying permission.        *
//  ************************************************************ 

using ExcelAllGrade.model;
using System.Collections.Generic;
using System.Net;

namespace ExcelAllGrade.web.adapter.interf
{
    /// <summary>
    /// Provides access to <see cref="ExcelAllGrade.model.Lesson"/>s on the WebAllGrade.
    /// </summary>
    public interface IWebAllGradeLessonAdapter
    {
        /// <summary>
        /// Gets or sets the <see cref="WebProxy"/> of the specified object.
        /// </summary>
        WebProxy Proxy { get; set; }

        /// <summary>
        /// Gets all <see cref="ExcelAllGrade.model.Lesson"/>s on WebAllGrade.
        /// </summary>
        /// <param name="classToken">The <c>classToken</c> of the <see cref="ExcelAllGrade.model.Lesson"/>.</param>
        /// <param name="teacher">The <c>teacher</c> of the <see cref="ExcelAllGrade.model.Lesson"/>.</param>
        /// <returns>A <see cref="List{T}"/> of <see cref="ExcelAllGrade.model.Lesson"/>s from WebAllGrade.</returns>
        List<JsonLesson> getLessonList(string classToken, string teacher);

        /// <summary>
        /// Posts new <see cref="ExcelAllGrade.model.Lesson"/>s to WebAllGrade.
        /// </summary>
        /// <param name="jsonLessonList">The <see cref="System.String"/> in JSON-form.</param>
        /// <returns>The <see cref="System.Net.HttpStatusCode"/> of the operation.</returns>
        HttpStatusCode postLessonList(string jsonLessonList);
    }
}
