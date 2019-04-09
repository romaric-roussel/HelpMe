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


        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item, resources.getStringArray(R.array.spinner_array)
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_message.adapter = adapter
        bt_send.setOnClickListener { v ->
            var author = et_user1.text.toString().trim()+"/"+et_user2.text.toString().trim()
            val pattern = Pattern.compile("^[a-zA-Z, /-]*$")
            val matcher = pattern.matcher(author)

            if(author.length>16){
                Toast.makeText(this,"Les noms dépasse la limite de 16 charactere",Toast.LENGTH_LONG).show()
            }else if (matcher.matches()){
                messageViewModel.newMessage(author,sp_message.selectedItem.toString())
                    .observe(this,messageResultDataObserver)
            }else {
                Toast.makeText(this,"Merci de ne pas mettre d'accent",Toast.LENGTH_SHORT).show()
            }

        }

        messageViewModel = ViewModelProviders.of(this).get(MessageViewModel::class.java)
        messageResultDataObserver= Observer {
            if(it.status.equals("ok")){
                Toast.makeText(this,"Demande envoyé",Toast.LENGTH_SHORT).show()
            }else {
                Toast.makeText(this,"erreur",Toast.LENGTH_SHORT).show()
            }

        }

    }

}
