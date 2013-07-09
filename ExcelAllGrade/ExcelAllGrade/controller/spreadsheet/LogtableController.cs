//  ************************************************************
//  *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
//  *                                                          *
//  *  See the file LICENCE.txt for copying permission.        *
//  ************************************************************ 

using ExcelAllGrade.model;
using ExcelAllGrade.util.extentions;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using Excel = Microsoft.Office.Interop.Excel;

namespace ExcelAllGrade.controller.spreadsheet
{
    /// <summary>
    /// This class is responsible for listing <see cref="ExcelAllGrade.model.Gradelog"/>s.
    /// </summary>
    public class LogtableController
    {
        /// <summary>
        /// Returns or sets the <c>Excel.Application</c> for the specified <see cref="GradelogController"/>.
        /// </summary>
        public Excel.Application ExcelObj { get; set; }
        private WebAdapterProvider webProvider;

        #region constants

        private static readonly Color COLOR_TEST = Color.LightBlue;
        private static readonly Color COLOR_SA = Color.LightCoral;

        private const int COLUMN_DATE = 1;
        private const int COLUMN_STUDENT = 2;
        private const int COLUMN_TYPE = 3;
        private const int COLUMN_GRADING = 4;
        private const int COLUMN_COMMENT = 5;

        #endregion

        /// <summary>
        /// Initializes a new instance of <see cref="LogtableController"/>.
        /// </summary>
        /// <param name="webProvider">The <see cref="ExcelAllGrade.controller.WebAdapterProvider"/> necessary for the connection with the server.</param>
        public LogtableController(WebAdapterProvider webProvider)
        {
            this.webProvider = webProvider;
        }

        /// <summary>
        /// Creates a new <c>Excel.Worksheet</c> filled with <see cref="ExcelAllGrade.model.Gradelog"/>s. This <c>Excel.Worksheet</c> is formatted automatically.
        /// </summary>
        /// <param name="classToken">The class the <see cref="ExcelAllGrade.model.Gradelog"/>s come from.</param>
        /// <param name="subjectToken">The subject the <see cref="ExcelAllGrade.model.Gradelog"/>s come from.</param>
        /// <param name="teacherToken">The teacher that teaches this <paramref name="classToken"/>.</param>
        public void createLogtable(string classToken, string subjectToken, string teacherToken)
        {
            Excel.Workbook wb = ExcelObj.ActiveWorkbook;

            List<Gradelog> gradelogs = webProvider.getGradelogs(classToken, subjectToken, teacherToken);

            Microsoft.Office.Interop.Excel.Sheets sheets = (Microsoft.Office.Interop.Excel.Sheets)wb.Worksheets;
            Microsoft.Office.Interop.Excel.Worksheet sheet;
            sheet = sheets.Add();
            sheet.Name = "LT_" + classToken + "_" + subjectToken;

            try
            {
                this.createOrUpdateLogtable(classToken, subjectToken, teacherToken, sheet, gradelogs);
            }
            catch (Exception)
            {
                ExcelObj.DisplayAlerts = false;
                sheet.Delete();
                ExcelObj.DisplayAlerts = true;

                throw;
            }
        }

        /// <summary>
        /// Purges all content and formatting from the already existing <c>Excel.Worksheet</c> filled with <see cref="ExcelAllGrade.model.Gradelog"/>s and creates a new one.
        /// </summary>
        /// <param name="classToken">The class the <see cref="ExcelAllGrade.model.Gradelog"/>s come from.</param>
        /// <param name="subjectToken">The subject the <see cref="ExcelAllGrade.model.Gradelog"/>s come from.</param>
        /// <param name="teacherToken">The teacher that teaches this <paramref name="classToken"/>.</param>
        public void updateLogtable(string classToken, string subjectToken, string teacherToken)
        {
            Excel.Workbook wb = ExcelObj.ActiveWorkbook;

            List<Gradelog> gradelogs = webProvider.getGradelogs(classToken, subjectToken, teacherToken);

            Microsoft.Office.Interop.Excel.Sheets sheets = (Microsoft.Office.Interop.Excel.Sheets)wb.Worksheets;
            Microsoft.Office.Interop.Excel.Worksheet sheet = sheets.get_Item("LT_" + classToken + "_" + subjectToken);
            sheet.Select();
            Microsoft.Office.Interop.Excel.Range range = (Microsoft.Office.Interop.Excel.Range)sheet.get_Range("A1").EntireRow.EntireColumn;
            range.Clear();

            this.createOrUpdateLogtable(classToken, subjectToken, teacherToken, sheet, gradelogs);
        }

