using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Dashboard
{
    class Dashboard
    {
        double earnings;
        int nbrParis;
        int pourcentage;

        public Dashboard(double earnings, int nbrParis, int pourcentage)
        {
            this.earnings = earnings;
            this.nbrParis = nbrParis;
            this.pourcentage = pourcentage;
        }

        public double Earnings { get => earnings; set => earnings = value; }
        public int NbrParis { get => nbrParis; set => nbrParis = value; }
        public int Pourcentage { get => pourcentage; set => pourcentage = value; }
    }
}
