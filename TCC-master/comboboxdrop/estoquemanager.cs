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
    public partial class estoquemanager : Form
    {
        public estoquemanager()
        {
            InitializeComponent();
        }
        bool expandir = false;
        bool retrair = false;

        private void timer1_Tick(object sender, EventArgs e) // o timer serve para ativar a animação de retração/expanção
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

        private void button1_Click(object sender, EventArgs e) // quando clicado expande o paínel na intenção de mostrar as outras opções

        {
            if (!expandir && !retrair)
            {
                expandir = true;
                timer1.Start();  // Iniciar animação de expansão
            }
        }

        private void button4_Click(object sender, EventArgs e)
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

        private void pbox1_Click(object sender, EventArgs e)
        {
            // Cria uma nova instância do formulário TransferTest
            transfertest transferTestForm = new transfertest();

            // Exibe o formulário TransferTest
            transferTestForm.Show(); // Use ShowDialog() se quiser que o formulário seja modal
        }

        private void button2_Click(object sender, EventArgs e)
        {
            // button inserido  caso queira atualizar as informações no banco 
            
            
            //https://www.youtube.com/watch?v=lhXpeae149M tutorial de como fazer 
        }

        private void estoquemanager_Load(object sender, EventArgs e)
        {

        }
    }
}
