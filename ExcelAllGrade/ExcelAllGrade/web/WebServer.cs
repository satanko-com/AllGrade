//  ************************************************************
//  *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
//  *                                                          *
//  *  See the file LICENCE.txt for copying permission.        *
//  ************************************************************ 

using ExcelAllGrade.controller;
using System;
using System.Net;
using System.Text;
using System.Threading;

namespace ExcelAllGrade.web
{
    /// <summary>
    /// This class acts as a WebServer to handle the OAuth-Process.
    /// </summary>
    public class WebServer
    {
        private HttpListener Listener;
        private UserController userController;
        private readonly string code_identifier = "?code=";
        private readonly string access_denied = "error=access_denied";

        /// <summary>
        /// Initializes a new instance of <see cref="WebServer"/>.
        /// </summary>
        /// <param name="userController">The <see cref="ExcelAllGrade.controller.UserController"/> required for the OAuth-Process.</param>
        public WebServer(UserController userController)
        {
            this.userController = userController;
            Listener = new HttpListener();
        }

        /// <summary>
        /// Starts the <see cref="WebServer"/>.
        /// </summary>
        public void Start()
        {
            Listener.Prefixes.Add("http://localhost:1337/");
            Listener.Start();

            IAsyncResult result = Listener.BeginGetContext(new AsyncCallback(ProcessRequest), Listener);
            result.AsyncWaitHandle.WaitOne();

            Console.WriteLine("Connection Started");
        }

        /// <summary>
        /// Stops the <see cref="WebServer"/>.
        /// </summary>
        public void Stop()
        {
            Listener.Stop();
        }

        /// <summary>
        /// Checks if the <see cref="WebServer"/> is running.
        /// </summary>
        /// <returns><c>true</c> if <see cref="WebServer"/> is running, <c>false</c> if not.</returns>
        public bool isRunning()
        {
            if (Listener.IsListening)
                return true;
            else
                return false;
        }

        private void ProcessRequest(IAsyncResult result)
        {
            HttpListener listener = (HttpListener)result.AsyncState;
            HttpListenerContext context = listener.EndGetContext(result);

            if (context.Request.RawUrl.Contains(code_identifier))
            {
                string authorizationCode = context.Request.Url.Query.Replace(code_identifier, "");

                userController.doOAuth(UserController.POST_FROM_SERVER, authorizationCode);

                string responseString = "<html style=background-color:#FFFFFF> <img src=https://dl.dropbox.com/u/1332679/29954343.jpg>";
                printResponse(context, responseString);

                new Thread(new ParameterizedThreadStart(stopServerAfterDelay)).Start(1000);
            }
            else if (context.Request.RawUrl.Contains(access_denied))
            {
                userController.doOAuth(UserController.ACCESS_DENIED);

                string responseString = "<html style=background-color:#2c4762> <img src=https://dl.dropbox.com/u/1332679/29092926.jpg>";
                printResponse(context, responseString);

                new Thread(new ParameterizedThreadStart(stopServerAfterDelay)).Start(1000);
            }
        }

        private void stopServerAfterDelay(object ms)
        {
            Thread.Sleep((int) ms);
            this.Stop();
        }

        private void printResponse(HttpListenerContext context, string responseString)
        {
            byte[] buffer = Encoding.UTF8.GetBytes(responseString);

            context.Response.ContentLength64 = buffer.Length;
            System.IO.Stream output = context.Response.OutputStream;
            output.Write(buffer, 0, buffer.Length);
            output.Close();
        }

    }
}
