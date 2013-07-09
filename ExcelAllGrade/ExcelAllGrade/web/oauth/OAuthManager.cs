//  ************************************************************
//  *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
//  *                                                          *
//  *  See the file LICENCE.txt for copying permission.        *
//  ************************************************************ 

using ExcelAllGrade.model;
using ExcelAllGrade.util;
using Newtonsoft.Json;
using OAuth;
using System;
using System.Diagnostics;
using System.IO;
using System.Net;
using System.Text;

namespace ExcelAllGrade.web.oauth
{
    /// <summary>
    /// This class is responsible for the OAuth-Process.
    /// </summary>
    public class OAuthManager
    {
        #region constants

        private const string consumerKey = "anonymous";
        private const string consumerSecret = "anonymous";
        private const string initialUri = "https://accounts.google.com/o/oauth2/auth";
        private const string client_id = "630443194503.apps.googleusercontent.com";
        private const string response_type = "code";
        private const string redirect_uri = "http://localhost:1337";
        private const string scope = "https://www.googleapis.com/auth/userinfo.email";
        private const string approval_prompt = "auto";
        private const string hd = "new.edvhtl.at";

        private const string authorizationUri = "https://accounts.google.com/o/oauth2/token";
        private const string client_secret = "bQOgXHuQmixeRkqGDgYbkV7_";
        private const string grant_type = "authorization_code";

        private const string grant_type_refresh = "refresh_token";

        private const string requestEmailUri = "https://www.googleapis.com/userinfo/email";

        #endregion

        /// <summary>
        /// Gets or sets the <see cref="WebProxy"/> of the specified object.
        /// </summary>
        public WebProxy Proxy { get; set; }

        /// <summary>
        /// Starts the OAuth-Process.
        /// </summary>
        public void sendOAuthInitialRequest()
        {
            OAuthBase oAuth = new OAuthBase();
            string nonce = oAuth.GenerateNonce();
            string timeStamp = oAuth.GenerateTimeStamp();
            string outURL;
            string queryString;

            string sig = oAuth.GenerateSignature(new Uri(initialUri),
                consumerKey, consumerSecret,
                string.Empty, string.Empty,
                "POST", timeStamp, nonce,
                OAuthBase.SignatureTypes.HMACSHA1, out outURL, out queryString);

            sig = WebUtility.HtmlEncode(sig);
            StringBuilder sb = new StringBuilder(initialUri);
            sb.AppendFormat("?client_id={0}&", client_id);
            sb.AppendFormat("scope={0}&", scope);
            sb.AppendFormat("response_type={0}&", response_type);
            sb.AppendFormat("redirect_uri={0}&", redirect_uri);
            sb.AppendFormat("hd={0}&", hd);
            if (!(UacHelper.IsUacEnabled && UacHelper.IsProcessElevated))
            {
                sb.AppendFormat("approval_prompt={0}&", approval_prompt);
                sb.AppendFormat("oauth_signature={0}&", sig);
                sb.AppendFormat("oauth_nonce={0}&", nonce);
                sb.AppendFormat("oauth_timestamp={0}&", timeStamp);
                sb.AppendFormat("oauth_signature_method={0}&", "HMAC-SHA1");
                sb.AppendFormat("oauth_version={0}", "2.0");
            }

            WebRequest tokenRequest = (HttpWebRequest)WebRequest.Create(sb.ToString());
            tokenRequest.Proxy = Proxy;
            tokenRequest.Method = "POST";
            tokenRequest.ContentLength = sb.Length;
            Stream dataStream = tokenRequest.GetRequestStream();
            dataStream.Write(Encoding.UTF8.GetBytes(sb.ToString()), 0, sb.Length);
            dataStream.Close();

            this.OpenUrlInDefaultBrowser(sb.ToString());

            tokenRequest.GetResponse().Close();
        }

