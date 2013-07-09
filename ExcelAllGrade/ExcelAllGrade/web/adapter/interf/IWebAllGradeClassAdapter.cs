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
    /// Provides access to <see cref="ExcelAllGrade.model.Class"/>es on WebAllGrade.
    /// </summary>
    public interface IWebAllGradeClassAdapter
    {
        /// <summary>
        /// Gets or sets the <see cref="WebProxy"/> of the specified object.
        /// </summary>
        WebProxy Proxy { get; set; }

        /// <summary>
        /// Gets all <see cref="ExcelAllGrade.model.Class"/>es on WebAllGrade.
        /// </summary>
        /// <returns>A <see cref="List{T}"/> of <see cref="ExcelAllGrade.model.Class"/>es from WebAllGrade.</returns>
        List<Class> getClassList();
    }
}
