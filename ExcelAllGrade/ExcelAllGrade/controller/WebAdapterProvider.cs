//  ************************************************************
//  *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
//  *                                                          *
//  *  See the file LICENCE.txt for copying permission.        *
//  ************************************************************ 

using ExcelAllGrade.model;
using ExcelAllGrade.web.adapter.impl;
using ExcelAllGrade.web.adapter.interf;
using System.Collections.Generic;
using System.Net;

namespace ExcelAllGrade.controller
{
    /// <summary>
    /// Provides a collection of all web-related methods.
    /// </summary>
    public class WebAdapterProvider :
        IWebAllGradeLessonAdapter, IWebAllGradeClassAdapter, IWebAllGradeSubjectAdapter,
        IWebAllGradeStudentAdapter, IWebAllGradeGradelogAdapter
    {
        private IWebAllGradeLessonAdapter lessonsAdapter = (IWebAllGradeLessonAdapter) new WebAllGradeLessonAdapter();
        private IWebAllGradeClassAdapter classesAdapter = (IWebAllGradeClassAdapter) new WebAllGradeClassAdapter();
        private IWebAllGradeSubjectAdapter subjectAdapter = (IWebAllGradeSubjectAdapter) new WebAllGradeSubjectAdapter();
        private IWebAllGradeStudentAdapter studentAdapter = (IWebAllGradeStudentAdapter) new WebAllGradeStudentAdapter();
        private IWebAllGradeGradelogAdapter gradelogAdapter = (IWebAllGradeGradelogAdapter) new WebAllGradeGradelogAdapter();

        private WebProxy proxy;
        /// <summary>
        /// The <see cref="WebProxy"/> for this object. IMPORTANT: Do NOT use <c>set</c>!
        /// </summary>
        public WebProxy Proxy { get { return proxy; } set { return; } }

        /// <inheritdoc />
        public List<JsonLesson> getLessonList(string classToken, string teacher)
        {
            return lessonsAdapter.getLessonList(classToken, teacher);
        }

        /// <inheritdoc />
        public HttpStatusCode postLessonList(string jsonLessonList)
        {
            return lessonsAdapter.postLessonList(jsonLessonList);
        }

        /// <inheritdoc />
        public List<Class> getClassList()
        {
            return classesAdapter.getClassList();
        }

        /// <inheritdoc />
        public List<Subject> getSubjectList()
        {
            return subjectAdapter.getSubjectList();
        }

        /// <inheritdoc />
        public List<Student> getStudentList(string classToken, string teacher)
        {
            return studentAdapter.getStudentList(classToken, teacher);
        }

        /// <inheritdoc />
        public List<Gradelog> getGradelogs(string classToken, string subjectToken, string teacher)
        {
            return gradelogAdapter.getGradelogs(classToken, subjectToken, teacher);
        }

        /// <summary>
        /// Sets the <see cref="WebProxy"/> for this object.
        /// </summary>
        /// <param name="proxy">The <see cref="WebProxy"/> to set.</param>
        public void setProxy(WebProxy proxy)
        {
            lessonsAdapter.Proxy = proxy;
            classesAdapter.Proxy = proxy;
            subjectAdapter.Proxy = proxy;
            studentAdapter.Proxy = proxy;
            gradelogAdapter.Proxy = proxy;

            this.proxy = proxy;
        }
    }
}
