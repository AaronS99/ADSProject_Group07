// ADS I Class Project
// Chisel Introduction
//
// Chair of Electronic Design Automation, RPTU in Kaiserslautern
// File created on 18/10/2022 by Tobias Jauch (@tojauch)

package readserial

import chisel3._
import chisel3.util._


/** controller class */
class Controller extends Module{
  //Controller erkennt beginn, startet counter, counter sagt wenn stopp, controller sendet valid
  //Controller nimmt valid weg, wartet auf nächsten start
  val io = IO(new Bundle {
    val reset_n  = Input(UInt(1.W))
    val rxd = Input(UInt(1.W))
    val cnt_s = Input(UInt(1.W))
    val cnt_en = Output(UInt(1.W))
    val valid = Output(UInt(1.W))

    val stateController = Output(UInt(1.W))
    /* 
     * TODO: Define IO ports of a the component as stated in the documentation
     */
    })

  // internal variables
  /* 
   * TODO: Define internal variables (registers and/or wires), if needed
   */
     val running :: idle :: Nil = Enum(2)
     val state = RegInit(idle)
     val cnt_s_regOld = RegInit(0.U(1.W)) //damit valid 1 takt verzögert
     val cnt_s_regNew = RegInit(0.U(1.W))

  cnt_s_regNew := io.cnt_s
  io.valid:=0.U
  io.cnt_en:=0.U
  when(io.reset_n === 1.U) {
    state := idle
    io.valid := 0.U
    io.cnt_en := 0.U
  }.otherwise {

    switch(state) {
      is (idle) {

        when(io.rxd === 0.U) {
          state := running
          io.cnt_en := 1.U
        }.otherwise {
          io.cnt_en := 0.U
        }
        when(cnt_s_regOld === 1.U) {
          io.valid := 1.U
        }.otherwise {
          io.valid := 0.U
        }
      }
      is (running) {
        when(cnt_s_regNew === 1.U) {
          io.valid := 0.U
          io.cnt_en := 0.U
          state := idle
        }.otherwise {
          io.cnt_en := 1.U
          io.valid := 0.U
        }
      }
    }
  }
  cnt_s_regOld := cnt_s_regNew //hier damit im nächsten cycle das alte s0

  io.stateController := state
  // state machine
  /* 
   * TODO: Describe functionality if the controller as a state machine
   */

}


/** counter class */
class Counter extends Module{
  
  val io = IO(new Bundle {
    /* 
     * TODO: Define IO ports of a the component as stated in the documentation
     */
    val reset_n = Input(UInt(1.W))
    val cnt_en = Input(UInt(1.W))
    val cnt_s = Output(UInt(1.W))

    val stateCounter = Output(UInt(8.W))
    })

  // internal variables
  //val cnt_s_reg = RegInit(0.U(1.W))
  //io.cnt_s := cnt_s_reg //damit kein combloop
  /* 
   * TODO: Define internal variables (registers and/or wires), if needed
   */
    val s0 :: s1 :: s2 :: s3 :: s4 :: s5 :: s6 :: s7 :: Nil = Enum(8)
    val state = RegInit(s0)
  val done = RegInit(0.U(1.W))

    when(io.reset_n === 1.U) { //bei reset state reset & cnt_s 0
      state := s0
      //cnt_s_reg := 0.U
      //io.cnt_s := 0.U
    }.elsewhen(io.cnt_en === 0.U){
      state := s0
    }.otherwise {

      //cnt_s_reg := 0.U
      io.cnt_s := 0.U
      switch(state) {
        is (s0) {state := s1}
        is (s1) {state := s2}
        is (s2) {state := s3}
        is (s3) {state := s4}
        is (s4) {state := s5}
        is (s5) {state := s6}
        is (s6) {state := s7}
        is (s7) {
          //cnt_s_reg := 1.U
          //io.cnt_s := 1.U
          state := s0
        }
      }
    }
  when(state===s7) {
    io.cnt_s := 1.U
  }.otherwise {
    io.cnt_s := 0.U
  }
  io.stateCounter := state
  // state machine
  /* 
   * TODO: Describe functionality if the counter as a state machine
   */


}

/** shift register class */
class ShiftRegister extends Module{
  
  val io = IO(new Bundle {
    /* 
     * TODO: Define IO ports of a the component as stated in the documentation
     */
    val rxd = Input(UInt(1.W))
    val data = Output(UInt(8.W))
    })

  // internal variables
  /* 
   * TODO: Define internal variables (registers and/or wires), if needed
   */
  val registerShift = RegInit(0.U(8.W))
  io.data := registerShift //erst data setzen danach reg shift, damit data 1 cycle verzögert ist,
  // wenn valid =1 richtiges data
  registerShift := (registerShift << 1) + io.rxd //shift left + databit aufs neue

  // functionality
  /* 
   * TODO: Describe functionality if the shift register
   */
}

/** 
  * The last warm-up task deals with a more complex component. Your goal is to design a serial receiver.
  * It scans an input line (“serial bus”) named rxd for serial transmissions of data bytes. A transmission 
  * begins with a start bit ‘0’ followed by 8 data bits. The most significant bit (MSB) is transmitted first. 
  * There is no parity bit and no stop bit. After the last data bit has been transferred a new transmission 
  * (beginning with a start bit, ‘0’) may immediately follow. If there is no new transmission the bus line 
  * goes high (‘1’, this is considered the “idle” bus signal). In this case the receiver waits until the next 
  * transmission begins. The outputs of the design are an 8-bit parallel data signal and a valid signal. 
  * The valid signal goes high (‘1’) for one clock cycle after the last serial bit has been transmitted, 
  * indicating that a new data byte is ready.
  */
class ReadSerial extends Module{
  
  val io = IO(new Bundle {
    /* 
     * TODO: Define IO ports of a the component as stated in the documentation
     */
    val reset_n = Input(UInt(1.W))
    val rxd = Input(UInt(1.W))
    val data = Output(UInt(8.W))
    val valid = Output(UInt(1.W))
    val cnt_en = Output(UInt(1.W))
    val cnt_s = Output(UInt(1.W))
    val stateCounter = Output(UInt(8.W))
    val stateController = Output(UInt(1.W))
    })

  val controller = Module(new Controller)
  val counter = Module(new Counter)
  val shiftR = Module(new ShiftRegister)
  // instanciation of modules
  /* 
   * TODO: Instanciate the modules that you need
   */
  controller.io.rxd := io.rxd
  shiftR.io.rxd := io.rxd
  controller.io.reset_n := io.reset_n
  counter.io.reset_n := io.reset_n

  counter.io.cnt_en := controller.io.cnt_en
  controller.io.cnt_s := counter.io.cnt_s

  io.valid := controller.io.valid
  io.data := shiftR.io.data

  io.cnt_en := controller.io.cnt_en
  io.cnt_s := counter.io.cnt_s

  io.stateController := controller.io.stateController
  io.stateCounter := counter.io.stateCounter
  // connections between modules
  /* 
   * TODO: connect the signals between the modules
   */

  // global I/O 
  /* 
   * TODO: Describe output behaviour based on the input values and the internal signals
   */

}