        /// <summary>
        /// Authorizes the <paramref name="authorizationCode"/> and requests the <c>access_code</c>.
        /// </summary>
        /// <param name="authorizationCode">The <c>authorization_code</c>.</param>
        /// <returns>The authorized <see cref="ExcelAllGrade.model.User"/>.</returns>
        public User sendOAuthAuthorizationRequest(string authorizationCode)
        {
            HttpWebRequest webRequest = (HttpWebRequest)WebRequest.Create(new Uri(authorizationUri));
            webRequest.Proxy = Proxy;

            webRequest.Method = "POST";
            webRequest.ContentType = "application/x-www-form-urlencoded";

            string parameterString = "";

            parameterString += "client_id=" + client_id + "&";
            parameterString += "client_secret=" + client_secret + "&";
            parameterString += "code=" + authorizationCode + "&";
            parameterString += "grant_type=" + grant_type + "&";
            parameterString += "redirect_uri=" + redirect_uri + "&";


            byte[] byteArray = Encoding.UTF8.GetBytes(parameterString);
            webRequest.ContentLength = byteArray.Length;

            using (Stream stream = webRequest.GetRequestStream())
            {
                stream.Write(byteArray, 0, byteArray.Length);
            }

            using (WebResponse webResponse = webRequest.GetResponse())
            {
                using (Stream responseStream = webResponse.GetResponseStream())
                {
                    using (StreamReader reader = new StreamReader(responseStream))
                    {
                        string responseString = reader.ReadToEnd();

                        return JsonConvert.DeserializeObject<User>(responseString);
                    }
                }
            }
        }

        /// <summary>
        /// Requests the Mail-Address of a <see cref="ExcelAllGrade.model.User"/>.
        /// </summary>
        /// <param name="access_token">The <c>access_token</c>.</param>
        /// <returns>The Mail-Address.</returns>
        public string requestEmailAddress(string access_token)
        {
            string sURL;
            sURL = requestEmailUri + "?access_token=" + access_token;

            WebRequest wrGETURL = WebRequest.Create(sURL);
            wrGETURL.Proxy = Proxy;

            Stream objStream;
            objStream = wrGETURL.GetResponse().GetResponseStream();

            StreamReader objReader = new StreamReader(objStream);

            string sLine = "";
            int i = 0;

            string[] parts = null;

            while (sLine != null)
            {
                i++;
                sLine = objReader.ReadLine();
                if (sLine != null)
                    parts = sLine.Split('&');
            }

            wrGETURL.GetResponse().Close();
            objStream.Close();
            objReader.Close();

            return parts[0].Split('=')[1];
        }

        /// <summary>
        /// Refreshes the <c>access_token</c> of a <see cref="ExcelAllGrade.model.User"/>.
        /// </summary>
        /// <param name="refresh_token">The <c>refresh_token</c> of the <see cref="ExcelAllGrade.model.User"/>.</param>
        /// <returns>A <see cref="ExcelAllGrade.model.User"/> with a refreshed <c>access_token</c>.</returns>
        public User refreshAccessToken(string refresh_token)
        {
            WebRequest webRequest = WebRequest.Create(new Uri(authorizationUri));
            webRequest.Proxy = Proxy;

            webRequest.Method = "POST";
            webRequest.ContentType = "application/x-www-form-urlencoded";

            string parameterString = "";

            parameterString += "client_id=" + client_id + "&";
            parameterString += "client_secret=" + client_secret + "&";
            parameterString += "refresh_token=" + refresh_token + "&";
            parameterString += "grant_type=" + grant_type_refresh + "&";

            byte[] byteArray = Encoding.UTF8.GetBytes(parameterString);
            webRequest.ContentLength = byteArray.Length;

            using (Stream stream = webRequest.GetRequestStream())
            {
                stream.Write(byteArray, 0, byteArray.Length);
            }

            using (WebResponse webResponse = webRequest.GetResponse())
            {
                using (Stream responseStream = webResponse.GetResponseStream())
                {
                    using (StreamReader reader = new StreamReader(responseStream))
                    {
                        string responseString = reader.ReadToEnd();

                        return JsonConvert.DeserializeObject<User>(responseString);
                    }
                }
            }
        }

        /// <summary>
        /// Opens an URL in the default browser
        /// </summary>
        /// <param name="url">The <c>URL</c> to open.</param>
        public void OpenUrlInDefaultBrowser(string url)
        {
            if (UacHelper.IsUacEnabled && UacHelper.IsProcessElevated)
            {
                ProcessStartInfo processStartInfo = new ProcessStartInfo(@"explorer.exe", "\"" + url + "\"");
                processStartInfo.UseShellExecute = true;
                processStartInfo.Verb = "runas";
                Process.Start(processStartInfo);
            }
            else
            {
                Process.Start(url);
            }
        }
    }
}
