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
    public partial class FormSetting : Form
    {
        public FormSetting()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {

            Cursor.Current = Cursors.WaitCursor;

            String email = textBox1.Text;
            Service matchService = new Service();
            EmailAdmin em = new EmailAdmin(email);
            String message = matchService.insereMail(em);

            this.dataGridView1.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;
            dataGridView1.DataSource = matchService.getAllEmail();

            


            Cursor.Current = Cursors.AppStarting;
            MessageBox.Show(message);

        }

        private void FormSetting_Load(object sender, EventArgs e)
        {

            Cursor.Current = Cursors.WaitCursor;

            Service matchService = new Service();
            this.dataGridView1.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;
            dataGridView1.DataSource = matchService.getAllEmail();

            DataGridViewButtonColumn btnEdit = new DataGridViewButtonColumn();
            btnEdit.HeaderText = "Delete";
            btnEdit.Name = "buttonEdit";
            btnEdit.Text = "delete";
            btnEdit.UseColumnTextForButtonValue = true;
            this.dataGridView1.Columns.Add(btnEdit);

            Cursor.Current = Cursors.AppStarting;

        }

        private void dataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {

            Cursor.Current = Cursors.WaitCursor;

            if (e.ColumnIndex == 0)
            {

                Service service = new Service();


                DataGridViewRow row = this.dataGridView1.Rows[e.RowIndex];
                String email = row.Cells["email"].Value.ToString();
                EmailAdmin em = new EmailAdmin(email);

                service.deleteEmail(em);

                this.dataGridView1.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;
                dataGridView1.DataSource = service.getAllEmail();

                


                Cursor.Current = Cursors.AppStarting;

                MessageBox.Show(email + " deleted");

            }
        }
    }
}
