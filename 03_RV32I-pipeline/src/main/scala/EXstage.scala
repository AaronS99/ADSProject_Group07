// ADS I Class Project
// Pipelined RISC-V Core - EX Stage
//
// Chair of Electronic Design Automation, RPTU in Kaiserslautern
// File created on 01/09/2026 by Tobias Jauch (@tojauch)

/*
Instruction Execute (EX) Stage: ALU operations and exception detection

Instantiated Modules:
    ALU: Integrate your module from Assignment02 for arithmetic/logical operations

ALU Interface:
    alu.io.operandA: first operand input
    alu.io.operandB: second operand input
    alu.io.operation: operation code controlling ALU function
    alu.io.aluResult: computation result output

Internal Signals:
    Map uopc codes to ALUOp values

Functionality:
    Map instruction uop to ALU operation code
    Pass operands to ALU
    Output results to pipeline

Outputs:
    aluResult: computation result from ALU
    exception: pass exception flag
*/

package core_tile

import chisel3._
import chisel3.util._
import Assignment02.{ALU, ALUOp}
import uopc._

// -----------------------------------------
// Execute Stage
// -----------------------------------------

class EXStage extends Module {
    val io = IO(new Bundle {
        val uop = Input(uopc.Type())
        val rd = Input(UInt(5.W))
        val operandA = Input(UInt(32.W))
        val operandB = Input(UInt(32.W))
        val XcptInvalid = Input(Bool())
        val wr_en = Input(Bool())

        val aluResult = Output(UInt(32.W))
        val outRD = Output(UInt(5.W))
        val outXcptInvalid = Output(Bool())
        val outwr_en = Output(Bool())
    })

    io.aluResult := 0.U
    io.outRD := io.rd
    io.outXcptInvalid := io.XcptInvalid
    io.outwr_en := io.wr_en

    val aluOp = WireDefault(ALUOp.ADD)

    switch(io.uop) {
        is(uopc.ADD) {aluOp := ALUOp.ADD}
        is(uopc.SUB) {aluOp := ALUOp.SUB}
        is(uopc.AND) {aluOp := ALUOp.AND}
        is(uopc.OR) {aluOp := ALUOp.OR}
        is(uopc.XOR) {aluOp := ALUOp.XOR}
        is(uopc.SLL) {aluOp := ALUOp.SLL}
        is(uopc.SRL) {aluOp := ALUOp.SRL}
        is(uopc.SRA) {aluOp := ALUOp.SRA}
        is(uopc.SLT) {aluOp := ALUOp.SLT}
        is(uopc.SLTU) {aluOp := ALUOp.SLTU}
        is(uopc.PASSB) {aluOp := ALUOp.PASSB}
        is(uopc.NOP) {aluOp := ALUOp.PASSB}
    }

    val alu = Module(new ALU)
    alu.io.operandA := io.operandA
    alu.io.operandB := io.operandB
    alu.io.operation := aluOp

    io.aluResult := alu.io.aluResult
}
//ToDo: Add your implementation according to the specification above here 
