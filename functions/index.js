const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();

exports.resetDesks = functions.pubsub
    .schedule("0 0 * * *") // every day at midnight
    .timeZone("Europe/Zagreb")
    .onRun(async (context) => {
      const db = admin.database();

      const snapshot = await db.ref("desks").once("value");

      snapshot.forEach((child) => {
        child.ref.update({
          reservedByUserId: null,
        });
      });

      return null;
    });
