package com.example.pauline.androboum;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    // variable globale pour spécifier si on filtre ou pas
    boolean filterConnected = false;
    MyArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final List<Profil> userList = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        ListView listeView = (ListView) findViewById(R.id.liste);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(myToolbar);
        
        adapter = new MyArrayAdapter(this, android.R.layout.simple_list_item_1, userList);
        listeView.setAdapter(adapter);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    userList.add(child.getValue(Profil.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.v("AndroBoum", "loadPost:onCancelled", databaseError.toException());
            }
        };

        mDatabase.addValueEventListener(postListener);
    }

    private void showFilterDialog() {
        // on crée un nouvel objet de type boite de dialogue
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // on lui affecte un titre, et une liste de choix possibles
        builder.setTitle(R.string.filter_dialog_title)
                .setSingleChoiceItems(R.array.users_filter, filterConnected ? 0 : 1, new DialogInterface.OnClickListener() {
                    @Override
                    // méthode appelée quand l'utilisateur fait un choix i contient le numéro du choix
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // si le premier item a été choisie, on filtre sur uniquement les utilisateurs connectés.
                        filterConnected = (i == 0);
                        // et on signale a l'adaptateur qu'il faut remettre la liste à jour.
                        adapter.notifyDataSetChanged();
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    // on a cliqué sur "ok", on ne fait rien.
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        // on crée la boite
        AlertDialog dialog = builder.create();
        // et on l'affiche
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actions1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filterConnected:
                Log.v("Androboum", "ocucofedjksl");
                showFilterDialog();

            default:
                // aucune action reconnue
                return super.onOptionsItemSelected(item);
        }
    }


}