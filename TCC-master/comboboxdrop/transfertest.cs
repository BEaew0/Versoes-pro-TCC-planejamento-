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
    public partial class transfertest : Form

    {
        private Form ultimoFormularioAberto; // Variável para armazenar a referência do último formulário aberto

        public transfertest()
        {
            InitializeComponent();
            // Adicionando eventos ao PictureBox
            pictureBox1.MouseEnter += pictureBox1_MouseEnter;
            pictureBox1.MouseLeave += pictureBox1_MouseLeave;

            // Adicionando eventos ao panel4
            panel4.MouseEnter += panel4_MouseEnter;
            panel4.MouseLeave += panel4_MouseLeave;
        }



        bool expandir = false;
        bool retrair = false;
        private void timer1_Tick(object sender, EventArgs e) // o timer serve para ativar a animação de retração/expanção
        {
            if (expandir)
            {
                // Expandir o painel
                dropdownContainer.Height += 150;  // Ajuste mais suave

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

        private void button2_Click(object sender, EventArgs e)
        {
            // Cria uma nova instância do formulário
          Login loginForm = new Login(); //  transfere para outro forms 


            // Exibe o formulário TransferTest
            loginForm.Show(); // Ou ShowDialog() se preferir
        }

        private void button3_Click(object sender, EventArgs e)
        {
            // Cria uma nova instância do formulário
            cadastro cadastroForm = new cadastro(); //  transfere para outro forms 


            // Exibe o formulário TransferTest
            cadastroForm.Show(); // Ou ShowDialog() se preferir
        }

        private void button5_Click(object sender, EventArgs e)
        {
            // Cria uma nova instância do formulário
            cadastro cadastroForm = new cadastro(); //  transfere para outro forms 


            // Exibe o formulário TransferTest
            cadastroForm.Show(); // Ou ShowDialog() se preferir
        }

        private void pictureBox5_Click(object sender, EventArgs e)
        {

            // cria uma nova instância do formulário
            estoquemanager estoque= new estoquemanager();


            //exibe o formulário  estoquemanager
            estoque.Show();  
        }

        private void pictureBox1_Click(object sender, EventArgs e)
        {
            prodvenc pvenc = new prodvenc();
            pvenc.Show();
          
        }
        private void pictureBox2_Click(object sender, EventArgs e)
        {
            produtosestoque produtosestoque= new produtosestoque();

            produtosestoque.Show();
        }
        private void pbox1_Click(object sender, EventArgs e)
        {
            // Cria uma nova instância do formulário TransferTest
            transfertest transferTestForm = new transfertest();

            // Exibe o formulário TransferTest
            transferTestForm.Show(); // Use ShowDialog() se quiser que o formulário seja modal
        }

        private void pictureBox4_Click(object sender, EventArgs e)
        {
            exitentry exitentry = new exitentry();
            exitentry.Show();
        }

        private void button6_Click(object sender, EventArgs e)
        {
            // botão para atualizar informações do banco de dados 
            //https://www.youtube.com/watch?v=lhXpeae149M tutorial de como fazer 
        }

        private void pictureBox1_MouseLeave(object sender, EventArgs e)
        {
            
        }

        private void pictureBox1_MouseHover(object sender, EventArgs e)
        {
          
        }

        private void pictureBox1_MouseEnter(object sender, EventArgs e)
        {
        }

        private void panel4_MouseEnter(object sender, EventArgs e)
        {
            Panel panel = (Panel)sender; // Obtenha a instância correta

            // Aumenta o tamanho do Panel
            panel.Size = new Size(panel.Width + 1, panel.Height + 10); // Aumenta 10 pixels
            panel.BackColor = Color.FromArgb(150, 150, 255, 255); // Efeito de hover
           
        }
        

        private void panel4_MouseLeave(object sender, EventArgs e)
        {
            Panel panel = (Panel)sender; // Obtenha a instância correta

            // Restaura o tamanho original do Panel
            panel.Size = new Size(panel.Width - 1, panel.Height - 10); // Restaura 10 pixels
            panel.BackColor = Color.Transparent; // Restaura a cor original
        }

        private void panel4_MouseClick(object sender, MouseEventArgs e)
        {
            prodvenc pvenc = new prodvenc();
            pvenc.Show();    // funcionalidade de click no painel 
        }
    }
}
