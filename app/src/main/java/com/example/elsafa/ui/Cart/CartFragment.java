package com.example.elsafa.ui.Cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elsafa.HomeActivity;
import com.example.elsafa.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import Controller.CartRecyclerViewAdapter;
import Model.Item;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseAuth auth;
    private FirebaseFirestore fStore;
    private List<Item> items;
    private CartRecyclerViewAdapter adapter;
    private LinearLayout emptyCart, shopping;


    public CartFragment() {
        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        items = new ArrayList();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = root.findViewById(R.id.cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new CartRecyclerViewAdapter(items, getContext());
        recyclerView.setAdapter(adapter);

        emptyCart = root.findViewById(R.id.empty_cart);
        shopping = root.findViewById(R.id.shopping);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        getCartItems();
    }

    private void getCartItems() {
        fStore.collection("Users")
                .document(auth.getUid())
                .collection("Cart")
                .get()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                            items.add(ds.toObject(Item.class));
                            adapter.notifyDataSetChanged();
                        }

                        if (queryDocumentSnapshots.isEmpty()) {
                            getEmptyCartView();
                        }
                    }
                });
    }

    private void getEmptyCartView() {
        emptyCart.setVisibility(View.VISIBLE);
        shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), HomeActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });

    }
}