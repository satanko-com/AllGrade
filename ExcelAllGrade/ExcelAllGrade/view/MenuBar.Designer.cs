//  ************************************************************
//  *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
//  *                                                          *
//  *  See the file LICENCE.txt for copying permission.        *
//  ************************************************************ 

namespace ExcelAllGrade
{
    partial class MenuBar : Microsoft.Office.Tools.Ribbon.RibbonBase
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        public MenuBar()
            : base(Globals.Factory.GetRibbonFactory())
        {
            InitializeComponent();
        }

        /// <summary> 
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                eVariablesLoaded.Dispose();
                eOAuthDone.Dispose();
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Component Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.allGradeTab = this.Factory.CreateRibbonTab();
            this.groupAdministration = this.Factory.CreateRibbonGroup();
            this.separator1 = this.Factory.CreateRibbonSeparator();
            this.btTestConnection = this.Factory.CreateRibbonButton();
            this.groupSpreadsheetTools = this.Factory.CreateRibbonGroup();
            this.groupStatus = this.Factory.CreateRibbonGroup();
            this.box1 = this.Factory.CreateRibbonBox();
            this.lbStatus = this.Factory.CreateRibbonLabel();
            this.lbStatus2 = this.Factory.CreateRibbonLabel();
            this.box2 = this.Factory.CreateRibbonBox();
            this.lbUser = this.Factory.CreateRibbonLabel();
            this.lbUser2 = this.Factory.CreateRibbonLabel();
            this.btLogInOut = this.Factory.CreateRibbonButton();
            this.btProxy = this.Factory.CreateRibbonButton();
            this.btManageLessons = this.Factory.CreateRibbonButton();
            this.btCreateSpreadsheets = this.Factory.CreateRibbonButton();
            this.btUpdateWorksheet = this.Factory.CreateRibbonButton();
            this.btDeleteWorksheet = this.Factory.CreateRibbonButton();
            this.allGradeTab.SuspendLayout();
            this.groupAdministration.SuspendLayout();
            this.groupSpreadsheetTools.SuspendLayout();
            this.groupStatus.SuspendLayout();
            this.box1.SuspendLayout();
            this.box2.SuspendLayout();
            // 
            // allGradeTab
            // 
            this.allGradeTab.ControlId.ControlIdType = Microsoft.Office.Tools.Ribbon.RibbonControlIdType.Office;
            this.allGradeTab.Groups.Add(this.groupAdministration);
            this.allGradeTab.Groups.Add(this.groupSpreadsheetTools);
            this.allGradeTab.Groups.Add(this.groupStatus);
            this.allGradeTab.Label = "AllGrade";
            this.allGradeTab.Name = "allGradeTab";
            // 
            // groupAdministration
            // 
            this.groupAdministration.Items.Add(this.btLogInOut);
            this.groupAdministration.Items.Add(this.btProxy);
            this.groupAdministration.Items.Add(this.separator1);
            this.groupAdministration.Items.Add(this.btManageLessons);
            this.groupAdministration.Items.Add(this.btTestConnection);
            this.groupAdministration.Label = "Administration";
            this.groupAdministration.Name = "groupAdministration";
            // 
            // separator1
            // 
            this.separator1.Name = "separator1";
            // 
            // btTestConnection
            // 
            this.btTestConnection.Label = "";
            this.btTestConnection.Name = "btTestConnection";
            // 
            // groupSpreadsheetTools
            // 
            this.groupSpreadsheetTools.Items.Add(this.btCreateSpreadsheets);
            this.groupSpreadsheetTools.Items.Add(this.btUpdateWorksheet);
            this.groupSpreadsheetTools.Items.Add(this.btDeleteWorksheet);
            this.groupSpreadsheetTools.Label = "Spreadsheet Tools";
            this.groupSpreadsheetTools.Name = "groupSpreadsheetTools";
            // 
            // groupStatus
            // 
            this.groupStatus.Items.Add(this.box1);
            this.groupStatus.Items.Add(this.box2);
            this.groupStatus.Label = "Status";
            this.groupStatus.Name = "groupStatus";
            // 
            // box1
            // 
            this.box1.Items.Add(this.lbStatus);
            this.box1.Items.Add(this.lbStatus2);
            this.box1.Name = "box1";
            // 
            // lbStatus
            // 
            this.lbStatus.Label = "Status:";
            this.lbStatus.Name = "lbStatus";
            // 
            // lbStatus2
            // 
            this.lbStatus2.Label = " ";
            this.lbStatus2.Name = "lbStatus2";
            // 
            // box2
            // 
            this.box2.Items.Add(this.lbUser);
            this.box2.Items.Add(this.lbUser2);
            this.box2.Name = "box2";
            // 
            // lbUser
            // 
            this.lbUser.Label = "User:   ";
            this.lbUser.Name = "lbUser";
            // 
            // lbUser2
            // 
            this.lbUser2.Label = " ";
            this.lbUser2.Name = "lbUser2";
            // 
            // btLogInOut
            // 
            this.btLogInOut.ControlSize = Microsoft.Office.Core.RibbonControlSize.RibbonControlSizeLarge;
            this.btLogInOut.Image = global::ExcelAllGrade.Properties.Resources.ico_login;
            this.btLogInOut.Label = "Log In";
            this.btLogInOut.Name = "btLogInOut";
            this.btLogInOut.ShowImage = true;
            this.btLogInOut.Click += new Microsoft.Office.Tools.Ribbon.RibbonControlEventHandler(this.onLogIn);
            // 
            // btProxy
            // 
            this.btProxy.ControlSize = Microsoft.Office.Core.RibbonControlSize.RibbonControlSizeLarge;
            this.btProxy.Image = global::ExcelAllGrade.Properties.Resources.ico_proxy;
            this.btProxy.Label = "Proxy Settings";
            this.btProxy.Name = "btProxy";
            this.btProxy.ShowImage = true;
            this.btProxy.Click += new Microsoft.Office.Tools.Ribbon.RibbonControlEventHandler(this.onProxySettings);
            // 
            // btManageLessons
            // 
            this.btManageLessons.ControlSize = Microsoft.Office.Core.RibbonControlSize.RibbonControlSizeLarge;
            this.btManageLessons.Enabled = false;
            this.btManageLessons.Image = global::ExcelAllGrade.Properties.Resources.ico_manage;
            this.btManageLessons.Label = "Manage Lessons";
            this.btManageLessons.Name = "btManageLessons";
            this.btManageLessons.ShowImage = true;
            this.btManageLessons.Click += new Microsoft.Office.Tools.Ribbon.RibbonControlEventHandler(this.onManageLessons);
            // 
            // btCreateSpreadsheets
            // 
            this.btCreateSpreadsheets.ControlSize = Microsoft.Office.Core.RibbonControlSize.RibbonControlSizeLarge;
            this.btCreateSpreadsheets.Enabled = false;
            this.btCreateSpreadsheets.Image = global::ExcelAllGrade.Properties.Resources.ico_createWorksheet;
            this.btCreateSpreadsheets.Label = "Create Worksheets";
            this.btCreateSpreadsheets.Name = "btCreateSpreadsheets";
            this.btCreateSpreadsheets.ShowImage = true;
            this.btCreateSpreadsheets.Click += new Microsoft.Office.Tools.Ribbon.RibbonControlEventHandler(this.onCreateSpreadsheets);
            // 
            // btUpdateWorksheet
            // 
            this.btUpdateWorksheet.Enabled = false;
            this.btUpdateWorksheet.Image = global::ExcelAllGrade.Properties.Resources.ico_updateWorksheet;
            this.btUpdateWorksheet.Label = "Update Worksheet";
            this.btUpdateWorksheet.Name = "btUpdateWorksheet";
            this.btUpdateWorksheet.ShowImage = true;
            this.btUpdateWorksheet.Click += new Microsoft.Office.Tools.Ribbon.RibbonControlEventHandler(this.onUpdateWorksheet);
            // 
            // btDeleteWorksheet
            // 
            this.btDeleteWorksheet.Enabled = false;
            this.btDeleteWorksheet.Image = global::ExcelAllGrade.Properties.Resources.ico_deleteWorksheet;
            this.btDeleteWorksheet.Label = "Delete Worksheet";
            this.btDeleteWorksheet.Name = "btDeleteWorksheet";
            this.btDeleteWorksheet.ShowImage = true;
            this.btDeleteWorksheet.Click += new Microsoft.Office.Tools.Ribbon.RibbonControlEventHandler(this.onDeleteWorksheet);
            // 
            // MenuBar
            // 
            this.Name = "MenuBar";
            this.RibbonType = "Microsoft.Excel.Workbook";
            this.Tabs.Add(this.allGradeTab);
            this.Load += new Microsoft.Office.Tools.Ribbon.RibbonUIEventHandler(this.MenuBar_Load);
            this.allGradeTab.ResumeLayout(false);
            this.allGradeTab.PerformLayout();
            this.groupAdministration.ResumeLayout(false);
            this.groupAdministration.PerformLayout();
            this.groupSpreadsheetTools.ResumeLayout(false);
            this.groupSpreadsheetTools.PerformLayout();
            this.groupStatus.ResumeLayout(false);
            this.groupStatus.PerformLayout();
            this.box1.ResumeLayout(false);
            this.box1.PerformLayout();
            this.box2.ResumeLayout(false);
            this.box2.PerformLayout();

        }

        #endregion

        internal Microsoft.Office.Tools.Ribbon.RibbonGroup groupAdministration;
        internal Microsoft.Office.Tools.Ribbon.RibbonButton btTestConnection;
        internal Microsoft.Office.Tools.Ribbon.RibbonGroup groupStatus;
        internal Microsoft.Office.Tools.Ribbon.RibbonBox box1;
        internal Microsoft.Office.Tools.Ribbon.RibbonLabel lbStatus;
        internal Microsoft.Office.Tools.Ribbon.RibbonLabel lbStatus2;
        internal Microsoft.Office.Tools.Ribbon.RibbonBox box2;
        internal Microsoft.Office.Tools.Ribbon.RibbonLabel lbUser;
        internal Microsoft.Office.Tools.Ribbon.RibbonLabel lbUser2;
        internal Microsoft.Office.Tools.Ribbon.RibbonButton btManageLessons;
        internal Microsoft.Office.Tools.Ribbon.RibbonButton btLogInOut;
        internal Microsoft.Office.Tools.Ribbon.RibbonGroup groupSpreadsheetTools;
        internal Microsoft.Office.Tools.Ribbon.RibbonButton btCreateSpreadsheets;
        private Microsoft.Office.Tools.Ribbon.RibbonTab allGradeTab;
        internal Microsoft.Office.Tools.Ribbon.RibbonSeparator separator1;
        internal Microsoft.Office.Tools.Ribbon.RibbonButton btUpdateWorksheet;
        internal Microsoft.Office.Tools.Ribbon.RibbonButton btDeleteWorksheet;
        internal Microsoft.Office.Tools.Ribbon.RibbonButton btProxy;
    }

    partial class ThisRibbonCollection
    {
        internal MenuBar MenuBar
        {
            get { return this.GetRibbon<MenuBar>(); }
        }
    }
}
