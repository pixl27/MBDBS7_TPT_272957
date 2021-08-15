using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Dashboard
{
    public partial class FormDashboard : Form
    {
        GraphJourSemaine mygs;
        public FormDashboard(GraphJourSemaine gs)
        {
            InitializeComponent();
            this.mygs = gs;
        }

        private  void FormDashboard_Load(object sender, EventArgs e)
        {
            Cursor.Current = Cursors.WaitCursor; 

            Service s = new Service();
            Dashboard ds = s.getDashboard();
            label5.Text = "$" + ds.Earnings;
            circularProgressBar1.Text = ds.Pourcentage+"%";
            circularProgressBar1.Value = ds.Pourcentage;
            label7.Text = ds.NbrParis+"";

            
            chart1.Series["Nbr paris"].Points.AddXY("Lundi", this.mygs.Lundi);
            chart1.Series["Nbr paris"].Points.AddXY("Mardi", this.mygs.Mardi);
            chart1.Series["Nbr paris"].Points.AddXY("Mercredi", this.mygs.Mercredi);
            chart1.Series["Nbr paris"].Points.AddXY("Jeudi", this.mygs.Jeudi);
            chart1.Series["Nbr paris"].Points.AddXY("Vendredi", this.mygs.Vendredi);
            chart1.Series["Nbr paris"].Points.AddXY("Samedi", this.mygs.Samedi);
            chart1.Series["Nbr paris"].Points.AddXY("Dimanche", this.mygs.Dimanche);

            Cursor.Current = Cursors.AppStarting;

        }
    }
}
