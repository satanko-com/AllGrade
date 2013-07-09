//  ************************************************************
//  *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
//  *                                                          *
//  *  See the file LICENCE.txt for copying permission.        *
//  ************************************************************ 

using ExcelAllGrade.controller;
using ExcelAllGrade.controller.spreadsheet;
using ExcelAllGrade.model;
using ExcelAllGrade.util.extentions;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Linq;
using System.Net;
using System.Reflection;
using System.Threading;
using System.Windows.Forms;
using Excel = Microsoft.Office.Interop.Excel;
using ExcelAllGrade.controller.spreadsheet.util;

namespace ExcelAllGrade.view
{
    /// <summary>
    /// This class is a dialog which lets the user create Gradelog- and Logtable-Worksheets.
    /// </summary>
    public partial class CreateSpreadsheetDialog : Form
    {
        #region constants

        private const int startingPadding = 12;
        private const int widthGroupBox = 280;

        private const int paddingTop = 19;
        private const int buttonHeight = 23;
        private const int buttonWidth = 90;
        private const int spacingButtons = 3;
        private const int paddingBot = 12;
        private const int differenceButtonLabel = 5;
        private const int xLabel = 6;
        private const int xButton = 100;
        private const int xGroupBox = 12;

        private const int spacingGroupBox = 6;

        #endregion

        private WebAdapterProvider webProvider = null;
        private List<Class> lessons = null;
        private VariableLoader vLoader;
        private AutoResetEvent eVariablesLoaded;
        private Thread loadingThread;
        private string teacher;

        private GradelogController gradelogController;
        private LogtableController logtableController;

        private bool hadException = true;

        /// <summary>
        /// Initializes a new instance of <see cref="CreateSpreadsheetDialog"/>.
        /// </summary>
        /// <param name="webProvider">The <see cref="ExcelAllGrade.controller.WebAdapterProvider"/> necessary for the connection with the server.</param>
        /// <param name="vLoader">The <see cref="ExcelAllGrade.controller.VariableLoader"/> to load the variables.</param>
        /// <param name="eVariablesLoaded">The <see cref="System.Threading.AutoResetEvent"/> to indicate when <see cref="ExcelAllGrade.controller.VariableLoader"/> is done loading.</param>
        /// <param name="teacher">The <c>teacher-token</c>.</param>
        /// <param name="gradelogController">The <see cref="ExcelAllGrade.controller.spreadsheet.GradelogController"/> of this <see cref="CreateSpreadsheetDialog"/>.</param>
        /// <param name="logtableController">The <see cref="ExcelAllGrade.controller.spreadsheet.LogtableController"/> of this <see cref="CreateSpreadsheetDialog"/>.</param>
        public CreateSpreadsheetDialog(WebAdapterProvider webProvider, GradelogController gradelogController, LogtableController logtableController, VariableLoader vLoader, AutoResetEvent eVariablesLoaded, string teacher)
        {
            InitializeComponent();

            this.eVariablesLoaded = eVariablesLoaded;
            this.webProvider = webProvider;
            this.vLoader = vLoader;
            this.teacher = teacher;

            this.gradelogController = gradelogController;
            this.logtableController = logtableController;

            loadingThread = new Thread(new ThreadStart(initializeVariables));
        }

        private void onLoad(object sender, EventArgs e)
        {
            this.pbRetrievingData.Style = ProgressBarStyle.Marquee;
            this.lbRetrievingData.Text = "Retrieving Data...";

            if (hadException || vLoader.LResult == LoaderResult.ThrewException)
            {
                hadException = false;

                if (loadingThread.ThreadState == ThreadState.Unstarted)
                {
                    loadingThread.Start();
                }
                else if (vLoader.LResult == LoaderResult.ThrewException || loadingThread.ThreadState == ThreadState.Stopped)
                {
                    loadingThread = new Thread(new ThreadStart(initializeVariables));
                    loadingThread.Start();
                }
            }
            else if (vLoader.LResult == LoaderResult.FinishedSuccessfully)
            {
                foreach (Class newClass in lessons)
                {
                    foreach (Lesson lesson in newClass.Lessons)
                    {
                        this.setButtonTextAndListener(newClass.Name, lesson.Subject.Token, ButtonUpdateAction.buttonGradelog);
                        this.setButtonTextAndListener(newClass.Name, lesson.Subject.Token, ButtonUpdateAction.buttonLogtable);
                    }
                }
            }
        }

