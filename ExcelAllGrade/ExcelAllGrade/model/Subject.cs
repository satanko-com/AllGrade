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
    /// This class represents a <c>Subject</c>.
    /// </summary>
    [Serializable()]
    public class Subject : ISerializable
    {
        /// <summary>
        /// Gets or sets the <c>Token</c> of the <see cref="Subject"/>.
        /// </summary>
        public string Token { get; set; }
        /// <summary>
        /// Gets or sets the <c>Name</c> of the <see cref="Subject"/>.
        /// </summary>
        public string Name { get; set; }

        /// <summary>
        /// Initializes a new instance of <see cref="Subject"/>.
        /// </summary>
        /// <param name="SubjectToken">The <see cref="Token"/> of the <see cref="Subject"/>.</param>
        /// <param name="SubjectName">The <see cref="Name"/> of the <see cref="Subject"/>.</param>
        public Subject(string SubjectToken, string SubjectName = "")
        {
            this.Token = SubjectToken;
            this.Name = SubjectName;
        }

        //Deserialization constructor.
        /// <summary>
        /// The deserialization-constructor of <see cref="Subject"/>.
        /// </summary>
        /// <param name="info">The <see cref="System.Runtime.Serialization.SerializationInfo"/>.</param>
        /// <param name="ctxt">The <see cref="System.Runtime.Serialization.StreamingContext"/>.</param>
        public Subject(SerializationInfo info, StreamingContext ctxt)
        {
            Token = (string)info.GetValue("SubjectToken", typeof(string));
            Name = (string)info.GetValue("SubjectName", typeof(string));
        }

        //Serialization function.
        /// <summary>
        /// The serialization-function of <see cref="Subject"/>.
        /// </summary>
        /// <param name="info">The <see cref="System.Runtime.Serialization.SerializationInfo"/>.</param>
        /// <param name="ctxt">The <see cref="System.Runtime.Serialization.StreamingContext"/>.</param>
        public void GetObjectData(SerializationInfo info, StreamingContext ctxt)
        {
            info.AddValue("SubjectToken", Token);
            info.AddValue("SubjectName", Name);
        }
    }
}
