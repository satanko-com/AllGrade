﻿//  ************************************************************
//  *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
//  *                                                          *
//  *  See the file LICENCE.txt for copying permission.        *
//  ************************************************************ 

using ExcelAllGrade.model;
using ExcelAllGrade.web.adapter.interf;
using ExcelAllGrade.web.json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Net;

namespace ExcelAllGrade.web.adapter.impl
{
    /// <inheritdoc />
    public class WebAllGradeStudentAdapter : IWebAllGradeStudentAdapter
    {
        /// <inheritdoc />
        public WebProxy Proxy { get; set; }

        /// <inheritdoc />
        public List<Student> getStudentList(string classToken, string teacher)
        {
            string baseUri = Constants.SERVER_HOST + ":" + Constants.PORT + Constants.PATH_GET_STUDENTS;

            string parameterString = "?";
            parameterString += Constants.PARAM_CLASS + "=" + classToken + "&";
            parameterString += Constants.PARAM_TEACHER + "=" + teacher;

            Uri uri = new Uri(baseUri + parameterString);

            HttpWebRequest webRequest = (HttpWebRequest)WebRequest.Create(uri);
            webRequest.Proxy = Proxy;

            webRequest.Method = "GET";

            try
            {
                using (WebResponse webResponse = webRequest.GetResponse())
                {
                    using (Stream responseStream = webResponse.GetResponseStream())
                    {
                        using (StreamReader reader = new StreamReader(responseStream))
                        {
                            string responseString = reader.ReadToEnd();

                            return JsonBuilder.deserializeStudentList(responseString);
                        }
                    }
                }
            }
            catch (WebException)
            {
                throw;
            }
        }
    }
}
