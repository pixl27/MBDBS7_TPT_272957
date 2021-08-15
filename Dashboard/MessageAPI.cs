using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Dashboard
{
    class MessageAPI
    {
        String message;

        public MessageAPI(string message)
        {
            this.message = message;
        }

        public string Message { get => message; set => message = value; }
    }
}
