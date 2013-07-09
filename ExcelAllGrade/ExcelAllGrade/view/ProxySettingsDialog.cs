//  ************************************************************
//  *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
//  *                                                          *
//  *  See the file LICENCE.txt for copying permission.        *
//  ************************************************************ 

using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace ExcelAllGrade.view
{
    /// <summary>
    /// This dialog lets the user enter proxy-settings.
    /// </summary>
    public partial class ProxySettingsDialog : Form
    {
        /// <summary>
        /// Gets or sets the address of the proxy.
        /// </summary>
        public string Address { get; set; }
        /// <summary>
        /// Gets or sets the port of the proxy.
        /// </summary>
        public int Port { get; set; }
        /// <summary>
        /// Gets or sets the username of the proxy-credentials.
        /// </summary>
        public string Username { get; set; }
        /// <summary>
        /// Gets or sets the password of the proxy-credentials.
        /// </summary>
        public string Password { get; set; }
        /// <summary>
        /// Gets or sets the value whether to use a proxy or not
        /// </summary>
        public bool UseProxy { get; set; }

        /// <summary>
        /// Initializes a new instance of <see cref="ProxySettingsDialog"/>.
        /// The parameter-variables are displayed in the dialog.
        /// </summary>
        /// <param name="address">The <see cref="Address"/> of the <see cref="ProxySettingsDialog"/>.</param>
        /// <param name="port">The <see cref="Port"/> of the <see cref="ProxySettingsDialog"/>.</param>
        /// <param name="username">The <see cref="Username"/> of the <see cref="ProxySettingsDialog"/>.</param>
        /// <param name="password">The <see cref="Password"/> of the <see cref="ProxySettingsDialog"/>.</param>
        /// <param name="useProxy">The <see cref="UseProxy"/> of the <see cref="ProxySettingsDialog"/>.</param>
        public ProxySettingsDialog(string address, int port, string username, string password, bool useProxy)
        {
            InitializeComponent();

            tbAddress.Text = address;
            tbPort.Text = port.ToString();
            tbUsername.Text = username;
            tbPassword.Text = password;
            cbUseProxy.Checked = useProxy;
        }

        /// <summary>
        /// Initializes a new instance of <see cref="ProxySettingsDialog"/>.
        /// </summary>
        public ProxySettingsDialog()
        {
            InitializeComponent();
        }

        private void onCheckedChanged(object sender, EventArgs e)
        {
            if (cbUseProxy.Checked)
            {
                tbAddress.Enabled = true;
                tbPort.Enabled = true;
                tbUsername.Enabled = true;
                tbPassword.Enabled = true;
            }
            else
            {
                tbAddress.Enabled = false;
                tbPort.Enabled = false;
                tbUsername.Enabled = false;
                tbPassword.Enabled = false;
            }
        }

        private void onSave(object sender, EventArgs e)
        {
            if (cbUseProxy.Checked)
            {
                if (tbAddress.Text.StartsWith("http://") || tbAddress.Text.StartsWith("https://"))
                    this.Address = tbAddress.Text;
                else
                    this.Address = "http://" + tbAddress.Text;
                this.Port = Convert.ToInt32(tbPort.Text);
                this.Username = tbUsername.Text;
                this.Password = tbPassword.Text;
                this.UseProxy = true;
            }
            else
            {
                this.Address = "";
                this.Port = -1;
                this.Username = "";
                this.Password = "";
                this.UseProxy = false;
            }
        }

        private void onKeyPress(object sender, KeyPressEventArgs e)
        {
            if (!char.IsControl(e.KeyChar)
                && !char.IsDigit(e.KeyChar)
                && e.KeyChar != '.')
            {
                e.Handled = true;
            }
        }
    }
}