        /// <summary>
        /// Sets the <see cref="ExcelAllGrade.model.Lesson"/>s for this form.
        /// </summary>
        /// <param name="lessons">The <see cref="ExcelAllGrade.model.Lesson"/>s to set.</param>
        public void setLessons(List<Class> lessons)
        {
            try
            {
                this.lessons = lessons;

                this.initializeForm();
            }
            catch (NullReferenceException)
            { }
        }

        private void initializeVariables()
        {
            if (vLoader.LResult == LoaderResult.InitializationRunning)
            {
                eVariablesLoaded.WaitOne();
            }
            else if (vLoader.LResult == LoaderResult.NotStarted || vLoader.LResult == LoaderResult.ThrewException)
            {
                vLoader.startInitialization();

                eVariablesLoaded.Set();
            }

            if (lessons != null)
            {
                this.initializeForm();
            }
            else if (vLoader.LResult == LoaderResult.FinishedSuccessfully)
            {
                lessons = this.jsonLessonsToClassList(vLoader.JsonLessons);

                this.initializeForm();
            }
            else if (vLoader.LResult == LoaderResult.ThrewException)
            {
                this.pbRetrievingData.InvokeIfRequired(() => { this.pbRetrievingData.Style = ProgressBarStyle.Continuous; });
                this.lbRetrievingData.InvokeIfRequired(() => { this.lbRetrievingData.Text = "Error"; });

                string message = vLoader.Exception.Message;
                WebExceptionStatus status = vLoader.Exception.Status;

                MessageBox.Show(status.ToString() + " : " + message, "Create Worksheets", MessageBoxButtons.OK, MessageBoxIcon.Error);

                hadException = true;
            }
        }

        private void setButtonTextAndListener(string className, string subjectToken, ButtonUpdateAction bua, Button button = null)
        {
            Button control;
            if (button == null)
            {
                if (bua == ButtonUpdateAction.buttonGradelog)
                {
                    control = (Button)this.Controls.Find("btG_" + className + "_" + subjectToken, true)[0];
                }
                else
                {
                    control = (Button)this.Controls.Find("btL_" + className + "_" + subjectToken, true)[0];
                }
            }
            else
            {
                control = button;
            }

            if(bua == ButtonUpdateAction.buttonGradelog)
            {
                control.InvokeIfRequired(() =>
                    {
                        if (SpreadsheetUtil.worksheetExists(className + "_" + subjectToken, gradelogController.ExcelObj))
                        {
                            control.Text = "Update Table";
                            this.RemoveClickEvents(control);
                            control.Click += new EventHandler(onUpdateGradelogSpreadsheet);
                        }
                        else
                        {
                            control.Text = "Create Table";
                            this.RemoveClickEvents(control);
                            control.Click += new EventHandler(onCreateGradelogSpreadsheet);
                        }
                    }
                );
            }
            else if(bua == ButtonUpdateAction.buttonLogtable)
            {
                control.InvokeIfRequired(() =>
                    {
                        if (SpreadsheetUtil.worksheetExists("LT_" + className + "_" + subjectToken, gradelogController.ExcelObj))
                        {
                            control.Text = "Update Log";
                            this.RemoveClickEvents(control);
                            control.Click += new EventHandler(onUpdateLogtableSpreadsheet);
                        }
                        else
                        {
                            control.Text = "Create Log";
                            this.RemoveClickEvents(control);
                            control.Click += new EventHandler(onCreateLogtableSpreadsheet);
                        }
                    }
                );
            }
        }

