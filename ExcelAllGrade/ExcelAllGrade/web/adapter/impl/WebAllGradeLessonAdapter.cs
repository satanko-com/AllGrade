//  ************************************************************
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
using System.Text;

namespace ExcelAllGrade.web.adapter.impl
{
    /// <inheritdoc />
    public class WebAllGradeLessonAdapter : IWebAllGradeLessonAdapter
    {
        /// <inheritdoc />
        public WebProxy Proxy { get; set; }

        /// <inheritdoc />
        public HttpStatusCode postLessonList(string jsonLessonList)
        {
            string baseUri = Constants.SERVER_HOST + ":" + Constants.PORT + Constants.PATH_GET_LESSONS;

            HttpWebRequest httpWReq = (HttpWebRequest)WebRequest.Create(baseUri);
            httpWReq.Proxy = Proxy;

            ASCIIEncoding encoding = new ASCIIEncoding();
            byte[] data = encoding.GetBytes(jsonLessonList);

            httpWReq.Method = "POST";
            httpWReq.ContentLength = data.Length;
            httpWReq.ContentType = "application/json";
            httpWReq.KeepAlive = false;
            httpWReq.ProtocolVersion = HttpVersion.Version10;
            httpWReq.ServicePoint.ConnectionLimit = 1;

            try
            {
                using (Stream newStream = httpWReq.GetRequestStream())
                {
                    newStream.Write(data, 0, data.Length);
                }

                using (HttpWebResponse webResponse = (HttpWebResponse)httpWReq.GetResponse())
                {
                    return webResponse.StatusCode;
                }
            }
            catch (WebException ex)
            {
                if (ex.Response != null)
                {
                    return ((HttpWebResponse)ex.Response).StatusCode;
                }
                else
                {
                    throw;
                }
            }
        }

        /// <inheritdoc />
        public List<JsonLesson> getLessonList(string classToken, string teacher)
        {
            string baseUri = Constants.SERVER_HOST + ":" + Constants.PORT + Constants.PATH_GET_LESSONS;

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

                            return JsonBuilder.deserializeJsonLessonList(responseString);
                        }
                    }
                }
            }
            catch (WebException ex)
            {
                if (((HttpWebResponse)ex.Response).StatusCode == HttpStatusCode.Conflict)
                {
                    return new List<JsonLesson>();
                }

                else
                    throw;
            }
        }
    }
}
