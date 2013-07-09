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
    /// This class represents a <c>Class</c>.
    /// </summary>
    [Serializable()]
    public class Class : ISerializable
    {
        /// <summary>
        /// Gets or sets the <c>Name</c> of the <see cref="Class"/>.
        /// </summary>
        public string Name { get; set; }
        /// <summary>
        /// Gets or sets the <c>Lessons</c> of the <see cref="Class"/>.
        /// </summary>
        public List<Lesson> Lessons { get; set; }

        /// <summary>
        /// Initializes a new instance of <see cref="Class"/>.
        /// </summary>
        /// <param name="ClassToken">The <see cref="Name"/> of the <see cref="Class"/>.</param>
        public Class(string ClassToken)
        {
            this.Name = ClassToken;
            Lessons = new List<Lesson>();
        }

        //Deserialization constructor.
        /// <summary>
        /// The deserialization-constructor of <see cref="Class"/>.
        /// </summary>
        /// <param name="info">The <see cref="System.Runtime.Serialization.SerializationInfo"/>.</param>
        /// <param name="ctxt">The <see cref="System.Runtime.Serialization.StreamingContext"/>.</param>
        public Class(SerializationInfo info, StreamingContext ctxt)
        {
            Name = (string)info.GetValue("ClassToken", typeof(string));
            try
            {
                Lessons = (List<Lesson>)info.GetValue("Lessons", typeof(List<Lesson>));
            }
            catch (SerializationException)
            {
                Lessons = new List<Lesson>();
            }
        }

        //Serialization function.
        /// <summary>
        /// The serialization-function of <see cref="Class"/>.
        /// </summary>
        /// <param name="info">The <see cref="System.Runtime.Serialization.SerializationInfo"/>.</param>
        /// <param name="ctxt">The <see cref="System.Runtime.Serialization.StreamingContext"/>.</param>
        public void GetObjectData(SerializationInfo info, StreamingContext ctxt)
        {
            info.AddValue("ClassToken", Name);
            info.AddValue("Lessons", Lessons);
        }

        /// <summary>
        /// Adds a new <see cref="ExcelAllGrade.model.Lesson"/> to <see cref="Lessons"/>.
        /// </summary>
        /// <param name="lesson"></param>
        public void addLesson(Lesson lesson)
        {
            this.Lessons.Add(lesson);
        }
    }
}
