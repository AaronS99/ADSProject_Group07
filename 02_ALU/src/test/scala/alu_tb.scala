// ADS I Class Project
// Pipelined RISC-V Core with Hazard Detection and Resolution
//
// Chair of Electronic Design Automation, RPTU in Kaiserslautern
// File created on 10/31/2025 by Tobias Jauch (tobias.jauch@rptu.de)

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import Assignment02._

// Test ADD operation
class ALUAddTest extends AnyFlatSpec with ChiselScalatestTester {
  "ALU_Add_Tester" should "test ADD operation" in {
    test(new ALU).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.clock.setTimeout(0)

      //normal addition
      dut.io.operandA.poke(10.U)
      dut.io.operandB.poke(10.U)
      dut.io.operation.poke(ALUOp.ADD)
      dut.io.aluResult.expect(20.U)
      dut.clock.step(1)

      //zero inputs
      dut.io.operandA.poke(0.U)
      dut.io.operandB.poke(0.U)
      dut.io.operation.poke(ALUOp.ADD)
      dut.io.aluResult.expect(0.U)
      dut.clock.step(1)

      //unsigned overflow
      dut.io.operandA.poke("hFFFFFFFF".U(32.W))
      dut.io.operandB.poke(1.U)
      dut.io.operation.poke(ALUOp.ADD)
      dut.io.aluResult.expect("h00000000".U(32.W))
      dut.clock.step(1)

      //signed overflow
      dut.io.operandA.poke("h7FFFFFFF".U(32.W))
      dut.io.operandB.poke(1.U)
      dut.io.operation.poke(ALUOp.ADD)
      dut.io.aluResult.expect("800000000".U(32.W))
      dut.clock.step(1)

      //signed negative addition
      dut.io.operandA.poke("hFFFFFFFF".U(32.W))
      dut.io.operandB.poke("hFFFFFFFE".U(32.W))
      dut.io.operation.poke(ALUOp.ADD)
      dut.io.aluResult.expect("hFFFFFFFD".U(32.W))
      dut.clock.step(1)
    }
  }
}

// ---------------------------------------------------
// ToDo: Add test classes for all other ALU operations
//---------------------------------------------------
class ALUSUBTest extends AnyFlatSpec with ChiselScalatestTester {
  "ALU_SUB_Tester" should "test SUB operation" in {
    test(new ALU).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.clock.setTimeout(0)

      //identical operands
      dut.io.operandA.poke(10.U)
      dut.io.operandB.poke(10.U)
      dut.io.operation.poke(ALUOp.SUB)
      dut.io.aluResult.expect(0.U)
      dut.clock.step(1)


      // unsigned underflow and zero - non zero
      dut.io.operandA.poke("h00000000".U(32.W))
      dut.io.operandB.poke("h00000001".U(32.W))
      dut.io.operation.poke(ALUOp.SUB)
      dut.io.aluResult.expect("hFFFFFFFF".U(32.W))
      dut.clock.step(1)

      //signed overflow negative
      dut.io.operandA.poke("hFFFFFFFF".U(32.W)) // -1
      dut.io.operandB.poke("h7FFFFFFF".U(32.W)) // 2^31 - 1
      dut.io.operation.poke(ALUOp.SUB)
      dut.io.aluResult.expect("h80000000".U(32.W))
      dut.clock.step(1)

      //signed overflow n
      dut.io.operandA.poke("hFFFFFFFF".U(32.W)) //-1
      dut.io.operandB.poke("h80000000".U(32.W)) //-2^31
      dut.io.operation.poke(ALUOp.SUB)
      dut.io.aluResult.expect("h7FFFFFFF".U(32.W)) //2^31-1
      dut.clock.step(1)

      // signed positive - negative
      dut.io.operandA.poke("h00000003".U(32.W)) //3
      dut.io.operandB.poke("hFFFFFFFE".U(32.W)) //-2
      dut.io.operation.poke(ALUOp.SUB)
      dut.io.aluResult.expect("h00000005".U(32.W))
      dut.clock.step(1)

    }
  }
}

