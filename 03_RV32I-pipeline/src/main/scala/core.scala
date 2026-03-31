// ADS I Class Project
// Pipelined RISC-V Core
//
// Chair of Electronic Design Automation, RPTU in Kaiserslautern
// File created on 01/15/2023 by Tobias Jauch (@tojauch)

/*
The goal of this task is to implement a 5-stage pipeline that features a subset of RV32I (all R-type and I-type instructions). 

    Instruction Memory:
        The CPU has an instruction memory (IMem) with 4096 words, each of 32 bits.
        The content of IMem is loaded from a binary file specified during the instantiation of the MultiCycleRV32Icore module.

    CPU Registers:
        The CPU has a program counter (PC) and a register file (regFile) with 32 registers, each holding a 32-bit value.
        Register x0 is hard-wired to zero.

    Microarchitectural Registers / Wires:
        Various signals are defined as either registers or wires depending on whether they need to be used in the same cycle or in a later cycle.

    Processor Stages:
        The FSM of the processor has five stages: fetch, decode, execute, memory, and writeback.
        All stages are active at the same time and process different instructions simultaneously.

        Fetch Stage:
            The instruction is fetched from the instruction memory based on the current value of the program counter (PC).

        Decode Stage:
            Instruction fields such as opcode, rd, funct3, and rs1 are extracted.
            For R-type instructions, additional fields like funct7 and rs2 are extracted.
            Control signals (isADD, isSUB, etc.) are set based on the opcode and funct3 values.
            Operands (operandA and operandB) are determined based on the instruction type.

        Execute Stage:
            Arithmetic and logic operations are performed based on the control signals and operands.
            The result is stored in the aluResult register.

        Memory Stage:
            No memory operations are implemented in this basic CPU.

        Writeback Stage:
            The result of the operation (writeBackData) is written back to the destination register (rd) in the register file.

    Check Result:
        The final result (writeBackData) is output to the io.check_res signal.
        The exception signal is also passed to the wrapper module. It indicates whether an invalid instruction has been encountered.
        In the fetch stage, a default value of 0 is assigned to io.check_res.
*/

package core_tile

import chisel3._
import chisel3.util._
import chisel3.util.experimental.loadMemoryFromFile
import Assignment02.{ALU, ALUOp}
import uopc._


class PipelinedRV32Icore (BinaryFile: String) extends Module {
  val io = IO(new Bundle {
    //ToDo: Add I/O ports
    val check_res    = Output(UInt(32.W)) 
    val exception = Output(Bool())
    val perf_totalBranches = Output(UInt(32.W))
    val perf_correctPred   = Output(UInt(32.W))
  })

//ToDo: Add your implementation according to the specification above here 

io.check_res := 0.U //default 0
io.exception := false.B //default false

val useBTB = false

val totalBranches = RegInit(0.U(32.W))
val correctPred   = RegInit(0.U(32.W))

val redirect = WireDefault(false.B)
val redirectPC = WireDefault(0.U(32.W))

val selectedPredTaken = WireDefault(false.B)
val selectedPredTarget = WireDefault(0.U(32.W))

    val ifStage = Module(new IF(BinaryFile))
    val ifBarrier = Module(new IFBarrier)
    ifBarrier.io.inInstr := ifStage.io.instr

    val btb = Module(new BTB)
    btb.io.PC := ifStage.io.pcOut
    btb.io.update := false.B
    btb.io.updatePC := 0.U
    btb.io.updateTarget := 0.U
    btb.io.mispredicted := false.B

    if(useBTB) {
        selectedPredTaken := btb.io.valid && btb.io.predictTaken
        selectedPredTarget := btb.io.target
    } else {
        selectedPredTaken := false.B
        selectedPredTarget := 0.U
    }
    

    val idStage = Module(new IDStage)
    val idBarrier = Module(new IDBarrier)
    val rf = Module(new regFile)

