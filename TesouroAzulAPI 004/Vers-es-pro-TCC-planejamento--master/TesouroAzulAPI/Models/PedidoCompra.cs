using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Diagnostics.Contracts;


namespace TesouroAzulAPI.Models
{
    [Table("TB_PEDIDO_COMPRA")]
    public class PedidosCompra
    {
        [Key, DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int ID_PEDIDO { get; set; }
        
        [Required]
        public int ID_USUARIO_FK { get; set; }

        [Required]
        public int ID_FORNECEDOR_FK { get; set; }

        [Required]
        public DateTime DATA_PEDIDO { get; set;} = DateTime.Now;


        [Required, Column(TypeName = "decimal(7,2)")]
        public decimal VALOR_PEDIDO { get; set;}

        [ForeignKey(nameof(ID_USUARIO_FK))]
        public Usuario Usuario { get; set; }

        [ForeignKey(nameof(ID_FORNECEDOR_FK))]
        public Fornecedor Fornecedor { get; set; }
    }

    [Table("TB_ITEM_COMPRA")]
    public class ItensCompra
    {
        [Key, DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int ID_ITEM_COMPRA { get; set; }

        [Required]
        public int ID_PRODUTO_FK { get; set; }

        [Required]
        public int ID_PEDIDO_FK { get; set; }

        [MaxLength(25)]
        public string LOTE_COMPRA { get; set; }

        [Required, Column(TypeName ="decimal(8,2)")]
        public decimal QUANTIDADE_ITEM_COMPRA { get; set;}

        [Required]
        public int N_ITEM_COMPRA { get; set;}

        [ForeignKey(nameof(ID_PRODUTO_FK))]
        public Produto Produto { get; set; }

        [ForeignKey(nameof(ID_PEDIDO_FK))]
        public PedidosCompra PedidoCompra { get; set; }

    }
}
