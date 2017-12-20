package com.example.pauline.androboum;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pauline on 19/12/2017.
 */

class MyArrayAdapter extends ArrayAdapter<Profil> {
    List<Profil> liste, origListe;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    boolean filterConnected = false;

    MyArrayAdapter(Context context, int resource, List<Profil> liste) {
        super(context, resource, liste);
        this.liste = liste;
        // on fait une copie de la liste dans une autre variable
        this.origListe = this.liste;

    }

    @Override
    public void notifyDataSetChanged() {
        if (filterConnected) {
            // on alloue une nouvelle liste et on la remplit uniquement avec les utilisateurs connectés.
            liste = new ArrayList<>();
            for (Profil p : origListe) {
                if (p.isConnected()) liste.add(p);
            }
            // sinon on reprend la liste complète que l'on avait sauvegardé dans la variable origListe.
        } else liste = origListe;
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // on va chercher le bon profil dans la liste
        Profil p = liste.get(position);

        // on instancie le layout sous la forme d'un objet de type View
        View layout = View.inflate(getContext(), R.layout.profil_list_item, null);

        // on va chercher les trois composants du layout
        ImageView imageProfilView = (ImageView) layout.findViewById(R.id.imageView);
        TextView textView = (TextView) layout.findViewById(R.id.textView);
        ImageView imageConnectedView = (ImageView) layout.findViewById(R.id.imageView2);

        // on télécharge dans le premier composant l'image du profil
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference photoRef = storage.getReference().child(p.getEmail() + "/photo.jpg");
        if (photoRef != null) {
            Glide.with(getContext()).using(new FirebaseImageLoader())
                    .load(photoRef)
                    .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.ic_person_black_24dp)
                    .into(imageProfilView);
        }

        // on positionne le email dans le TextView
        textView.setText(p.getEmail());

        // si l'utilisateur n'est pas connecté, on rend invisible le troisième composant
        if (!p.isConnected) {
            imageConnectedView.setVisibility(View.INVISIBLE);
        }

        // on retourne le layout
        return layout;
    }
}
