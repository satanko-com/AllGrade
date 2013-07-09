//  ************************************************************
//  *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
//  *                                                          *
//  *  See the file LICENCE.txt for copying permission.        *
//  ************************************************************ 

using Newtonsoft.Json;

namespace ExcelAllGrade.model
{
    /// <summary>
    /// This class represents a <c>Student</c>.
    /// </summary>
    public class Student
    {
        /// <summary>
        /// Gets or sets the <c>StudentToken</c> of the <see cref="Student"/>.
        /// </summary>
        public string StudentToken { get; set; }
        /// <summary>
        /// Gets or sets the <c>StudentFirstName</c> of the <see cref="Student"/>.
        /// </summary>
        public string StudentFirstName { get; set; }
        /// <summary>
        /// Gets or sets the <c>StudentLastName</c> of the <see cref="Student"/>.
        /// </summary>
        public string StudentLastName { get; set; }

        /// <summary>
        /// Initializes a new instance of <see cref="Student"/>.
        /// </summary>
        /// <param name="StudentToken">The <see cref="StudentToken"/> of the <see cref="Student"/>.</param>
        /// <param name="StudentFirstName">The <see cref="StudentFirstName"/> of the <see cref="Student"/>.</param>
        /// <param name="StudentLastName">The <see cref="StudentLastName"/> of the <see cref="Student"/>.</param>
        [JsonConstructor]
        public Student(string StudentToken, string StudentFirstName, string StudentLastName)
        {
            this.StudentToken = StudentToken;
            this.StudentFirstName = StudentFirstName;
            this.StudentLastName = StudentLastName;
        }
    }
}