        private void initializeForm()
        {
            this.splitContainer.InvokeIfRequired(() =>
                {
                    this.splitContainer.Panel1.Controls.Clear();
                    this.splitContainer.SplitterDistance = this.Size.Height - 50;
                }
            );

            int locationYGroupBox = startingPadding;

            foreach (Class newClass in lessons)
            {
                int height = 0;

                GroupBox groupBox = new GroupBox();
                groupBox.InvokeIfRequired(() =>
                    {
                        groupBox.Name = "gb_" + newClass.Name;
                        groupBox.Text = newClass.Name.ToUpper();
                        groupBox.Location = new Point(xGroupBox, locationYGroupBox);

                        height = paddingTop + newClass.Lessons.Count * buttonHeight + newClass.Lessons.Count * spacingButtons + paddingBot;

                        groupBox.Size = new Size(widthGroupBox, height);
                    }
                );

                int locationYInnerControls = paddingTop;

                foreach (Lesson lesson in newClass.Lessons)
                {
                    Label label = new Label();
                    Button buttonSpreadsheet = new Button();
                    Button buttonLogtable = new Button();

                    label.InvokeIfRequired(() =>
                        {
                            label.Name = "lb_" + newClass.Name + "_" + lesson.Subject.Token;
                            label.Text = lesson.Subject.Token;
                            label.Location = new Point(xLabel, locationYInnerControls + differenceButtonLabel);
                            label.Width = 50;
                        }
                    );

                    buttonSpreadsheet.InvokeIfRequired(() =>
                        {
                            buttonSpreadsheet.Name = "btG_" + newClass.Name + "_" + lesson.Subject.Token;
                            buttonSpreadsheet.Location = new Point(xButton, locationYInnerControls);
                            buttonSpreadsheet.Size = new Size(buttonWidth, buttonHeight);

                            this.setButtonTextAndListener(newClass.Name, lesson.Subject.Token, ButtonUpdateAction.buttonGradelog, buttonSpreadsheet);
                        }
                    );

                    buttonLogtable.InvokeIfRequired(() =>
                        {
                            buttonLogtable.Name = "btL_" + newClass.Name + "_" + lesson.Subject.Token;
                            buttonLogtable.Location = new Point(xButton + buttonWidth + spacingButtons, locationYInnerControls);
                            buttonLogtable.ForeColor = Color.Gray;

                            this.setButtonTextAndListener(newClass.Name, lesson.Subject.Token, ButtonUpdateAction.buttonLogtable, buttonLogtable);
                        }
                    );

                    groupBox.Controls.Add(label);
                    groupBox.Controls.Add(buttonSpreadsheet);
                    groupBox.Controls.Add(buttonLogtable);

                    locationYInnerControls = locationYInnerControls + buttonHeight + spacingButtons;
                }

                locationYGroupBox = locationYGroupBox + height + spacingGroupBox;

                this.splitContainer.InvokeIfRequired(() =>
                    {
                        this.splitContainer.Panel1.Controls.Add(groupBox);
                    }
                );

                this.InvokeIfRequired(() => { this.Size = new Size(325, 580); });
                this.splitContainer.InvokeIfRequired(() =>
                    {
                        this.splitContainer.SplitterDistance = (this.Size.Height / 100) * 105;
                        this.splitContainer.Panel1.Focus();
                    }
                );
            }

            if (lessons.Count == 0)
            {
                Label label = new Label();
                label.Text = "You don't teach any classes.";
                label.Size = new Size(300, 13);

                label.Location = new Point(12, 9);
                this.splitContainer.InvokeIfRequired(() =>
                    {
                        this.splitContainer.Panel1.Controls.Add(label);
                    }
                );
            }

            this.InvokeIfRequired(() => { this.CenterToScreen(); });
        }

        private void RemoveClickEvents(Button b)
        {
            FieldInfo f1 = typeof(Control).GetField("EventClick",
                BindingFlags.Static | BindingFlags.NonPublic);
            object obj = f1.GetValue(b);
            PropertyInfo pi = b.GetType().GetProperty("Events",
                BindingFlags.NonPublic | BindingFlags.Instance);
            EventHandlerList list = (EventHandlerList)pi.GetValue(b, null);
            list.RemoveHandler(obj, list[obj]);
        }

