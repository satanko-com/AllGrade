//  ************************************************************
//  *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
//  *                                                          *
//  *  See the file LICENCE.txt for copying permission.        *
//  ************************************************************ 

using ExcelAllGrade.model;
using System;
using System.Collections.Generic;
using System.Net;

namespace ExcelAllGrade.controller
{
    /// <summary>
    /// Loads variables from the server.
    /// </summary>
    public class VariableLoader
    {
        private WebAdapterProvider webProvider;
        private LoaderResult LResultInternal { get; set; }

        /// <summary>
        /// Gets the <see cref="LoaderResult"/> of the <see cref="VariableLoader"/>.
        /// </summary>
        public LoaderResult LResult
        {
            get { return LResultInternal; }
        }

        /// <summary>
        /// Gets or sets <see cref="List{T}"/> of the <see cref="VariableLoader"/>.
        /// </summary>
        public List<JsonLesson> JsonLessons { get; set; }
        /// <summary>
        /// Gets or sets <see cref="List{T}"/> of the <see cref="VariableLoader"/>.
        /// </summary>
        public List<Class> Classes { get; set; }
        /// <summary>
        /// Gets or sets <see cref="List{T}"/> of the <see cref="VariableLoader"/>.
        /// </summary>
        public List<Subject> Subjects { get; set; }

        private string teacher;

        /// <summary>
        /// Gets or sets <see cref="WebException"/> of the <see cref="VariableLoader"/>.
        /// </summary>
        public WebException Exception { get; set; }

        /// <summary>
        /// Initializes a new instance of <see cref="VariableLoader"/>.
        /// </summary>
        /// <param name="webProvider">The <see cref="ExcelAllGrade.controller.WebAdapterProvider"/> necessary for the connection with the server.</param>
        /// <param name="teacher">The <c>teacher-token</c>.</param>
        public VariableLoader( WebAdapterProvider webProvider, string teacher)
        {
            this.webProvider = webProvider;
            this.teacher = teacher;

            LResultInternal = LoaderResult.NotStarted;
        }

        /// <summary>
        /// Starts the initialization.
        /// </summary>
        public void startInitialization()
        {
            LResultInternal = LoaderResult.InitializationRunning;

            this.initializeVariables();
        }

        /// <summary>
        /// Initializes the variables.
        /// </summary>
        private void initializeVariables()
        {
            try
            {
                Subjects = webProvider.getSubjectList();
                Classes = webProvider.getClassList();
                JsonLessons = webProvider.getLessonList("", teacher);

                LResultInternal = LoaderResult.FinishedSuccessfully;
            }
            catch (WebException ex)
            {
                LResultInternal = LoaderResult.ThrewException;
                Exception = ex;
            }
            catch (Exception ex)
            {
                LResultInternal = LoaderResult.ThrewException;
                Exception = new WebException(ex.Message);
            }
        }
    }

    /// <summary>
    /// Specifies the result of the <see cref="VariableLoader"/>.
    /// </summary>
    public enum LoaderResult
    {
        /// <summary>
        /// The <see cref="VariableLoader"/> was successful.
        /// </summary>
        FinishedSuccessfully = 0,
        /// <summary>
        /// The <see cref="VariableLoader"/> is working.
        /// </summary>
        InitializationRunning = 3,
        /// <summary>
        /// The <see cref="VariableLoader"/> has not started yet.
        /// </summary>
        NotStarted = 1,
        /// <summary>
        /// The <see cref="VariableLoader"/> encountered an <c>Exception</c>.
        /// </summary>
        ThrewException = 2
    }
}
