// ADS I Class Project
// Pipelined RISC-V Core - WB Stage
//
// Chair of Electronic Design Automation, RPTU in Kaiserslautern
// File created on 01/09/2026 by Tobias Jauch (@tojauch)


/*
Writeback (WB) Stage: result storage and register file updates

Register File Interface:
    regFileReq: write request bundle
        regFileReq.addr: destination register index
        regFileReq.data: result value to write
        regFileReq.wr_en: write enable signal

Inputs:
    aluResult: computation result from pipeline
    rd: destination register address

Internal Signals:
    Result forwarding paths
    Write enable control

Functionality:
    Forward aluResult to register file write port
    Set write address to rd
    Assert wr_en = true for all R-type and I-type instructions
    Output result on check_res for verification and debugging

Outputs:
    check_res: result value for verification
*/

package core_tile

import chisel3._

// -----------------------------------------
// Writeback Stage
// -----------------------------------------
class WBStage extends Module {
    val io = IO(new Bundle{
        val aluResult = Input(UInt(32.W))
        val rd = Input(UInt(5.W))
        val XcptInvalid = Input(Bool())
        val wr_en = Input(Bool())

        val check_res = Output(UInt(32.W))

        //regfileZeug
        val regFileReq = Output(new regFileWriteReq)
    })
    
    io.check_res := io.aluResult
    io.regFileReq.addr := io.rd
    io.regFileReq.data := io.aluResult
    when(io.wr_en && !io.XcptInvalid) {
        io.regFileReq.wr_en := true.B
    }
    .otherwise {
        io.regFileReq.wr_en := false.B
    }
    
}
//ToDo: Add your implementation according to the specification above here 
