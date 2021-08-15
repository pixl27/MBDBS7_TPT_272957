using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Dashboard
{
    class EmailAdmin
    {
        string email;

        public EmailAdmin(string email)
        {
            this.Email = email;
        }

        public string Email { get => email; set => email = value; }
    }
}
