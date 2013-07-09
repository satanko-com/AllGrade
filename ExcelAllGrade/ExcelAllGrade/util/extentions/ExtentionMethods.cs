//  ************************************************************
//  *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
//  *                                                          *
//  *  See the file LICENCE.txt for copying permission.        *
//  ************************************************************ 

using System;
using System.Reflection;
using System.Windows.Forms;

namespace ExcelAllGrade.util.extentions
{
    /// <summary>
    /// This class provides a variety of useful extention methods.
    /// </summary>
    public static class ExtentionMethods
    {
        /// <summary>
        /// Invokes the action of a <see cref="System.Windows.Forms.Control"/> if required.
        /// </summary>
        /// <param name="control">The <see cref="System.Windows.Forms.Control"/> to invoke the <paramref name="action"/> on.</param>
        /// <param name="action">The <see cref="System.Windows.Forms.MethodInvoker"/> to invoke.</param>
        public static void InvokeIfRequired(this Control control, MethodInvoker action)
        {
            if (control.InvokeRequired)
            {
                control.Invoke(action);
            }
            else
            {
                action();
            }
        }

        /// <summary>
        /// Increments the alphabetic column-name by one step.
        /// </summary>
        /// <param name="s">The current alphabetic column-name.</param>
        /// <returns><paramref name="s"/> incremented by one step.</returns>
        public static string ExcelIncrementByOne(this string s)
        {
            char[] characters = s.ToCharArray();

            s = "";
            int index = 1;

            if (characters[characters.Length - index] < 'Z')
            {
                characters[characters.Length - index]++;

                foreach (char c in characters)
                {
                    s = s + c;
                }
            }
            else if (characters[characters.Length - index] == 'Z')
            {
                while (true)
                {
                    try
                    {
                        characters[characters.Length - index] = 'A';

                        index++;
                    
                        if (characters[characters.Length - index] < 'Z')
                        {
                            characters[characters.Length - index]++;

                            break;
                        }
                        else if (characters[characters.Length - index] == 'Z')
                        {
                            continue;
                        }
                    }
                    catch (IndexOutOfRangeException)
                    {
                        foreach (char c in characters)
                        {
                            s = s + c;
                        }

                        return 'A' + s;
                    }
                }

                foreach (char c in characters)
                {
                    s = s + c;
                }
            }

            return s;
        }

        /// <summary>
        /// Will get the string value for a given enums value, this will
        /// only work if you assign the StringValue attribute to
        /// the items in your enum.
        /// </summary>
        /// <param name="value">The <see cref="System.Enum"/> to get the string-value from.</param>
        /// <returns>The string-value of <paramref name="value"/>.</returns>
        public static string GetStringValue(this Enum value)
        {
            // Get the type
            Type type = value.GetType();

            // Get fieldinfo for this type
            FieldInfo fieldInfo = type.GetField(value.ToString());

            // Get the stringvalue attributes
            StringValueAttribute[] attribs = fieldInfo.GetCustomAttributes(
                typeof(StringValueAttribute), false) as StringValueAttribute[];

            // Return the first if there was a match.
            return attribs.Length > 0 ? attribs[0].StringValue : null;
        }

        /// <summary>
        /// Sets the port of the specified <see cref="Uri"/>.
        /// </summary>
        /// <param name="uri">The <see cref="Uri"/> where the port should be set.</param>
        /// <param name="newPort">The new port.</param>
        /// <returns>The new <see cref="Uri"/> with the specified port.</returns>
        public static Uri SetPort(this Uri uri, int newPort)
        {
            var builder = new UriBuilder(uri);
            builder.Port = newPort;
            return builder.Uri;
        }
    }
}