class ALUANDTest extends AnyFlatSpec with ChiselScalatestTester {
  "ALU_AND_Tester" should "test AND operation" in {
    test(new ALU).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.clock.setTimeout(0)

      // and with 0
      dut.io.operandA.poke("h00000000".U(32.W))
      dut.io.operandB.poke("h0F0F0F00".U(32.W))
      dut.io.operation.poke(ALUOp.AND)
      dut.io.aluResult.expect("h00000000".U(32.W))
      dut.clock.step(1)

      // and identity
      dut.io.operandA.poke("hFFFFFFFF".U(32.W))
      dut.io.operandB.poke("h0F0F0F00".U(32.W))
      dut.io.operation.poke(ALUOp.AND)
      dut.io.aluResult.expect("h0F0F0F00".U(32.W))
      dut.clock.step(1)

      // alternate bits
      dut.io.operandA.poke("hAAAAAAAA".U(32.W))
      dut.io.operandB.poke("h55555555".U(32.W))
      dut.io.operation.poke(ALUOp.AND)
      dut.io.aluResult.expect("h00000000".U(32.W))
      dut.clock.step(1)
    }
    }
  }


class ALUORTest extends AnyFlatSpec with ChiselScalatestTester {
  "ALU_OR_Tester" should "test OR operation" in {
    test(new ALU).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.clock.setTimeout(0)

      // or with 0
      dut.io.operandA.poke("h00000000".U(32.W))
      dut.io.operandB.poke("h0F0F0F00".U(32.W))
      dut.io.operation.poke(ALUOp.OR)
      dut.io.aluResult.expect("h0F0F0F00".U(32.W))
      dut.clock.step(1)

      // or with 1 mask
      dut.io.operandA.poke("hFFFFFFFF".U(32.W))
      dut.io.operandB.poke("h0F0F0F00".U(32.W))
      dut.io.operation.poke(ALUOp.OR)
      dut.io.aluResult.expect("hFFFFFFFF".U(32.W))
      dut.clock.step(1)

      // alternate bits
      dut.io.operandA.poke("hAAAAAAAA".U(32.W))
      dut.io.operandB.poke("h55555555".U(32.W))
      dut.io.operation.poke(ALUOp.OR)
      dut.io.aluResult.expect("hFFFFFFFF".U(32.W))
      dut.clock.step(1)
    }
  }
}


class ALUXORTest extends AnyFlatSpec with ChiselScalatestTester {
  "ALU_XOR_Tester" should "test XOR operation" in {
    test(new ALU).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.clock.setTimeout(0)

      // xor identical operands
      dut.io.operandA.poke("h0F0F0F00".U(32.W))
      dut.io.operandB.poke("h0F0F0F00".U(32.W))
      dut.io.operation.poke(ALUOp.XOR)
      dut.io.aluResult.expect("h00000000".U(32.W))
      dut.clock.step(1)

      // xor with 0
      dut.io.operandA.poke("h00000000".U(32.W))
      dut.io.operandB.poke("h0F0F0F00".U(32.W))
      dut.io.operation.poke(ALUOp.XOR)
      dut.io.aluResult.expect("h0F0F0F00".U(32.W))
      dut.clock.step(1)

      // alternate bits
      dut.io.operandA.poke("hAAAAAAAA".U(32.W))
      dut.io.operandB.poke("h55555555".U(32.W))
      dut.io.operation.poke(ALUOp.XOR)
      dut.io.aluResult.expect("hFFFFFFFF".U(32.W))
      dut.clock.step(1)
    }
  }
}


class ALUSLLTest extends AnyFlatSpec with ChiselScalatestTester {
  "ALU_SLL_Tester" should "test SLL operation" in {
    test(new ALU).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.clock.setTimeout(0)

      //shift left logically with 0
      dut.io.operandA.poke("h00000FFF".U(32.W))
      dut.io.operandB.poke(0.U)
      dut.io.operation.poke(ALUOp.SLL)
      dut.io.aluResult.expect("h00000FFF".U(32.W))
      dut.clock.step(1)

      //shift left logically by max
      dut.io.operandA.poke("h00000FFF".U(32.W))
      dut.io.operandB.poke(31.U)
      dut.io.operation.poke(ALUOp.SLL)
      dut.io.aluResult.expect("h80000000".U(32.W))
      dut.clock.step(1)

      //shift left logically  5 bit mask
      dut.io.operandA.poke("h00000FFF".U(32.W))
      dut.io.operandB.poke("hF0000004".U(32.W))
      dut.io.operation.poke(ALUOp.SLL)
      dut.io.aluResult.expect("h0000FFF0".U(32.W))
      dut.clock.step(1)

      //shift left logically
      dut.io.operandA.poke("hFFF00000".U(32.W))
      dut.io.operandB.poke(4.U)
      dut.io.operation.poke(ALUOp.SLL)
      dut.io.aluResult.expect("hFF000000".U(32.W))
      dut.clock.step(1)
    }
  }
}


