package core_tile

import chisel3._
import chisel3.util._
import uopc._

class BTB extends Module {
    val io = IO(new Bundle {
        val PC = Input(UInt(32.W))
        val update = Input(Bool())
        val updatePC = Input(UInt(32.W))
        val updateTarget = Input(UInt(32.W))
        val mispredicted = Input(Bool())

        val valid = Output(Bool())
        val target = Output(UInt(32.W))
        val predictTaken = Output(Bool())
    })
    val strongNotTaken = "b00".U(2.W)
    val weakNotTaken = "b01".U(2.W)
    val weakTaken = "b10".U(2.W)
    val strongTaken = "b11".U(2.W)

    val validBits = RegInit(VecInit(Seq.fill(8)(VecInit(Seq.fill(2)(false.B)))))
    val tags = RegInit(VecInit(Seq.fill(8)(VecInit(Seq.fill(2)(0.U(29.W))))))
    val targets = RegInit(VecInit(Seq.fill(8)(VecInit(Seq.fill(2)(0.U(32.W))))))
    val states = RegInit(VecInit(Seq.fill(8)(VecInit(Seq.fill(2)(weakNotTaken)))))
    val lru = RegInit(VecInit(Seq.fill(8)(0.U(1.W))))

    def nextState(state: UInt, actualTaken: Bool): UInt = {
        val next = WireDefault(state)

        when(actualTaken) {
            switch(state) {
                is(strongNotTaken) {next := weakNotTaken}
                is(weakNotTaken) {next := weakTaken}
                is(weakTaken) {next := strongTaken}
                is(strongTaken) {next := strongTaken}
            }
        }.otherwise {
            switch(state) {
                is(strongNotTaken) {next := strongNotTaken}
                is(weakNotTaken) {next := strongNotTaken}
                is(weakTaken) {next := weakNotTaken}
                is(strongTaken) {next := weakTaken}
            }
        }
        next
    }

    val setIdx = io.PC(2, 0)
    val pcTag = io.PC(31, 3)

    val way0Hit = validBits(setIdx)(0) && (tags(setIdx)(0) === pcTag)
    val way1Hit = validBits(setIdx)(1) && (tags(setIdx)(1) === pcTag)

    io.valid := way0Hit || way1Hit
    io.target := 0.U
    io.predictTaken := false.B

        val upSet = io.updatePC(2, 0)
    val upTag = io.updatePC(31, 3)

    when(way0Hit) {
        io.target := targets(setIdx)(0)
        io.predictTaken := states(setIdx)(0)(1)
        when(!(io.update && (upSet === setIdx))) {
            lru(setIdx) := 1.U
        }

    }.elsewhen(way1Hit) {
        io.target := targets(setIdx)(1)
        io.predictTaken := states(setIdx)(1)(1)
        when(!(io.update && (upSet === setIdx))) {
            lru(setIdx) := 0.U
        }
    }



    val upWay0Hit = validBits(upSet)(0) && (tags(upSet)(0) === upTag)
    val upWay1Hit = validBits(upSet)(1) && (tags(upSet)(1) === upTag)

    val writeWay = Wire(UInt(1.W))
    writeWay := 0.U

    when(upWay0Hit) {
        writeWay := 0.U
    }.elsewhen(upWay1Hit) {
        writeWay := 1.U
    }.elsewhen(!validBits(upSet)(0)) {
        writeWay := 0.U
    }.elsewhen(!validBits(upSet)(1)) {
        writeWay := 1.U
    }.otherwise {
        writeWay := lru(upSet)
    }

    when(io.update) {
        when(upWay0Hit || upWay1Hit) {
            val oldState = states(upSet)(writeWay)
            val oldPredTaken = oldState(1)
            val actualTaken = io.mispredicted

            targets(upSet)(writeWay) := io.updateTarget
            states(upSet)(writeWay) := nextState(oldState, actualTaken)
        }.otherwise {
            val actualTaken = io.mispredicted

            validBits(upSet)(writeWay) := true.B
            tags(upSet)(writeWay) := upTag
            targets(upSet)(writeWay) := io.updateTarget
            states(upSet)(writeWay) := Mux(actualTaken, weakTaken, weakNotTaken)
        }

        when(writeWay === 0.U) {
            lru(upSet) := 1.U
        }.otherwise {
            lru(upSet) := 0.U
        }
    }

}