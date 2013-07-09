//  ************************************************************
//  *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
//  *                                                          *
//  *  See the file LICENCE.txt for copying permission.        *
//  ************************************************************ 

using System;
using System.Runtime.Serialization;

namespace ExcelAllGrade.model
{
    /// <summary>
    /// This class represents a <c>Teacher</c>.
    /// </summary>
    [Serializable()]
    public class Teacher : ISerializable
    {
        /// <summary>
        /// Gets or sets the <c>TeacherToken</c> of the <see cref="Teacher"/>.
        /// </summary>
        public string TeacherToken { get; set; }

        /// <summary>
        /// Initializes a new instance of <see cref="Teacher"/>.
        /// </summary>
        /// <param name="teacherToken">The <see cref="TeacherToken"/> of <see cref="Teacher"/>.</param>
        public Teacher(string teacherToken)
        {
            this.TeacherToken = teacherToken;
        }

        //Deserialization constructor.
        /// <summary>
        /// The deserialization-constructor of <see cref="Teacher"/>.
        /// </summary>
        /// <param name="info">The <see cref="System.Runtime.Serialization.SerializationInfo"/>.</param>
        /// <param name="ctxt">The <see cref="System.Runtime.Serialization.StreamingContext"/>.</param>
        public Teacher(SerializationInfo info, StreamingContext ctxt)
        {
            TeacherToken = (string)info.GetValue("TeacherToken", typeof(string));
        }

        //Serialization function.
        /// <summary>
        /// The serialization-function of <see cref="Teacher"/>.
        /// </summary>
        /// <param name="info">The <see cref="System.Runtime.Serialization.SerializationInfo"/>.</param>
        /// <param name="ctxt">The <see cref="System.Runtime.Serialization.StreamingContext"/>.</param>
        public void GetObjectData(SerializationInfo info, StreamingContext ctxt)
        {
            info.AddValue("TeacherToken", TeacherToken);
        }
    }

}
