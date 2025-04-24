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
            pictureBox1.MouseLeave += pictureBox1_MouseLeave; // eventos utilizados para fazer efeito "hover"

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
            panel4.Size = new Size(panel4.Width - 0, panel4.Height - 0); // mantém o tamanho do painel que faz a alteração de cor.
            // paineis são utilizados para fazer a animação de hover, pode ser ajustado.
            


            // Restaura a cor original do Panel  -- alteração de cor para azul mais claro, pode ser alterado.
            panel4.BackColor = Color.FromArgb(0, 0, 159); // Cor original do Panel
        }
        

        private void pictureBox1_MouseHover(object sender, EventArgs e)
            // inserção desnecessaria 
        {
          
        }

        private void pictureBox1_MouseEnter(object sender, EventArgs e)
        {
            PictureBox picturebox = (PictureBox)sender;

            
          
            // Muda a cor do Panel
            panel4.BackColor = Color.FromArgb(0, 50, 255); // Cor de hover para o Panel

            panel4.Size = new Size(panel4.Width + 0, panel4.Height + 0);
        }

        private void panel4_MouseEnter(object sender, EventArgs e)
        {
            Panel panel = (Panel)sender; // Obtenha a instância correta

            // Aumenta o tamanho do Panel
            panel.Size = new Size(panel.Width-0, panel.Height -0); // tamanho de painel  não cresce para ser controlado pelo próprio componente 
            panel.BackColor = Color.FromArgb(0, 50, 255); // Efeito de hover
           
        }
        

        private void panel4_MouseLeave(object sender, EventArgs e)
        {
            Panel panel = (Panel)sender; // Obtenha a instância correta

            // Restaura o tamanho original do Panel
            panel.Size = new Size(panel.Width - 0, panel.Height - 0);
            panel.BackColor = Color.Transparent; // fica transparente para voltar para a back color do form
        }

        private void panel4_MouseClick(object sender, MouseEventArgs e)
        {
            prodvenc pvenc = new prodvenc();
            pvenc.Show();    // funcionalidade de click no painel 
        }

        private void panel5_MouseClick(object sender, MouseEventArgs e)
        {
            produtosestoque produtosestoque = new produtosestoque();

            produtosestoque.Show();
        }

        private void panel5_MouseEnter(object sender, EventArgs e)
        {
            panel5.BackColor = Color.FromArgb(0, 50, 255); // Cor de hover para o Panel
        }

        private void panel5_MouseLeave(object sender, EventArgs e)
        {
            panel5.BackColor = Color.Transparent; // Restaura a cor original
        }

        private void pictureBox2_MouseEnter(object sender, EventArgs e)
        {
            panel5.BackColor = Color.FromArgb(0, 50, 255); // Cor de hover para o Panel
        }

        private void pictureBox2_MouseLeave(object sender, EventArgs e)
        {
            panel5.BackColor = Color.Transparent; // Restaura a cor original
        }

        private void panel6_MouseEnter(object sender, EventArgs e)
        {
            panel6.BackColor = Color.FromArgb(0, 50, 255); // Cor de hover para o Panel
        }

        private void panel6_MouseLeave(object sender, EventArgs e)
        {
            panel6.BackColor = Color.Transparent;// fica transparente para voltar para a back color do form
        }
        private void pictureBox4_MouseEnter(object sender, EventArgs e)
        {
            panel6.BackColor = Color.FromArgb(0, 50, 255); // Cor de hover para o Panel
        }

        private void pictureBox4_MouseLeave(object sender, EventArgs e)
        {
            panel6.BackColor = Color.Transparent; // Restaura a cor original
        }

        private void pictureBox5_MouseEnter(object sender, EventArgs e)
        {
            panel7.BackColor = Color.FromArgb(0, 50, 255); // Cor de hover para o Panel
        }

        private void pictureBox5_MouseLeave(object sender, EventArgs e)
        {
            panel7.BackColor = Color.Transparent; // Restaura a cor original
        }

        private void panel7_MouseEnter(object sender, EventArgs e)
        {
            panel7.BackColor = Color.FromArgb(0, 50, 255); // Cor de hover para o Panel
        }

        private void panel7_MouseLeave(object sender, EventArgs e)
        {
            panel7.BackColor = Color.Transparent; // Restaura a cor original
        }

        private void panel8_MouseEnter(object sender, EventArgs e)
        {
            panel8.BackColor = Color.FromArgb(0, 50, 255); // Cor de hover para o Panel
        }

        private void panel8_MouseLeave(object sender, EventArgs e)
        {
            panel8.BackColor = Color.Transparent; // Restaura a cor original
        }

        private void pictureBox3_MouseEnter(object sender, EventArgs e)
        {
            panel8.BackColor = Color.FromArgb(0, 50, 255); // Cor de hover para o Panel
        }

        private void pictureBox3_MouseLeave(object sender, EventArgs e)
        {
            panel8.BackColor = Color.Transparent; // Restaura a cor original
        }

        private void panel6_MouseClick(object sender, MouseEventArgs e)
        {
            exitentry exitentry = new exitentry();
            exitentry.Show();
        }

        private void panel7_MouseClick(object sender, MouseEventArgs e)
        {
            // cria uma nova instância do formulário
            estoquemanager estoque = new estoquemanager();


            //exibe o formulário  estoquemanager
            estoque.Show();
        }

        private void panel8_MouseClick(object sender, MouseEventArgs e)
        {
            //  evento do painel não utilizado mantido devido a natureza do c# de ferrrar com tudo

        }

        private void pictureBox3_Click(object sender, EventArgs e)
        {
            deleteaccount delete = new deleteaccount();

            delete.Show();
        }
    }
}
