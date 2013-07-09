//  ************************************************************
//  *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
//  *                                                          *
//  *  See the file LICENCE.txt for copying permission.        *
//  ************************************************************ 

using ExcelAllGrade.model;
using ExcelAllGrade.util.extentions;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.InteropServices;
using Excel = Microsoft.Office.Interop.Excel;
using ExcelAllGrade.controller.spreadsheet.util;

namespace ExcelAllGrade.controller.spreadsheet.util
{
    /// <summary>
    /// This class provides various Spreadsheet utility methods.
    /// </summary>
    public static class SpreadsheetUtil
    {
        private const string NO_COMMENT = "This is no comment, I just hope nobody actually makes a comment like this";

        /// <summary>
        /// Returns a <see cref="List{T}"/> with the specified search-string. Searching for comments is optional.
        /// </summary>
        /// <param name="range">The <c>Excel.Range</c> in which the method should search for the <c>string</c>.</param>
        /// <param name="searchTerm">The <c>string</c> the method should search for.</param>
        /// <param name="comment">If specified, searches for the <paramref name="searchTerm"/> plus the comment.</param>
        /// <returns>A collection of <c>Excel.Range</c> with all found date-occurrences.</returns>
        public static List<Excel.Range> findAllDateOccurences(Excel.Range range, string searchTerm, string comment = NO_COMMENT)
        {
            List<Excel.Range> allFinds = new List<Excel.Range>();
            Excel.Range currentFind = null;
            Excel.Range firstFind = null;

            // You should specify all these parameters every time you call this method,
            // since they can be overridden in the user interface. 
            currentFind = range.Find(searchTerm, System.Type.Missing,
                Excel.XlFindLookIn.xlValues, Excel.XlLookAt.xlPart,
                Excel.XlSearchOrder.xlByRows, Excel.XlSearchDirection.xlNext, false,
                System.Type.Missing, System.Type.Missing);

            while (currentFind != null)
            {
                // Keep track of the first range you find. 
                if (firstFind == null)
                {
                    firstFind = currentFind;
                }
                // If you didn't move to a new range, you are done.
                else if (currentFind.get_Address(System.Type.Missing, System.Type.Missing, Excel.XlReferenceStyle.xlA1, System.Type.Missing, System.Type.Missing)
                      == firstFind.get_Address(System.Type.Missing, System.Type.Missing, Excel.XlReferenceStyle.xlA1, System.Type.Missing, System.Type.Missing))
                {
                    break;
                }

                if (!comment.Equals(NO_COMMENT))
                {
                    try
                    {
                        if (currentFind.Comment.Text().Equals(comment))
                        {
                            allFinds.Add(currentFind);
                        }
                    }
                    catch (NullReferenceException) { }
                }

                currentFind = range.FindNext(currentFind);
            }

            allFinds.Reverse();
            return allFinds;
        }

        /// <summary>
        /// Converts the alphabetic column-name to a numeric column-name, e.g. C becomes 3.
        /// </summary>
        /// <param name="columnName">The alphabetic column-name to convert.</param>
        /// <returns>The numeric column-name.</returns>
        public static int excelColumnNameToNumber(string columnName)
        {
            if (string.IsNullOrEmpty(columnName)) throw new ArgumentNullException("columnName");

            char[] characters = columnName.ToUpperInvariant().ToCharArray();

            int sum = 0;

            for (int i = 0; i < characters.Length; i++)
            {
                sum *= 26;
                sum += (characters[i] - 'A' + 1);
            }

            return sum;
        }

        /// <summary>
        /// Converts an <c>Excel.Range</c> object to the representing <c>string</c>-address.
        /// </summary>
        /// <param name="rng"></param>
        /// <returns></returns>
        public static string rangeAddress(Excel.Range rng)
        {
            return rng.get_AddressLocal(false, false, Excel.XlReferenceStyle.xlA1,
                   System.Type.Missing, System.Type.Missing);
        }

        /// <summary>
        /// Finds out whether a <c>Excel.Worksheet</c> exists in the specified <c>Excel.Application</c>.
        /// </summary>
        /// <param name="name">The name of the <c>Excel.Worksheet</c>.</param>
        /// <param name="excelApplication">The <c>Excel.Application</c> to search in.</param>
        /// <returns><c>true</c> if the <c>Excel.Worksheet</c> exists, <c>false</c> if not.</returns>
        public static bool worksheetExists(string name, Excel.Application excelApplication)
        {
            Excel.Workbook wb = excelApplication.ActiveWorkbook;

            Microsoft.Office.Interop.Excel.Sheets sheets = (Microsoft.Office.Interop.Excel.Sheets)wb.Worksheets;
            try
            {
                Microsoft.Office.Interop.Excel.Worksheet sheet = (Microsoft.Office.Interop.Excel.Worksheet)sheets.get_Item(name);
            }
            catch (COMException)
            {
                return false;
            }

            return true;
        }
    }
}
