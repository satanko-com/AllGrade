//  ************************************************************
//  *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
//  *                                                          *
//  *  See the file LICENCE.txt for copying permission.        *
//  ************************************************************ 

using ExcelAllGrade.controller;
using ExcelAllGrade.controller.spreadsheet;
using ExcelAllGrade.model;
using ExcelAllGrade.util;
using ExcelAllGrade.util.extentions;
using ExcelAllGrade.view;
using Microsoft.Office.Tools.Ribbon;
using System;
using System.Collections.Generic;
using System.Net;
using System.Runtime.InteropServices;
using System.Threading;
using System.Windows.Forms;
using Excel = Microsoft.Office.Interop.Excel;

namespace ExcelAllGrade
{
    /// <summary>
    /// This is the main ribbon control.
    /// </summary>
    public partial class MenuBar
    {
        [DllImport("User32.dll")]
        private static extern Int32 SetForegroundWindow(int hWnd);

        private UserController uController;
        private WebAdapterProvider webProvider = new WebAdapterProvider();
        private GradelogController glController;
        private LogtableController ltController;
        private List<Class> lessons = null;
        private VariableLoader vLoader;
        private string teacher = null;

        private Excel.Application ExcelObj;

        AutoResetEvent eOAuthDone = new AutoResetEvent(false);
        Thread oAuthThread;
        ManageLessonsDialog mlDialog = null;
        CreateSpreadsheetDialog csDialog = null;

        AutoResetEvent eVariablesLoaded = new AutoResetEvent(false);

        private void MenuBar_Load(object sender, RibbonUIEventArgs e)
        {
            glController = new GradelogController(webProvider);
            ltController = new LogtableController(webProvider);

            lbStatus2.Label = "Not Connected";

            uController = new UserController(eOAuthDone);

            if (!String.IsNullOrEmpty(Properties.Settings.Default.Teacher))
            {
                this.setLoggedIn();
            }

            WebProxy proxy = new WebProxy();
            if (Properties.Settings.Default.UseProxy)
            {
                Uri newUri = new Uri(Properties.Settings.Default.Address);
                newUri = newUri.SetPort(Properties.Settings.Default.Port);

                proxy.Address = newUri;
                proxy.Credentials = new NetworkCredential(Properties.Settings.Default.Username, PasswordCryptography.decryptString(Properties.Settings.Default.Password));
                webProvider.setProxy(proxy);
                uController.setProxy(proxy);
            }
        }

        private void onManageLessons(object sender, RibbonControlEventArgs e)
        {
            mlDialog.ShowDialog();

            this.lessons = mlDialog.getLessons();
        }

        private void onLogOut(object sender, RibbonControlEventArgs e)
        {
            Properties.Settings.Default.Teacher = "";
            Properties.Settings.Default.Save();
            teacher = null;

            btLogInOut.Label = "Log In";
            btLogInOut.Image = Properties.Resources.ico_login;
            btLogInOut.Click -= new RibbonControlEventHandler(onLogOut);
            btLogInOut.Click += new RibbonControlEventHandler(onLogIn);

            lbStatus2.Label = "Not Connected";
            lbUser2.Label = "";

            btManageLessons.Enabled = false;
            btCreateSpreadsheets.Enabled = false;
            btUpdateWorksheet.Enabled = false;
            btDeleteWorksheet.Enabled = false;
        }

        private void onLogIn(object sender, RibbonControlEventArgs e)
        {
            ThreadStart ts = new ThreadStart(executeOAuth);
            try
            {
                if (oAuthThread.ThreadState == ThreadState.WaitSleepJoin)
                {
                    oAuthThread.Abort();
                    oAuthThread = new Thread(ts);
                    oAuthThread.Start();
                }
                else if (oAuthThread.ThreadState == ThreadState.Running)
                {
                    MessageBox.Show("The program is processing your Login, please wait!", "Information", MessageBoxButtons.OK, MessageBoxIcon.Exclamation);
                }
                else
                {
                    oAuthThread = new Thread(ts);
                    oAuthThread.Start();
                }
            }
            catch (NullReferenceException)
            {
                oAuthThread = new Thread(ts);
                oAuthThread.Start();
            }
        }

