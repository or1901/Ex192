package com.example.ex192;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * FBRef class:
 * an helper class for the DB - saves the pointers of the firebase DB.
 * @author Ori Roitzaid <or1901 @ bs.amalnet.k12.il>
 * @version	1
 * @since 12/4/2024
 */
public class FBRef {
    public static FirebaseDatabase FBDB = FirebaseDatabase.getInstance();

    public static DatabaseReference REF_STUDENTS = FBDB.getReference("Students");
}
