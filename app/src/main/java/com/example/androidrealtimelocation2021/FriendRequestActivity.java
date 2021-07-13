package com.example.androidrealtimelocation2021;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.androidrealtimelocation2021.Interface.IFirebaseLoadDone;
import com.example.androidrealtimelocation2021.Model.User;
import com.example.androidrealtimelocation2021.Utils.Common;
import com.example.androidrealtimelocation2021.ViewHolder.FriendRequestViewHolder;
import com.example.androidrealtimelocation2021.ViewHolder.UserViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

public class FriendRequestActivity extends AppCompatActivity implements IFirebaseLoadDone {
    FirebaseRecyclerAdapter<User, FriendRequestViewHolder> adapter, searchAdapter;
    RecyclerView recycler_all_user;
    IFirebaseLoadDone firebaseLoadDone;

    MaterialSearchBar searchBar;
    List<String> suggestList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);

        //Init View
        searchBar= (MaterialSearchBar)findViewById(R.id.material_search_bar);
        searchBar.setCardViewElevation(10);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> suggest=new ArrayList<>();
                for(String search:suggestList){
                    if(search.toLowerCase().contains(searchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                searchBar.setLastSuggestions(suggest);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(!enabled){
                    if(adapter!= null){
                        //if close search, restore defaults
                        recycler_all_user.setAdapter(adapter);

                    }
                }

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text.toString());

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
        recycler_all_user=(RecyclerView)findViewById(R.id.recycler_all_people);
        recycler_all_user.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recycler_all_user.setLayoutManager(layoutManager);
        recycler_all_user.addItemDecoration(new DividerItemDecoration(this,((LinearLayoutManager)layoutManager).getOrientation()));

        firebaseLoadDone=this;

        loadFriendRequestList();
        loadSearchData();
    }

    private void startSearch(String toString) {
    }

    private void loadFriendRequestList() {
        Query query= FirebaseDatabase.getInstance().getReference().child(Common.USER_INFORMATION)
                .child(Common.loggedUser.getUid())
                .child(Common.FRIEND_REQUEST);

        FirebaseRecyclerOptions<User> options= new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(query,User.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<User, FriendRequestViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FriendRequestViewHolder holder, int i, @NonNull final User model) {

                holder.txt_user_email.setText(model.getEmail());
                holder.btn_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteFriendRequest(model,false);
                        addToAcceptList(model);
                        addUserToFriendContact(model);

                    }
                });

                holder.btn_decline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Deleting Friend Request
                        deleteFriendRequest(model,true);

                    }
                });


            }

            @NonNull
            @Override
            public FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View itemView= LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.layout_friend_request,viewGroup,false);
                return new FriendRequestViewHolder(itemView);
            }
        };

        adapter.startListening();
        recycler_all_user.setAdapter(adapter);

    }

    private void addUserToFriendContact(User model) {
        //Friend aadd user
        DatabaseReference acceptList = FirebaseDatabase.getInstance()
                .getReference(Common.USER_INFORMATION)
                .child(model.getUid())
                .child(Common.ACCEPT_LIST);
        acceptList.child(model.getUid()).setValue(Common.loggedUser);
    }

    private void addToAcceptList(User model) {
        //User add Friend
        DatabaseReference acceptList = FirebaseDatabase.getInstance()
                .getReference(Common.USER_INFORMATION)
                .child(Common.loggedUser.getUid())
                .child(Common.ACCEPT_LIST);
        acceptList.child(model.getUid()).setValue(model);
    }

    private void deleteFriendRequest(final  User model,final boolean b) {
        DatabaseReference friendRequest = FirebaseDatabase.getInstance()
                .getReference(Common.USER_INFORMATION)
                .child(Common.loggedUser.getUid())
                .child(Common.FRIEND_REQUEST);
        friendRequest.child(model.getUid()).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (b)
                            Toast.makeText(FriendRequestActivity.this," Remove!!!! ",Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    protected void onStop() {
        if(adapter != null)
            adapter.stopListening();
        if(searchAdapter != null)
            searchAdapter.stopListening();
        super.onStop();
    }

    private void loadSearchData()   {
        Query query= FirebaseDatabase.getInstance().getReference().child(Common.USER_INFORMATION)
                .child(Common.loggedUser.getUid())
                .child(Common.FRIEND_REQUEST);

        FirebaseRecyclerOptions<User> options= new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(query,User.class)
                .build();
        searchAdapter = new FirebaseRecyclerAdapter<User, FriendRequestViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FriendRequestViewHolder holder, int i, @NonNull final User model) {

                holder.txt_user_email.setText(model.getEmail());
                holder.btn_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteFriendRequest(model,false);
                        addToAcceptList(model);
                        addUserToFriendContact(model);

                    }
                });

                holder.btn_decline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Deleting Friend Request
                        deleteFriendRequest(model,true);

                    }
                });


            }

            @NonNull
            @Override
            public FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View itemView= LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.layout_friend_request,viewGroup,false);
                return new FriendRequestViewHolder(itemView);
            }
        };

        searchAdapter.startListening();
        recycler_all_user.setAdapter(searchAdapter);
    }
    @Override
    public void onFireLoadUserNameDone(List<String> lstEmail) {
        searchBar.setLastSuggestions(lstEmail);

    }

    @Override
    public void onFirebaseLoadFailed(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }


}