        private void executeOAuth()
        {
            eOAuthDone.Reset();

            lbStatus2.Label = "Retrieving Mail...";
            lbUser2.Label = "";

            try
            {
                uController.doOAuth(UserController.INITIAL_REQUEST);

                eOAuthDone.WaitOne();

                if (uController.wasSuccessful())
                {
                    this.setLoggedIn();
                }
                else
                {
                    lbStatus2.Label = "Not Successful";
                }
            }
            catch (ThreadAbortException)
            { }
            catch (Exception e)
            {
                lbStatus2.Label = "Error";
                MessageBox.Show("An error occurred! \n" + e.Message, "Error while logging in", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private void setLoggedIn()
        {
            lbStatus2.Label = "Logged In";
            lbUser2.Label = Properties.Settings.Default.Teacher.ToUpper();
            teacher = Properties.Settings.Default.Teacher.ToUpper();
            btManageLessons.Enabled = true;
            btCreateSpreadsheets.Enabled = true;
            btUpdateWorksheet.Enabled = true;
            btDeleteWorksheet.Enabled = true;

            btLogInOut.Click -= new RibbonControlEventHandler(onLogIn);
            btLogInOut.Click += new RibbonControlEventHandler(onLogOut);
            btLogInOut.Label = "Log Out";
            btLogInOut.Image = Properties.Resources.ico_logout;

            vLoader = new VariableLoader(webProvider, teacher);
            mlDialog = new ManageLessonsDialog(teacher, webProvider, vLoader, eVariablesLoaded);
            csDialog = new CreateSpreadsheetDialog(webProvider, glController, ltController, vLoader, eVariablesLoaded, teacher);
        }

        private void onCreateSpreadsheets(object sender, RibbonControlEventArgs e)
        {
            setExcelApplication();
            exitEditMode();

            if (lessons != null)
            {
                csDialog.setLessons(lessons);
            }

            csDialog.ShowDialog();
        }

        private void onUpdateWorksheet(object sender, RibbonControlEventArgs e)
        {
            setExcelApplication();
            exitEditMode();

            try
            {
                Excel.Worksheet sheet = ExcelObj.ActiveWorkbook.ActiveSheet;

                string[] names = sheet.Name.Split('_');
                if (sheet.Name.StartsWith("LT_"))
                {
                    ltController.updateLogtable(names[1], names[2], teacher);
                }
                else
                {
                    glController.updateWorksheet(names[0], names[1], teacher);
                }
            }
            catch (IndexOutOfRangeException)
            {
                MessageBox.Show("The Worksheet you want to update is not a valid ExcelAllGrade-Worksheet!", "Error while updating Worksheet", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message, "Error while updating worksheet", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private void onDeleteWorksheet(object sender, RibbonControlEventArgs e)
        {
            setExcelApplication();
            exitEditMode();

            try
            {
                Excel.Worksheet sheet = ExcelObj.ActiveWorkbook.ActiveSheet;

                sheet.Delete();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message, "Error while deleting Worksheet", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        /// <summary>
        /// Sets the <see cref="Excel.Application"/> of the <see cref="ExcelAllGrade.controller.spreadsheet.GradelogController"/> and <see cref="ExcelAllGrade.controller.spreadsheet.LogtableController"/>.
        /// </summary>
        private void setExcelApplication()
        {
            if (ExcelObj == null)
            {
                ExcelObj = Globals.ThisAddIn.Application;

                glController.ExcelObj = ExcelObj;
                ltController.ExcelObj = ExcelObj;
            }
        }

        private void exitEditMode()
        {
            if (!isExcelInteractive())
            {
                // get the current range
                Excel.Range r = ExcelObj.ActiveCell;
                // bring Excel to the foreground, with focus
                // and issue keys to exit the cell
                xlBringToFront();
                ExcelObj.ActiveWindow.Activate();
                SendKeys.Flush();
                SendKeys.SendWait("{ENTER}");
                // now make sure the original cell is
                // selected…
                r.Select();
            }
        }

        private bool isExcelInteractive()
        {
            try
            {
                // this line does nothing if Excel is not
                // in edit mode. However, trying to set
                // this property while Excel is in edit
                // cell mdoe will cause an exception
                ExcelObj.Interactive = ExcelObj.Interactive;
                return true; // no exception, ecel is 
                // interactive
            }
            catch
            {
                return false; // in edit mode
            }
        }

        private void xlBringToFront()
        {
            SetForegroundWindow(ExcelObj.Hwnd);
        }

        private void onProxySettings(object sender, RibbonControlEventArgs e)
        {
            ProxySettingsDialog psd;
            if (Properties.Settings.Default.UseProxy)
            {
                psd = new ProxySettingsDialog(Properties.Settings.Default.Address, Properties.Settings.Default.Port, Properties.Settings.Default.Username, PasswordCryptography.decryptString(Properties.Settings.Default.Password), Properties.Settings.Default.UseProxy);
            }
            else
            {
                psd = new ProxySettingsDialog();
            }
            psd.ShowDialog();

            WebProxy proxy = new WebProxy();
            if (psd.DialogResult == DialogResult.OK)
            {
                Properties.Settings.Default.UseProxy = psd.UseProxy;

                if (psd.UseProxy)
                {
                    Uri newUri = new Uri(psd.Address);
                    newUri = newUri.SetPort(psd.Port);

                    proxy.Address = newUri;
                    proxy.Credentials = new NetworkCredential(psd.Username, psd.Password);

                    Properties.Settings.Default.Address = psd.Address;
                    Properties.Settings.Default.Port = psd.Port;
                    Properties.Settings.Default.Username = psd.Username;
                    Properties.Settings.Default.Password = PasswordCryptography.encryptString(psd.Password);
                    }

                Properties.Settings.Default.Save();
                webProvider.setProxy(proxy);
                uController.setProxy(proxy);
            }
        }
    }
}
