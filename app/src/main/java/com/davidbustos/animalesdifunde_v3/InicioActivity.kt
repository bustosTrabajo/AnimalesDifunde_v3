package com.davidbustos.animalesdifunde_v3

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_inicio.*
import kotlinx.android.synthetic.main.activity_registro.*
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

class InicioActivity : AppCompatActivity() {
    //Autentificación con Google
    private val GOOGLE_SIGN_IN = 100

    //Autentificación con Firebase
    private val auth = Firebase.auth
    lateinit var email: EditText
    lateinit var contrasena: EditText
    lateinit var url:String

    //Iniciar Base de Datos
    private val db= FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        btnEntrar.setOnClickListener(){
            login()
        }

        btnRegistro.setOnClickListener(){
            registro()
        }
        btnSalirAplicacion.setOnClickListener(){
            cerrarAplicacion()
        }
        btnGoogleLogin.setOnClickListener(){
            Toast.makeText(this,"Login con Google", Toast.LENGTH_LONG).show()
            /*
            //Configuración
            val googleConf:GoogleSignInOptions =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()

            val googleClient: GoogleSignInClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
             */

        }
        btnFacebookLogin.setOnClickListener{
            Toast.makeText(this,"Login con Facebook", Toast.LENGTH_LONG).show()
            //Configuración




        }




    }


    override fun onBackPressed(){

    }
    private fun cerrarAplicacion(){
        finish()
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
    private fun login(){
        if(etEmail.text.isNotEmpty() && etContrasena.text.isNotEmpty()) {
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(etEmail.text.toString(),
                    etContrasena.text.toString())
                .addOnCompleteListener{
                    if(it.isSuccessful){
                        var intent= Intent(this,AnimalesDifundeActivity::class.java)
                        guardarDatos()
                        startActivity(intent)
                    }else{
                        showAlert()
                    }
                }
        }
    }
    //Guardar datos del Usuario
    private fun guardarDatos(){
        val email=etEmail.text.toString()
        //val urlString=url.toString()
        val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE)
        val editor: SharedPreferences.Editor =sharedPreferences.edit()

        editor.putString("usuario",email)
        editor.apply()


    }
    //Guardar datos del Usuario
    private fun guardarDatos(email:String){
        //val urlString=url.toString()
        val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE)
        val editor: SharedPreferences.Editor =sharedPreferences.edit()

        editor.putString("usuario",email)
        editor.apply()


    }
    private fun registro(){
        var intent= Intent(this,RegistroActivity::class.java)
        startActivity(intent)

    }
    private fun showAlert(){
        val builder= AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == GOOGLE_SIGN_IN){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)

            try{
                val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)

                if(account!=null){
                    val credential: AuthCredential =
                        GoogleAuthProvider.getCredential(account.idToken,null)

                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener{
                        if(it.isSuccessful){
                            registrarUsuarioGoogle(account.email.toString())
                            var intent= Intent(this,AnimalesDifundeActivity::class.java)
                            startActivity(intent)
                        }else{
                            showAlert()
                        }
                    }

                }
            }catch(e: ApiException){
                showAlert()
            }

        }
    }
    private fun registrarUsuarioGoogle(email:String){
        //Recoger valores de nuestros RadioGroup
        db.collection("usuarios").document(email)
            .set(
                hashMapOf("nombre" to "",
                    "apellido" to "",
                    "correo" to email,
                    "telefono" to "",
                    "direccion" to "",
                    "soy" to ""
                )
            )
    }

}