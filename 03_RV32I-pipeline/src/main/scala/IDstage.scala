// ADS I Class Project
// Pipelined RISC-V Core - ID Stage
//
// Chair of Electronic Design Automation, RPTU in Kaiserslautern
// File created on 01/09/2026 by Tobias Jauch (@tojauch)

/*
Instruction Decode (ID) Stage: decoding and operand fetch

Extracted Fields from 32-bit Instruction (see RISC-V specification for reference):
    opcode: instruction format identifier
    funct3: selects variant within instruction format
    funct7: further specifies operation type (R-type only)
    rd: destination register address
    rs1: first source register address
    rs2: second source register address
    imm: 12-bit immediate value (I-type, sign-extended)

Register File Interfaces:
    regFileReq_A, regFileResp_A: read port for rs1 operand
    regFileReq_B, regFileResp_B: read port for rs2 operand

Internal Signals:
    Combinational decoders for instructions

Functionality:
    Decode opcode to determine instruction and identify operation (ADD, SUB, XOR, ...)
    Output: uop (operation code), rd, operandA (from rs1), operandB (rs2 or immediate)

Outputs:
    uop: micro-operation code (identifies instruction type)
    rd: destination register index
    operandA: first operand
    operandB: second operand 
    XcptInvalid: exception flag for invalid instructions
*/

package core_tile

import chisel3._
import chisel3.util._
import uopc._

// -----------------------------------------
// Decode Stage
// -----------------------------------------

//ToDo: Add your implementation according to the specification above here 

class IDStage extends Module {
    val io = IO(new Bundle {
        val instr = Input(UInt(32.W))

        val regFileReq_A = Output(UInt(5.W)) //register file interfaces
        val regFileResp_A = Input(UInt(32.W))
        val regFileReq_B = Output(UInt(5.W))
        val regFileResp_B = Input(UInt(32.W))

        val uop = Output(uopc.Type())
        val rd = Output(UInt(5.W))
        val operandA = Output(UInt(32.W))
        val operandB = Output(UInt(32.W))
        val XcptInvalid = Output(Bool())

        val wr_en = Output(Bool())

        //SA4
        val rs1 = Output(UInt(5.W))
        val rs2 = Output(UInt(5.W))

        val pc = Input(UInt(32.W))
        val outPC = Output(UInt(32.W))
        val imm = Output(UInt(32.W))

        val opBIsImm = Output(Bool())

        val inPredTaken = Input(Bool())
        val outPredTaken = Output(Bool())

        val inPredTarget = Input(UInt(32.W))
        val outPredTarget = Output(UInt(32.W))
        //EA4
    })
/*
    opcode: instruction format identifier
    funct3: selects variant within instruction format
    funct7: further specifies operation type (R-type only)
    rd: destination register address
    rs1: first source register address
    rs2: second source register address
    imm: 12-bit immediate value (I-type, sign-extended)
*/
    val opcode = io.instr(6,0)
    val funct3 = io.instr(14,12)
    val funct7 = io.instr(31,25)
    val rd = io.instr(11,7)
    val rs1 = io.instr(19,15)
    val rs2 = io.instr(24,20)
    val imm = Cat(Fill(20, io.instr(31)), io.instr(31,20))


    //SA4


    io.opBIsImm := false.B
    io.rs1 := rs1
    io.rs2 := rs2

    io.outPC := io.pc
    io.imm := 0.U

    io.outPredTaken := io.inPredTaken
    io.outPredTarget := io.inPredTarget

    val immB = Cat(Fill(19, io.instr(31)), io.instr(31), io.instr(7), io.instr(30,25), io.instr(11,8), 0.U(1.W))
    val immJ = Cat(Fill(11, io.instr(31)), io.instr(31), io.instr(19,12), io.instr(20), io.instr(30,21), 0.U(1.W))
    
    val isB = opcode === "b1100011".U
    val isJAL = opcode === "b1101111".U
    val isJALR = opcode === "b1100111".U
    //EA4


    io.regFileReq_A := rs1
    io.regFileReq_B := rs2

