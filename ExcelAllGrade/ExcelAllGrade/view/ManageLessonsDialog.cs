//  ************************************************************
//  *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
//  *                                                          *
//  *  See the file LICENCE.txt for copying permission.        *
//  ************************************************************ 

using ExcelAllGrade.controller;
using ExcelAllGrade.model;
using ExcelAllGrade.util.extentions;
using ExcelAllGrade.util.serialization;
using ExcelAllGrade.web.json;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Net;
using System.Threading;
using System.Windows.Forms;

namespace ExcelAllGrade.view
{
    /// <summary>
    /// This class is a dialog which lets the user manage <see cref="ExcelAllGrade.model.Lesson"/>s.
    /// </summary>
    public partial class ManageLessonsDialog : Form
    {
        private WebAdapterProvider webProvider;
        private VariableLoader vLoader;
        private AutoResetEvent eVariablesLoaded;
        private Thread loadingThread;

        private List<Class> classesWithLessons = new List<Class>();
        private List<Class> oldClasses = new List<Class>();
        private List<JsonLesson> jsonLessons;
        private List<Class> classes;
        private List<Subject> subjects;

        private bool hadException = true;

        private string teacher;

        /// <summary>
        /// Initializes a new instance of <see cref="CreateSpreadsheetDialog"/>.
        /// </summary>
        /// <param name="webProvider">The <see cref="ExcelAllGrade.controller.WebAdapterProvider"/> necessary for the connection with the server.</param>
        /// <param name="vLoader">The <see cref="ExcelAllGrade.controller.VariableLoader"/> to load the variables.</param>
        /// <param name="eVariablesLoaded">The <see cref="System.Threading.AutoResetEvent"/> to indicate when <see cref="ExcelAllGrade.controller.VariableLoader"/> is done loading.</param>
        /// <param name="teacher">The <c>teacher-token</c>.</param>
        public ManageLessonsDialog(string teacher, WebAdapterProvider webProvider, VariableLoader vLoader, AutoResetEvent eVariablesLoaded)
        {
            InitializeComponent();

            this.teacher = teacher;
            this.webProvider = webProvider;
            this.vLoader = vLoader;
            this.eVariablesLoaded = eVariablesLoaded;

            loadingThread = new Thread(new ThreadStart(initializeVariables));
        }

