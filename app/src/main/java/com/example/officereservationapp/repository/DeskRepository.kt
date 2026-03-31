package com.example.officereservationapp.repository

import android.system.Os
import com.example.officereservationapp.model.Desk
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class DeskRepository {

    fun getDesks(): Flow<List<Desk>> = callbackFlow {
        val database = FirebaseDatabase.getInstance()
        val myReference = database.reference.child("desks")

        // 1. Create the listener
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val desksFetched = snapshot.children.mapNotNull {
                    it.getValue(Desk::class.java)
                }
                // 2. Send the new list into the flow
                trySend(desksFetched)
            }

            override fun onCancelled(error: DatabaseError) {
                //Os.close(error.toException())
            }
        }

        // 3. Attach the listener
        myReference.addValueEventListener(listener)

        // 4. IMPORTANT: Remove the listener when the Flow is no longer needed
        awaitClose {
            myReference.removeEventListener(listener)
        }
    }

    suspend fun checkIfDesksExist(): Boolean {
        val snapshot = FirebaseDatabase.getInstance()
            .getReference("desks")
            .get()
            .await()
        return snapshot.exists()
    }


    fun reserveDesk(deskId: Int?, userId: String, userName: String) {
        val database2 = FirebaseDatabase.getInstance()
        val referenceForUpdatingDatabase = database2.reference.child("desks")
        val referenceForUpdatingUserDatabase = database2.reference.child("users")
        referenceForUpdatingDatabase.child(deskId.toString()).child("reservedByUserId").setValue(userId)
        referenceForUpdatingDatabase.child(deskId.toString()).child("reservedByUserName").setValue(userName)
        referenceForUpdatingUserDatabase.child(userId).child("reservedDeskId").setValue(deskId.toString())

    }
    fun unreserveDesk(deskId: Int?, userId: String) {
        val database2 = FirebaseDatabase.getInstance()
        val referenceForUpdatingDatabase = database2.reference.child("desks")
        val referenceForUpdatingUserDatabase = database2.reference.child("users")
        referenceForUpdatingDatabase.child(deskId.toString()).child("reservedByUserId").setValue("")
        referenceForUpdatingDatabase.child(deskId.toString()).child("reservedByUserName").setValue("")
        referenceForUpdatingUserDatabase.child(userId).child("reservedDeskId").setValue("")

    }

    suspend fun createInitialDesksInDatabase() {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child("desks")

        val initialDesks = listOf(
            Desk(
                id = 1,
                xCoordinatePercentage = .15f,
                yCoordinatePercentage = 0.06f,
                reservedByUserId = ""
            ),
            Desk(
                id = 2,
                xCoordinatePercentage = 0.45f,
                yCoordinatePercentage = 0.06f,
                reservedByUserId = ""
            ),
            Desk(
                id = 3,
                xCoordinatePercentage = 0.75f,
                yCoordinatePercentage = 0.06f,
                reservedByUserId = ""
            ),

            Desk(
                id = 4,
                xCoordinatePercentage = 0.45f,
                yCoordinatePercentage = 0.26f,
                reservedByUserId = ""
            ),
            Desk(
                id = 5,
                xCoordinatePercentage = 0.75f,
                yCoordinatePercentage = 0.26f,
                reservedByUserId = ""
            ),

            Desk(
                id = 6,
                xCoordinatePercentage = 0.45f,
                yCoordinatePercentage = 0.36f,
                reservedByUserId = ""
            ),
            Desk(
                id = 7,
                xCoordinatePercentage = 0.75f,
                yCoordinatePercentage = 0.36f,
                reservedByUserId = ""
            ),

            Desk(
                id = 8,
                xCoordinatePercentage = 0.15f,
                yCoordinatePercentage = 0.56f,
                reservedByUserId = ""
            ),
            Desk(
                id = 9,
                xCoordinatePercentage = 0.45f,
                yCoordinatePercentage = 0.56f,
                reservedByUserId = ""
            ),
            Desk(
                id = 10,
                xCoordinatePercentage = 0.75f,
                yCoordinatePercentage = 0.56f,
                reservedByUserId = ""
            )
        )
        initialDesks.forEach { desk ->
            myRef.child(desk.id.toString()).setValue(desk).await()
        }


    }
}


