package com.rohitksingh.nytarticles_kotlin

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main


/**
 *  This class Mocks fetching User from a Network call and Update on UI
 */
class FetchUser : AppCompatActivity() {

    lateinit var userTextView : TextView

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);

        userTextView = findViewById(R.id.counter)

        CoroutineScope(IO)
            .launch {
                fetchAndShowUserDetail()
            }

    }

    /**
     *  This looks async function but it is not
     *  fetchUser() runs on Background thread
     *  showUser() runs on Main Thread
     *  Coroutine makes this general Pattern simple to implement
     *  It uses DISPATCHER which makes switching Thread pretty simple
     *
     *  The Suspend keyword is the main ingredient.
     *  It suspends the fetchAndShowUserDetail() until fetchUser is completed and Resumes afterwards
     */
    private suspend fun fetchAndShowUserDetail(){
        var user = fetchUser()
        showUser(user)
    }

    /**
     *  This part of the function runs on the background thread
     *
     *  What is GlobalScope? async? await?
     */
    private suspend fun fetchUser() : User{
        return GlobalScope.async(IO) {
            delay(3000)
            User("Rohit", "r4rohit002@gmail.com")
        }.await()
    }


    /**
     * This runs on Main Thread
     * withContext switches The scope
     */
    private suspend fun showUser(user: User){
        withContext(Main){
            userTextView.setText(user.email)
        }
    }

}


/**
 * User Model class
 */
data class User(val name: String, val email: String)