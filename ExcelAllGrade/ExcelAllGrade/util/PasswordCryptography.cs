//  ************************************************************
//  *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
//  *                                                          *
//  *  See the file LICENCE.txt for copying permission.        *
//  ************************************************************ 

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Security.Cryptography;

namespace ExcelAllGrade.util
{
    /// <summary>
    /// This class is responsible for encrypting and decrypting password-strings.
    /// </summary>
    public static class PasswordCryptography
    {
        private static byte[] s_aditionalEntropy = { 68, 67, 50, 46, 68, 119 };

        /// <summary>
        /// Encrypts the specified string.
        /// </summary>
        /// <param name="str">The string to encrypt.</param>
        /// <returns>The encrypted string.</returns>
        public static string encryptString(string str)
        {
            return System.Convert.ToBase64String(PasswordCryptography.Protect(System.Convert.FromBase64String(PasswordCryptography.EncodeTo64(str))));
        }

        /// <summary>
        /// Decrypts the specified string.
        /// </summary>
        /// <param name="str">The string to decrypt.</param>
        /// <returns>The decrypted string.</returns>
        public static string decryptString(string str)
        {
            return PasswordCryptography.DecodeFrom64(System.Convert.ToBase64String(PasswordCryptography.Unprotect(System.Convert.FromBase64String(str)))); 
        }

        private static byte[] Protect(byte[] data)
        {
            try
            {
                // Encrypt the data using DataProtectionScope.CurrentUser. The result can be decrypted
                //  only by the same current user.
                return ProtectedData.Protect(data, s_aditionalEntropy, DataProtectionScope.CurrentUser);
            }
            catch (CryptographicException e)
            {
                Console.WriteLine("Data was not encrypted. An error occurred.");
                Console.WriteLine(e.ToString());
                return null;
            }
        }

        private static byte[] Unprotect(byte[] data)
        {
            try
            {
                //Decrypt the data using DataProtectionScope.CurrentUser.
                return ProtectedData.Unprotect(data, s_aditionalEntropy, DataProtectionScope.CurrentUser);
            }
            catch (CryptographicException e)
            {
                Console.WriteLine("Data was not decrypted. An error occurred.");
                Console.WriteLine(e.ToString());
                return null;
            }
        }

        private static string EncodeTo64(string toEncode)
        {
            byte[] toEncodeAsBytes
                  = System.Text.ASCIIEncoding.ASCII.GetBytes(toEncode);
            string returnValue
                  = System.Convert.ToBase64String(toEncodeAsBytes);
            return returnValue;
        }

        private static string DecodeFrom64(string encodedData)
        {
            byte[] encodedDataAsBytes
                = System.Convert.FromBase64String(encodedData);
            string returnValue =
               System.Text.ASCIIEncoding.ASCII.GetString(encodedDataAsBytes);
            return returnValue;
        }
    }
}
