package com.vroste.adsclient.codec

import scodec.{Attempt, Codec, Err, codecs => scodecs}

/**
  * Codecs for PLC variable types
  */
trait AdsCodecs {
  val bool: Codec[Boolean] = scodecs.bool
  val byte: Codec[Byte] = scodecs.byte
  val word: Codec[Int] = scodecs.uint16L
  val dword: Codec[Long] = scodecs.uint32L
  val sint: Codec[Int] = scodecs.int8L
  val usint: Codec[Int] = scodecs.uint8L
  val int: Codec[Int] = scodecs.int16L
  val uint: Codec[Int] = scodecs.uint16L
  val dint: Codec[Int] = scodecs.int32L
  val udint: Codec[Long] = scodecs.uint32L
  val real: Codec[Float] = scodecs.floatL
  val lreal: Codec[Double] = scodecs.doubleL

  val maxDefaultStringLength = 80
  val string = stringN(maxDefaultStringLength)

  def stringN(maxLength: Int) = scodecs.cstring.narrow[String](s =>
    if (s.length <= maxLength) {
      Attempt.successful(s)
    } else {
      Attempt.failure(Err(s"String ${s} is longer than maximum string length ${maxLength}"))
    },
    identity)

  // TODO TIME, TIME_OF_DAY, DATE, DATE_AND_TIME, ENUMcomp
}

object CustomDataTypeReadableExample extends AdsCodecs {
  import scodec.codecs.StringEnrichedWithCodecContextSupport

  // How to define a codec for a custom data structure (case class)
  // It will be read and written as 3*4 bytes
  case class MyDummyObject(a: Int, b: Int, c: Boolean)

  val myDummyObjectCodec: Codec[MyDummyObject] = (("a" | int) :: ("b" | int) :: ("c" | bool)).as
}
