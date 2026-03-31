package com.example.officereservationapp.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().getReference("users")

    suspend fun registerNewUser(
        name: String,
        email: String,
        password: String,
        office: String
    ): Result<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            val userMap = mapOf(
                "name" to name,
                "email" to email,
                "office" to office
            )
            db.child(auth.currentUser?.uid ?: "").setValue(userMap).await()
            Result.success(Unit)

        } catch (e: FirebaseAuthUserCollisionException) {
            Result.failure(Exception("Email already in use"))

        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.failure(Exception("Invalid email format"))

        } catch (e: FirebaseAuthWeakPasswordException) {
            Result.failure(Exception("Password is too weak"))

        } catch (e: Exception) {
            Result.failure(e)
        }

        //if (userId != null) {

//        } else {
//            ""
//        }
//    }catch (e: FirebaseAuthUserCollisionException)
//    {
//        android.util.Log.e("AUTH_ERROR", "Registration failed: ${e.message}", e)
//
//    }
//    catch (e: Exception)
//    {
//        android.util.Log.e("AUTH_ERROR", "Registration failed: ${e.message}", e)
//    }
    }

    suspend fun loginUser(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()

            Result.success(Unit)
        } catch (e: FirebaseAuthInvalidUserException) {
            Log.d("DEBUG1", "$e")

            Result.failure(Exception("Invalid email or password"))


        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Log.d("DEBUG2", "$e")

            Result.failure(Exception("Invalid email or password"))

        } catch (e: Exception) {
            Log.d("DEBUG3", "$e")

            Result.failure(e)
        }
    }

    suspend fun fetchUsername(userId: String): String {
        return try {
            val snapshot = db.child(userId).child("name").get().await()
            snapshot.getValue(String::class.java) ?: ""
        } catch (e: Exception) {
            Log.e("DEBUG"," fetchUsrname ${e.message}")
            ""
        }

    }
    suspend fun fetchDeskId(userId: String): String {
        return try {
            val snapshot = db.child(userId).child("reservedDeskId").get().await()
            snapshot.getValue(String::class.java) ?: ""
        } catch (e: Exception) {
            Log.e("DEBUG"," fetchDeskId ${e.message}")
            ""
        }

    }
}
