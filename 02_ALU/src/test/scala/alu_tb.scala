// ADS I Class Project
// Pipelined RISC-V Core with Hazard Detection and Resolution
//
// Chair of Electronic Design Automation, RPTU in Kaiserslautern
// File created on 10/31/2025 by Tobias Jauch (tobias.jauch@rptu.de)
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
      dut.io.aluResult.expect("h80000000".U(32.W))
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
      dut.io.aluResult.expect("hFFFFFFFF".U(32.W))
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



/*
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

      dut.io.operandA.poke(10.U)
      dut.io.operandB.poke(10.U)
      dut.io.operation.poke(ALUOp.ADD)
      dut.io.aluResult.expect(20.U)
      dut.clock.step(1)

      //ToDo: add more test cases for ADD operation

    //aufrufbar zum testen
    def tests(a: BigInt, b: BigInt, exp: BigInt): Unit = {
      dut.io.operandA.poke(a.U)
      dut.io.operandB.poke(b.U)
      dut.io.operation.poke(ALUOp.ADD)
      dut.io.aluResult.expect(exp.U)
      dut.clock.step(1)
    }

    tests(0, 0, 0) //0+0=0
    tests(BigInt("FFFFFFFF", 16), 1, 0)  //max 32 + 1 
    tests(BigInt("FFFFFFFF", 16), 10, 9)

    }
  }
}

// ---------------------------------------------------
// ToDo: Add test classes for all other ALU operations
//---------------------------------------------------

// Test SUB operation
class ALUSubTest extends AnyFlatSpec with ChiselScalatestTester {
  "ALU_Sub_Tester" should "test SUB operation" in {
    test(new ALU).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.clock.setTimeout(0)

    //aufrufbar zum testen
    def tests(a: BigInt, b: BigInt, exp: BigInt): Unit = {
      dut.io.operandA.poke(a.U)
      dut.io.operandB.poke(b.U)
      dut.io.operation.poke(ALUOp.SUB)
      dut.io.aluResult.expect(exp.U)
      dut.clock.step(1)
    }

    tests(50, 20, 30) 
    tests(0, 1, BigInt("FFFFFFFF", 16)) //0-1 = max
    tests(0, 0, 0)
    tests(0, 10, BigInt("FFFFFFF6", 16)) //0-10 = max-9

    }
  }
}

// Test AND operation
class ALUAndTest extends AnyFlatSpec with ChiselScalatestTester {
  "ALU_And_Tester" should "test AND operation" in {
    test(new ALU).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.clock.setTimeout(0)

    //aufrufbar zum testen
    def tests(a: BigInt, b: BigInt, exp: BigInt): Unit = {
      dut.io.operandA.poke(a.U)
      dut.io.operandB.poke(b.U)
      dut.io.operation.poke(ALUOp.AND)
      dut.io.aluResult.expect(exp.U)
      dut.clock.step(1)
    }

    tests(463, 0, 0) //irgendwas mit 0 immer 0 
    tests(32476, BigInt("FFFFFFFF", 16), 32476)  //irgendwas mit max = irgendwas
    //tests(0b10101010, 0b01010101, 0) //genau unterschied (zumindest 8 bits)
    tests(170, 85, 0) //genau unterschied (zumindest 8 bits)
    tests(12546, 12546, 12546) //gleich dann gleich
    tests(BigInt("F0FF00F0", 16), BigInt("FF0F0FF0", 16), BigInt("F00F00F0", 16)) //random check

    }
  }
}

// Test OR operation
class ALUOrTest extends AnyFlatSpec with ChiselScalatestTester {
  "ALU_Or_Tester" should "test OR operation" in {
    test(new ALU).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.clock.setTimeout(0)

    //aufrufbar zum testen
    def tests(a: BigInt, b: BigInt, exp: BigInt): Unit = {
      dut.io.operandA.poke(a.U)
      dut.io.operandB.poke(b.U)
      dut.io.operation.poke(ALUOp.OR)
      dut.io.aluResult.expect(exp.U)
      dut.clock.step(1)
    }

    tests(BigInt("12345678", 16), BigInt("12345678", 16), BigInt("12345678", 16)) //gleiches = gleiches
    tests(0, BigInt("FFFFFFFF", 16), BigInt("FFFFFFFF", 16))  //0s or 1s = 1s
    //tests(0b10101010, 0b01010101, 0b11111111) //abwechselnd 1
    tests(170, 85, 255) //abwechselnd 1
    }
  }
}

// Test XOR operation
class ALUXorTest extends AnyFlatSpec with ChiselScalatestTester {
  "ALU_Xor_Tester" should "test XOR operation" in {
    test(new ALU).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.clock.setTimeout(0)

    //aufrufbar zum testen
    def tests(a: BigInt, b: BigInt, exp: BigInt): Unit = {
      dut.io.operandA.poke(a.U)
      dut.io.operandB.poke(b.U)
      dut.io.operation.poke(ALUOp.XOR)
      dut.io.aluResult.expect(exp.U)
      dut.clock.step(1)
    }

    tests(BigInt("12345678", 16), BigInt("12345678", 16), 0) //gleiches = 0 
    tests(0, BigInt("FFFFFFFF", 16), BigInt("FFFFFFFF", 16))  //0 und 1 => 1
    //tests(0b10101010, 0b01010101, 0b11111111) //8bit unterschied
    tests(170, 85, 255) //8bit unterschied
    }
  }
}

// Test SLL operation
class ALUSllTest extends AnyFlatSpec with ChiselScalatestTester {
  "ALU_Sll_Tester" should "test SLL operation" in {
    test(new ALU).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.clock.setTimeout(0)

    //aufrufbar zum testen
    def tests(a: BigInt, b: BigInt, exp: BigInt): Unit = {
      dut.io.operandA.poke(a.U)
      dut.io.operandB.poke(b.U)
      dut.io.operation.poke(ALUOp.SLL)
      dut.io.aluResult.expect(exp.U)
      dut.clock.step(1)
    }

    tests(12345, 0, 12345) //shift um 0 = gleich 
    tests(0, 10, 0)  //0 shifted = 0
    tests(1, 31, BigInt("80000000", 16)) //2^31
    tests(1, 32, 1) //32 mehr als 5 bit -> erkennt shift um 0
    tests(1, 33, 2) //erkennt 1 -> =2
    tests(BigInt("80000000", 16), 1, 0) //rausgeshifted
    }
  }
}

// Test SRL operation
class ALUSrlTest extends AnyFlatSpec with ChiselScalatestTester {
  "ALU_Srl_Tester" should "test SRL operation" in {
    test(new ALU).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.clock.setTimeout(0)

    //aufrufbar zum testen
    def tests(a: BigInt, b: BigInt, exp: BigInt): Unit = {
      dut.io.operandA.poke(a.U)
      dut.io.operandB.poke(b.U)
      dut.io.operation.poke(ALUOp.SRL)
      dut.io.aluResult.expect(exp.U)
      dut.clock.step(1)
    }

    tests(432768, 0, 432768) //um 0 geshifted
    //tests(0b100000000, 3, 0b100000)  //3 nach rechts
    tests(256, 3, 32)  //3 nach rechts
    tests(BigInt("FFFFFFFF", 16), 4, BigInt("0FFFFFFF", 16)) //0en eingefügt
    tests(32467, 32, 32467) //32 letzte 5 bit = 0 -> 0 erkannt
    tests(1, 1, 0) //eins raus
    //tests(0b100, 33, 0b10) //1 erkannt -> 1 shift
    tests(4, 33, 2) //1 erkannt -> 1 shift
    }
  }
}

// Test SRA operation
class ALUSraTest extends AnyFlatSpec with ChiselScalatestTester {
  "ALU_Sra_Tester" should "test SRA operation" in {
    test(new ALU).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.clock.setTimeout(0)

    //aufrufbar zum testen
    def tests(a: BigInt, b: BigInt, exp: BigInt): Unit = {
      dut.io.operandA.poke(a.U)
      dut.io.operandB.poke(b.U)
      dut.io.operation.poke(ALUOp.SRA)
      dut.io.aluResult.expect(exp.U)
      dut.clock.step(1)
    }
    //SRA = Signed
    tests(432768, 0, 432768) //um 0 geshifted
    tests(64, 1, 32)  //positive zahlen normal
    tests(BigInt("80000000", 16), 1, BigInt("C0000000", 16)) //sign eingefügt -> aus 100000.. wird 110000..
    tests(BigInt("FFFFFFFF", 16), 4, BigInt("FFFFFFFF", 16)) //-1 bleibt
    tests(BigInt("80000000", 16), 31, BigInt("FFFFFFFF", 16)) //alles 1
    tests(3256, 32, 3256) //wieder >5bit
    tests(128, 33, 64) //auch wieder 1 erkannt
    }
  }
}

// Test SLT operation
class ALUSltTest extends AnyFlatSpec with ChiselScalatestTester {
  "ALU_Slt_Tester" should "test SLT operation" in {
    test(new ALU).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.clock.setTimeout(0)

    //aufrufbar zum testen
    def tests(a: BigInt, b: BigInt, exp: BigInt): Unit = {
      dut.io.operandA.poke(a.U)
      dut.io.operandB.poke(b.U)
      dut.io.operation.poke(ALUOp.SLT)
      dut.io.aluResult.expect(exp.U)
      dut.clock.step(1)
    }
//signed
    tests(BigInt("FFFFFFFF", 16), 0, 1) //-1 < 0 -> 1 
    tests(0, BigInt("FFFFFFFF", 16), 0)  //0 > -1 -> 0
    tests(50, 50, 0) //= nicht kleiner
    tests(BigInt("0FFFFFFF", 16), BigInt("FFFFFFFF", 16), 0) //groß > -1 -> 0
    tests(345672, 345673, 1) //kleiner -> 1
    }
  }
}

// Test SLTU operation
class ALUSltuTest extends AnyFlatSpec with ChiselScalatestTester {
  "ALU_Sltu_Tester" should "test SLTU operation" in {
    test(new ALU).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.clock.setTimeout(0)

    //aufrufbar zum testen
    def tests(a: BigInt, b: BigInt, exp: BigInt): Unit = {
      dut.io.operandA.poke(a.U)
      dut.io.operandB.poke(b.U)
      dut.io.operation.poke(ALUOp.SLTU)
      dut.io.aluResult.expect(exp.U)
      dut.clock.step(1)
    }

    tests(BigInt("FFFFFFFF", 16), 0, 0) //max > 0 -> 0 
    tests(0, BigInt("FFFFFFFF", 16), 1)  //0 < max -> 1
    tests(50, 50, 0) //= nicht kleiner
    tests(BigInt("0FFFFFFF", 16), BigInt("FFFFFFFF", 16), 1) //groß < max -> 1
    tests(345672, 345673, 1) //kleiner -> 1

    }
  }
}

// Test PASSB operation
class ALUPassbTest extends AnyFlatSpec with ChiselScalatestTester {
  "ALU_Passb_Tester" should "test PASSB operation" in {
    test(new ALU).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.clock.setTimeout(0)

    //aufrufbar zum testen HIER IN EXPECT DIREKT B
    def tests(a: BigInt, b: BigInt): Unit = {
      dut.io.operandA.poke(a.U)
      dut.io.operandB.poke(b.U)
      dut.io.operation.poke(ALUOp.PASSB)
      dut.io.aluResult.expect(b.U)
      dut.clock.step(1)
    }

    tests(50, 20) 
    tests(0, 1) 
    tests(0, 0)
    tests(BigInt("FFFFFFFF", 16), 32478)
    tests(3124, BigInt("FFFFFFFF", 16))

    }
  }
}

*/