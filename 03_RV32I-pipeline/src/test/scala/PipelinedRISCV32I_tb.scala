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
      val runBTB = false
      val oldCycles = 21
      val btbCycles = 12
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
      // forwarding tests without inserted NOPs
      dut.io.result.expect(4.U)      // addi x14, x0, 4        -> setup operand 1 for forwarding chain
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(5.U)      // addi x15, x0, 5        -> setup operand 2 for forwarding chain
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(9.U)      // add  x16, x14, x15     -> 4 + 5 = 9, first dependent result
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(14.U)     // add  x17, x16, x15     -> 9 + 5 = 14, requires forwarding of x16
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(23.U)     // add  x18, x16, x17     -> 9 + 14 = 23, multi-source forwarding case
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      if(!runBTB) {
     // branch/jump tests for 4.2

      // beq taken
      dut.io.result.expect(1.U)      // addi x19, x0, 1        -> first compare operand
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(1.U)      // addi x20, x0, 1        -> second compare operand, equal to x19
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.exception.expect(false.B) // beq x19, x20, +2      -> branch taken because 1 == 1
      dut.clock.step(1)

      dut.io.result.expect(0.U)      // flushed wrong-path slot after taken branch
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(0.U)      // second flushed / bubble slot caused by redirect
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(7.U)      // addi x22, x0, 7        -> correct branch target value
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      // blt: signed comparison edge case -> taken because -1 < 1
      dut.io.result.expect("hFFFFFFFF".U) // addi x23, x0, -1  -> signed negative operand
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(1.U)      // addi x24, x0, 1        -> signed positive operand
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.exception.expect(false.B) // blt x23, x24, +2      -> taken because -1 < 1 (signed)
      dut.clock.step(1)

      dut.io.result.expect(0.U)      // flushed wrong-path slot after taken blt
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(0.U)      // second flushed / bubble slot
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(9.U)      // addi x26, x0, 9        -> correct blt target
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      // bge: equality edge case -> taken because 5 >= 5
      dut.io.result.expect(5.U)      // addi x25, x0, 5        -> first compare operand
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(5.U)      // addi x26, x0, 5        -> second compare operand, equal
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.exception.expect(false.B) // bge x25, x26, +2      -> taken because equality counts as >=
      dut.clock.step(1)

      dut.io.result.expect(0.U)      // flushed wrong-path slot after taken bge
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(0.U)      // second flushed / bubble slot
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(11.U)     // addi x28, x0, 11       -> correct bge target
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      // bltu: unsigned comparison edge case -> NOT taken because 0xFFFFFFFF < 1 is false unsigned
      dut.io.result.expect("hFFFFFFFF".U) // addi x27, x0, -1  -> becomes 0xFFFFFFFF as unsigned
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(1.U)      // addi x28, x0, 1        -> compare against 1
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.exception.expect(false.B) // bltu x27, x28, +2     -> not taken because 0xFFFFFFFF is larger unsigned
      dut.clock.step(1)

      dut.io.result.expect(13.U)     // addi x29, x0, 13       -> sequential path executes because branch was NOT taken
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(94.U)     // addi x30, x0, 94       -> sequential path continues; prepares absolute jalr target
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      // bgeu: unsigned comparison edge case -> taken because 0xFFFFFFFF >= 1 is true unsigned
      dut.io.exception.expect(false.B) // bgeu x27, x28, +2     -> taken in unsigned domain
      dut.clock.step(1)

      dut.io.result.expect(0.U)      // flushed wrong-path slot after taken bgeu
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(0.U)      // second flushed / bubble slot
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(15.U)     // addi x6, x0, 15        -> correct bgeu target
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      // jal: unconditional jump, always taken
      dut.io.exception.expect(false.B) // jal x7, +2            -> jump taken unconditionally, writes return address to x7
      dut.clock.step(1)

      dut.io.result.expect(0.U)      // flushed wrong-path slot after jal
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(0.U)      // second flushed / bubble slot
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(16.U)     // addi x9, x0, 16        -> correct jal target
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      // jalr: unconditional indirect jump, always taken
      dut.io.result.expect(17.U)     // addi x11, x0, 17       -> visible WB value immediately before jalr in current timing
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.exception.expect(false.B) // jalr x31, x30, 0      -> jumps indirectly to absolute target held in x30 (=94)
      dut.clock.step(1)

      dut.io.result.expect(0.U)      // flushed wrong-path slot after jalr
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(0.U)      // second flushed / bubble slot
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(17.U)     // addi x11, x0, 17       -> correct jalr target at absolute PC 94
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      }
      else {
        // Skip 4.2 block in BTB mode
        // until marker values 123 and 124 appear.

        var found123 = false
        var found124 = false
        var guard = 0

        while (!found124 && guard < 200) {
          dut.io.exception.expect(false.B)
          val res = dut.io.result.peek().litValue

          if (!found123 && res == 123) {
            found123 = true
          } else if (found123 && res == 124) {
            found124 = true
          }

          dut.clock.step(1)
          guard += 1
        }

        assert(found123, "BTB test: marker 123 not seen")
        assert(found124, "BTB test: marker 124 not seen")

        // BTB tests

        // loop in BTB section should produce:
        // init/setup values 0,1,2,3 and loop-exit values 7,8,9,10,11.

      val seen = scala.collection.mutable.Set[BigInt]()
guard = 0
while (!(Set[BigInt](0,1,2,3,7,8,9,10,11).subsetOf(seen)) && guard < 120) {
  dut.io.exception.expect(false.B)
  seen += dut.io.result.peek().litValue
  dut.clock.step(1)
  guard += 1
}

        // exit values of the trained loops
        assert(seen.contains(BigInt(7)),  s"BTB test: missing exit value 7, seen=$seen")
        assert(seen.contains(BigInt(8)),  s"BTB test: missing exit value 8, seen=$seen")
        assert(seen.contains(BigInt(9)),  s"BTB test: missing exit value 9, seen=$seen")
        assert(seen.contains(BigInt(10)), s"BTB test: missing exit value 10, seen=$seen")
        assert(seen.contains(BigInt(11)), s"BTB test: missing exit value 11, seen=$seen")

        // loop body / setup values should also appear
        assert(seen.contains(BigInt(0)), s"BTB test: missing init value 0, seen=$seen")
        assert(seen.contains(BigInt(1)), s"BTB test: missing loop value 1, seen=$seen")
        assert(seen.contains(BigInt(2)), s"BTB test: missing loop value 2, seen=$seen")
        assert(seen.contains(BigInt(3)), s"BTB test: missing loop limit value 3, seen=$seen")
      }
      // wrong instruction / invalid opcode test at the very end of the binary
      dut.clock.step(4)
      //dut.io.exception.expect(true.B)
      dut.clock.step(1)
    }
}
}//nobtb 1116ns