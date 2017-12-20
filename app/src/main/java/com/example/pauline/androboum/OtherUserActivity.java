package com.example.pauline.androboum;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class OtherUserActivity extends AppCompatActivity {
    UserListActivity.MyArrayAdapter adapter;
    private Profil user = new Profil();
    final List<Profil> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context c = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user);
        // on obtient l'intent utilisé pour l'appel
        Intent intent = getIntent();
        // on va chercher la valeur du paramètre position, et on renvoie zéro si ce paramètre n'est pas positionné (ce qui ne devrait
        // pas arriver dans notre cas).
        final int position = intent.getIntExtra("position",0);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v("AndroBoum", "Coucou");
                userList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    userList.add(child.getValue(Profil.class));
                }
                //adapter.notifyDataSetChanged();
                user = userList.get(position);

                // on va chercher les trois composants du layout
                ImageView imageProfilView = (ImageView) findViewById(R.id.imageUser);
                TextView textView = (TextView) findViewById(R.id.mailUser);
                ImageView imageConnectedView = (ImageView) findViewById(R.id.connectUser);

                // on télécharge dans le premier composant l'image du profil
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference photoRef = storage.getReference().child(user.getEmail() + "/photo.jpg");
                if (photoRef != null) {
                    Glide.with(c).using(new FirebaseImageLoader())
                            .load(photoRef)
                            .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                            .placeholder(R.drawable.ic_person_black_24dp)
                            .into(imageProfilView);
                }

                // on positionne le email dans le TextView
                textView.setText(user.getEmail());

                // si l'utilisateur n'est pas connecté, on rend invisible le troisième composant
                if (!user.isConnected) {
                    imageConnectedView.setVisibility(View.GONE );
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.v("AndroBoum", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.addValueEventListener(postListener);






    }


}