class ALUSRLTest extends AnyFlatSpec with ChiselScalatestTester {
  "ALU_SRL_Tester" should "test SRL operation" in {
    test(new ALU).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.clock.setTimeout(0)

      //shift Right logically with 0
      dut.io.operandA.poke("h00000FFF".U(32.W))
      dut.io.operandB.poke(0.U)
      dut.io.operation.poke(ALUOp.SRL)
      dut.io.aluResult.expect("h00000FFF".U(32.W))
      dut.clock.step(1)

      //shift Right logically by max
      dut.io.operandA.poke("h00000FFF".U(32.W))
      dut.io.operandB.poke(31.U)
      dut.io.operation.poke(ALUOp.SRL)
      dut.io.aluResult.expect("h00000000".U(32.W))
      dut.clock.step(1)

      //shift Right logically  5 bit mask
      dut.io.operandA.poke("h00000FFF".U(32.W))
      dut.io.operandB.poke("hF0000004".U(32.W))
      dut.io.operation.poke(ALUOp.SRL)
      dut.io.aluResult.expect("h000000FF".U(32.W))
      dut.clock.step(1)

      //shift Right logically
      dut.io.operandA.poke("hFFF00000".U(32.W))
      dut.io.operandB.poke(4.U)
      dut.io.operation.poke(ALUOp.SRL)
      dut.io.aluResult.expect("h0FFF0000".U(32.W))
      dut.clock.step(1)
    }
  }
}


class ALUSRATest extends AnyFlatSpec with ChiselScalatestTester {
  "ALU_SRA_Tester" should "test SRA operation" in {
    test(new ALU).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.clock.setTimeout(0)

      //sra with 0
      dut.io.operandA.poke("h00000FFF".U(32.W))
      dut.io.operandB.poke(0.U)
      dut.io.operation.poke(ALUOp.SRA)
      dut.io.aluResult.expect("h00000FFF".U(32.W))
      dut.clock.step(1)

      //sra negative
      dut.io.operandA.poke("h80000FFF".U(32.W))
      dut.io.operandB.poke(4.U)
      dut.io.operation.poke(ALUOp.SRA)
      dut.io.aluResult.expect("hF80000FF".U(32.W))
      dut.clock.step(1)

      //sra positive
      dut.io.operandA.poke("h00000FFF".U(32.W))
      dut.io.operandB.poke(4.U)
      dut.io.operation.poke(ALUOp.SRA)
      dut.io.aluResult.expect("h000000FF".U(32.W))
      dut.clock.step(1)

      //sra  5 bit mask
      dut.io.operandA.poke("h00000FFF".U(32.W))
      dut.io.operandB.poke("hF0000004".U(32.W))
      dut.io.operation.poke(ALUOp.SRA)
      dut.io.aluResult.expect("h000000FF".U(32.W))
      dut.clock.step(1)

    }
  }
}

class ALUSLTTest extends AnyFlatSpec with ChiselScalatestTester {
  "ALU_SLT_Tester" should "test SLT operation" in {
    test(new ALU).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.clock.setTimeout(0)

      //negative < positive
      dut.io.operandA.poke("h80000000".U(32.W))
      dut.io.operandB.poke("h0000000F".U(32.W))
      dut.io.operation.poke(ALUOp.SLT)
      dut.io.aluResult.expect("h00000001".U(32.W))
      dut.clock.step(1)

      //positive > negative
      dut.io.operandA.poke("h0000000F".U(32.W))
      dut.io.operandB.poke("h8000000F".U(32.W))
      dut.io.operation.poke(ALUOp.SLT)
      dut.io.aluResult.expect("h00000000".U(32.W))
      dut.clock.step(1)

      // equal operands
      dut.io.operandA.poke("h0000000F".U(32.W))
      dut.io.operandB.poke("h0000000F".U(32.W))
      dut.io.operation.poke(ALUOp.SLT)
      dut.io.aluResult.expect("h00000000".U(32.W))
      dut.clock.step(1)

      // both positive
      dut.io.operandA.poke("h0000000F".U(32.W))
      dut.io.operandB.poke("h000000FF".U(32.W))
      dut.io.operation.poke(ALUOp.SLT)
      dut.io.aluResult.expect("h00000001".U(32.W))
      dut.clock.step(1)

      // both negative
      dut.io.operandA.poke("h800000FF".U(32.W))
      dut.io.operandB.poke("h8000000F".U(32.W))
      dut.io.operation.poke(ALUOp.SLT)
      dut.io.aluResult.expect("h00000000".U(32.W))
      dut.clock.step(1)

      //edge cases
      dut.io.operandA.poke("h7FFFFFFF".U(32.W))
      dut.io.operandB.poke("hFFFFFFFF".U(32.W))
      dut.io.operation.poke(ALUOp.SLT)
      dut.io.aluResult.expect("h00000000".U(32.W))
      dut.clock.step(1)

      //0 case
      dut.io.operandA.poke("h00000000".U(32.W))
      dut.io.operandB.poke("hFFFFFFFF".U(32.W))
      dut.io.operation.poke(ALUOp.SLT)
      dut.io.aluResult.expect("h00000000".U(32.W))
      dut.clock.step(1)


    }
  }
}

