package cloudator.util

object StringOps {
  implicit class StringOperations(str: String) {

    def fitToLen(len: Int, char: Char = ' '): String = {
      if (str.length >= len) str.substring(len)
      else {
        val padLength = len - str.length
        val pads = ("" + char) * padLength
        str.concat(pads)
      }
    }


  }

}