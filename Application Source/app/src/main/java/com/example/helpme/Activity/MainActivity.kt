package com.example.helpme.Activity

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.widget.ArrayAdapter
import com.example.helpme.Data.Model.MessageResult
import com.example.helpme.R
import com.example.helpme.ViewModel.MessageViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.regex.Pattern


class MainActivity : BaseActivity(), LifecycleOwner {

    private lateinit var messageViewModel: MessageViewModel
    private lateinit var messageResultDataObserver: Observer<MessageResult>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //création d'un adapter pour la liste déroulante
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item, resources.getStringArray(R.array.spinner_array)
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_message.adapter = adapter

        //Ajout d'un listener sur le bouton send
        bt_send.setOnClickListener { v ->
            //Concaténation des 2 champs de saisit
            var author = et_user1.text.toString().trim()+"/"+et_user2.text.toString().trim()
            //Création d'un regex pour la validation des champs
            val pattern = Pattern.compile("^[a-zA-Z, /-]*$")
            //Si les champs sont OK
            val matcher = pattern.matcher(author)


            if(author.length>16){
                Toast.makeText(this,"Les noms dépasse la limite de 16 charactere",Toast.LENGTH_LONG).show()
            }else if (matcher.matches()){
                //Envoie du message et attribution de l'observer
                messageViewModel.newMessage(author,sp_message.selectedItem.toString())
                    .observe(this,messageResultDataObserver)
            }else {
                Toast.makeText(this,"Merci de ne pas mettre d'accent",Toast.LENGTH_SHORT).show()
            }

        }

        //Creation du viewModel
        messageViewModel = ViewModelProviders.of(this).get(MessageViewModel::class.java)
        //Initialisation de l'observer et action effectuer en fonction des valeurs de retour
        messageResultDataObserver= Observer {
            if(it.status.equals("ok")){
                Toast.makeText(this,"Demande envoyé",Toast.LENGTH_SHORT).show()
            }else {
                Toast.makeText(this,"erreur",Toast.LENGTH_SHORT).show()
            }

        }

    }

}
