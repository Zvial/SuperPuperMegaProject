package com.example.superpupermegaproject

import android.app.Application
import com.example.superpupermegaproject.model.RESTApiInteractor
import com.example.superpupermegaproject.model.Repository

class App: Application() {
    companion object {
        private var repositoryInstance: Repository? = null
        private var APIInteractorInstance: RESTApiInteractor? = null

        fun getRepositoryInstance() : Repository = if(repositoryInstance==null) {
            Repository.getInstance(getAPIInteractorInstance())
        } else {
            repositoryInstance!!
        }


        fun getAPIInteractorInstance(): RESTApiInteractor =
            if(APIInteractorInstance==null) {
                RESTApiInteractor()
            } else {
                APIInteractorInstance!!
            }
    }
}