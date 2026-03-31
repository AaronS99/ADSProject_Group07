package PipelinedRV32I_Tester

import chisel3._
import chiseltest._
import PipelinedRV32I._
import org.scalatest.flatspec.AnyFlatSpec

class PipelinedRISCV32IPerfTest extends AnyFlatSpec with ChiselScalatestTester {

  "RV32I_PerformanceTester" should "measure cycles until end marker" in {
    test(new PipelinedRV32I("src/test/programs/Binary_performance"))
      .withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
 dut.clock.setTimeout(0)

        var cycles = 0
        var seenEndMarker = false

        while (!seenEndMarker && cycles < 500) {
          val res = dut.io.result.peek().litValue

          if (res == 123) {
            seenEndMarker = true
          } else {
            dut.clock.step(1)
            cycles += 1
          }
        }

        assert(seenEndMarker, s"end marker 123 not seen within $cycles cycles")
        val total = dut.io.perf_totalBranches.peek().litValue
        val correct = dut.io.perf_correctPred.peek().litValue

        println(s"Total branches = $total")
        println(s"Correct predictions = $correct")

        val acc = correct.toDouble / total.toDouble * 100
        println(f"Prediction accuracy = $acc%.2f %%")
        println(s"PERF_CYCLES = $cycles")
      }
  }
}


//no btb 474 cycles
//btb
//Total branches = 134
//Correct predictions = 122
//Prediction accuracy = 91.04 %
//PERF_CYCLES = 415

// 59/474 = 12.45% faster