class ALUSLTUTest extends AnyFlatSpec with ChiselScalatestTester {
  "ALU_SLTU_Tester" should "test SLTU operation" in {
    test(new ALU).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.clock.setTimeout(0)

      //0 case
      dut.io.operandA.poke("h00000000".U(32.W))
      dut.io.operandB.poke("hFFFFFFFF".U(32.W))
      dut.io.operation.poke(ALUOp.SLTU)
      dut.io.aluResult.expect("h00000001".U(32.W))
      dut.clock.step(1)

      //large number > small number
      dut.io.operandA.poke("hFFFFFFFF".U(32.W))
      dut.io.operandB.poke("h00000001".U(32.W))
      dut.io.operation.poke(ALUOp.SLTU)
      dut.io.aluResult.expect("h00000000".U(32.W))
      dut.clock.step(1)

      //large number > small number
      dut.io.operandA.poke("h80000001".U(32.W))
      dut.io.operandB.poke("h00000001".U(32.W))
      dut.io.operation.poke(ALUOp.SLTU)
      dut.io.aluResult.expect("h00000000".U(32.W))
      dut.clock.step(1)

      //same operands
      dut.io.operandA.poke("h80000001".U(32.W))
      dut.io.operandB.poke("h80000001".U(32.W))
      dut.io.operation.poke(ALUOp.SLTU)
      dut.io.aluResult.expect("h00000000".U(32.W))
      dut.clock.step(1)

      // max and zero
      dut.io.operandA.poke("hFFFFFFFF".U(32.W))
      dut.io.operandB.poke("h00000000".U(32.W))
      dut.io.operation.poke(ALUOp.SLTU)
      dut.io.aluResult.expect("h00000000".U(32.W))
      dut.clock.step(1)

    }
  }
}



class ALUPASSBTest extends AnyFlatSpec with ChiselScalatestTester {
  "ALU_PASSB_Tester" should "test PASSB operation" in {
    test(new ALU).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.clock.setTimeout(0)

      // zero case
      dut.io.operandA.poke("hFFFFFFFF".U(32.W))
      dut.io.operandB.poke("h00000000".U(32.W))
      dut.io.operation.poke(ALUOp.PASSB)
      dut.io.aluResult.expect("h00000000".U(32.W))
      dut.clock.step(1)

      // all ones case
      dut.io.operandA.poke("hFFFFFFFF".U(32.W))
      dut.io.operandB.poke("hFFFFFFFF".U(32.W))
      dut.io.operation.poke(ALUOp.PASSB)
      dut.io.aluResult.expect("h00000000".U(32.W))
      dut.clock.step(1)

      // random case
      dut.io.operandA.poke("hFFFFFF32".U(32.W))
      dut.io.operandB.poke("h12345678".U(32.W))
      dut.io.operation.poke(ALUOp.PASSB)
      dut.io.aluResult.expect("h12345678".U(32.W))
      dut.clock.step(1)

    }
  }
}


class ALUWrongOPTEST extends AnyFlatSpec with ChiselScalatestTester {
  "ALU_WrongOP_Tester" should "test WrongOP operation" in {
    test(new ALU).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.clock.setTimeout(0)

      dut.io.operandA.poke("hFFFFFF32".U(32.W))
      dut.io.operandB.poke("h12345678".U(32.W))
      dut.io.operation.poke(0.U)
      dut.io.aluResult.expect("h00000000".U(32.W))
      dut.clock.step(1)

    }
  }
}



