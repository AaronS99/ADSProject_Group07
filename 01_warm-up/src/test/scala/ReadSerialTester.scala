// ADS I Class Project
// Chisel Introduction
//
// Chair of Electronic Design Automation, RPTU in Kaiserslautern
// File created on 18/10/2022 by Tobias Jauch (@tojauch)

package readserial

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import scala.util.Random

/** 
  *read serial tester
  */
class ReadSerialTester extends AnyFlatSpec with ChiselScalatestTester {

  "ReadSerial" should "work" in {
    test(new ReadSerial).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      //erstmal test nur 1er 2x ohne pause
      dut.io.rxd.poke(0.U)
      dut.io.reset_n.poke(0.U)
      dut.clock.step(1)
      for(i<-0 to 7) {
        dut.io.rxd.poke(1.U)
        dut.io.reset_n.poke(0.U)
        dut.io.valid.expect(0.U)
        dut.clock.step(1)
      }
      dut.io.valid.expect(1.U)
      dut.io.data.expect("b11111111".U)

      dut.io.rxd.poke(0.U)
      dut.io.reset_n.poke(0.U)
      dut.clock.step(1)
      dut.io.valid.expect(0.U) //valid nur 1 takt lang
      for(i<-0 to 7) {
        dut.io.rxd.poke(1.U)
        dut.io.reset_n.poke(0.U)
        dut.clock.step(1)
      }
      dut.io.valid.expect(1.U)
      dut.io.data.expect("b11111111".U)
         val rnd = new Random(123) //123 seed
      for(i<-0 to 7) { //testcase 0,8 random bit,0,8 random bit ... 8 mal
        val databyte = rnd.nextInt(256) //random byte
        dut.io.rxd.poke(0.U) //Anfangs 0 zum start
        dut.io.reset_n.poke(0.U)
        dut.clock.step(1)
        dut.io.valid.expect(0.U)
        for (j<-7 to 0 by -1) {
          val bit = (databyte >> j) & 1 //shift und lsb genommen aber 7 to 0 also msb to lsb
          dut.io.rxd.poke(bit.U)
          dut.io.reset_n.poke(0.U)
          //dut.io.valid.expect(0.U)
          dut.clock.step(1)

        }
        dut.io.valid.expect(1.U)
        dut.io.data.expect(databyte.U)
      //dut.io.valid.expect(1.U)
      //dut.io.data.expect(databyte.U)
      }

      //testcase reset in Übertragung
      val random = rnd.nextInt(256)
      dut.io.rxd.poke(0.U)
      dut.io.reset_n.poke(0.U)
      dut.clock.step(1)
      for (i<-7 to 4 by -1){
        val bit = (random >> i) & 1
        dut.io.rxd.poke(bit.U)
        dut.io.reset_n.poke(0.U)
        dut.clock.step(1)
      }
      dut.io.rxd.poke(((random >> 3) & 1).U)
      dut.io.reset_n.poke(1.U)
      dut.clock.step(1)
      for (i<-2 to 0 by -1) {
        val bit = (random >> i) & 1
        dut.io.rxd.poke(bit.U)
        dut.io.reset_n.poke(0.U)
        dut.clock.step(1)
      }

      dut.io.valid.expect(0.U)
      // valid 0, aber wenn danach das unterbrochene Byte weiter gesendet wird und weitere
      //neue Bytes hinterher kommt nicht der Output den man erwartet

      //testcase länger idle zwischendurch
      dut.io.rxd.poke(1.U)
      dut.io.reset_n.poke(1.U)
      dut.clock.step(1)

      for (i<-0 to 20) { //20 mal 1
        dut.io.rxd.poke(1.U)
        dut.io.reset_n.poke(0.U)
        dut.clock.step(1)
        dut.io.valid.expect(0.U)
      }

      for (i<-0 to 10) {
        val newRand = rnd.nextInt(256)
        dut.io.rxd.poke(0.U)
        dut.io.reset_n.poke(0.U)
        dut.clock.step(1)
        for (j<-7 to 0 by -1) {
          val bit = (newRand >> j) & 1
          dut.io.rxd.poke(bit.U)
          dut.io.reset_n.poke(0.U)
          dut.clock.step(1)
        }
        dut.io.valid.expect(1.U)
        dut.io.data.expect(newRand.U)

        for (j<-0 to 10) {
          dut.io.rxd.poke(1.U)
          dut.io.reset_n.poke(0.U)
          dut.clock.step(1)
          dut.io.valid.expect(0.U)
        }
      }



        /*dut.io.rxd.poke(...)
         *dut.clock.step(...)
         *dut.io.valid.expect(...)
         *dut.io.data.expect("b11111111".U) 
         *...
         *TODO: Add your testcases here
         */
        }
    } 
}

