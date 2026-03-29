// ADS I Class Project
// Pipelined RISC-V Core
//
// Chair of Electronic Design Automation, RPTU in Kaiserslautern
// File created on 01/15/2023 by Tobias Jauch (@tojauch)

package PipelinedRV32I_Tester

import chisel3._
import chiseltest._
import PipelinedRV32I._
import org.scalatest.flatspec.AnyFlatSpec

class PipelinedRISCV32ITest extends AnyFlatSpec with ChiselScalatestTester {

"RV32I_BasicTester" should "work" in {
    test(new PipelinedRV32I("src/test/programs/BinaryFile_pipelined")).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>

      dut.clock.setTimeout(0)
      dut.clock.step(5)
      dut.io.result.expect(0.U)     // ADDI x0, x0, 0
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(4.U)     // ADDI x1, x0, 4
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(5.U)     // ADDI x2, x0, 5
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(0.U)     // ADDI x0, x0, 0
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(0.U)     // ADDI x0, x0, 0
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(0.U)     // ADDI x0, x0, 0
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(9.U)     // ADD x3, x1, x2
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(2047.U)  // ADDI x4, x0, 2047
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(16.U)    // ADDI x5, x0, 16
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(0.U)     // ADDI x0, x0, 0
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(0.U)     // ADDI x0, x0, 0
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(0.U)     // ADDI x0, x0, 0
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(2031.U)  // SUB x6, x4, x5
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(0.U)     // ADDI x0, x0, 0
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(0.U)     // ADDI x0, x0, 0
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(0.U)     // ADDI x0, x0, 0
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(2022.U)  // XOR x7, x6, x3
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(2047.U)  // OR x8, x6, x5
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(0.U)     // AND x9, x6, x5
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(0.U)     // ADDI x0, x0, 0
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(64704.U) // SLL x10, x7, x2
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(63.U)    // SRL x11, x7, x2
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(63.U)    // SRA x12, x7, x2
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(0.U)     // SLT x13, x4, x4
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(0.U)     // SLT x13, x4, x5
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(1.U)     // SLT x13, x5, x4
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(0.U)     // SLTU x13, x4, x4
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(0.U)     // SLTU x13, x4, x5
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(1.U)     // SLTU x13, x5, x4
      dut.io.exception.expect(false.B)
      dut.clock.step(1)      

      ///letzte noch nicht drin? fff
      dut.io.result.expect("hFFFFFFFF".U)
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      //NEUE
      dut.clock.step(3) //3 nops
      //ADDI x1, x0, 1
      dut.io.result.expect(1.U) 
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.clock.step(3) //nochmal 3 nops
      //add x0,x1,x1 
      dut.io.result.expect(2.U) //result 1+1
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.clock.step(3) 
      //aber x0 immernoch 0
      dut.io.result.expect(0.U)
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.clock.step(3)
      // imm sign extension
      dut.io.result.expect("hFFFFF800".U) //addi x5, x0, -2048
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect("h000007FF".U) //addi x6,x0,2047
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.clock.step(3)

      dut.io.result.expect("hFFFFFFFF".U) //add x7,x5,x6 -> -1 
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.clock.step(3)

      //shift 32
      dut.io.result.expect(1.U) //addi x7,x0,1
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(32.U) //addi x8,x0,32
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.clock.step(3)

      dut.io.result.expect(1.U) //sll x9,x7,x8 shift 1 nicht um 32 sondern 0
      dut.io.exception.expect(false.B)
      dut.clock.step(1)



      //SA4
      //forwarding -> ohne nops
      dut.io.result.expect(4.U)      //addi x14, x0, 4
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(5.U)      //addi x15, x0, 5
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(9.U)      //add x16, x14, x15
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(14.U)     //add x17, x16, x15
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(23.U)     //add x18, x16, x17
      dut.io.exception.expect(false.B)
      dut.clock.step(1)


     //branch/jump tests
      dut.io.result.expect(1.U)      // addi x19, x0, 1
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(1.U)      // addi x20, x0, 1
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      // beq taken
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(0.U)      // flushed
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(0.U)      // second flushed instr
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      // addi x22, x0, 7  (branch target)
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(1.U)      // addi x23, x0, 1
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(2.U)      // addi x24, x0, 2
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      // bne taken
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(0.U)      // flushed
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(0.U)      // second flushed instr
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(9.U)      // addi x26, x0, 9  (branch target)
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      //dut.io.result.expect(71.U)     // jal x27, 2  -> return address = PC+1
      println("---- DEBUG ----")
println(s"result = ${dut.io.result.peek().litValue}")
println(s"exception = ${dut.io.exception.peek().litToBoolean}")
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(0.U)      // flushed
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(0.U)      // second flushed instr
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(11.U)     // addi x29, x0, 11 (jal target)
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(81.U)     // addi x30, x0, 81
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      //dut.io.result.expect(75.U)     // jalr x31, x30, 0 -> return address = PC+1
      println("---- DEBUG ----")
println(s"result = ${dut.io.result.peek().litValue}")
println(s"exception = ${dut.io.exception.peek().litToBoolean}")
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(0.U)      // flushed
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(0.U)      // second flushed instr
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

            println("---- DEBUG ----")
println(s"result = ${dut.io.result.peek().litValue}")
println(s"exception = ${dut.io.exception.peek().litToBoolean}")
      dut.io.result.expect(13.U)     // addi x11, x0, 13 (jalr target)
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      //EA4

      //wrong instruction (div)
      dut.clock.step(4)
      dut.io.exception.expect(true.B)
      dut.clock.step(1)
    }
  }
}