// ADS I Class Project
// Pipelined RISC-V Core - ID Barrier
//
// Chair of Electronic Design Automation, RPTU in Kaiserslautern
// File created on 01/09/2026 by Tobias Jauch (@tojauch)

/*
ID-Barrier: pipeline register between Decode and Execute stages

Internal Registers:
    uop: micro-operation code (from uopc enum)
    rd: destination register index, initialized to 0
    operandA: first source operand, initialized to 0
    operandB: second operand/immediate, initialized to 0

Inputs:
    inUOP: micro-operation code from ID stage
    inRD: destination register from ID stage
    inOperandA: first operand from ID stage
    inOperandB: second operand/immediate from ID stage
    inXcptInvalid: exception flag from ID stage

Outputs:
    outUOP: micro-operation code to EX stage
    outRD: destination register to EX stage
    outOperandA: first operand to EX stage
    outOperandB: second operand to EX stage
    outXcptInvalid: exception flag to EX stage
Functionality:
    Save all input signals to a register and output them in the following clock cycle
*/

package core_tile

import chisel3._
import uopc._

// -----------------------------------------
// ID-Barrier
// -----------------------------------------

//ToDo: Add your implementation according to the specification above here 
 class IDBarrier extends Module {
    val io = IO(new Bundle {
        val inUOP = Input(uopc.Type())
        val inRD = Input(UInt(5.W))
        val inOperandA = Input(UInt(32.W))
        val inOperandB = Input(UInt(32.W))
        val inXcptInvalid = Input(Bool())
        val inwr_en = Input(Bool())

        val outUOP = Output(uopc.Type())
        val outRD = Output(UInt(5.W))
        val outOperandA = Output(UInt(32.W))
        val outOperandB = Output(UInt(32.W))
        val outXcptInvalid = Output(Bool())
        val outwr_en = Output(Bool())
    })
    /*
        uop: micro-operation code (from uopc enum)
    rd: destination register index, initialized to 0
    operandA: first source operand, initialized to 0
    operandB: second operand/immediate, initialized to 0
    */

    val uopR = RegInit(uopc.NOP)
    val rdR = RegInit(0.U(5.W))
    val operandAR = RegInit(0.U(32.W))
    val operandBR = RegInit(0.U(32.W))
    val xcptInvalidR = RegInit(false.B) //reg für letztes
    val wr_enR = RegInit(false.B)

    uopR := io.inUOP
    rdR := io.inRD
    operandAR := io.inOperandA
    operandBR := io.inOperandB
    xcptInvalidR := io.inXcptInvalid
    wr_enR := io.inwr_en

    io.outUOP := uopR
    io.outRD := rdR
    io.outOperandA := operandAR
    io.outOperandB := operandBR
    io.outXcptInvalid := xcptInvalidR 
    io.outwr_en := wr_enR
 }