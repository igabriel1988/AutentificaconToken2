package com.example.autentificacontoken2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.autentificacontoken2.databinding.ActivityMainBinding
import com.example.autentificacontoken2.databinding.ActivitySecondBinding
import okhttp3.ResponseBody
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class MainActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding
    private lateinit var userClient: UserClient
    private lateinit var token : String

    var builder = Retrofit.Builder()
        .baseUrl( "http://192.168.100.3:8080/" )
        .addConverterFactory( GsonConverterFactory.create() )

    var retrofit = builder.build()


    override fun onCreate(savedInstanceState: Bundle?)
            {
                super.onCreate(savedInstanceState)
                binding = ActivityMainBinding.inflate( layoutInflater )
                setContentView( binding.root )

                userClient = retrofit.create( UserClient ::class.java )
                supportActionBar?.hide()
                binding.login.setOnClickListener{
                    login()
                }
            }

        private fun login(){
            var usuario = binding.username.text.toString()
            var password = binding.pasword.text.toString()
            if( !usuario.isEmpty() && !usuario.isEmpty() ){
                var login = Login( usuario, password )
                var call = userClient.login( login )
                call.enqueue( object:Callback<Usuario> {
                    override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                        if( response.isSuccessful ){
                            var token = response.body()?.token.toString()
                            Toast.makeText( binding.login.context, response.body()?.token.toString(), Toast.LENGTH_SHORT ).show()
                            var paso: Intent=Intent(binding.login.context, activity_second::class.java).apply {
                                putExtra( "token", token )
                            }
                            startActivity( paso )
                        }else{
                            Toast.makeText( binding.login.context, "Credenciales no validas", Toast.LENGTH_SHORT ).show()
                        }
                    }

                    override fun onFailure(call: Call<Usuario>, t: Throwable) {
                        Toast.makeText( binding.login.context, "error : (", Toast.LENGTH_SHORT ).show()
                    }
                })
            }else{
                Toast.makeText( binding.login.context, "Introduzca las credenciales", Toast.LENGTH_SHORT ).show()
            }
        }



}