    io.uop := uopc.NOP
    io.rd := rd
    io.operandA := io.regFileResp_A
    io.operandB := 0.U
    io.XcptInvalid := false.B
    io.wr_en := false.B

    val isR = opcode === "b0110011".U //Opcode R
    val isI = opcode === "b0010011".U //Opcode I

    when(isR) {
        io.wr_en := (rd =/= 0.U)
        io.operandB := io.regFileResp_B
        io.opBIsImm := false.B

        switch(funct3) {
            is("b000".U) {
                when(funct7 === "b0000000".U) {io.uop := uopc.ADD}
                .elsewhen(funct7 === "b0100000".U) {io.uop := uopc.SUB}
                .otherwise {
                    io.uop := uopc.NOP
                    io.XcptInvalid := true.B
                    io.wr_en := false.B
                    }
            }
            is("b111".U) {io.uop := uopc.AND}
            is("b110".U) {io.uop := uopc.OR}
            is("b100".U) {io.uop := uopc.XOR}
            is("b001".U) {io.uop := uopc.SLL}
            is("b010".U) {io.uop := uopc.SLT}
            is("b011".U) {io.uop := uopc.SLTU}
            is("b101".U) {
                when(funct7 === "b0000000".U) {io.uop := uopc.SRL}
                .elsewhen(funct7 === "b0100000".U) {io.uop := uopc.SRA}
                .otherwise{
                    io.uop := uopc.NOP
                    io.XcptInvalid := true.B
                    io.wr_en := false.B
                }
            }
        }
    }.elsewhen(isI) {
        io.wr_en := (rd =/= 0.U)
        io.operandB := imm
        io.opBIsImm := true.B
        //default
        io.uop := uopc.NOP
        io.XcptInvalid := false.B

        switch(funct3) {
            is("b000".U) {io.uop := uopc.ADD}
            is("b111".U) {io.uop := uopc.AND}
            is("b110".U) {io.uop := uopc.OR}
            is("b100".U) {io.uop := uopc.XOR}
            is("b010".U) {io.uop := uopc.SLT}
            is("b011".U) {io.uop := uopc.SLTU}
            is("b001".U) {
                when(funct7 === "b0000000".U) {io.uop := uopc.SLL}
                .otherwise {
                    
                    io.XcptInvalid := true.B
                    io.wr_en := false.B
                }
            }
            is("b101".U) {
                when(funct7 === "b0000000".U) {io.uop := uopc.SRL}
                .elsewhen(funct7 === "b0100000".U) {io.uop := uopc.SRA}
                .otherwise {
                    
                    io.XcptInvalid := true.B
                    io.wr_en := false.B
                }
            }


        }
    }.elsewhen(isB) { //SA4
        io.wr_en := false.B
        io.operandB := io.regFileResp_B
        io.opBIsImm := false.B
        io.imm := immB
        io.uop := uopc.NOP
        io.XcptInvalid := false.B

        switch(funct3) {
            is("b000".U) {io.uop := uopc.BEQ}
            is("b001".U) {io.uop := uopc.BNE}
            is("b100".U) {io.uop := uopc.BLT}
            is("b101".U) {io.uop := uopc.BGE}
            is("b110".U) {io.uop := uopc.BLTU}
            is("b111".U) {io.uop := uopc.BGEU}
            is("b010".U) {
                io.XcptInvalid := true.B
                io.wr_en := false.B}
            is("b011".U) {
                io.XcptInvalid := true.B
                io.wr_en := false.B}
        }

    }.elsewhen(isJAL) {
        io.uop := uopc.JAL
        io.wr_en := (rd =/= 0.U)
        io.imm := immJ
        io.opBIsImm := true.B
    }.elsewhen(isJALR) {
        when(funct3 === "b000".U) {
            io.uop := uopc.JALR
            io.wr_en := (rd =/= 0.U)
            io.imm := imm
            io.opBIsImm := true.B
        }.otherwise {
            io.uop := uopc.NOP
            io.XcptInvalid := true.B
            io.wr_en := false.B
        }
    }.otherwise {
                io.uop := uopc.NOP
                io.XcptInvalid := true.B
                io.wr_en := false.B
                io.operandB := 0.U
    }

}