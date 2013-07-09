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
    /// Provides access to <see cref="ExcelAllGrade.model.Subject"/>s on WebAllGrade.
    /// </summary>
    public interface IWebAllGradeSubjectAdapter
    {
        /// <summary>
        /// Gets or sets the <see cref="WebProxy"/> of the specified object.
        /// </summary>
        WebProxy Proxy { get; set; }

        /// <summary>
        /// Gets all <see cref="ExcelAllGrade.model.Subject"/>s on WebAllGrade.
        /// </summary>
        /// <returns>A <see cref="List{T}"/> of <see cref="ExcelAllGrade.model.Subject"/>s from WebAllGrade.</returns>
        List<Subject> getSubjectList();
    }
}
