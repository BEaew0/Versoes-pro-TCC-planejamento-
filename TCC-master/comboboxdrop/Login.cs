﻿using System;
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
    public partial class Login : Form
    {
        public Login()
        {
            InitializeComponent();
        }

        bool expandir = false;
        bool retrair = false;

        private void timer1_Tick(object sender, EventArgs e) // timer1 é utilizado para ser feita a animação dos links no "hamburguer"
        {
            if (expandir)
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
                dropdownContainer.Height -= 15;  // Ajuste mais suave

                if (dropdownContainer.Height <= dropdownContainer.MinimumSize.Height)
                {
                    timer1.Stop();  // Parar a animação de retração
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

        private void button4_Click(object sender, EventArgs e) // é a seta para cima para retrair o painel e fazer voltar o painel
        {
            if (!expandir && !retrair)
            {
                retrair = true;  // Setar para retração
                timer1.Start();  // Iniciar animação de retração
            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
            // Cria uma nova instância do formulário
            Login loginForm = new Login(); //  transfere para outro forms 


            // Exibe o formulário TransferTest
            loginForm.Show(); // Ou ShowDialog() se preferir
        }

        private void button5_Click(object sender, EventArgs e)
        {
            // Cria uma nova instância do formulário
            cadastro cadastroForm = new cadastro(); //  transfere para outro forms 


            // Exibe o formulário TransferTest
            cadastroForm.Show(); // Ou ShowDialog() se preferir
        }

        private void pbox1_Click(object sender, EventArgs e)
        {
            // Cria uma nova instância do formulário TransferTest
            transfertest transferTestForm = new transfertest();

            // Exibe o formulário TransferTest
            transferTestForm.Show(); // Use ShowDialog() se quiser que o formulário seja modal
        }
    }
}
