package com.github.kmizu.scomb

object Calculator extends SComb {
  def expression: Parser[Int] = additive
  def additive: Parser[Int] = (multitive ~ (string("+") ~ multitive | string("-") ~ multitive).*).map{
    case (left, rights) =>
      rights.foldLeft(left) {
        case (result, ("+", right)) => result + right
        case (result, ("-", right)) => result - right
      }
  }
  def multitive: Parser[Int] = (primary ~ (string("*") ~ primary | string("/") ~ primary).*).map{
    case (left, rights) =>
      rights.foldLeft(left) {
        case (result, ("*", right)) => result * right
        case (result, ("/", right)) => result / right
      }
  }
  def primary: Parser[Int] = (for {
    _ <- string("(")
    e <- expression
    _ <- string(")")
  } yield e) | number
  def number: Parser[Int] = oneOf('0'to'9').*.map{digits => digits.mkString.toInt}

  def main(args: Array[String]): Unit = {
    println(expression("1+2*3"))
    println(expression("1+5*3/4"))
    println(expression("(1+5)*3/2"))
  }
}