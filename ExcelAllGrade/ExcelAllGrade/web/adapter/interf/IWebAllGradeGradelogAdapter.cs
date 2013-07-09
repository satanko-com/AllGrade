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
    /// Provides access to <see cref="ExcelAllGrade.model.Gradelog"/>s on WebAllGrade.
    /// </summary>
    public interface IWebAllGradeGradelogAdapter
    {
        /// <summary>
        /// Gets or sets the <see cref="WebProxy"/> of the specified object.
        /// </summary>
        WebProxy Proxy { get; set; }

        /// <summary>
        /// Gets all <see cref="ExcelAllGrade.model.Gradelog"/>s on WebAllGrade.
        /// </summary>
        /// <param name="classToken">The <c>classToken</c> of the <see cref="ExcelAllGrade.model.Gradelog"/>.</param>
        /// <param name="subjectToken">The <c>subjectToken</c> of the <see cref="ExcelAllGrade.model.Gradelog"/>.</param>
        /// <param name="teacher">The <c>teacher</c> of the <see cref="ExcelAllGrade.model.Gradelog"/>.</param>
        /// <returns>A <see cref="List{T}"/> of <see cref="ExcelAllGrade.model.Gradelog"/>s from WebAllGrade.</returns>
        List<Gradelog> getGradelogs(string classToken, string subjectToken, string teacher);
    }
}