    idStage.io.instr := ifBarrier.io.outInstr
    rf.io.req_1.addr := idStage.io.regFileReq_A
    idStage.io.regFileResp_A := rf.io.resp_1.data
    rf.io.req_2.addr := idStage.io.regFileReq_B
    idStage.io.regFileResp_B := rf.io.resp_2.data
    idBarrier.io.inUOP := idStage.io.uop
    idBarrier.io.inRD := idStage.io.rd
    idBarrier.io.inOperandA := idStage.io.operandA
    idBarrier.io.inOperandB := idStage.io.operandB
    idBarrier.io.inXcptInvalid := idStage.io.XcptInvalid
    idBarrier.io.inwr_en := idStage.io.wr_en

    val exStage = Module(new EXStage)
    val exBarrier = Module(new EXBarrier)
    //ID -> EX
    exStage.io.uop := idBarrier.io.outUOP

    //exStage.io.operandA := idBarrier.io.outOperandA
    //exStage.io.operandB := idBarrier.io.outOperandB

    exStage.io.XcptInvalid := idBarrier.io.outXcptInvalid
    exStage.io.wr_en := idBarrier.io.outwr_en
    exStage.io.rd := idBarrier.io.outRD
    //exs->exb
    exBarrier.io.inAluResult := exStage.io.aluResult
    exBarrier.io.inRD := exStage.io.outRD
    exBarrier.io.inXcptInvalid := exStage.io.outXcptInvalid
    exBarrier.io.inwr_en := exStage.io.outwr_en

    val memStage = Module(new MEM)
    val memBarrier = Module(new MEMBarrier)
    //Mem Zeug überspringen?
    memStage.io.aluResult := exBarrier.io.outAluResult
    memStage.io.rd := exBarrier.io.outRD
    memStage.io.XcptInvalid := exBarrier.io.outXcptInvalid
    memStage.io.wr_en := exBarrier.io.outwr_en

    memBarrier.io.inAluResult := memStage.io.outAluResult
    memBarrier.io.inRD := memStage.io.outRD
    memBarrier.io.inXcptInvalid := memStage.io.outXcptInvalid
    memBarrier.io.inWr_en := memStage.io.outWr_en

    val wbStage = Module(new WBStage)
    val wbBarrier = Module(new WBBarrier)

    wbStage.io.aluResult := memBarrier.io.outAluResult
    wbStage.io.rd := memBarrier.io.outRD
    wbStage.io.XcptInvalid := memBarrier.io.outXcptInvalid
    wbStage.io.wr_en := memBarrier.io.outWr_en

    wbBarrier.io.inCheckRes := wbStage.io.check_res
    wbBarrier.io.inXcptInvalid := wbStage.io.XcptInvalid

    rf.io.req_3.addr := wbStage.io.regFileReq.addr
    rf.io.req_3.data := wbStage.io.regFileReq.data
    rf.io.req_3.wr_en := wbStage.io.regFileReq.wr_en

    io.check_res := wbBarrier.io.outCheckRes
    io.exception := wbBarrier.io.outXcptInvalid


    //SA4
    exStage.io.rs1 := idBarrier.io.outRS1
    exStage.io.rs2 := idBarrier.io.outRS2

    idBarrier.io.inRS1 := idStage.io.rs1
    idBarrier.io.inRS2 := idStage.io.rs2

    val fwd = Module(new ForwardingUnit)
    fwd.io.id_ex_rs1 := idBarrier.io.outRS1
    fwd.io.id_ex_rs2 := idBarrier.io.outRS2

    fwd.io.ex_mem_rd := exBarrier.io.outRD
    fwd.io.ex_mem_regWrite := exBarrier.io.outwr_en

    fwd.io.mem_wb_rd := memBarrier.io.outRD
    fwd.io.mem_wb_regWrite := memBarrier.io.outWr_en

    val operandA = WireDefault(idBarrier.io.outOperandA)
    val operandB = WireDefault(idBarrier.io.outOperandB)

