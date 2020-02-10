package com.example.u2tema1;

public class ClienteRetrofit {
        private String Cod_persona;
        private String Nombre;
        private String Apellidos;

    public ClienteRetrofit(String Cod_persona, String Nombre, String Apellidos) {
            this.Cod_persona = Cod_persona;
            this.Nombre = Nombre;
            this.Apellidos = Apellidos;
        }

        public String getcodigo() {
            return Cod_persona;
        }

        public String getNombre() {
            return Nombre;
        }

        public String getApellido() {

            return Apellidos;
        }
}
