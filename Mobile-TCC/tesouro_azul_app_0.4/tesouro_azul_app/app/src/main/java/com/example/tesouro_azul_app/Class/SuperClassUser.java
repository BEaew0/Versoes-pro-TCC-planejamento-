package com.example.tesouro_azul_app.Class;

import com.google.gson.annotations.SerializedName;

public class SuperClassUser {

    public static class Usuario {

        @SerializedName("nomE_USUARIO")
        private String nomeUsuario;

        @SerializedName("emaiL_USUARIO")
        private String emailUsuario;

        @SerializedName("datA_NASC_USUARIO")
        private String dataNascUsuario;

        @SerializedName("cpF_USUARIO")
        private String cpfUsuario;

        @SerializedName("cnpJ_USUARIO")
        private String cnpjUsuario;

        @SerializedName("iD_ASSINATURA_FK")
        private int idAssinaturaFk;

        @SerializedName("fotO_USUARIO")
        private String fotoUsuario;

        @SerializedName("senhA_USUARIO")
        private String senhaUsuario;

        @SerializedName("statuS_USUARIO")
        private String statusUsuario;

        // Construtor
        public Usuario(String nomeUsuario, String emailUsuario, String dataNascUsuario,
                       String cpfUsuario, String cnpjUsuario, int idAssinaturaFk,
                       String fotoUsuario, String senhaUsuario) {
            this.nomeUsuario = nomeUsuario;
            this.emailUsuario = emailUsuario;
            this.dataNascUsuario = dataNascUsuario;
            this.cpfUsuario = cpfUsuario;
            this.cnpjUsuario = cnpjUsuario;
            this.idAssinaturaFk = idAssinaturaFk;
            this.fotoUsuario = fotoUsuario;
            this.senhaUsuario = senhaUsuario;
        }

        // Getters e Setters
        public String getNomeUsuario() {
            return nomeUsuario;
        }

        public void setNomeUsuario(String nomeUsuario) {
            this.nomeUsuario = nomeUsuario;
        }

        public String getEmailUsuario() {
            return emailUsuario;
        }

        public void setEmailUsuario(String emailUsuario) {
            this.emailUsuario = emailUsuario;
        }

        public String getDataNascUsuario() {
            return dataNascUsuario;
        }

        public void setDataNascUsuario(String dataNascUsuario) {
            this.dataNascUsuario = dataNascUsuario;
        }

        public String getCpfUsuario() {
            return cpfUsuario;
        }

        public void setCpfUsuario(String cpfUsuario) {
            this.cpfUsuario = cpfUsuario;
        }

        public String getCnpjUsuario() {
            return cnpjUsuario;
        }

        public void setCnpjUsuario(String cnpjUsuario) {
            this.cnpjUsuario = cnpjUsuario;
        }

        public int getIdAssinaturaFk() {
            return idAssinaturaFk;
        }

        public void setIdAssinaturaFk(int idAssinaturaFk) {
            this.idAssinaturaFk = idAssinaturaFk;
        }

        public String getFotoUsuario() {
            return fotoUsuario;
        }

        public void setFotoUsuario(String fotoUsuario) {
            this.fotoUsuario = fotoUsuario;
        }

        public String getSenhaUsuario() {
            return senhaUsuario;
        }

        public void setSenhaUsuario(String senhaUsuario) {
            this.senhaUsuario = senhaUsuario;
        }

    }
        public class AtualizarCampoUsuarioDto {
            private String Campo;
            private String NovoValor;

            public AtualizarCampoUsuarioDto(String campo, String novoValor) {
                this.Campo = campo;
                this.NovoValor = novoValor;
            }

            // Getters e Setters...

            public String getCampo() {
                return Campo;
            }

            public void setCampo(String campo) {
                Campo = campo;
            }

            public String getNovoValor() {
                return NovoValor;
            }

            public void setNovoValor(String novoValor) {
                NovoValor = novoValor;
            }
        }

        public static class ImagemDto extends SuperClassProd.ImagemDto {

            @SerializedName("imagemBase64")
            private String imagemBase64;

            public ImagemDto(String imagemBase64) {
                this.imagemBase64 = imagemBase64;
            }

            public String getImagemBase64() {
                return imagemBase64;
            }

            public void setImagemBase64(String imagemBase64) {
                this.imagemBase64 = imagemBase64;
            }
        }


        //Para requisições de login
        public static class LoginRequestDto {
            private String EMAIL_USUARIO;
            private String SENHA_USUARIO;

            public LoginRequestDto(String email, String senha) {
                this.EMAIL_USUARIO = email;
                this.SENHA_USUARIO = senha;
            }

            // Getters e Setters
            public String getEMAIL_USUARIO() {
                return EMAIL_USUARIO;
            }

            public void setEMAIL_USUARIO(String EMAIL_USUARIO) {
                this.EMAIL_USUARIO = EMAIL_USUARIO;
            }

            public String getSENHA_USUARIO() {
                return SENHA_USUARIO;
            }

            public void setSENHA_USUARIO(String SENHA_USUARIO) {
                this.SENHA_USUARIO = SENHA_USUARIO;
            }
        }

        //Para respostas de login
        public static class LoginResponseDto {
            private String mensagem;
            private String token;

            // Getters e Setters
            public String getMensagem() {
                return mensagem;
            }

            public String getToken() {
                return token;
            }

            public void setMensagem(String mensagem) {
                this.mensagem = mensagem;
            }

            public void setToken(String token) {
                this.token = token;
            }
        }

        public static class UsuarioTokenDto {
            public int id;
            public String nome;
            public String email;
            // public String exp; data de expiração, se houver

            // Você pode adicionar outros campos conforme o payload do seu JWT
            public UsuarioTokenDto() {
                this.id = id;
                this.nome = nome;
                this.email = email;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getNome() {
                return nome;
            }

            public void setNome(String nome) {
                this.nome = nome;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }
        }
    }



