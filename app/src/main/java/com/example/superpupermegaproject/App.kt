package com.example.superpupermegaproject

import android.app.Application
import com.example.superpupermegaproject.model.RemoteAPIInteractor
import com.example.superpupermegaproject.model.Repository

class App: Application() {
    companion object {
        private var repositoryInstance: Repository? = null
        private var APIInteractorInstance: RemoteAPIInteractor? = null

        fun getRepositoryInstance() : Repository = if(repositoryInstance==null) {
            Repository.getInstance(getAPIInteractorInstance())
        } else {
            repositoryInstance!!
        }


        fun getAPIInteractorInstance(): RemoteAPIInteractor =
            if(APIInteractorInstance==null) {
                RemoteAPIInteractor()
            } else {
                APIInteractorInstance!!
            }
    }
}