package io.github.binaryfoo.decoders.annotator

import io.github.binaryfoo.tlv.BerTlv
import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.decoders.DecodeSession
import io.github.binaryfoo.crypto.RecoveredPublicKeyCertificate
import io.github.binaryfoo.decoders.SignedDataRecoverer
import io.github.binaryfoo.crypto.PublicKeyCertificate

trait Annotater {
    fun createNotes(session: DecodeSession, decoded: List<DecodedData>)

    public fun recoverText(signedData: String,
                       certificateOfSigner: RecoveredPublicKeyCertificate,
                       decode: (ByteArray, Int) -> String): String {
        if (certificateOfSigner.exponent == null) {
            return "Failed to recover: missing ${certificateOfSigner.name} exponent"
        } else {
            try {
                val recovered: ByteArray = SignedDataRecoverer().recover(signedData, certificateOfSigner.exponent!!, certificateOfSigner.modulus)
                return "Recovered using ${certificateOfSigner.name}:\n" + decode(recovered, certificateOfSigner.modulusLength)
            } catch(e: Exception) {
                return "Failed to recover: ${e}"
            }
        }
    }

    public fun recoverCertificate(signedData: String,
                       certificateOfSigner: PublicKeyCertificate,
                       decode: (ByteArray, Int) -> RecoveredPublicKeyCertificate): RecoveryResult {
        if (certificateOfSigner.exponent == null) {
            return RecoveryResult("Failed to recover: missing ${certificateOfSigner.name} exponent")
        } else {
            try {
                val recoveredBytes: ByteArray = SignedDataRecoverer().recover(signedData, certificateOfSigner.exponent!!, certificateOfSigner.modulus)
                val recoveredCertificate = decode(recoveredBytes, certificateOfSigner.modulusLength)
                return RecoveryResult("Recovered using ${certificateOfSigner.name}:\n${recoveredCertificate.textDump}", recoveredCertificate)
            } catch(e: Exception) {
                return RecoveryResult("Failed to recover: ${e}")
            }
        }
    }

    public data class RecoveryResult(public val text: String, public val certificate: RecoveredPublicKeyCertificate? = null)

}
