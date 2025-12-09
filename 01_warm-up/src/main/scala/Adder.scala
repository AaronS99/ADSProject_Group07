// ADS I Class Project
// Chisel Introduction
//
// Chair of Electronic Design Automation, RPTU in Kaiserslautern
// File created on 18/10/2022 by Tobias Jauch (@tojauch)

package adder

import chisel3._
import chisel3.util._


/** 
  * Half Adder Class 
  * 
  * Your task is to implement a basic half adder as presented in the lecture.
  * Each signal should only be one bit wide (inputs and outputs).
  * There should be no delay between input and output signals, we want to have
  * a combinational behaviour of the component.
  */
class HalfAdder extends Module{
  
  val io = IO(new Bundle {
    /* 
     * TODO: Define IO ports of a half adder as presented in the lecture
     */
         val a  = Input(UInt(1.W))
         val b  = Input(UInt(1.W))
         val c  = Output(UInt(1.W))  //carry
         val s  = Output(UInt(1.W))  //sum
    })

  /* 
   * TODO: Describe output behaviour based on the input values
   */
  io.c := io.a & io.b   //carry = a AND b
  io.s := io.a ^ io.b   //sum = a XOR b

}

/** 
  * Full Adder Class 
  * 
  * Your task is to implement a basic full adder. The component's behaviour should 
  * match the characteristics presented in the lecture. In addition, you are only allowed 
  * to use two half adders (use the class that you already implemented) and basic logic 
  * operators (AND, OR, ...).
  * Each signal should only be one bit wide (inputs and outputs).
  * There should be no delay between input and output signals, we want to have
  * a combinational behaviour of the component.
  */
class FullAdder extends Module{

  val io = IO(new Bundle {
    /* 
     * TODO: Define IO ports of a half adder as presented in the lecture
     */
    val a  = Input(UInt(1.W))
    val b  = Input(UInt(1.W))
    val ci = Input(UInt(1.W))  //carry in
    val co = Output(UInt(1.W)) //carry out
    val s  = Output(UInt(1.W))  //sum
    })


  /* 
   * TODO: Instanciate the two half adders you want to use based on your HalfAdder class
   */
   val halfAdderOne = Module(new HalfAdder)
   val halfAdderTwo = Module(new HalfAdder)

  /* 
   * TODO: Describe output behaviour based on the input values and the internal signals
   */
  halfAdderOne.io.a := io.a
  halfAdderOne.io.b := io.b
  halfAdderTwo.io.a := io.ci
  halfAdderTwo.io.b := halfAdderOne.io.s
  io.s := halfAdderTwo.io.s
  io.co := halfAdderOne.io.c | halfAdderTwo.io.c
}

/** 
  * 4-bit Adder class 
  * 
  * Your task is to implement a 4-bit ripple-carry-adder. The component's behaviour should 
  * match the characteristics presented in the lecture.  Remember: An n-bit adder can be 
  * build using one half adder and n-1 full adders.
  * The inputs and the result should all be 4-bit wide, the carry-out only needs one bit.
  * There should be no delay between input and output signals, we want to have
  * a combinational behaviour of the component.
  */
class FourBitAdder extends Module{

  val io = IO(new Bundle {
    val a = Input(UInt(4.W))
    val b = Input(UInt(4.W))
    val sum = Output(UInt(4.W))
    val c = Output(UInt(1.W))
    /* 
     * TODO: Define IO ports of a 4-bit ripple-carry-adder as presented in the lecture
     */
    })

  /* 
   * TODO: Instanciate the full adders and one half adderbased on the previously defined classes
   */
   val halfAdder = Module(new HalfAdder)
   val fA1 = Module(new FullAdder)
   val fA2 = Module(new FullAdder)
   val fA3 = Module(new FullAdder)

  /* 
   * TODO: Describe output behaviour based on the input values and the internal 
   */
  halfAdder.io.a := io.a(0)
  halfAdder.io.b := io.b(0)

  fA1.io.a := io.a(1)
  fA1.io.b := io.b(1)
  fA1.io.ci := halfAdder.io.c

  fA2.io.a := io.a(2)
  fA2.io.b := io.b(2)
  fA2.io.ci := fA1.io.co

  fA3.io.a := io.a(3)
  fA3.io.b := io.b(3)
  fA3.io.ci := fA2.io.co

  io.sum := Cat(
    fA3.io.s,
    fA2.io.s,
    fA1.io.s,
    halfAdder.io.s
  )
  /*
  io.sum(0) := halfAdder.io.s
  io.sum(1) := fA1.io.s
  io.sum(2) := fA2.io.s
  io.sum(3) := fA3.io.s

   */
  io.c := fA3.io.co
}
