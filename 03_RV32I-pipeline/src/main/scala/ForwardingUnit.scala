package core_tile

import chisel3._
import uopc._

class ForwardingUnit extends Module {
    val io = IO(new Bundle {
        val id_ex_rs1 = Input(UInt(5.W))    //quellreg aus id/ex stage
        val id_ex_rs2 = Input(UInt(5.W))
        
        val ex_mem_rd = Input(UInt(5.W)) //zielreg und regwrite aus ex/mem
        val ex_mem_regWrite = Input(Bool())

        val mem_wb_rd = Input(UInt(5.W)) //zielreg und regwrite aus mem/wb
        val mem_wb_regWrite = Input(Bool())

        val forwardA = Output(UInt(2.W))
        val forwardB = Output(UInt(2.W))
    })
    //0->aus ID/EX | 1->aus EX/MEM | 2->aus MEM/WB
    io.forwardA := "b00".U //default 0
    io.forwardB := "b00".U

    when(io.ex_mem_regWrite && (io.ex_mem_rd =/= 0.U) && (io.ex_mem_rd === io.id_ex_rs1)) { //schreibt alt in neu reg?
        io.forwardA := "b10".U
    }.elsewhen(io.mem_wb_regWrite && (io.mem_wb_rd =/= 0.U) && (io.mem_wb_rd === io.id_ex_rs1)) { //schreibt alt in mem in neu reg?
        io.forwardA := "b01".U
    }

    when(io.ex_mem_regWrite && (io.ex_mem_rd =/= 0.U) && (io.ex_mem_rd === io.id_ex_rs2)) {
        io.forwardB := "b10".U
    }.elsewhen(io.mem_wb_regWrite && (io.mem_wb_rd =/= 0.U) && (io.mem_wb_rd === io.id_ex_rs2)) {
        io.forwardB := "b01".U
    }
}