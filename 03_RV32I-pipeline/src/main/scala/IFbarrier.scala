// ADS I Class Project
// Pipelined RISC-V Core - IF Barrier
//
// Chair of Electronic Design Automation, RPTU in Kaiserslautern
// File created on 01/09/2026 by Tobias Jauch (@tojauch)

/*
IF-Barrier: pipeline register between Fetch and Decode stages

Internal Registers:
    instrReg: holds instruction between pipeline stages, initialized to 0

Inputs:
    inInstr: fetched instruction from IF stage

Outputs:
    outInstr: instruction to ID stage

Functionality:
    Save all input signals to a register and output them in the following clock cycle
*/

package core_tile

import chisel3._

// -----------------------------------------
// IF-Barrier
// -----------------------------------------

class IFBarrier extends Module {
  val io = IO(new Bundle {
    //ToDo: Add I/O ports
    val inInstr = Input(UInt(32.W))
    val outInstr = Output(UInt(32.W))

    //SA4
    val inPC = Input(UInt(32.W))
    val outPC = Output(UInt(32.W))

    val flush = Input(Bool())
    //EA4
  })

    val instrReg = RegInit(0.U(32.W))
    instrReg := io.inInstr
    io.outInstr := instrReg

    //SA4
    val pcReg = RegInit(0.U(32.W))
    pcReg := io.inPC
    io.outPC := pcReg

    when(io.flush) {
        instrReg := "h00000013".U //nop addi x0, x0, 0
        pcReg := 0.U
    }


//ToDo: Add your implementation according to the specification above here 

}
