using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Runtime.InteropServices;

namespace Dashboard
{
    public partial class Form1 : Form
    {

        [DllImport("Gdi32.dll", EntryPoint = "CreateRoundRectRgn")]

        private static extern IntPtr CreateRoundRectRgn
         (
               int nLeftRect,
               int nTopRect,
               int nRightRect,
               int nBottomRect,
               int nWidthEllipse,
               int nHeightEllipse

         );

        GraphJourSemaine mygs;
        public Form1(GraphJourSemaine mygs)
        {
            InitializeComponent();
            Region = System.Drawing.Region.FromHrgn(CreateRoundRectRgn(0, 0, Width, Height, 25, 25));
            pnlNav.Height = btnDashbord.Height;
            pnlNav.Top = btnDashbord.Top;
            pnlNav.Left = btnDashbord.Left;
            //btnDashbord.BackColor = Color.FromArgb(46, 51, 73);

            lbltitle.Text = "Dashboard";
            this.PnlFormLoader.Controls.Clear();

            this.mygs = mygs;
           
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            this.WindowState = FormWindowState.Minimized;
            this.WindowState = FormWindowState.Normal;
            this.Focus(); this.Show();

            FormDashboard fd = new FormDashboard(this.mygs) { Dock = DockStyle.Fill, TopLevel = false, TopMost = true };
            fd.FormBorderStyle = FormBorderStyle.None;
            this.PnlFormLoader.Controls.Add(fd);
            fd.Show();
        }

        private void btnDashbord_Click(object sender, EventArgs e)
        {
            pnlNav.Height = btnDashbord.Height;
            pnlNav.Top = btnDashbord.Top;
            pnlNav.Left = btnDashbord.Left;
            btnDashbord.BackColor = Color.FromArgb(46, 51, 73);

            lbltitle.Text = "Dashboard";
            this.PnlFormLoader.Controls.Clear();
            FormDashboard fd = new FormDashboard(this.mygs) { Dock=DockStyle.Fill, TopLevel=false,TopMost=true};
            fd.FormBorderStyle = FormBorderStyle.None;
            this.PnlFormLoader.Controls.Add(fd);
            fd.Show();

        }

        private void btnAnalytics_Click(object sender, EventArgs e)
        {
            pnlNav.Height = btnAnalytics.Height;
            pnlNav.Top = btnAnalytics.Top;
            btnAnalytics.BackColor = Color.FromArgb(46, 51, 73);

            lbltitle.Text = "Liste probleme";
            this.PnlFormLoader.Controls.Clear();
            FormListeProbleme fd = new FormListeProbleme() { Dock = DockStyle.Fill, TopLevel = false, TopMost = true };
            fd.FormBorderStyle = FormBorderStyle.None;
            this.PnlFormLoader.Controls.Add(fd);
            fd.Show();
        }

        

        private void btnsettings_Click(object sender, EventArgs e)
        {
            pnlNav.Height = btnsettings.Height;
            pnlNav.Top = btnsettings.Top;
            btnsettings.BackColor = Color.FromArgb(46, 51, 73);

            lbltitle.Text = "Setting";
            this.PnlFormLoader.Controls.Clear();
            FormSetting fd = new FormSetting() { Dock = DockStyle.Fill, TopLevel = false, TopMost = true };
            fd.FormBorderStyle = FormBorderStyle.None;
            this.PnlFormLoader.Controls.Add(fd);
            fd.Show();
        }

        private void btnDashbord_Leave(object sender, EventArgs e)
        {
            btnDashbord.BackColor = Color.FromArgb(24, 30, 54);
        }

        private void btnAnalytics_Leave(object sender, EventArgs e)
        {
            btnAnalytics.BackColor = Color.FromArgb(24, 30, 54);
        }

      

        private void btnsettings_Leave(object sender, EventArgs e)
        {
            btnsettings.BackColor = Color.FromArgb(24, 30, 54);
        }

        private void button1_Click_1(object sender, EventArgs e)
        {
            Application.Exit();
        }
    }
}
