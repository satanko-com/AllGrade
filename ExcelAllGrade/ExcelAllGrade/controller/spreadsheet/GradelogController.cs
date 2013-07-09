//  ************************************************************
//  *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
//  *                                                          *
//  *  See the file LICENCE.txt for copying permission.        *
//  ************************************************************ 

using ExcelAllGrade.controller.spreadsheet.util;
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
    /// This class is responsible for creating a visual representation of <see cref="ExcelAllGrade.model.Gradelog"/>s.
    /// </summary>
    public class GradelogController
    {
        /// <summary>
        /// Returns or sets the <c>Excel.Application</c> for the specified <see cref="GradelogController"/>.
        /// </summary>
        public Excel.Application ExcelObj { get; set; }
        private WebAdapterProvider webProvider;

        string headerRange = "A";

        #region Colors

        private Color COLOR_TEST = Color.LightBlue;
        private Color COLOR_SA = Color.LightCoral;

        #endregion

        /// <summary>
        /// Initializes a new instance of <see cref="GradelogController"/>.
        /// </summary>
        /// <param name="webProvider">The <see cref="ExcelAllGrade.controller.WebAdapterProvider"/> necessary for the connection with the server.</param>
        public GradelogController(WebAdapterProvider webProvider)
        {
            this.webProvider = webProvider;
        }

        /// <summary>
        /// Creates a new <c>Excel.Worksheet</c> filled with <see cref="ExcelAllGrade.model.Gradelog"/>s. This <c>Excel.Worksheet</c> is formatted automatically.
        /// </summary>
        /// <param name="classToken">The class the <see cref="ExcelAllGrade.model.Gradelog"/>s come from.</param>
        /// <param name="subjectToken">The subject the <see cref="ExcelAllGrade.model.Gradelog"/>s come from.</param>
        /// <param name="teacherToken">The teacher that teaches this <paramref name="classToken"/>.</param>
        public void createWorksheet(string classToken, string subjectToken, string teacherToken)
        {
            Excel.Workbook wb = ExcelObj.ActiveWorkbook;

            List<Student> students = webProvider.getStudentList(classToken, teacherToken);
            List<Gradelog> gradelogs = webProvider.getGradelogs(classToken, subjectToken, teacherToken);

            // Insert a new worksheet
            Microsoft.Office.Interop.Excel.Sheets sheets = (Microsoft.Office.Interop.Excel.Sheets)wb.Worksheets;
            Microsoft.Office.Interop.Excel.Worksheet sheet;
            sheet = sheets.Add();
            sheet.Name = classToken + "_" + subjectToken;

            try
            {
                this.createOrUpdateWorksheet(classToken, subjectToken, teacherToken, sheet, students, gradelogs);
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
        public void updateWorksheet(string classToken, string subjectToken, string teacherToken)
        {
            Excel.Workbook wb = ExcelObj.ActiveWorkbook;

            List<Student> students = webProvider.getStudentList(classToken, teacherToken);
            List<Gradelog> gradelogs = webProvider.getGradelogs(classToken, subjectToken, teacherToken);

            // Select the worksheet and clear it
            Microsoft.Office.Interop.Excel.Sheets sheets = (Microsoft.Office.Interop.Excel.Sheets)wb.Worksheets;
            Microsoft.Office.Interop.Excel.Worksheet sheet = sheets.get_Item(classToken + "_" + subjectToken);
            sheet.Select();
            Microsoft.Office.Interop.Excel.Range range = (Microsoft.Office.Interop.Excel.Range)sheet.get_Range("A1").EntireRow.EntireColumn;
            range.Clear();

            this.createOrUpdateWorksheet(classToken, subjectToken, teacherToken, sheet, students, gradelogs);
        }

        /// <summary>
        /// This method is called by <see cref="updateWorksheet(string, string, string)"/> and <see cref="createWorksheet(string, string, string)"/> and is responsible for filling and formatting the cells of the <c>Excel.Worksheet</c>.
        /// </summary>
        /// <param name="classToken">The class the <see cref="ExcelAllGrade.model.Gradelog"/>s come from.</param>
        /// <param name="subjectToken">The subject the <see cref="ExcelAllGrade.model.Gradelog"/>s come from.</param>
        /// <param name="teacherToken">The teacher that teaches this <paramref name="classToken"/>.</param>
        /// <param name="sheet">The <c>Excel.Worksheet</c> to write the <see cref="ExcelAllGrade.model.Gradelog"/>s in.</param>
        /// <param name="students">All <see cref="ExcelAllGrade.model.Student"/>s from a specific <see cref="ExcelAllGrade.model.Class"/>.</param>
        /// <param name="gradelogs">All <see cref="ExcelAllGrade.model.Gradelog"/>s from a specific <see cref="ExcelAllGrade.model.Lesson"/>.</param>
        private void createOrUpdateWorksheet(string classToken, string subjectToken, string teacherToken, Excel.Worksheet sheet, List<Student> students, List<Gradelog> gradelogs)
        {
            Microsoft.Office.Interop.Excel.Range range = (Microsoft.Office.Interop.Excel.Range)sheet.get_Range("A2",
                                                            "B" + students.Count + 1);
            headerRange = "B";

            // Fill Studentnames
            for (int i = 0; i < students.Count; i++)
            {
                range.set_Item(i + 1, 1, students.ElementAt(i).StudentToken);
                range.set_Item(i + 1, 2, students.ElementAt(i).StudentFirstName + " " + students.ElementAt(i).StudentLastName);
            }

            // Add Gradelogs
            foreach (Gradelog gradelog in gradelogs)
            {
                addGradelogToSpreadsheet(sheet, range, gradelog, students.Count);
            }

            // Merge Headers + Apply Styles
            Excel.Range headers = sheet.get_Range("A1", headerRange + "1");
            ExcelObj.DisplayAlerts = false;
            for (int i = 3; i < SpreadsheetUtil.excelColumnNameToNumber(headerRange); )
            {
                List<Excel.Range> allSameDates = SpreadsheetUtil.findAllDateOccurences(headers, headers.Cells[1, i].Text, headers.Cells[1, i].Comment.Text());

                sheet.Range[sheet.Cells[1, allSameDates.ElementAt(0).Column], sheet.Cells[1, allSameDates.ElementAt(allSameDates.Count - 1).Column]].Merge();

                i += allSameDates.Count;
            }
            ExcelObj.DisplayAlerts = true;
            headers.Orientation = 90;
            headers.HorizontalAlignment = Excel.XlHAlign.xlHAlignCenter;
            headers.VerticalAlignment = Excel.XlVAlign.xlVAlignBottom;

            Excel.Range allCells = sheet.Cells;
            allCells.Columns.ColumnWidth = sheet.StandardWidth;
            allCells.Rows.RowHeight = sheet.StandardHeight;

            Excel.Range usedRange = sheet.UsedRange;
            usedRange.Columns.AutoFit();
            usedRange.Rows.AutoFit();
            headers.Rows.RowHeight = headers.Rows.RowHeight + 7;

            if (!headerRange.Equals("B"))
            {
                Excel.Range gradingCells = sheet.get_Range("C2", headerRange + (students.Count + 1));
                gradingCells.Borders.LineStyle = Excel.XlLineStyle.xlContinuous;
                gradingCells.HorizontalAlignment = Excel.XlHAlign.xlHAlignCenter;
            }
        }

        /// <summary>
        /// Adds a single <see cref="ExcelAllGrade.model.Gradelog"/> to the <c>Excel.Worksheet</c>.
        /// </summary>
        /// <param name="sheet">The <c>Excel.Worksheet</c> to write the <see cref="ExcelAllGrade.model.Gradelog"/>s in.</param>
        /// <param name="range">The <c>Excel.Range</c> in which the <see cref="ExcelAllGrade.model.Gradelog"/> can be written.</param>
        /// <param name="gradelog">The <see cref="ExcelAllGrade.model.Gradelog"/> which should be added to the <c>Excel.Worksheeet</c>.</param>
        /// <param name="studentCount">The amount of <see cref="ExcelAllGrade.model.Student"/>s in the <c>Excel.Worksheet</c>.</param>
        private void addGradelogToSpreadsheet(Excel.Worksheet sheet, Excel.Range range, Gradelog gradelog, int studentCount)
        {
            // Gets all cells with the timestamp and the GradelogType of the gradelog
            List<Excel.Range> dateRangeColumns = SpreadsheetUtil.findAllDateOccurences(sheet.get_Range("A1", headerRange + "1"), gradelog.GradelogTimestamp.Date.ToString().Split(' ')[0], gradelog.GradelogType);
            string comment = "";
            try
            {
                comment = dateRangeColumns.ElementAt(0).Comment.Text();
            }
            catch (ArgumentOutOfRangeException) { }

            // If dateColumn == null or if the comment is different from the gradelog-type, create a new column
            if (dateRangeColumns.Count == 0 || (dateRangeColumns.Count > 0 && !gradelog.GradelogType.Equals(comment) && !String.IsNullOrEmpty(comment)))
            {
                headerRange = headerRange.ExcelIncrementByOne();

                Excel.Range dateColumn = sheet.get_Range(headerRange + "1", headerRange + (studentCount + 1));
                dateColumn.set_Item(1, 1, gradelog.GradelogTimestamp.Date.ToString().Split(' ')[0]);

                setColumnColor(dateColumn, gradelog.GradelogType);
                
                sheet.get_Range(headerRange + "1").AddComment(gradelog.GradelogType);
            }

            // Get the location of the new gradelog-cell
            Excel.Range nameRangeRow = range.Find(gradelog.GradelogStudent);
            string address = SpreadsheetUtil.rangeAddress(nameRangeRow);
            string row = new String(address.Where(Char.IsDigit).ToArray());
            dateRangeColumns = SpreadsheetUtil.findAllDateOccurences(sheet.get_Range("A1", headerRange + "1"), gradelog.GradelogTimestamp.Date.ToString().Split(' ')[0], gradelog.GradelogType);
            address = SpreadsheetUtil.rangeAddress(dateRangeColumns.ElementAt(0));
            string column = new String(address.Where(c => c != '-' && (c < '0' || c > '9')).ToArray());
            Excel.Range newGradingCell = sheet.get_Range(column + row);

            // If a grading already exists, it means that there are 2 or more gradings for that timestamp
            // A new column has to be inserted if all cells in a date-column are already filled
            int index = 0;
            while (!String.IsNullOrWhiteSpace(newGradingCell.Text))
            {
                if (index < dateRangeColumns.Count)
                {
                    address = SpreadsheetUtil.rangeAddress(dateRangeColumns.ElementAt(index));
                    column = new String(address.Where(c => c != '-' && (c < '0' || c > '9')).ToArray());
                    newGradingCell = sheet.get_Range(column + row);
                    index++;
                }
                else
                {
                    Excel.Range columnNew = newGradingCell.EntireColumn;
                    columnNew.Insert(Microsoft.Office.Interop.Excel.XlInsertShiftDirection.xlShiftToRight, false);
                    Excel.Range temporaryRange = sheet.Cells[1, columnNew.Column - 1];
                    temporaryRange.set_Item(1, 1, gradelog.GradelogTimestamp.Date.ToString().Split(' ')[0]);
                    temporaryRange.AddComment(gradelog.GradelogType);

                    setColumnColor(sheet.Range[sheet.Cells[1, temporaryRange.Column], sheet.Cells[studentCount+1, temporaryRange.Column]], gradelog.GradelogType);

                    newGradingCell = sheet.Cells[Convert.ToInt32(row), newGradingCell.Column - 1];

                    headerRange = headerRange.ExcelIncrementByOne();
                }
            }

            // Sets the content of the cell
            newGradingCell.set_Item(1, 1, gradelog.GradelogGrading);
            if (gradelog.GradelogGrading == 5)
            {
                newGradingCell.Font.Bold = true;
            }
            if (!String.IsNullOrEmpty(gradelog.GradelogComment))
            {
                newGradingCell.AddComment(gradelog.GradelogComment);
            }
        }

        /// <summary>
        /// Sets the background-color of a specified <c>Excel.Range</c> depending on the <paramref name="gradelogType"/>.
        /// </summary>
        /// <param name="column">The <c>Excel.Range</c> which should be changed.</param>
        /// <param name="gradelogType">The type of the <see cref="ExcelAllGrade.model.Gradelog"/>.</param>
        private void setColumnColor(Excel.Range column, string gradelogType)
        {
            if (gradelogType.Equals(Gradelog.GradelogTypeEnum.Test.GetStringValue()))
            {
                column.Interior.Color = System.Drawing.ColorTranslator.ToOle(COLOR_TEST);
            }
            else if (gradelogType.Equals(Gradelog.GradelogTypeEnum.SA.GetStringValue()))
            {
                column.Interior.Color = System.Drawing.ColorTranslator.ToOle(COLOR_SA);
            }
            else
            {
                column.Interior.Color = System.Drawing.ColorTranslator.ToOle(Color.White);
            }
        }
    }
}
