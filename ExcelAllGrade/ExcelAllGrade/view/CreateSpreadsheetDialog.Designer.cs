//  ************************************************************
//  *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
//  *                                                          *
//  *  See the file LICENCE.txt for copying permission.        *
//  ************************************************************ 

namespace ExcelAllGrade.view
{
    partial class CreateSpreadsheetDialog
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.splitContainer = new System.Windows.Forms.SplitContainer();
            this.lbRetrievingData = new System.Windows.Forms.Label();
            this.pbRetrievingData = new System.Windows.Forms.ProgressBar();
            this.btCancel = new System.Windows.Forms.Button();
            ((System.ComponentModel.ISupportInitialize)(this.splitContainer)).BeginInit();
            this.splitContainer.Panel1.SuspendLayout();
            this.splitContainer.Panel2.SuspendLayout();
            this.splitContainer.SuspendLayout();
            this.SuspendLayout();
            // 
            // splitContainer
            // 
            this.splitContainer.Dock = System.Windows.Forms.DockStyle.Fill;
            this.splitContainer.IsSplitterFixed = true;
            this.splitContainer.Location = new System.Drawing.Point(0, 0);
            this.splitContainer.Name = "splitContainer";
            this.splitContainer.Orientation = System.Windows.Forms.Orientation.Horizontal;
            // 
            // splitContainer.Panel1
            // 
            this.splitContainer.Panel1.AutoScroll = true;
            this.splitContainer.Panel1.Controls.Add(this.lbRetrievingData);
            this.splitContainer.Panel1.Controls.Add(this.pbRetrievingData);
            // 
            // splitContainer.Panel2
            // 
            this.splitContainer.Panel2.Controls.Add(this.btCancel);
            this.splitContainer.Size = new System.Drawing.Size(319, 60);
            this.splitContainer.SplitterDistance = 31;
            this.splitContainer.TabIndex = 10000;
            this.splitContainer.TabStop = false;
            // 
            // lbRetrievingData
            // 
            this.lbRetrievingData.Location = new System.Drawing.Point(12, 9);
            this.lbRetrievingData.Name = "lbRetrievingData";
            this.lbRetrievingData.Size = new System.Drawing.Size(117, 13);
            this.lbRetrievingData.TabIndex = 0;
            this.lbRetrievingData.Text = "Receiving Data...";
            // 
            // pbRetrievingData
            // 
            this.pbRetrievingData.Location = new System.Drawing.Point(135, 9);
            this.pbRetrievingData.MarqueeAnimationSpeed = 30;
            this.pbRetrievingData.Name = "pbRetrievingData";
            this.pbRetrievingData.Size = new System.Drawing.Size(172, 13);
            this.pbRetrievingData.Style = System.Windows.Forms.ProgressBarStyle.Marquee;
            this.pbRetrievingData.TabIndex = 0;
            // 
            // btCancel
            // 
            this.btCancel.DialogResult = System.Windows.Forms.DialogResult.Cancel;
            this.btCancel.Dock = System.Windows.Forms.DockStyle.Right;
            this.btCancel.Location = new System.Drawing.Point(208, 0);
            this.btCancel.Name = "btCancel";
            this.btCancel.Size = new System.Drawing.Size(111, 25);
            this.btCancel.TabIndex = 0;
            this.btCancel.Text = "Cancel";
            // 
            // CreateSpreadsheetDialog
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.CancelButton = this.btCancel;
            this.ClientSize = new System.Drawing.Size(319, 60);
            this.Controls.Add(this.splitContainer);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.Name = "CreateSpreadsheetDialog";
            this.ShowIcon = false;
            this.ShowInTaskbar = false;
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "Create Worksheets";
            this.Load += new System.EventHandler(this.onLoad);
            this.splitContainer.Panel1.ResumeLayout(false);
            this.splitContainer.Panel2.ResumeLayout(false);
            ((System.ComponentModel.ISupportInitialize)(this.splitContainer)).EndInit();
            this.splitContainer.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.SplitContainer splitContainer;
        private System.Windows.Forms.Button btCancel;
        private System.Windows.Forms.Label lbRetrievingData;
        private System.Windows.Forms.ProgressBar pbRetrievingData;
    }
}