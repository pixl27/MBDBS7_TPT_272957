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
    public partial class FormListeProbleme : Form
    {
        public FormListeProbleme()
        {
            InitializeComponent();
        }

        private void FormListeProbleme_Load(object sender, EventArgs e)
        {

            Cursor.Current = Cursors.WaitCursor;

            Service service = new Service();
            this.dataGridView1.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;
            dataGridView1.DataSource = service.getAllProbleme();

            DataGridViewButtonColumn btnWin = new DataGridViewButtonColumn();
            btnWin.HeaderText = "";
            btnWin.Name = "ParisWin";
            btnWin.Text = "Parie gagner";
            btnWin.UseColumnTextForButtonValue = true;
            this.dataGridView1.Columns.Add(btnWin);

            DataGridViewButtonColumn btnLoose = new DataGridViewButtonColumn();
            btnLoose.HeaderText = "";
            btnLoose.Name = "ParisLoose";
            btnLoose.Text = "Parie perdue";
            btnLoose.UseColumnTextForButtonValue = true;
            this.dataGridView1.Columns.Add(btnLoose);

            Cursor.Current = Cursors.AppStarting;
        }

        private void label1_Click(object sender, EventArgs e)
        {

        }

        private void dataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {
            Service service = new Service();
            if (e.ColumnIndex == 0)
            {
                Cursor.Current = Cursors.WaitCursor;

                DataGridViewRow row = this.dataGridView1.Rows[e.RowIndex];
                String id = row.Cells["idParis"].Value.ToString();
                int idParis = (int)Int64.Parse(id);
                String message = service.finaliserWinManuel(idParis);

                this.dataGridView1.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;
                dataGridView1.DataSource = service.getAllProbleme();

                Cursor.Current = Cursors.AppStarting;
                MessageBox.Show(message);

                

            }
            if(e.ColumnIndex == 1)
            {
                Cursor.Current = Cursors.WaitCursor;
                DataGridViewRow row = this.dataGridView1.Rows[e.RowIndex];
                String id = row.Cells["idParis"].Value.ToString();
                int idParis = (int)Int64.Parse(id);
                String message = service.finaliserLossManuel(idParis);

                this.dataGridView1.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;
                dataGridView1.DataSource = service.getAllProbleme();

                Cursor.Current = Cursors.WaitCursor;
                MessageBox.Show(message);
            }
        }
    }
}
