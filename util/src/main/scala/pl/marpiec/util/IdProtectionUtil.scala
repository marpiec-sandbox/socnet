package pl.marpiec.util



/**
 * http://www.numberempire.com/primenumbers.php.
 * Works for -347 to Integer.MAX_VALUE-347
 *
 * @author mpieciukiewicz
 */

object IdProtectionUtil {

  /** Podstawa modulo. */
  val MODULO = BigInt("7223372036854775801")
  /** Liczba przez jaka mnozymy komunikat. */
  val MULTIPLIER = BigInt("4203322016853775759")
  /** Odwrotnosc R w modulo N, do wyznaczenia komunikatu z szyfrogramu. */
  val INVERSED_MULTIPLIER = MULTIPLIER.modInverse(MODULO)
  /** System liczbowy w jakim zapisujemy szyfrogram. */
  val RADIX:Int = Character.MAX_RADIX
  /**
   * Zakłócenie, które utrudnia złamanie szyfru na podstawie komunikatów typu 1, 2, 3... (czyli
   * bardzo malych wartosci).
   */
  val SALT = BigInt("347")


  def encrypt(id:UID):String = {
    // Dodajemy zaklocenie
    val messageBI  = BigInt(id.uid) + SALT
    // Mnozymy modulo
    val cypherBI = messageBI * MULTIPLIER mod MODULO
    // Zamieniamy w liczbe w systemie 36
    return cypherBI.toString(RADIX)
  }

  def decrypt(cypher: String):UID = {
    try {
      // Zczytujemy liczbe w systemie 36
      val cypherBI = BigInt(cypher, RADIX)
      // Mnozymy przez odwrotnosc R
      val messageBI = cypherBI * INVERSED_MULTIPLIER mod MODULO
      // Odejmujemy zakłócenie
      new UID((messageBI - SALT).longValue)
    } catch {
      case e:NumberFormatException => return new UID(0)
    }
  }

}
