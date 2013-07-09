//  ************************************************************
//  *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
//  *                                                          *
//  *  See the file LICENCE.txt for copying permission.        *
//  ************************************************************ 

using ExcelAllGrade.util;
using Newtonsoft.Json;
using System;

namespace ExcelAllGrade.model
{
    /// <summary>
    /// This class represents a <c>Gradelog</c>.
    /// </summary>
    public class Gradelog
    {
        /// <summary>
        /// Gets or sets the <c>GradelogTimestamp</c> of the <see cref="Gradelog"/>.
        /// </summary>
        public DateTime GradelogTimestamp { get; set; }
        /// <summary>
        /// Gets or sets the <c>GradelogComment</c> of the <see cref="Gradelog"/>.
        /// </summary>
        public string GradelogComment { get; set; }
        /// <summary>
        /// Gets or sets the <c>GradelogGrading</c> of the <see cref="Gradelog"/>.
        /// </summary>
        public int GradelogGrading { get; set; }
        /// <summary>
        /// Gets or sets the <c>GradelogType</c> of the <see cref="Gradelog"/>.
        /// </summary>
        public string GradelogType { get; set; }
        /// <summary>
        /// Gets or sets the <c>GradelogStudent</c> of the <see cref="Gradelog"/>.
        /// </summary>
        public string GradelogStudent { get; set; }

        /// <summary>
        /// Initializes a new instance of <see cref="Gradelog"/>.
        /// </summary>
        /// <param name="GradelogTimestamp">The <see cref="GradelogTimestamp"/> of the <see cref="Gradelog"/>.</param>
        /// <param name="GradelogComment">The <see cref="GradelogComment"/> of the <see cref="Gradelog"/>.</param>
        /// <param name="GradelogGrading">The <see cref="GradelogGrading"/> of the <see cref="Gradelog"/>.</param>
        /// <param name="GradelogType">The <see cref="GradelogType"/> of the <see cref="Gradelog"/>.></param>
        /// <param name="GradelogStudent">The <see cref="GradelogStudent"/> of the <see cref="Gradelog"/>.></param>
        [JsonConstructor]
        public Gradelog(string GradelogTimestamp, string GradelogComment, int GradelogGrading, string GradelogType, string GradelogStudent)
        {
            this.GradelogTimestamp = DateTime.Parse(GradelogTimestamp);
            this.GradelogComment = GradelogComment;
            switch (GradelogGrading)
            {
                case 5:
                    this.GradelogGrading = 1;
                    break;
                case 4:
                    this.GradelogGrading = 2;
                    break;
                case 3:
                    this.GradelogGrading = 3;
                    break;
                case 2:
                    this.GradelogGrading = 4;
                    break;
                case 1:
                    this.GradelogGrading = 5;
                    break;
            }
            this.GradelogType = GradelogType;
            this.GradelogStudent = GradelogStudent;
        }

        /// <summary>
        /// Represents all possible <see cref="GradelogType"/>s.
        /// </summary>
        public enum GradelogTypeEnum
        {
            /// <summary>
            /// The <see cref="GradelogType"/> is a "Test".
            /// </summary>
            [StringValue("Test")]
            Test = 0,
            /// <summary>
            /// The <see cref="GradelogType"/> is a "Mitarbeit".
            /// </summary>
            [StringValue("Mitarbeit")]
            Mitarbeit = 1,
            /// <summary>
            /// The <see cref="GradelogType"/> is a "Hausübung".
            /// </summary>
            [StringValue("Hausübung")]
            Hausuebung = 2,
            /// <summary>
            /// The <see cref="GradelogType"/> is a "Lernzielkontrolle".
            /// </summary>
            [StringValue("Lernzielkontrolle")]
            Lernzielkontrolle = 3,
            /// <summary>
            /// The <see cref="GradelogType"/> is a "Praktische Leistungsfeststellung".
            /// </summary>
            [StringValue("Praktische Leistungsfestst.")]
            PLF = 4,
            /// <summary>
            /// The <see cref="GradelogType"/> is a "Schularbeit".
            /// </summary>
            [StringValue("Schularbeit")]
            SA = 5
        }
    }
}
