//  ************************************************************
//  *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
//  *                                                          *
//  *  See the file LICENCE.txt for copying permission.        *
//  ************************************************************ 

using Newtonsoft.Json;

namespace ExcelAllGrade.model
{
    /// <summary>
    /// This class represents a <c>User</c>.
    /// </summary>
    public class User
    {
        /// <summary>
        /// Gets or sets the <c>AccessToken</c> of the <see cref="User"/>.
        /// </summary>
        public string AccessToken { get; set; }
        /// <summary>
        /// Gets or sets the <c>RefreshToken</c> of the <see cref="User"/>.
        /// </summary>
        public string RefreshToken { get; set; }
        /// <summary>
        /// Gets or sets the <c>Mail</c> of the <see cref="User"/>.
        /// </summary>
        public string Mail { get; set; }
        /// <summary>
        /// Gets or sets the <c>TokenType</c> of the <see cref="User"/>.
        /// </summary>
        public string TokenType { get; set; }

        /// <summary>
        /// Initializes a new instance of <see cref="User"/>.
        /// </summary>
        /// <param name="access_token">The <see cref="AccessToken"/> of <see cref="User"/>.</param>
        /// <param name="token_type">The <see cref="TokenType"/> of <see cref="User"/>.</param>
        /// <param name="refresh_token">The <see cref="RefreshToken"/> of <see cref="User"/>.</param>
        [JsonConstructor]
        public User(string access_token, string token_type, string refresh_token)
        {
            this.AccessToken = access_token;
            this.TokenType = token_type;
            this.RefreshToken = refresh_token;
        }

        /// <summary>
        /// Initializes a new instance of <see cref="User"/>.
        /// </summary>
        /// <param name="access_token">The <see cref="AccessToken"/> of <see cref="User"/>.</param>
        /// <param name="token_type">The <see cref="TokenType"/> of <see cref="User"/>.</param>
        public User(string access_token, string token_type)
        {
            this.AccessToken = access_token;
            this.TokenType = token_type;
        }
    }
}
