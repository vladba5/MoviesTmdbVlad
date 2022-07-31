package com.example.moviestmdb.core.managers

import com.example.moviestmdb.Movie
import com.example.moviestmdb.core.data.login.FirebaseAuthStateUserDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FireBaseManager @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseAuthStateUserDataSource: FirebaseAuthStateUserDataSource,
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

//    fun updateProfile(name: String, phone : String, age : Int) {
//        val userCollectionRef = firebaseFireStore.collection("User")
//        val profile : Profile = Profile("vlad", "023423", 30)
//        userCollectionRef.add(profile)
//    }
//
//    fun updateMovies(movie: Movie) {
//        val moviesCollectionRef = firebaseFireStore.collection("Favourite movies")
//        moviesCollectionRef.add(movie)
//    }

    fun isConnected(): Flow<Boolean?> {
        return firebaseAuthStateUserDataSource.getBasicUserInfo().map {
            it?.isSignedIn()
        }
    }

    fun insertFavoriteMovies(movieId: Int): Flow<Boolean> {
        val favoriteMovies = HashMap<String, Any>()
        favoriteMovies[movieId.toString()] = true

        return callbackFlow {
            firebaseAuth.currentUser?.let { user ->
                firebaseDatabase.reference
                    .child("favorites")
                    .child(user.uid)
                    .updateChildren(favoriteMovies)
                    .addOnCompleteListener {
                        it.isSuccessful
                    }
            }
        }
    }


    fun removeFavoriteMovies(movieId: Int): Flow<Boolean> {
        return callbackFlow {
            firebaseAuth.currentUser?.let { user ->
                firebaseDatabase.reference
                    .child("favorites")
                    .child(user.uid)
                    .child(movieId.toString())
                    .removeValue()
                    .addOnCompleteListener {
                        it.isSuccessful
                    }
            }
        }
    }

    fun observeFavoritesMovies() = callbackFlow {
        val favStateListener: ((f: List<Int>) -> Unit) = { f ->
            trySend(f)
        }

        val favoritesEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                (snapshot.value as? Map<String, Boolean>)?.let {
                    val favList = mutableListOf<Int>()
                    it.keys.forEach {
                        favList.add(it.toInt())
                    }
                    favStateListener(favList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }

        val ref = firebaseAuth.currentUser?.let {
            firebaseDatabase.reference
                .child("favorites")
                .child(it.uid)
        }

        ref?.addValueEventListener(favoritesEventListener)
        awaitClose { ref?.removeEventListener(favoritesEventListener) }
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

//    fun updateProfile(name: String){
//        firebaseAuth.currentUser?.let {
//            val updates = UserProfileChangeRequest.Builder()
//                .setDisplayName(name)
//        }
//    }
}


data class Profile(
    val name: String,
    val phone: String,
    val age: Int
)