    switch(fwd.io.forwardA) {
        is("b10".U) { operandA := exBarrier.io.outAluResult }
        is("b01".U) { operandA := memBarrier.io.outAluResult }
    }

    switch(fwd.io.forwardB) {
        is("b10".U) { operandB := exBarrier.io.outAluResult }
        is("b01".U) { operandB := memBarrier.io.outAluResult }
    }
    exStage.io.operandA := operandA
    when(idBarrier.io.outOpBisImm) { //bei immediate nicht forwarden
        exStage.io.operandB := idBarrier.io.outOperandB
    }.otherwise {
        exStage.io.operandB := operandB
    }


    val exIsCondBranch = idBarrier.io.outUOP.isOneOf(uopc.BEQ, uopc.BNE, uopc.BLT, uopc.BGE, uopc.BLTU, uopc.BGEU)



    val exIsJump = idBarrier.io.outUOP.isOneOf(uopc.JAL, uopc.JALR)
    val actualTaken = exStage.io.branchTaken
    val actualTarget = exStage.io.branchTarget
    val predTakenInEx = idBarrier.io.outPredTaken
    val predTargetInEx = idBarrier.io.outPredTarget
    val branchMispredict = exIsCondBranch && ((predTakenInEx =/= actualTaken) || (predTakenInEx && actualTaken && (predTargetInEx =/= actualTarget)))
    val jumpRedirect = exIsJump && actualTaken
    val correctNextPC = Mux(
        exIsCondBranch, Mux(actualTaken, actualTarget, idBarrier.io.outPC + 1.U), actualTarget
    )
    when(useBTB.B) {
        when(branchMispredict || jumpRedirect) {
            redirect := true.B
            redirectPC := correctNextPC
        }
        }.otherwise {
        when(exStage.io.branchTaken) {
            redirect := true.B
            redirectPC := exStage.io.branchTarget
        }
    }
    btb.io.update := useBTB.B && exIsCondBranch
    btb.io.updatePC := idBarrier.io.outPC
    btb.io.updateTarget := actualTarget
    btb.io.mispredicted := actualTaken

    idBarrier.io.inPC := idStage.io.outPC
    idBarrier.io.inImm := idStage.io.imm
    idBarrier.io.inOpBisImm := idStage.io.opBIsImm
    idStage.io.pc := ifBarrier.io.outPC
    idStage.io.inPredTaken := ifBarrier.io.outPredTaken
    idStage.io.inPredTarget := ifBarrier.io.outPredTarget
    idBarrier.io.inPredTaken := idStage.io.outPredTaken
    idBarrier.io.inPredTarget := idStage.io.outPredTarget

    //ifStage.io.redirect := exStage.io.branchTaken
    //ifStage.io.redirectPC := exStage.io.branchTarget

    ifBarrier.io.inPC := ifStage.io.pcOut
    //ifBarrier.io.flush := exStage.io.branchTaken

    exStage.io.pc := idBarrier.io.outPC
    exStage.io.imm := idBarrier.io.outImm

    //val doFlush = exStage.io.branchTaken
    //ifBarrier.io.flush := doFlush
    //idBarrier.io.flush := doFlush
    ifBarrier.io.flush := redirect
    idBarrier.io.flush := redirect

    ifStage.io.redirect := redirect
    ifStage.io.redirectPC := redirectPC
    ifStage.io.predictedTaken := selectedPredTaken
    ifStage.io.predictedTarget := selectedPredTarget
    ifBarrier.io.inPredTaken := selectedPredTaken
    ifBarrier.io.inPredTarget := selectedPredTarget


        when(exIsCondBranch) {
        totalBranches := totalBranches + 1.U
        when(!branchMispredict) {
            correctPred := correctPred + 1.U
        }
    }
    io.perf_totalBranches := totalBranches
    io.perf_correctPred   := correctPred


    //EA4

}
