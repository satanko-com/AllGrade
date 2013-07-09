//  ************************************************************
//  *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
//  *                                                          *
//  *  See the file LICENCE.txt for copying permission.        *
//  ************************************************************ 

using System;
using System.Collections.Generic;
using System.Runtime.Serialization;

namespace ExcelAllGrade.model
{
    /// <summary>
    /// This class represents a <c>Lesson</c>.
    /// </summary>
    [Serializable()]
    public class Lesson : ISerializable
    {
        /// <summary>
        /// Gets or sets the <c>Teachers</c> of the <see cref="Lesson"/>.
        /// </summary>
        public List<Teacher> Teachers { get; set; }
        /// <summary>
        /// Gets or sets the <see cref="ExcelAllGrade.model.Subject"/> of the <see cref="Lesson"/>.
        /// </summary>
        public Subject Subject { get; set; }

        /// <summary>
        /// Initializes a new instance of <see cref="Lesson"/>.
        /// </summary>
        /// <param name="teachers">The <see cref="Teachers"/> of the <see cref="Lesson"/>.</param>
        /// <param name="subject">The <see cref="Subject"/> of the <see cref="Lesson"/>.</param>
        public Lesson(List<Teacher> teachers, Subject subject)
        {
            this.Teachers = teachers;
            this.Subject = subject;
        }

        /// <summary>
        /// Initializes a new instance of <see cref="Lesson"/>. A new <see cref="ExcelAllGrade.model.Teacher"/> is added to <see cref="Teachers"/>.
        /// </summary>
        /// <param name="teacher">The <see cref="Teachers"/> of the <see cref="Lesson"/>.</param>
        /// <param name="subject">The <see cref="Subject"/> of the <see cref="Lesson"/>.</param>
        public Lesson(string teacher, Subject subject)
        {
            this.Subject = subject;
            this.Teachers = new List<Teacher>();
            this.Teachers.Add(new Teacher(teacher));
        }

        //Deserialization constructor.
        /// <summary>
        /// The deserialization-constructor of <see cref="Lesson"/>.
        /// </summary>
        /// <param name="info">The <see cref="System.Runtime.Serialization.SerializationInfo"/>.</param>
        /// <param name="ctxt">The <see cref="System.Runtime.Serialization.StreamingContext"/>.</param>
        public Lesson(SerializationInfo info, StreamingContext ctxt)
        {
            Teachers = (List<Teacher>)info.GetValue("Teachers", typeof(List<Teacher>));
            Subject = (Subject)info.GetValue("Subject", typeof(Subject));
        }

        //Serialization function.
        /// <summary>
        /// The serialization-function of <see cref="Lesson"/>.
        /// </summary>
        /// <param name="info">The <see cref="System.Runtime.Serialization.SerializationInfo"/>.</param>
        /// <param name="ctxt">The <see cref="System.Runtime.Serialization.StreamingContext"/>.</param>
        public void GetObjectData(SerializationInfo info, StreamingContext ctxt)
        {
            info.AddValue("Teachers", Teachers);
            info.AddValue("Subject", Subject);
        }
    }
}