        private void onCreateGradelogSpreadsheet(object sender, EventArgs e)
        {
            this.Cursor = Cursors.WaitCursor;

            string[] buttonName = ((Button)sender).Name.Split('_');

            try
            {
                ((Button)sender).Text = "Working...";
                ((Button)sender).Enabled = false;

                gradelogController.createWorksheet(buttonName[1], buttonName[2], teacher);

                ((Button)sender).Text = "Update Table";
                this.RemoveClickEvents((Button)sender);
                ((Button)sender).Click += new EventHandler(onUpdateGradelogSpreadsheet);
            }
            catch (Exception ex)
            {
                catchExceptionWhileCreating(ex, "Error while creating worksheet");

                ((Button)sender).Text = "Create Table";
            }
            finally
            {
                ((Button)sender).Enabled = true;
                this.Cursor = Cursors.Default;
            }
        }

        private void onUpdateGradelogSpreadsheet(object sender, EventArgs e)
        {
            this.Cursor = Cursors.WaitCursor;

            string[] buttonName = ((Button)sender).Name.Split('_');

            try
            {
                ((Button)sender).Text = "Working...";
                ((Button)sender).Enabled = false;

                gradelogController.updateWorksheet(buttonName[1], buttonName[2], teacher);
            }
            catch (Exception ex)
            {
                catchExceptionWhileCreating(ex, "Error while updating worksheet");
            }
            finally
            {
                ((Button)sender).Text = "Update Table";
                ((Button)sender).Enabled = true;
                this.Cursor = Cursors.Default;
            }
        }

        private void onCreateLogtableSpreadsheet(object sender, EventArgs e)
        {
            this.Cursor = Cursors.WaitCursor;

            string[] buttonName = ((Button)sender).Name.Split('_');

            try
            {
                ((Button)sender).Text = "Working...";
                ((Button)sender).Enabled = false;

                logtableController.createLogtable(buttonName[1], buttonName[2], teacher);

                ((Button)sender).Text = "Update Log";
                this.RemoveClickEvents((Button)sender);
                ((Button)sender).Click += new EventHandler(onUpdateLogtableSpreadsheet);
            }
            catch (Exception ex)
            {
                catchExceptionWhileCreating(ex, "Error while creating worksheet");

                ((Button)sender).Text = "Create Log";
            }
            finally
            {
                ((Button)sender).Enabled = true;
                this.Cursor = Cursors.Default;
            }
        }

        private void onUpdateLogtableSpreadsheet(object sender, EventArgs e)
        {
            this.Cursor = Cursors.WaitCursor;

            string[] buttonName = ((Button)sender).Name.Split('_');

            try
            {
                ((Button)sender).Text = "Working...";
                ((Button)sender).Enabled = false;

                logtableController.updateLogtable(buttonName[1], buttonName[2], teacher);
            }
            catch (Exception ex)
            {
                catchExceptionWhileCreating(ex, "Error while updating worksheet");
            }
            finally
            {
                ((Button)sender).Text = "Update Log";
                ((Button)sender).Enabled = true;
                this.Cursor = Cursors.Default;
            }
        }

        private void catchExceptionWhileCreating(Exception ex, string textToDisplay)
        {
            string message = ex.Message;

            if (ex is WebException)
            {
                WebExceptionStatus status = ((WebException)ex).Status;

                MessageBox.Show(status.ToString() + " : " + message, textToDisplay, MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
            else
            {
                MessageBox.Show(message, textToDisplay, MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private List<Class> jsonLessonsToClassList(List<JsonLesson> jsonLessons)
        {
            List<Class> newLessons = new List<Class>();

            foreach (JsonLesson jsonLesson in jsonLessons)
            {
                if (!newLessons.Any(p => p.Name.Equals(jsonLesson.ClassToken)))
                {
                    Lesson lesson = new Lesson(jsonLesson.Teachers, new Subject(jsonLesson.SubjectToken));
                    Class newClass = new Class(jsonLesson.ClassToken);
                    newClass.addLesson(lesson);
                    newLessons.Add(newClass);
                }
                else
                {
                    int index = newLessons.FindIndex(p => p.Name.Equals(jsonLesson.ClassToken));
                    Lesson lesson = new Lesson(jsonLesson.Teachers, new Subject(jsonLesson.SubjectToken));
                    newLessons.ElementAt(index).addLesson(lesson);
                }
            }

            return newLessons;
        }
    }

    enum ButtonUpdateAction
    {
        buttonGradelog = 0,
        buttonLogtable = 1
    }
}