        private void onLoad(object sender, EventArgs e)
        {
            if (hadException || vLoader.LResult == LoaderResult.ThrewException)
            {
                this.lbClasses.ItemCheck -= new ItemCheckEventHandler(onClassesCheckstateChanged);
                this.lbClasses.SelectedIndexChanged -= new EventHandler(onSelectedClassChanged);
                this.lbSubjects.ItemCheck -= new ItemCheckEventHandler(onSubjectsCheckstateChanged);

                btSaveChanges.Enabled = false;
                lbClasses.Items.Clear();
                lbSubjects.Items.Clear();
                lbClasses.Items.Add("Loading...", CheckState.Indeterminate);
                lbSubjects.Items.Add("Loading...", CheckState.Indeterminate);
                lbClasses.SelectionMode = SelectionMode.None;
                lbSubjects.SelectionMode = SelectionMode.None;

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

            if (vLoader.LResult == LoaderResult.FinishedSuccessfully)
            {
                subjects = vLoader.Subjects;
                classes = vLoader.Classes;
                jsonLessons = vLoader.JsonLessons;

                this.initializeVariablesDone();
            }
            else if (vLoader.LResult == LoaderResult.ThrewException)
            {
                this.catchWebException(vLoader.Exception);
            }
        }

        private void catchWebException(WebException ex)
        {
            lbClasses.InvokeIfRequired(() =>
                {
                    lbClasses.Items.Clear();
                    lbClasses.Items.Add("Error", CheckState.Indeterminate);
                    lbClasses.SelectionMode = SelectionMode.None;

                    this.lbClasses.ItemCheck -= new ItemCheckEventHandler(onClassesCheckstateChanged);
                    this.lbClasses.SelectedIndexChanged -= new EventHandler(onSelectedClassChanged);
                }
            );
            lbSubjects.InvokeIfRequired(() =>
                {
                    lbSubjects.Items.Clear();
                    lbSubjects.Items.Add("Error", CheckState.Indeterminate);
                    lbSubjects.SelectionMode = SelectionMode.None;

                    this.lbSubjects.ItemCheck -= new ItemCheckEventHandler(onSubjectsCheckstateChanged);
                }
            );
            

            string message = ex.Message;
            WebExceptionStatus status = ex.Status;

            MessageBox.Show(status.ToString() + " : " + message, "Manage Lessons", MessageBoxButtons.OK, MessageBoxIcon.Error);

            hadException = true;
        }

        private void initializeVariablesDone()
        {
            if (hadException)
            {
                return;
            }

            this.lbClasses.ItemCheck -= new ItemCheckEventHandler(onClassesCheckstateChanged);
            this.lbClasses.SelectedIndexChanged -= new EventHandler(onSelectedClassChanged);
            this.lbSubjects.ItemCheck -= new ItemCheckEventHandler(onSubjectsCheckstateChanged);

            foreach (JsonLesson jsonLesson in jsonLessons)
            {
                if (!classesWithLessons.Any(p => p.Name.Equals(jsonLesson.ClassToken)))
                {
                    Lesson lesson = new Lesson(jsonLesson.Teachers, new Subject(jsonLesson.SubjectToken));
                    Class newClass = new Class(jsonLesson.ClassToken);
                    newClass.addLesson(lesson);
                    classesWithLessons.Add(newClass);
                }
                else
                {
                    int index = classesWithLessons.FindIndex(p => p.Name.Equals(jsonLesson.ClassToken));
                    Lesson lesson = new Lesson(jsonLesson.Teachers, new Subject(jsonLesson.SubjectToken));
                    classesWithLessons.ElementAt(index).addLesson(lesson);
                }
            }

            oldClasses = (List<Class>)ObjectCloner.DeepClone(classesWithLessons);

            lbClasses.InvokeIfRequired(() => { lbClasses.Items.Clear(); });
            lbSubjects.InvokeIfRequired(() => { lbSubjects.Items.Clear(); });

            foreach (Class newClass in classes)
            {
                if (classesWithLessons.Any(p => p.Name.Equals(newClass.Name)))
                    lbClasses.InvokeIfRequired(() => { lbClasses.Items.Add(newClass.Name, CheckState.Checked); });
                else
                    lbClasses.InvokeIfRequired(() => { lbClasses.Items.Add(newClass.Name, CheckState.Unchecked); });
            }

            foreach (Subject subject in subjects)
            {
                if (classesWithLessons.Any(p => p.Name.Equals(classes.ElementAt(0).Name) && p.Lessons.Any(q => q.Subject.Token.Equals(subject.Token))))
                    lbSubjects.InvokeIfRequired(() => { lbSubjects.Items.Add(subject.Token + " - " + subject.Name, CheckState.Checked); });
                else
                    lbSubjects.InvokeIfRequired(() => { lbSubjects.Items.Add(subject.Token + " - " + subject.Name, CheckState.Unchecked); });
            }

            lbClasses.InvokeIfRequired(() => { lbClasses.SelectionMode = SelectionMode.One; });
            lbSubjects.InvokeIfRequired(() => { lbSubjects.SelectionMode = SelectionMode.One; });

            lbClasses.InvokeIfRequired(() => { lbClasses.SelectedIndex = 0; });

            btSaveChanges.InvokeIfRequired(() => { btSaveChanges.Enabled = true; });

            lbClasses.InvokeIfRequired(() =>
                {
                    lbClasses.ItemCheck += new ItemCheckEventHandler(onClassesCheckstateChanged);
                    lbClasses.SelectedIndexChanged += new EventHandler(onSelectedClassChanged);
                }
            );
            lbSubjects.InvokeIfRequired(() => { lbSubjects.ItemCheck += new ItemCheckEventHandler(onSubjectsCheckstateChanged); });
        }

        private void onSelectedClassChanged(object sender, EventArgs e)
        {
            int index = lbClasses.SelectedIndex;

            if (index < 0)
                return;

            foreach (Subject subject in subjects)
            {
                try
                {
                    lbSubjects.ItemCheck -= new ItemCheckEventHandler(this.onSubjectsCheckstateChanged);

                    if (classesWithLessons.Any(p => p.Name.Equals(classes.ElementAt(index).Name) && p.Lessons.Any(q => q.Subject.Token.Equals(subject.Token))))
                        lbSubjects.SetItemCheckState(lbSubjects.Items.IndexOf(subject.Token + " - " + subject.Name), CheckState.Checked);
                    else
                        lbSubjects.SetItemCheckState(lbSubjects.Items.IndexOf(subject.Token + " - " + subject.Name), CheckState.Unchecked);
                }
                catch (NullReferenceException)
                {
                    lbSubjects.SetItemCheckState(lbSubjects.Items.IndexOf(subject.Token + " - " + subject.Name), CheckState.Unchecked);
                }
                finally
                {
                    lbSubjects.ItemCheck += new ItemCheckEventHandler(this.onSubjectsCheckstateChanged);
                }
            }
        }

        private void onOk(object sender, EventArgs e)
        {
            this.Cursor = Cursors.WaitCursor;
            this.btSaveChanges.Enabled = false;

            string serializedJsonLessons = JsonBuilder.serializeChangedLessons(oldClasses, classesWithLessons);
            oldClasses = (List<Class>)ObjectCloner.DeepClone(classesWithLessons);

            try
            {
                switch (webProvider.postLessonList(serializedJsonLessons))
                {
                    case System.Net.HttpStatusCode.OK:
                        MessageBox.Show("Changes saved successfully!", "Success", MessageBoxButtons.OK, MessageBoxIcon.Information);
                        break;
                    case System.Net.HttpStatusCode.InternalServerError:
                        MessageBox.Show("A database-error has occurred!", "Error while updating lessons", MessageBoxButtons.OK, MessageBoxIcon.Error);
                        break;
                    case System.Net.HttpStatusCode.BadRequest:
                        MessageBox.Show("The provided JSON string was invalid", "Error while updating lessons", MessageBoxButtons.OK, MessageBoxIcon.Error);
                        break;
                }

                this.btSaveChanges.Enabled = true;
            }
            catch (WebException ex)
            {
                this.catchWebException(ex);
            }
            finally
            {
                this.Cursor = Cursors.Default;
            }
        }

        private void onSubjectsCheckstateChanged(object sender, ItemCheckEventArgs e)
        {
            int indexClasses = lbClasses.SelectedIndex;
            int indexSubject = e.Index;
            CheckState isChecked = e.NewValue;

            if (indexClasses < 0 || indexSubject < 0)
                return;

            lbClasses.ItemCheck -= new ItemCheckEventHandler(this.onClassesCheckstateChanged);

            if (classesWithLessons.Any(p => p.Name.Equals(classes.ElementAt(indexClasses).Name)))
            {
                if (isChecked == CheckState.Unchecked)
                {
                    int indexClassInLessons = classesWithLessons.FindIndex(p => p.Name.Equals(classes.ElementAt(indexClasses).Name));
                    int indexSubjectInLesson = classesWithLessons.ElementAt(indexClassInLessons).Lessons.FindIndex(p => p.Subject.Token.Equals(subjects.ElementAt(indexSubject).Token));

                    if (indexClassInLessons < 0 || indexSubjectInLesson < 0)
                        return;

                    classesWithLessons.ElementAt(indexClassInLessons).Lessons.RemoveAt(indexSubjectInLesson);
                    if (classesWithLessons.ElementAt(indexClassInLessons).Lessons.Count == 0)
                    {
                        lbClasses.SetItemChecked(indexClasses, false);
                        classesWithLessons.RemoveAt(indexClassInLessons);
                    }
                }
                else if (isChecked == CheckState.Checked)
                {
                    int indexClassInLessons = classesWithLessons.FindIndex(p => p.Name.Equals(classes.ElementAt(indexClasses).Name));

                    classesWithLessons.ElementAt(indexClassInLessons).addLesson(new Lesson(teacher, subjects.ElementAt(indexSubject)));

                    lbClasses.SetItemChecked(indexClasses, true);
                }
            }
            else
            {
                if (isChecked == CheckState.Checked)
                {
                    Class newClass = new Class(classes.ElementAt(indexClasses).Name);
                    newClass.addLesson(new Lesson(teacher, subjects.ElementAt(indexSubject)));
                    classesWithLessons.Add(newClass);

                    lbClasses.SetItemChecked(indexClasses, true);
                }
            }

            lbClasses.ItemCheck += new ItemCheckEventHandler(this.onClassesCheckstateChanged);
        }

        private void onClassesCheckstateChanged(object sender, ItemCheckEventArgs e)
        {
            int indexClasses = lbClasses.SelectedIndex;

            if (indexClasses < 0)
                return;

            lbSubjects.ItemCheck -= new ItemCheckEventHandler(this.onSubjectsCheckstateChanged);

            if (e.NewValue == CheckState.Checked)
            {
                e.NewValue = e.CurrentValue;
            }
            else if (e.NewValue == CheckState.Unchecked)
            {
                int indexClassInLessons = classesWithLessons.FindIndex(p => p.Name.Equals(classes.ElementAt(indexClasses).Name));

                if (indexClassInLessons < 0)
                    return;

                classesWithLessons.RemoveAt(indexClassInLessons);

                for (int i = 0; i < lbSubjects.Items.Count; i++)
                {
                    lbSubjects.SetItemChecked(i, false);
                }
            }

            lbSubjects.ItemCheck += new ItemCheckEventHandler(this.onSubjectsCheckstateChanged);
        }

        /// <summary>
        /// Gets all <see cref="ExcelAllGrade.model.Lesson"/>s this <see cref="ExcelAllGrade.model.Teacher"/> teaches.
        /// </summary>
        /// <returns>A <see cref="List{T}"/> with all current <see cref="ExcelAllGrade.model.Lesson"/>s.
        /// <para><c>null</c> if the class previously encountered an exception.</para>
        /// </returns>
        public List<Class> getLessons()
        {
            if (hadException || vLoader.LResult == LoaderResult.ThrewException)
            {
                return null;
            }
            else
            {
                return this.classesWithLessons;
            }
        }
    }
}
