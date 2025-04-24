using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace comboboxdrop
{
    public partial class deleteaccount : Form
    {
        public deleteaccount()
        {
            InitializeComponent();
        }
        bool expandir = false;
        bool retrair = false;

        private void timer1_Tick(object sender, EventArgs e)
        {
            if (expandir) // sexo
            {
                // Expandir o painel
                dropdownContainer.Height += 15;  // Ajuste mais suave

                if (dropdownContainer.Height >= dropdownContainer.MaximumSize.Height)
                {
                    timer1.Stop();  // Parar a animação de expansão
                    expandir = false; // Terminar a expansão
                }
            }
            else if (retrair)
            {
                // Retrair o painel
                dropdownContainer.Height -= 15;  // aqui o ajuste conforme numeros maiores mais rapido 

                if (dropdownContainer.Height <= dropdownContainer.MinimumSize.Height)
                {
                    timer1.Stop();  // Parar a animação de retração (timer1) utilizado para fazer a animação

                    retrair = false; // Terminar a retração
                }
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            if (!expandir && !retrair)
            {
                expandir = true;
                timer1.Start();  // Iniciar animação de expansão
            }
        }

        private void btn4_Click(object sender, EventArgs e)
        {
            if (!expandir && !retrair)
            {
                retrair = true;  // Setar para retração
                timer1.Start();  // Iniciar animação de retração
            }
        }

        private void btn2_Click(object sender, EventArgs e)
        {
            // Cria uma nova instância do formulário
            cadastro cadastroForm = new cadastro(); //  transfere para outro forms 


            // Exibe o formulário TransferTest
            cadastroForm.Show(); // Ou ShowDialog() se preferir
        }

        private void btn3_Click(object sender, EventArgs e)
        {
            // Cria uma nova instância do formulário
            Login loginForm = new Login(); //  transfere para outro forms 


            // Exibe o formulário TransferTest
            loginForm.Show(); // Ou ShowDialog() se preferir

        }
    }
}
