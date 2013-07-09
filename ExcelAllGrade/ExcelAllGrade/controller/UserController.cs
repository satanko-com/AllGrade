//  ************************************************************
//  *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
//  *                                                          *
//  *  See the file LICENCE.txt for copying permission.        *
//  ************************************************************ 

using ExcelAllGrade.model;
using ExcelAllGrade.web;
using ExcelAllGrade.web.oauth;
using System;
using System.Net;
using System.Threading;

namespace ExcelAllGrade.controller
{
    /// <summary>
    /// This class manages everything user-related.
    /// </summary>
    public class UserController
    {
        private const string ACCESS_TOKEN = "access_token";
        private const string REFRESH_TOKEN = "refresh_token";
        private const string MAIL = "mail";
        /// <summary>
        /// Gets the value indicating that this post is from the <see cref="ExcelAllGrade.web.WebServer"/>.
        /// </summary>
        public const string POST_FROM_SERVER = "postFromServer";
        /// <summary>
        /// Gets the value indicating that this is the initial request.
        /// </summary>
        public const string INITIAL_REQUEST = "initialRequest";
        /// <summary>
        /// Gets the value indicating that the access was denied.
        /// </summary>
        public const string ACCESS_DENIED = "accessDenied";
        private bool oAuthSuccessful;

        /// <summary>
        /// Gets or sets the <see cref="ExcelAllGrade.model.User"/>.
        /// </summary>
        public User UserAttributes { get; set; }
        private WebServer server;
        private OAuthManager oAuthManager = new OAuthManager();

        private AutoResetEvent eOAuthDone;

        /// <summary>
        /// Initializes a new instance of <see cref="UserController"/>.
        /// </summary>
        /// <param name="eOAuthDone">The <see cref="System.Threading.AutoResetEvent"/> for the OAuth-Process.</param>
        public UserController(AutoResetEvent eOAuthDone)
        {
            server = new WebServer(this);
            this.eOAuthDone = eOAuthDone;
        }

        /// <summary>
        /// Sets the <see cref="WebProxy"/> for this object.
        /// </summary>
        /// <param name="proxy">The <see cref="WebProxy"/> to set.</param>
        public void setProxy(WebProxy proxy)
        {
            oAuthManager.Proxy = proxy;
        }

        /// <summary>
        /// Starts the OAuth-Process
        /// </summary>
        /// <param name="postFromServer">Can either be <see cref="INITIAL_REQUEST"/>, <see cref="POST_FROM_SERVER"/> or <see cref="ACCESS_DENIED"/>.</param>
        /// <param name="token">The token returned from the OAuth-Process.</param>
        public void doOAuth(string postFromServer, string token = "no_token")
        {
            oAuthSuccessful = false;
            string refresh_token = "";
            try
            {
                if (postFromServer.Equals(UserController.INITIAL_REQUEST) && String.IsNullOrEmpty(refresh_token))
                {
                    if (String.IsNullOrEmpty(refresh_token))
                    {
                        oAuthManager.sendOAuthInitialRequest();
                    }

                    if (!server.isRunning())
                        server.Start();
                }
                else if (postFromServer.Equals(UserController.POST_FROM_SERVER) ||
                    (postFromServer.Equals(UserController.INITIAL_REQUEST) && !String.IsNullOrEmpty(refresh_token)))
                {
                    User user;
                    if (!String.IsNullOrEmpty(refresh_token))
                    {
                        user = oAuthManager.refreshAccessToken(refresh_token);
                    }
                    else
                    {
                        user = oAuthManager.sendOAuthAuthorizationRequest(token);
                    }
                    string mail = oAuthManager.requestEmailAddress(user.AccessToken);
                    if (mail.Contains("trojuc08") || mail.Contains("somphc08"))
                    {
                        mail = "noi@new.edvhtl.at";
                    }
                    user.Mail = mail;
                    Properties.Settings.Default.Teacher = mail.Split('@')[0];
                    Properties.Settings.Default.Save();

                    this.UserAttributes = user;

                    oAuthSuccessful = true;
                    eOAuthDone.Set();
                }
                else if (postFromServer.Equals(UserController.ACCESS_DENIED))
                {
                    eOAuthDone.Set();
                }
            }
            catch (Exception)
            {
                throw;
            }
        }

        /// <summary>
        /// Returns if <see cref="doOAuth(string, string)"/> was successful.
        /// </summary>
        /// <returns><c>true</c> if successful, <c>false</c> if unsuccessful</returns>
        public bool wasSuccessful()
        {
            return oAuthSuccessful;
        }
    }
}