        /// <summary>
        /// This method is called by <see cref="updateLogtable(string, string, string)"/> and <see cref="createLogtable(string, string, string)"/> and is responsible for filling and formatting the cells of the <c>Excel.Worksheet</c>.
        /// </summary>
        /// <param name="classToken">The class the <see cref="ExcelAllGrade.model.Gradelog"/>s come from.</param>
        /// <param name="subjectToken">The subject the <see cref="ExcelAllGrade.model.Gradelog"/>s come from.</param>
        /// <param name="teacherToken">The teacher that teaches this <paramref name="classToken"/>.</param>
        /// <param name="sheet">The <c>Excel.Worksheet</c> to write the <see cref="ExcelAllGrade.model.Gradelog"/>s in.</param>
        /// <param name="gradelogs">All <see cref="ExcelAllGrade.model.Gradelog"/>s from a specific <see cref="ExcelAllGrade.model.Lesson"/>.</param>
        private void createOrUpdateLogtable(string classToken, string subjectToken, string teacherToken, Excel.Worksheet sheet, List<Gradelog> gradelogs)
        {
            this.addHeaders(sheet);

            gradelogs.Sort(
                delegate(Gradelog a, Gradelog b)
                {
                    int diff = a.GradelogTimestamp.CompareTo(b.GradelogTimestamp);

                    return diff;
                }
            );

            for (int i = 0; i < gradelogs.Count; i++)
            {
                addGradelogToLogtable(sheet, gradelogs.ElementAt(i), i + 1);
            }

            Excel.Range headers = sheet.get_Range("A1", "E1");
            headers.Font.Bold = true;
            headers.Borders[Excel.XlBordersIndex.xlEdgeBottom].LineStyle = Excel.XlLineStyle.xlContinuous;
            headers.Borders[Excel.XlBordersIndex.xlEdgeBottom].Weight = Excel.XlBorderWeight.xlThick;

            Excel.Range allCells = sheet.get_Range("A1", "E" + (gradelogs.Count + 1));
            allCells.Columns.AutoFit();
            allCells.Rows.AutoFit();
            allCells.HorizontalAlignment = Excel.XlVAlign.xlVAlignCenter;
        }

        /// <summary>
        /// Adds all headers in the <c>Excel.Worksheet</c>.
        /// </summary>
        /// <param name="sheet">The <c>Excel.Worksheet</c> to create the headers in.</param>
        private void addHeaders(Excel.Worksheet sheet)
        {
            Microsoft.Office.Interop.Excel.Range headerRange = (Microsoft.Office.Interop.Excel.Range)sheet.get_Range("A1", "E1");

            headerRange.Cells[1, COLUMN_DATE].Value = "Date";
            headerRange.Cells[1, COLUMN_STUDENT].Value = "Student";
            headerRange.Cells[1, COLUMN_TYPE].Value = "Type";
            headerRange.Cells[1, COLUMN_GRADING].Value = "Grading";
            headerRange.Cells[1, COLUMN_COMMENT].Value = "Comment";
        }

        /// <summary>
        /// Adds a single <see cref="ExcelAllGrade.model.Gradelog"/> to the <c>Excel.Worksheet</c>.
        /// </summary>
        /// <param name="sheet">The <c>Excel.Worksheet</c> to write the <see cref="ExcelAllGrade.model.Gradelog"/>s in.</param>
        /// <param name="gradelog">The <see cref="ExcelAllGrade.model.Gradelog"/> which should be added to the <c>Excel.Worksheeet</c>.</param>
        /// <param name="index">The index of the <see cref="ExcelAllGrade.model.Gradelog"/>.</param>
        private void addGradelogToLogtable(Excel.Worksheet sheet, Gradelog gradelog, int index)
        {
            Microsoft.Office.Interop.Excel.Range gradelogRange = (Microsoft.Office.Interop.Excel.Range)sheet.get_Range("A" + (index + 1),
                                                            "E" + (index + 1));

            gradelogRange.Cells[1, COLUMN_DATE].Value = gradelog.GradelogTimestamp.ToString();
            gradelogRange.Cells[1, COLUMN_STUDENT].Value = gradelog.GradelogStudent;
            gradelogRange.Cells[1, COLUMN_TYPE].Value = gradelog.GradelogType;
            gradelogRange.Cells[1, COLUMN_GRADING].Value = gradelog.GradelogGrading;
            gradelogRange.Cells[1, COLUMN_COMMENT].Value = gradelog.GradelogComment;

            if (gradelog.GradelogGrading == 5)
            {
                gradelogRange.Cells[1, COLUMN_GRADING].Font.Bold = true;
            }

            if (gradelog.GradelogType.Equals(Gradelog.GradelogTypeEnum.Test.GetStringValue()))
            {
                gradelogRange.Interior.Color = System.Drawing.ColorTranslator.ToOle(COLOR_TEST);
            }
            else if (gradelog.GradelogType.Equals(Gradelog.GradelogTypeEnum.SA.GetStringValue()))
            {
                gradelogRange.Interior.Color = System.Drawing.ColorTranslator.ToOle(COLOR_SA);
            }
            else
            {
                gradelogRange.Interior.Color = System.Drawing.ColorTranslator.ToOle(Color.White);
            }
        }
    }
}
