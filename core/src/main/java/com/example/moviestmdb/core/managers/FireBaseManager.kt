package com.example.moviestmdb.core.managers

import com.example.moviestmdb.Movie
import com.example.moviestmdb.core.data.login.FirebaseAuthStateUserDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FireBaseManager @Inject constructor(
    val firebaseAuth: FirebaseAuth,
    val firebaseDataBase: FirebaseDatabase,
    val firebaseFireStore: FirebaseFirestore,
    val firebaseAuthStateUserDataSource: FirebaseAuthStateUserDataSource,
    val firebaseUser: FirebaseUser?
) {

    fun loginUser(email: String, pass: String) {
        firebaseAuth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.d("vlad Sign in success")
                    val user = firebaseAuth.currentUser
                } else {
                    Timber.d("Sign in fail ${task.exception}")
                }
            }
    }

    fun logout() {
        firebaseAuth.signOut()
    }

    fun updateProfile(name: String, phone : String, age : Int) {
        val userCollectionRef = firebaseFireStore.collection("User")
        val profile : Profile = Profile("vlad", "023423", 30)
        userCollectionRef.add(profile)
    }

    fun updateMovies(movie: Movie) {
        val moviesCollectionRef = firebaseFireStore.collection("Favourite movies")
        moviesCollectionRef.add(movie)
    }

    fun isConnected() : Flow<Boolean?>{
        return firebaseAuthStateUserDataSource.getBasicUserInfo().map {
            it?.isSignedIn()
        }
    }


//    fun updateProfile(name: String){
//        firebaseAuth.currentUser?.let {
//            val updates = UserProfileChangeRequest.Builder()
//                .setDisplayName(name)
//        }
//    }

    fun getDataBase() {
//        firebaseDataBase.
    }

    fun getUserInfo(): Flow<FirebaseUser?> =
        callbackFlow {
            val authStateListener = FirebaseAuth.AuthStateListener {
                trySend(it.currentUser)
            }
            firebaseAuth.addAuthStateListener(authStateListener)
            awaitClose {
                firebaseAuth.removeAuthStateListener(authStateListener)
            }
        }
}


data class Profile (
    val name: String,
    val phone: String,
    val age: Int)