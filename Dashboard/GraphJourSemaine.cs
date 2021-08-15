using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Dashboard
{
    public class GraphJourSemaine
    {
        int lundi;
        int mardi;
        int mercredi;
        int jeudi;
        int vendredi;
        int samedi;
        int dimanche;

        public GraphJourSemaine(int lundi, int mardi, int mercredi, int jeudi, int vendredi, int samedi, int dimanche)
        {
            this.lundi = lundi;
            this.mardi = mardi;
            this.mercredi = mercredi;
            this.jeudi = jeudi;
            this.vendredi = vendredi;
            this.samedi = samedi;
            this.dimanche = dimanche;
        }

        public int Lundi { get => lundi; set => lundi = value; }
        public int Mardi { get => mardi; set => mardi = value; }
        public int Mercredi { get => mercredi; set => mercredi = value; }
        public int Jeudi { get => jeudi; set => jeudi = value; }
        public int Vendredi { get => vendredi; set => vendredi = value; }
        public int Samedi { get => samedi; set => samedi = value; }
        public int Dimanche { get => dimanche; set => dimanche = value; }
    }
}
