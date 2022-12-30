package app.lodemsu.authtestfirebase

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var btnGenerateOTP : Button
    lateinit var btnSignIn : Button
    lateinit var etPhoneNumber : EditText
    lateinit var etOTP : EditText

    lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var auth : FirebaseAuth
    lateinit var verificationCode:String
    lateinit var phoneNumber:String
    lateinit var otp:String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViews()
        StartFirebaseLogin()
        btnGenerateOTP.setOnClickListener {
            phoneNumber = etPhoneNumber.text.toString()
            Toast.makeText(this, phoneNumber, Toast.LENGTH_SHORT).show()
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks
            )

        }

        btnSignIn.setOnClickListener {
            otp =etOTP.text.toString()
            Toast.makeText(this, "OTP is $otp", Toast.LENGTH_SHORT).show()
            val credential = PhoneAuthProvider.getCredential(verificationCode, otp)
            SigninWithPhone(credential)

        }


    }

    private fun findViews() {
        btnGenerateOTP = findViewById(R.id.btn_generate_otp)
        btnSignIn = findViewById(R.id.btn_sign_in)

        etPhoneNumber = findViewById(R.id.et_phone_number)
        etOTP = findViewById(R.id.et_otp)
    }


    private fun StartFirebaseLogin() {

        auth = FirebaseAuth.getInstance()
        mCallbacks = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                Toast.makeText(this@MainActivity, "verification completed", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@MainActivity, "verification fialed", Toast.LENGTH_SHORT).show()
            }
                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    super.onCodeSent(verificationId, token)
                    verificationCode = verificationId
                    Toast.makeText(this@MainActivity, "Code sent", Toast.LENGTH_SHORT).show()
                }

        }
    }

    private fun SigninWithPhone(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this@MainActivity, SignedIn::class.java))
                    finish()
                    Toast.makeText(this@MainActivity, "Sign in successful", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(this@MainActivity, "Incorrect OTP", Toast.LENGTH_SHORT).show()
                }
            }
    }

}