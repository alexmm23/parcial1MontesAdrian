package com.example.parcial1movil2

class Solicitud(
    var curp: String = "",
    var nombre: String = "",
    var apellidos: String = "",
    var domicilio: String = "",
    var cantidadIngreso: Float,
    var tipoPrestamo: Int = 0
) {


    fun validarIngreso(): Boolean {
       var autorizado: Boolean = false
        if(tipoPrestamo == 0) return false
        //Prestamo personal
        if(cantidadIngreso < 1000){
            return false
        }

        if(tipoPrestamo == 1 && cantidadIngreso >= 20000 && cantidadIngreso <= 40000){
            //rango = "10 a 20mil"
            autorizado = true
        }
        //Negocio
        if(tipoPrestamo == 2 && cantidadIngreso >= 40000 && cantidadIngreso <= 60000){
            //rango = "40 a 60mil"
            autorizado = true
        }
        //vivienda
        if(tipoPrestamo == 3 && cantidadIngreso >=15000 && cantidadIngreso <= 35000){
             //rango = "15 a 35mil"
            autorizado = true
        }

        return autorizado
    }
}