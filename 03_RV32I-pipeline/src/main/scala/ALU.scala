// ToDo: Add your ALU implementation from Assignment02 here

package Assignment02

import chisel3._
import chisel3.util._
import chisel3.experimental.ChiselEnum

//ToDo: define AluOp Enum
object ALUOp extends ChiselEnum {
  val ADD, SUB, AND, OR, XOR, SLL, SRL, SRA, SLT, SLTU, PASSB = Value
}

class ALU extends Module {
  
  val io = IO(new Bundle {
    //ToDo: define IOs
    val operandA = Input(UInt(32.W))
    val operandB = Input(UInt(32.W))
    val operation = Input(ALUOp())
    val aluResult = Output(UInt(32.W))
  })

  io.aluResult := 0.U 
  when(!io.operation.isValid) {
    io.aluResult := 999.U
  }.otherwise {

 
  switch(io.operation) {
    is(ALUOp.ADD) {
      io.aluResult := io.operandA + io.operandB //a+b
    }
    is(ALUOp.SUB) {
      io.aluResult := io.operandA - io.operandB //a-b
    }
    is(ALUOp.AND) {
      io.aluResult := io.operandA & io.operandB //a and b
    }
    is(ALUOp.OR) {
      io.aluResult := io.operandA | io.operandB //a or b
    }
    is(ALUOp.XOR) {
      io.aluResult := io.operandA ^ io.operandB //a xor b
    }
    is(ALUOp.SLL) {
      io.aluResult := io.operandA << io.operandB(4, 0) //nur die 5 LSBs
    }
    is(ALUOp.SRL) {
      io.aluResult := io.operandA >> io.operandB(4, 0) //shift right um b
    }
    is(ALUOp.SRA) {
      io.aluResult := (io.operandA.asSInt >> io.operandB(4, 0)).asUInt //UINT as Signed INT nochmal back to UINT
    }
    is(ALUOp.SLT) {
      io.aluResult := (io.operandA.asSInt < io.operandB.asSInt).asUInt //nicht als bool
    }
    is(ALUOp.SLTU) {
      io.aluResult := (io.operandA < io.operandB).asUInt
    }
    is(ALUOp.PASSB) {
      io.aluResult := io.operandB
    }
  }
   }

  //ToDo: implement ALU functionality according to the task specification

}