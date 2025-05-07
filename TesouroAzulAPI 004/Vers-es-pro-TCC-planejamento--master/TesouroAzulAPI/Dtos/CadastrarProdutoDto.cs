using Microsoft.AspNetCore.Mvc.ModelBinding;
using System.ComponentModel.DataAnnotations;

namespace TesouroAzulAPI.Dtos
{
    public class CadastrarProdutoDto
    {
        public int ID_USUARIO { get; set; }
        [MaxLength(80)]
        public string COD_PRODUTO { get; set; }
        [MaxLength(20)]
        public string NOME_PRODUTO { get; set; }
        [MaxLength(40)]
        public string TIPO_PRODUTO { get; set; }
        public DateTime? DATA_VAL_PRODUTO { get; set; }
        public byte[]? IMG_PRODUTO { get; set; }

    }
}
